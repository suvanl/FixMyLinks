package com.suvanl.fixmylinks.data.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

/**
 * Represents a preference key-value pair, where the value is the default/fallback value.
 */
data class PreferenceItem<T>(
    val key: Preferences.Key<T>,
    val defaultValue: T,
)

interface PreferencesRepository<TPreferences> {

    /**
     * All preferences as an async observable [Flow], where [TPreferences] is the preferences object
     * (defined as a data class) that is used throughout the app to represent preference data.
     */
    val allPreferencesFlow: Flow<TPreferences>

    /**
     * Updates the given preferences key with the given new value.
     * @param preference The preference to update.
     * @param newValue The new value to assign to the [preference] key.
     */
    suspend fun <T> updatePreference(preference: PreferenceItem<T>, newValue: T)
}