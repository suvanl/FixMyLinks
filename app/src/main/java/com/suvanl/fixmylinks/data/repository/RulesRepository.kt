package com.suvanl.fixmylinks.data.repository

import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import kotlinx.coroutines.flow.Flow

interface RulesRepository {
    suspend fun saveRule(rule: BaseMutationModel)

    suspend fun updateRule(baseRuleId: Long, newData: BaseMutationModel)

    suspend fun deleteAllRules()

    suspend fun deleteByBaseRuleId(baseRuleId: Long)

    fun getRuleByBaseId(baseRuleId: Long, ruleType: MutationType): Flow<BaseMutationModel?>

    fun getAllRules(): Flow<List<BaseMutationModel>>
}