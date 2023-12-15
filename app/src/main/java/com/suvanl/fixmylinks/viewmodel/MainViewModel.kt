package com.suvanl.fixmylinks.viewmodel

import androidx.lifecycle.ViewModel
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * A ViewModel scoped to the lifetime of the app-level composable, responsible for sharing state
 * between the app-level composable and the NavHost.
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _multiSelectedRules = MutableStateFlow(setOf<BaseMutationModel>())
    val multiSelectedRules = _multiSelectedRules.asStateFlow()

    fun updateMultiSelectedRules(updatedSet: Set<BaseMutationModel>) {
        _multiSelectedRules.value = updatedSet
    }

    fun resetState() {
        _multiSelectedRules.value = setOf()
    }
}