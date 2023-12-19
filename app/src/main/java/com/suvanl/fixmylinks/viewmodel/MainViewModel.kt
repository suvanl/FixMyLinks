package com.suvanl.fixmylinks.viewmodel

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
class MainViewModel @Inject constructor() : AppLevelViewModel() {
    private val _multiSelectedRules = MutableStateFlow(setOf<BaseMutationModel>())
    override val multiSelectedRules = _multiSelectedRules.asStateFlow()

    override fun updateMultiSelectedRules(updatedSet: Set<BaseMutationModel>) {
        _multiSelectedRules.value = updatedSet
    }

    override fun clearMultiSelectedRules() {
        _multiSelectedRules.value = setOf()
    }
}