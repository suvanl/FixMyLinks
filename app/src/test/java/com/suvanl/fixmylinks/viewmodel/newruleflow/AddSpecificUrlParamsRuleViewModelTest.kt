package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.FakeRulesRepository
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.validation.ValidateDomainNameUseCase
import com.suvanl.fixmylinks.domain.validation.ValidateRemovableParamsListUseCase
import com.suvanl.fixmylinks.domain.validation.ValidateUrlParamKeyUseCase
import com.suvanl.fixmylinks.viewmodel.newruleflow.util.FakeDomainNameValidator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AddSpecificUrlParamsRuleViewModelTest {

    private lateinit var viewModel: AddSpecificUrlParamsRuleViewModel
    private lateinit var repository: RulesRepository
    private lateinit var validateDomainNameUseCase: ValidateDomainNameUseCase
    private lateinit var validateRemovableParamsListUseCase: ValidateRemovableParamsListUseCase
    private lateinit var validateUrlParamKeyUseCase: ValidateUrlParamKeyUseCase

    @Before
    fun setup() {
        repository = FakeRulesRepository()
        validateDomainNameUseCase = ValidateDomainNameUseCase(FakeDomainNameValidator())
        validateRemovableParamsListUseCase = ValidateRemovableParamsListUseCase()
        validateUrlParamKeyUseCase = ValidateUrlParamKeyUseCase()
        viewModel = AddSpecificUrlParamsRuleViewModel(
            { repository },
            validateDomainNameUseCase,
            validateRemovableParamsListUseCase,
            validateUrlParamKeyUseCase
        )
    }

    @Test
    fun `adding a valid URL param to the list of removable params results in formUiState being updated to include the newly added param`() {
        viewModel.addParam("s")

        runBlocking {
            val state = viewModel.formUiState.first()
            assertEquals(listOf("s"), state.addedParamNames)
        }
    }

    @Test
    fun `adding an invalid URL param to the list of removable params results in the associated error message containing a non-null value`() {
        viewModel.addParam("s?@=")

        runBlocking {
            val state = viewModel.formUiState.first()
            assertNotNull(state.urlParamKeyError)
        }
    }

    @Test
    fun `submitting data with invalid URL params list results in associated error message in FormUiState containing a non-null value`() {
        viewModel.setDomainName("google.com")

        runBlocking {
            viewModel.saveRule()

            val state = viewModel.formUiState.first()
            assertNotNull(state.addedParamNamesError)
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

    @Test
    fun `saving a rule with a blank domain name fails to save because validation fails`() {
        val ruleName = "My Test Rule 001"
        viewModel.setRuleName(ruleName)

        runBlocking {
            viewModel.saveRule()

            // Get all rules from the repository
            val allRules = repository.getAllRules().first()

            // Assert that the rule hasn't been saved in the data source
            val rule = allRules.find { it.name == ruleName }
            assertNull(rule)
        }
    }

    @Test
    fun `saving a rule with an empty URL params list fails to save because validation fails`() {
        val ruleName = "My Test Rule"
        viewModel.setRuleName(ruleName)

        val domainName = "instagram.com"
        viewModel.setDomainName(domainName)

        // notice how we're not using viewModel.addParam()

        runBlocking {
            viewModel.saveRule()

            // Get all rules from the repository
            val allRules = repository.getAllRules().first()

            // Assert that the rule doesn't exist in the data source
            val rule = allRules.find { it.name == ruleName && it.triggerDomain == domainName }
            assertNull(rule)
        }
    }

    @Test
    fun `saving a rule results in it being inserted into the data source, accessible via the repository`() {
        val ruleName = "My Test Rule"
        viewModel.setRuleName(ruleName)

        val domainName = "instagram.com"
        viewModel.setDomainName(domainName)

        val removableParams = listOf("igshid", "share_id", "testparam")
        removableParams.forEach { viewModel.addParam(it) }

        runBlocking {
            viewModel.saveRule()

            // Get all rules from the repository
            val allRules = repository.getAllRules().first()

            // Assert that the rule exists in the data source
            val rule = allRules.find { it.name == ruleName && it.triggerDomain == domainName }
            assertNotNull(rule)
        }
    }
}