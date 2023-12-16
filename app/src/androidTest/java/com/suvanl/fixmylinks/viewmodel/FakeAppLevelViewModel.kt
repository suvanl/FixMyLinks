package com.suvanl.fixmylinks.viewmodel

import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeAppLevelViewModel : AppLevelViewModel() {
    override val multiSelectedRules: StateFlow<Set<BaseMutationModel>> =
        MutableStateFlow(fakeSelectedRules)

    override fun updateMultiSelectedRules(updatedSet: Set<BaseMutationModel>) {
        TODO("Not yet implemented")
    }

    override fun clearMultiSelectedRules() {
        TODO("Not yet implemented")
    }

    companion object {
        val fakeSelectedRules = mutableSetOf<BaseMutationModel>()
    }
}