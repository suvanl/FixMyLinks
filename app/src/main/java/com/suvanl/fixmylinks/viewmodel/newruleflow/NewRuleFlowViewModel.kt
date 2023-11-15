package com.suvanl.fixmylinks.viewmodel.newruleflow

import androidx.lifecycle.ViewModel

abstract class NewRuleFlowViewModel : ViewModel() {
    abstract suspend fun saveRule()
}