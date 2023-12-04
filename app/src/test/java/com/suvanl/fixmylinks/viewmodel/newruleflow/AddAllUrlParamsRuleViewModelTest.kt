package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.FakePreferencesRepository
import com.suvanl.fixmylinks.data.repository.FakeRulesRepository
import com.suvanl.fixmylinks.data.repository.PreferencesRepository
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.data.repository.UserPreferences
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.validation.ValidateDomainNameUseCase
import com.suvanl.fixmylinks.ui.components.form.AllUrlParamsRuleFormState
import com.suvanl.fixmylinks.viewmodel.newruleflow.util.FakeDomainNameValidator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddAllUrlParamsRuleViewModelTest {

    private lateinit var viewModel: AddAllUrlParamsRuleViewModel
    private lateinit var rulesRepository: RulesRepository
    private lateinit var preferencesRepository: PreferencesRepository<UserPreferences>
    private lateinit var validateDomainNameUseCase: ValidateDomainNameUseCase

    @Before
    fun setup() {
        validateDomainNameUseCase = ValidateDomainNameUseCase(FakeDomainNameValidator())
        rulesRepository = FakeRulesRepository()
        preferencesRepository = FakePreferencesRepository()
        viewModel = AddAllUrlParamsRuleViewModel(
            rulesRepository = { rulesRepository },
            preferencesRepository = { preferencesRepository },
            validateDomainNameUseCase = validateDomainNameUseCase
        )
    }

    @Test
    fun `saving a rule results in it being inserted into the data source, accessible via the repository`() {
        val ruleName = "My Test Rule"
        viewModel.setRuleName(ruleName)

        val domainName = "instagram.com"
        viewModel.setDomainName(domainName)

        runBlocking {
            viewModel.saveRule()

            // Get all rules from the repository
            val allRules = rulesRepository.getAllRules().first()

            // Assert that the rule exists in the data source
            val rule = allRules.find { it.name == ruleName && it.triggerDomain == domainName }
            assertNotNull(rule)
        }
    }

    @Test
    fun `saving a rule with a blank domain name fails to save because validation fails`() {
        val ruleName = "My Test Rule 001"
        viewModel.setRuleName(ruleName)

        runBlocking {
            viewModel.saveRule()

            // Get all rules from the repository
            val allRules = rulesRepository.getAllRules().first()

            // Assert that the rule hasn't been saved in the data source
            val rule = allRules.find { it.name == ruleName }
            assertNull(rule)
        }
    }

    @Test
    fun `updating an existing rule results in it being replaced with new data`() {
        val baseRuleId = 2L
        val formUiState = AllUrlParamsRuleFormState(
            ruleName = "My Spotify rule",
            domainName = "www.spotify.com",
        )

        val newData = AllUrlParamsMutationModel(
            baseRuleId = baseRuleId,
            name = formUiState.ruleName,
            triggerDomain = formUiState.domainName,
            isLocalOnly = true,
        )

        runBlocking {
            (rulesRepository as FakeRulesRepository).insertFakeData()
            rulesRepository.updateRule(baseRuleId, newData)

            // verify update
            val updated = rulesRepository.getRuleByBaseId(baseRuleId, MutationType.URL_PARAMS_ALL).first()
            assertTrue(updated is AllUrlParamsMutationModel)
            assertEquals(newData, updated)
        }
    }

    @Test
    fun `updating a rule that doesn't exist throws an exception`() {
        val baseRuleId = 20L
        val formUiState = AllUrlParamsRuleFormState()
        val newData = AllUrlParamsMutationModel(
            baseRuleId = baseRuleId,
            name = formUiState.ruleName,
            triggerDomain = formUiState.domainName,
            isLocalOnly = true,
        )

        // should throw an IAE if a rule with the given baseRuleId can't be found in the data source
        assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                (rulesRepository as FakeRulesRepository).insertFakeData()
                rulesRepository.updateRule(baseRuleId, newData)
            }
        }
    }

    @Test
    fun `invalid (empty) domain name results in an error message being set in formUiState`() {
        // validate with default values (empty string for domain name)
        viewModel.validateData()

        runBlocking {
            val state = viewModel.formUiState.first()
            assertNotNull(state.domainNameError)
        }
    }

    @Test
    fun `invalid (blank) domain name results in an error message being set in formUiState`() {
        viewModel.setDomainName("                                        ")
        viewModel.validateData()

        runBlocking {
            val state = viewModel.formUiState.first()
            assertNotNull(state.domainNameError)
        }
    }

    @Test
    fun `invalid (contains disallowed characters) domain name results in an error message being set in formUiState`() {
        viewModel.setDomainName("d.android.com@google.com!")
        viewModel.validateData()

        runBlocking {
            val state = viewModel.formUiState.first()
            assertNotNull(state.domainNameError)
        }
    }
}