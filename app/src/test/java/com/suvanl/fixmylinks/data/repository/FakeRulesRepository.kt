package com.suvanl.fixmylinks.data.repository

import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

class FakeRulesRepository : RulesRepository {

    private val customRules = mutableListOf<BaseMutationModel>()
    private var customRulesStream = flowOf(customRules.toList())

    override suspend fun saveRule(rule: BaseMutationModel) {
        customRules += rule
        refreshFlow()
    }

    override suspend fun deleteAllRules() {
        customRules.clear()
        refreshFlow()
    }

    override suspend fun deleteByBaseRuleId(baseRuleId: Long) {
        val rule = customRules.find { it.baseRuleId == baseRuleId }
        require(rule != null)

        customRules.remove(rule)
        refreshFlow()
    }

    override fun getAllRules(): Flow<List<BaseMutationModel>> {
        runBlocking {
            refreshFlow()
        }

        return customRulesStream
    }

    private suspend fun refreshFlow() {
        customRulesStream = flow { emit(customRules) }
    }
}