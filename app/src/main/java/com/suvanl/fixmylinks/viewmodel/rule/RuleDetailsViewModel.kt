package com.suvanl.fixmylinks.viewmodel.rule

import androidx.lifecycle.ViewModel
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RuleDetailsViewModel @Inject constructor(
    private val rulesRepository: RulesRepository,
) : ViewModel() {

    private val _selectedRule = MutableStateFlow<BaseMutationModel?>(null)
    val selectedRule = _selectedRule.asStateFlow()

    suspend fun updateSelectedRule(baseRuleId: Long, mutationType: MutationType) {
        rulesRepository.getRuleByBaseId(baseRuleId, mutationType).collect { rule ->
            _selectedRule.value = rule
        }
    }
}