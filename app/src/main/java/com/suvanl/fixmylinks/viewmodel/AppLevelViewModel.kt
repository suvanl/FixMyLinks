package com.suvanl.fixmylinks.viewmodel

import androidx.lifecycle.ViewModel
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import kotlinx.coroutines.flow.StateFlow

abstract class AppLevelViewModel : ViewModel() {

    /**
     * An observable [Set] of rules that have been selected by the user on `FmlScreen.Rules`
     */
    abstract val multiSelectedRules: StateFlow<Set<BaseMutationModel>>

    abstract fun updateMultiSelectedRules(updatedSet: Set<BaseMutationModel>)

    abstract fun clearMultiSelectedRules()

    fun resetState() {
        clearMultiSelectedRules()
    }
}