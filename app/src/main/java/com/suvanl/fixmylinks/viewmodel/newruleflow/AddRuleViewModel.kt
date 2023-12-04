package com.suvanl.fixmylinks.viewmodel.newruleflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvanl.fixmylinks.data.repository.PreferencesRepository
import com.suvanl.fixmylinks.data.repository.UserPreferences
import com.suvanl.fixmylinks.data.repository.UserPreferencesRepository
import com.suvanl.fixmylinks.domain.mutation.MutationType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

abstract class AddRuleViewModel(
    private val preferencesRepository: PreferencesRepository<UserPreferences>
) : ViewModel() {
    /**
     * All stored user preferences.
     */
    val userPreferences: StateFlow<UserPreferences> = preferencesRepository.allPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = TIMEOUT_MILLIS),
            initialValue = UserPreferences()
        )

    /**
     * Sets the initial form UI state with non-default values.
     * Use this function to prepopulate the form with data from an existing rule.
     * @param mutationType The existing rule's [MutationType].
     * @param baseRuleId The existing rule's base rule ID.
     */
    abstract suspend fun setInitialFormUiState(mutationType: MutationType, baseRuleId: Long)

    /**
     * Saves the rule locally and optionally remotely based on user preferences.
     */
    abstract suspend fun saveRule()

    /**
     * Updates a rule that has already been saved (locally and remotely if applicable).
     * @param baseRuleId The ID of the existing BaseRule entity.
     */
    abstract suspend fun updateExistingRule(baseRuleId: Long)

    /**
     * Validates form data.
     * @return Boolean representing whether validation has passed or not. `true` if data is valid,
     *  else `false`.
     */
    abstract fun validateData(): Boolean

    /**
     * Updates the user's "Show [[form field]] hints" preference.
     */
    suspend fun updateShowHintsPreference(value: Boolean) {
        preferencesRepository.updatePreference(
            preference = UserPreferencesRepository.SHOW_FORM_FIELD_HINTS,
            newValue = value
        )
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }
}