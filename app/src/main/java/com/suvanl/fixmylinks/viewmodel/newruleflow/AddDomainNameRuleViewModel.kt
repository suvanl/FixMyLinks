package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.validation.ValidateDomainNameUseCase
import com.suvanl.fixmylinks.ui.components.form.DomainNameRuleFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddDomainNameRuleViewModel(
    private val rulesRepository: RulesRepository,
    private val validateDomainNameUseCase: ValidateDomainNameUseCase
) : AddRuleViewModel() {

    private val _removeAllUrlParams = MutableStateFlow(false)

    private val _formUiState = MutableStateFlow(DomainNameRuleFormState())
    val formUiState = _formUiState.asStateFlow()

    fun setRuleName(ruleName: String) {
        _formUiState.value = _formUiState.value.copy(ruleName = ruleName)
    }

    fun setInitialDomainName(domainName: String) {
        _formUiState.value = _formUiState.value.copy(initialDomainName = domainName)
    }

    fun setTargetDomainName(domainName: String) {
        _formUiState.value = _formUiState.value.copy(targetDomainName = domainName)
    }

    fun setRemoveAllUrlParams(shouldRemove: Boolean) {
        _removeAllUrlParams.value = shouldRemove
    }

    override suspend fun saveRule() {
        if (!_removeAllUrlParams.value) {
            rulesRepository.saveRule(
                DomainNameMutationModel(
                    name = _formUiState.value.ruleName,
                    triggerDomain = _formUiState.value.initialDomainName,
                    isLocalOnly = true,
                    mutationInfo = DomainNameMutationInfo(
                        initialDomain = _formUiState.value.initialDomainName,
                        targetDomain = _formUiState.value.targetDomainName,
                    )
                )
            )
        } else {
            rulesRepository.saveRule(
                DomainNameAndAllUrlParamsMutationModel(
                    name = _formUiState.value.ruleName,
                    triggerDomain = _formUiState.value.initialDomainName,
                    isLocalOnly = true,
                    mutationInfo = DomainNameMutationInfo(
                        initialDomain = _formUiState.value.initialDomainName,
                        targetDomain = _formUiState.value.targetDomainName,
                    )
                )
            )
        }
    }

    override fun validateData(): Boolean {
        val initialDomainNameText = _formUiState.value.initialDomainName
        val targetDomainNameText = _formUiState.value.targetDomainName

        val initialDomainNameResult = validateDomainNameUseCase(initialDomainNameText)
        val targetDomainNameResult = validateDomainNameUseCase(targetDomainNameText)

        val hasInvalidFields = listOf(
            initialDomainNameResult,
            targetDomainNameResult
        ).any { !it.isSuccessful }

        _formUiState.value = _formUiState.value.copy(
            initialDomainNameError = initialDomainNameResult.errorMessage,
            targetDomainNameError = targetDomainNameResult.errorMessage,
        )

        return !hasInvalidFields
    }
}