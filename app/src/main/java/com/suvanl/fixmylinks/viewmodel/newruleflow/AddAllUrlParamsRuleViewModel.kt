package com.suvanl.fixmylinks.viewmodel.newruleflow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddAllUrlParamsRuleViewModel : ViewModel() {

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
}