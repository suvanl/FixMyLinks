package com.suvanl.fixmylinks.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

data class UserPreferences(
    val showFormFieldHints: Boolean = UserPreferencesRepository.SHOW_FORM_FIELD_HINTS.defaultValue,
)

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository<UserPreferences> {
    /**
     * All user preferences ([UserPreferences]) as an async observable [Flow].
     */
    override val allPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception !is IOException) throw exception

            exception.printStackTrace()
            emit(emptyPreferences())
        }
        .map { preferences ->
            mapUserPreferences(preferences)
        }

    override suspend fun <T> updatePreference(preference: PreferenceItem<T>, newValue: T) {
        dataStore.edit { preferences ->
            preferences[preference.key] = newValue
        }
    }

    /**
     * Maps each preference to its associated [UserPreferences] property.
     * @return An instance of [UserPreferences] containing all stored preferences.
     */
    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val showFormFieldHints = preferences[SHOW_FORM_FIELD_HINTS.key]
            ?: SHOW_FORM_FIELD_HINTS.defaultValue

        return UserPreferences(
            showFormFieldHints = showFormFieldHints
        )
    }

    companion object PreferencesKeys {
        val SHOW_FORM_FIELD_HINTS = PreferenceItem(
            key = booleanPreferencesKey("show_form_field_hints"),
            defaultValue = true
        )
    }
}