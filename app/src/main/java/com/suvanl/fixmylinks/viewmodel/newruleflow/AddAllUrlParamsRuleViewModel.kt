package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.PreferencesRepository
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.data.repository.UserPreferences
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.validation.ValidateDomainNameUseCase
import com.suvanl.fixmylinks.ui.components.form.AllUrlParamsRuleFormState
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddAllUrlParamsRuleViewModel @Inject constructor(
    private val rulesRepository: Lazy<RulesRepository>,
    preferencesRepository: Lazy<PreferencesRepository<UserPreferences>>,
    private val validateDomainNameUseCase: ValidateDomainNameUseCase,
) : AddRuleViewModel(preferencesRepository = preferencesRepository.get()) {

    private val _formUiState = MutableStateFlow(AllUrlParamsRuleFormState())
    val formUiState = _formUiState.asStateFlow()

    fun setRuleName(ruleName: String) {
        _formUiState.value = _formUiState.value.copy(ruleName = ruleName)
    }

    fun setDomainName(domainName: String) {
        _formUiState.value = _formUiState.value.copy(domainName = domainName)
    }

    override suspend fun setInitialFormUiState(mutationType: MutationType, baseRuleId: Long) {
        rulesRepository.get().getRuleByBaseId(baseRuleId, mutationType).collect { rule ->
            if (rule !is AllUrlParamsMutationModel) return@collect

            _formUiState.value = AllUrlParamsRuleFormState(
                ruleName = rule.name,
                domainName = rule.triggerDomain,
            )
            updateRuleOptionsState(ruleOptions.value.copy(ruleEnabled = rule.isEnabled))
        }
    }

    override suspend fun saveRule() {
        if (!validateData()) return

        rulesRepository.get().saveRule(
            AllUrlParamsMutationModel(
                name = _formUiState.value.ruleName,
                mutationType = MutationType.URL_PARAMS_ALL,
                triggerDomain = _formUiState.value.domainName,
                isLocalOnly = true,
                isEnabled = ruleOptions.value.ruleEnabled,
            )
        )
    }

    override suspend fun updateExistingRule(baseRuleId: Long) {
        if (!validateData()) return

        val newData = AllUrlParamsMutationModel(
            name = _formUiState.value.ruleName,
            triggerDomain = _formUiState.value.domainName,
            isLocalOnly = true,
            isEnabled = ruleOptions.value.ruleEnabled,
            baseRuleId = baseRuleId
        )
        rulesRepository.get().updateRule(baseRuleId, newData)
    }

    override fun validateData(): Boolean {
        val domainNameValidationResult = validateDomainNameUseCase(_formUiState.value.domainName)

        _formUiState.value = _formUiState.value.copy(
            domainNameError = domainNameValidationResult.errorMessage
        )

        return domainNameValidationResult.isSuccessful
    }
}