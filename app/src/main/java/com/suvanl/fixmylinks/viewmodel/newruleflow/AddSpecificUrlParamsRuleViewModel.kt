package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.PreferencesRepository
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.data.repository.UserPreferences
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.validation.ValidateDomainNameUseCase
import com.suvanl.fixmylinks.domain.validation.ValidateRemovableParamsListUseCase
import com.suvanl.fixmylinks.domain.validation.ValidateUrlParamKeyUseCase
import com.suvanl.fixmylinks.ui.components.form.SpecificUrlParamsRuleFormState
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddSpecificUrlParamsRuleViewModel @Inject constructor(
    private val rulesRepository: Lazy<RulesRepository>,
    preferencesRepository: Lazy<PreferencesRepository<UserPreferences>>,
    private val validateDomainNameUseCase: ValidateDomainNameUseCase,
    private val validateRemovableParamsListUseCase: ValidateRemovableParamsListUseCase,
    private val validateUrlParamKeyUseCase: ValidateUrlParamKeyUseCase
) : AddRuleViewModel(preferencesRepository = preferencesRepository.get()) {

    private val _formUiState = MutableStateFlow(SpecificUrlParamsRuleFormState())
    val formUiState = _formUiState.asStateFlow()

    fun setRuleName(ruleName: String) {
        _formUiState.value = _formUiState.value.copy(ruleName = ruleName)
    }

    fun setDomainName(domainName: String) {
        _formUiState.value = _formUiState.value.copy(domainName = domainName)
    }

    fun addParam(paramName: String) {
        if (!validateUrlParamKey(paramName)) return

        val updatedParamList = _formUiState.value.addedParamNames.toMutableList().plus(paramName)
        _formUiState.value = _formUiState.value.copy(
            addedParamNames = updatedParamList
        )
    }

    fun removeParam(index: Int) {
        val updatedParamList = _formUiState.value.addedParamNames.toMutableList()
        updatedParamList.removeAt(index)

        _formUiState.value = _formUiState.value.copy(addedParamNames = updatedParamList)
    }

    override suspend fun saveRule() {
        if (!validateData()) return

        rulesRepository.get().saveRule(
            SpecificUrlParamsMutationModel(
                name = _formUiState.value.ruleName,
                triggerDomain = _formUiState.value.domainName,
                isLocalOnly = true,
                mutationInfo = SpecificUrlParamsMutationInfo(
                    removableParams = _formUiState.value.addedParamNames
                )
            )
        )
    }

    fun validateUrlParamKey(key: String): Boolean {
        val urlParamKeyResult = validateUrlParamKeyUseCase(key)

        _formUiState.value = _formUiState.value.copy(
            urlParamKeyError = urlParamKeyResult.errorMessage
        )

        return urlParamKeyResult.isSuccessful
    }

    fun resetUrlParamKeyValidationState() {
        _formUiState.value = _formUiState.value.copy(
            urlParamKeyError = null
        )
    }

    override fun validateData(): Boolean {
        val domainNameResult = validateDomainNameUseCase(_formUiState.value.domainName)
        val removableParamsListResult =
            validateRemovableParamsListUseCase(_formUiState.value.addedParamNames)

        _formUiState.value = _formUiState.value.copy(
            domainNameError = domainNameResult.errorMessage,
            addedParamNamesError = removableParamsListResult.errorMessage
        )

        val hasInvalidFields = listOf(
            domainNameResult,
            removableParamsListResult
        ).any { !it.isSuccessful }

        return !hasInvalidFields
    }
}