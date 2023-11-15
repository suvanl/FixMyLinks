package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.domain.mutation.MutationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectRuleTypeViewModel : NewRuleFlowViewModel() {
    private val _mutationType = MutableStateFlow(MutationType.DOMAIN_NAME)
    val mutationType = _mutationType.asStateFlow()

    fun updateSelectedMutationType(selection: MutationType) {
        _mutationType.value = selection
    }

    override suspend fun saveRule() {
        TODO("Not yet implemented")
    }
}