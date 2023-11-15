package com.suvanl.fixmylinks.viewmodel.newruleflow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddSpecificUrlParamsRuleViewModel : NewRuleFlowViewModel() {

    private val _ruleName = MutableStateFlow("")
    val ruleName = _ruleName.asStateFlow()

    private val _domainName = MutableStateFlow("")
    val domainName = _domainName.asStateFlow()

    private val _removableParams = MutableStateFlow(emptyList<String>())
    val removableParams = _removableParams.asStateFlow()

    fun setRuleName(ruleName: String) {
        _ruleName.value = ruleName
    }

    fun setDomainName(domainName: String) {
        _domainName.value = domainName
    }

    fun addParam(paramName: String) {
        _removableParams.value += paramName
    }

    fun removeParam(index: Int) {
        val mutable = _removableParams.value.toMutableList()
        mutable.removeAt(index)

        _removableParams.value = mutable.toList()
    }

    override suspend fun saveRule() {
        TODO("Not yet implemented")
    }
}