package com.suvanl.fixmylinks.viewmodel.newruleflow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddDomainNameRuleViewModel : ViewModel(), NewRuleFlowViewModel {

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
        TODO("Not yet implemented")
    }
}