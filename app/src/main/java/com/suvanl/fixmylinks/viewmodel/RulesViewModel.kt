package com.suvanl.fixmylinks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.ui.screens.RulesScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _selectedRule = MutableStateFlow<BaseMutationModel?>(null)
    val selectedRule = _selectedRule.asStateFlow()

    // whether a selectedRule requires delete confirmation
    private val _deleteConfirmationRequired = MutableStateFlow(false)
    val deleteConfirmationRequired = _deleteConfirmationRequired.asStateFlow()

    fun setSelectedRule(rule: BaseMutationModel?) {
        _selectedRule.value = rule
    }

    fun setDeleteConfirmationRequired(isRequired: Boolean) {
        _deleteConfirmationRequired.value = isRequired
    }

    suspend fun refreshSelectedRule() {
        if (_selectedRule.value == null) return

        rulesRepository.getRuleByBaseId(
            _selectedRule.value!!.baseRuleId,
            _selectedRule.value!!.mutationType
        ).collect { rule ->
            _selectedRule.value = rule
        }
    }

    suspend fun deleteSingleRule(baseRuleId: Long) {
        setSelectedRule(null)
        rulesRepository.deleteByBaseRuleId(baseRuleId)
        setDeleteConfirmationRequired(false)
    }

    suspend fun deleteAllRules() {
        rulesRepository.deleteAllRules()
    }

    suspend fun toggleRuleEnabled(rule: BaseMutationModel, isEnabled: Boolean) {
        val updated = when (rule) {
            is AllUrlParamsMutationModel -> rule.copy(isEnabled = isEnabled)
            is DomainNameAndAllUrlParamsMutationModel -> rule.copy(isEnabled = isEnabled)
            is DomainNameAndSpecificUrlParamsMutationModel -> rule.copy(isEnabled = isEnabled)
            is DomainNameMutationModel -> rule.copy(isEnabled = isEnabled)
            is SpecificUrlParamsMutationModel -> rule.copy(isEnabled = isEnabled)
            else -> throw IllegalArgumentException("rule has unexpected type")
        }

        rulesRepository.updateRule(rule.baseRuleId, updated)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }
}