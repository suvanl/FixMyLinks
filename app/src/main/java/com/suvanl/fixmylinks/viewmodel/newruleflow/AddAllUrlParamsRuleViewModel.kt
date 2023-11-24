package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.validation.ValidateDomainNameUseCase
import com.suvanl.fixmylinks.ui.components.form.AllUrlParamsRuleFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AddAllUrlParamsRuleViewModel @Inject constructor(
    private val rulesRepository: RulesRepository,
    private val validateDomainNameUseCase: ValidateDomainNameUseCase,
) : AddRuleViewModel() {

    private val _formUiState = MutableStateFlow(AllUrlParamsRuleFormState())
    val formUiState = _formUiState.asStateFlow()

    fun setRuleName(ruleName: String) {
        _formUiState.value = _formUiState.value.copy(ruleName = ruleName)
    }

    fun setDomainName(domainName: String) {
        _formUiState.value = _formUiState.value.copy(domainName = domainName)
    }

    override suspend fun saveRule() {
        rulesRepository.saveRule(
            AllUrlParamsMutationModel(
                name = _formUiState.value.ruleName,
                mutationType = MutationType.URL_PARAMS_ALL,
                triggerDomain = _formUiState.value.domainName,
                isLocalOnly = true,
            )
        )
    }

    override fun validateData(): Boolean {
        val domainNameValidationResult = validateDomainNameUseCase(_formUiState.value.domainName)

        _formUiState.value = _formUiState.value.copy(
            domainNameError = domainNameValidationResult.errorMessage
        )

        return domainNameValidationResult.isSuccessful
    }
}