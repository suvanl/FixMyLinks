package com.suvanl.fixmylinks.viewmodel.newruleflow

import androidx.lifecycle.ViewModel

abstract class NewRuleFlowViewModel : ViewModel() {
    /**
     * Save the rule locally and optionally remotely based on user preferences.
     */
    abstract suspend fun saveRule()
}