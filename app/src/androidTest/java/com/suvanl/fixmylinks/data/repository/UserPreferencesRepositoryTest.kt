package com.suvanl.fixmylinks.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATASTORE_NAME = "test_preferences_datastore"

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserPreferencesRepositoryTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testCoroutineScope = TestScope()
    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testCoroutineScope,
        produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
    )
    private val repository: UserPreferencesRepository = UserPreferencesRepository(testDataStore)

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun cleanUp() {
        testCoroutineScope.launch {
            testDataStore.edit { it.clear() }
        }

        testCoroutineScope.cancel()
    }

    @Test
    fun repository_testGetAllUserPreferences_asFlow() {
        val expectedUserPreferences = UserPreferences(
            showFormFieldHints = true
        )

        testCoroutineScope.runTest {
            assertThat(repository.allPreferencesFlow.first())
                .isEqualTo(expectedUserPreferences)
        }
    }

    @Test
    fun repository_testUpdatePreference_updatesValueIn_allPreferencesFlow() {
        testCoroutineScope.runTest {
            repository.updatePreference(SHOW_FORM_FIELD_HINTS, false)

            assertThat(repository.allPreferencesFlow.first().showFormFieldHints)
                .isEqualTo(false)
        }
    }

    companion object {
        val SHOW_FORM_FIELD_HINTS = PreferenceItem(
            key = booleanPreferencesKey("show_form_field_hints"),
            defaultValue = true
        )
    }
}