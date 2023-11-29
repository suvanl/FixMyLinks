package com.suvanl.fixmylinks.viewmodel.newruleflow

import androidx.lifecycle.ViewModel

abstract class AddRuleViewModel : ViewModel() {
    /**
     * Save the rule locally and optionally remotely based on user preferences.
     */
    abstract suspend fun saveRule()

    /**
     * Validate form data.
     * @return Boolean representing whether validation has passed or not. `true` if data is valid,
     *  else `false`.
     */
    abstract fun validateData(): Boolean
}