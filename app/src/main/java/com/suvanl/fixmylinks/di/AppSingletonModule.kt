package com.suvanl.fixmylinks.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.suvanl.fixmylinks.data.local.db.RuleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"

/**
 * Provides singleton dependencies that live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppSingletonModule {

    @Provides
    @Singleton
    fun provideRuleDatabase(@ApplicationContext context: Context): RuleDatabase {
        return RuleDatabase.getDatabase(context = context)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.preferencesDataStoreFile(name = USER_PREFERENCES) }
        )
    }
}