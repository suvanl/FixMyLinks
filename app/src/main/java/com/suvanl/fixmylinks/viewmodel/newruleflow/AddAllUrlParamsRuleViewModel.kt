package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddAllUrlParamsRuleViewModel(
    private val rulesRepository: RulesRepository
) : NewRuleFlowViewModel() {

    private val _ruleName = MutableStateFlow("")
    val ruleName = _ruleName.asStateFlow()

    private val _domainName = MutableStateFlow("")
    val domainName = _domainName.asStateFlow()

    fun setRuleName(ruleName: String) {
        _ruleName.value = ruleName
    }

    fun setDomainName(domainName: String) {
        _domainName.value = domainName
    }

    override suspend fun saveRule() {
        rulesRepository.insertRule(
            AllUrlParamsMutationModel(
                name = _ruleName.value,
                mutationType = MutationType.URL_PARAMS_ALL,
                triggerDomain = _domainName.value,
                isLocalOnly = true,
            )
        )
    }
}