package com.suvanl.fixmylinks.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakePreferencesRepository : PreferencesRepository<UserPreferences> {

    private var fakePreferences = UserPreferences(
        showFormFieldHints = true
    )

    override var allPreferencesFlow: Flow<UserPreferences> = flowOf(fakePreferences)

    override suspend fun <T> updatePreference(preference: PreferenceItem<T>, newValue: T) {
        when (preference.key.name) {
            "show_form_field_hints" -> {
                if (newValue !is Boolean) {
                    throw IllegalArgumentException(
                        "Expected newValue to be a Boolean, but got ${newValue!!::class.simpleName}"
                    )
                }

                fakePreferences = fakePreferences.copy(showFormFieldHints = newValue)
            }
        }

        refreshFlow()
    }

    private fun refreshFlow() {
        allPreferencesFlow = flow { emit(fakePreferences) }
    }
}