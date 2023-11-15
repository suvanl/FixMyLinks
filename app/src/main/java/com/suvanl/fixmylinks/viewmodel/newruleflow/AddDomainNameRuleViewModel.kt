package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddDomainNameRuleViewModel(
    private val rulesRepository: RulesRepository
) : NewRuleFlowViewModel() {

    private val _ruleName = MutableStateFlow("")
    val ruleName = _ruleName.asStateFlow()

    private val _initialDomainName = MutableStateFlow("")
    val initialDomainName = _initialDomainName.asStateFlow()

    private val _targetDomainName = MutableStateFlow("")
    val targetDomainName = _targetDomainName.asStateFlow()

    private val _removeAllUrlParams = MutableStateFlow(false)

    fun setRuleName(ruleName: String) {
        _ruleName.value = ruleName
    }

    fun setInitialDomainName(domainName: String) {
        _initialDomainName.value = domainName
    }

    fun setTargetDomainName(domainName: String) {
        _targetDomainName.value = domainName
    }

    fun setRemoveAllUrlParams(shouldRemove: Boolean) {
        _removeAllUrlParams.value = shouldRemove
    }

    override suspend fun saveRule() {
        if (!_removeAllUrlParams.value) {
            rulesRepository.insertRule(
                DomainNameMutationModel(
                    name = _ruleName.value,
                    triggerDomain = _initialDomainName.value,
                    isLocalOnly = true,
                    mutationInfo = DomainNameMutationInfo(
                        initialDomain = _initialDomainName.value,
                        targetDomain = _targetDomainName.value,
                    )
                )
            )
        }
    }
}