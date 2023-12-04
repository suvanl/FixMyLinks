package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.FakePreferencesRepository
import com.suvanl.fixmylinks.data.repository.FakeRulesRepository
import com.suvanl.fixmylinks.data.repository.PreferencesRepository
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.data.repository.UserPreferences
import com.suvanl.fixmylinks.domain.validation.ValidateDomainNameUseCase
import com.suvanl.fixmylinks.viewmodel.newruleflow.util.FakeDomainNameValidator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AddDomainNameRuleViewModelTest {

    private lateinit var viewModel: AddDomainNameRuleViewModel
    private lateinit var rulesRepository: RulesRepository
    private lateinit var preferencesRepository: PreferencesRepository<UserPreferences>
    private lateinit var validateDomainNameUseCase: ValidateDomainNameUseCase

    @Before
    fun setup() {
        rulesRepository = FakeRulesRepository()
        preferencesRepository = FakePreferencesRepository()
        validateDomainNameUseCase = ValidateDomainNameUseCase(FakeDomainNameValidator())
        viewModel = AddDomainNameRuleViewModel(
            rulesRepository = { rulesRepository },
            preferencesRepository = { preferencesRepository },
            validateDomainNameUseCase = validateDomainNameUseCase
        )
    }

    @Test
    fun `saving a rule results in it being inserted into the data source, accessible via the repository`() {
        val ruleName = "My Test Rule"
        viewModel.setRuleName(ruleName)

        val initialDomainName = "x.com"
        viewModel.setInitialDomainName(initialDomainName)

        val targetDomainName = "twitter.com"
        viewModel.setTargetDomainName(targetDomainName)

        runBlocking {
            viewModel.saveRule()

            // Get all rules from the repository
            val allRules = rulesRepository.getAllRules().first()

            // Assert that the rule exists in the data source
            val rule = allRules.find { it.name == ruleName && it.triggerDomain == initialDomainName }
            assertNotNull(rule)
        }
    }

    @Test
    fun `saving a rule with an empty initial domain name fails to save because validation fails`() {
        val ruleName = "My Test Rule"
        viewModel.setRuleName(ruleName)

        val targetDomainName = "d.android.com"
        viewModel.setTargetDomainName(targetDomainName)

        // notice how we're not using viewModel.setInitialDomainName()

        runBlocking {
            viewModel.saveRule()

            // Get all rules from the repository
            val allRules = rulesRepository.getAllRules().first()

            // Assert that the rule doesn't exist in the data source
            val rule = allRules.find { it.name == ruleName }
            assertNull(rule)

            // Assert that initialDomainNameError is not null in FormUiState
            val state = viewModel.formUiState.first()
            assertNotNull(state.initialDomainNameError)

            // Assert that targetDomainNameError is null, since we have actually provided a value for it
            assertNull(state.targetDomainNameError)
        }
    }

    @Test
    fun `saving a rule with an empty target domain name fails to save because validation fails`() {
        val ruleName = "My Test Rule"
        viewModel.setRuleName(ruleName)

        val initialDomainName = "developer.android.com"
        viewModel.setInitialDomainName(initialDomainName)

        // notice how we're not using viewModel.setTargetDomainName()

        runBlocking {
            viewModel.saveRule()

            // Get all rules from the repository
            val allRules = rulesRepository.getAllRules().first()

            // Assert that the rule doesn't exist in the data source
            val rule = allRules.find { it.name == ruleName && it.triggerDomain == initialDomainName }
            assertNull(rule)

            // Assert that targetDomainNameError is not null in FormUiState
            val state = viewModel.formUiState.first()
            assertNotNull(state.targetDomainNameError)

            // Assert that initialDomainNameError is null, since we have actually provided a value for it
            assertNull(state.initialDomainNameError)
        }
    }

    @Test
    fun `saving a rule with empty target _and_ initial domain name fails to save because validation fails`() {
        val ruleName = "My Test Rule"
        viewModel.setRuleName(ruleName)

        runBlocking {
            viewModel.saveRule()

            // Get all rules from the repository
            val allRules = rulesRepository.getAllRules().first()

            // Assert that the rule doesn't exist in the data source
            val rule = allRules.find { it.name == ruleName }
            assertNull(rule)

            val state = viewModel.formUiState.first()
            assertNotNull(state.targetDomainNameError)
            assertNotNull(state.initialDomainNameError)
        }
    }
}