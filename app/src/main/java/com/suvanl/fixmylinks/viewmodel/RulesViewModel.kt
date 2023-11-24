package com.suvanl.fixmylinks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.ui.screens.RulesScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RulesViewModel @Inject constructor(
    private val rulesRepository: RulesRepository
) : ViewModel() {
    val rulesScreenUiState: StateFlow<RulesScreenUiState> =
        rulesRepository.getAllRules()
            .map { RulesScreenUiState(rules = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = TIMEOUT_MILLIS),
                initialValue = RulesScreenUiState()
            )

    suspend fun deleteSingleRule(baseRuleId: Long) {
        rulesRepository.deleteByBaseRuleId(baseRuleId)
    }

    suspend fun deleteAll() {
        rulesRepository.deleteAllRules()
    }

    override fun onCleared() {
        super.onCleared()
        println("RulesViewModel cleared")
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }
}