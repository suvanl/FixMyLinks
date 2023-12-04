package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.PreferencesRepository
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.data.repository.UserPreferences
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.validation.ValidateDomainNameUseCase
import com.suvanl.fixmylinks.ui.components.form.DomainNameRuleFormState
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddDomainNameRuleViewModel @Inject constructor(
    private val rulesRepository: Lazy<RulesRepository>,
    preferencesRepository: Lazy<PreferencesRepository<UserPreferences>>,
    private val validateDomainNameUseCase: ValidateDomainNameUseCase
) : AddRuleViewModel(preferencesRepository = preferencesRepository.get()) {

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

    override suspend fun setInitialFormUiState(mutationType: MutationType, baseRuleId: Long) {
        rulesRepository.get().getRuleByBaseId(baseRuleId, mutationType).collect { rule ->
            // Note: while the code itself may look identical in each execution path, notice that
            // the smart cast on `rule` is different in each path due to the type being narrowed to
            // a different type in each path.
            if (!_removeAllUrlParams.value && rule is DomainNameMutationModel) {
                _formUiState.value = DomainNameRuleFormState(
                    ruleName = rule.name,
                    initialDomainName = rule.mutationInfo.initialDomain,
                    targetDomainName = rule.mutationInfo.targetDomain,
                )
            } else if (rule is DomainNameAndAllUrlParamsMutationModel) {
                _formUiState.value = DomainNameRuleFormState(
                    ruleName = rule.name,
                    initialDomainName = rule.mutationInfo.initialDomain,
                    targetDomainName = rule.mutationInfo.targetDomain,
                )
            }
        }
    }

    override suspend fun saveRule() {
        if (!validateData()) return

        val shouldRemoveAllUrlParams = _removeAllUrlParams.value
        // "domain name" rule
        if (!shouldRemoveAllUrlParams) {
            rulesRepository.get().saveRule(
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
            return
        }

        // "domain name and all url parameters" rule
        rulesRepository.get().saveRule(
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


    override suspend fun updateExistingRule(baseRuleId: Long) {
        if (!validateData()) return

        val shouldRemoveAllUrlParams = _removeAllUrlParams.value
        // "domain name" rule
        if (!shouldRemoveAllUrlParams) {
            val newData = DomainNameMutationModel(
                name = _formUiState.value.ruleName,
                triggerDomain = _formUiState.value.initialDomainName,
                isLocalOnly = true,
                mutationInfo = DomainNameMutationInfo(
                    initialDomain = _formUiState.value.initialDomainName,
                    targetDomain = _formUiState.value.targetDomainName,
                ),
                baseRuleId = baseRuleId
            )
            rulesRepository.get().updateRule(baseRuleId, newData)
            return
        }

        // "domain name and all url parameters" rule
        val newData = DomainNameAndAllUrlParamsMutationModel(
            name = _formUiState.value.ruleName,
            triggerDomain = _formUiState.value.initialDomainName,
            isLocalOnly = true,
            mutationInfo = DomainNameMutationInfo(
                initialDomain = _formUiState.value.initialDomainName,
                targetDomain = _formUiState.value.targetDomainName,
            ),
            baseRuleId = baseRuleId
        )
        rulesRepository.get().updateRule(baseRuleId, newData)
    }

    override fun validateData(): Boolean {
        val initialDomainNameText = _formUiState.value.initialDomainName
        val targetDomainNameText = _formUiState.value.targetDomainName

        val initialDomainNameResult = validateDomainNameUseCase(initialDomainNameText)
        val targetDomainNameResult = validateDomainNameUseCase(targetDomainNameText)

        _formUiState.value = _formUiState.value.copy(
            initialDomainNameError = initialDomainNameResult.errorMessage,
            targetDomainNameError = targetDomainNameResult.errorMessage,
        )

        val hasInvalidFields = listOf(
            initialDomainNameResult,
            targetDomainNameResult
        ).any { !it.isSuccessful }

        return !hasInvalidFields
    }
}