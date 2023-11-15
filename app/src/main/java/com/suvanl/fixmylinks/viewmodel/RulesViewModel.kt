package com.suvanl.fixmylinks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.ui.screens.RulesScreenUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RulesViewModel(
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