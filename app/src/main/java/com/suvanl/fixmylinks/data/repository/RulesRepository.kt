package com.suvanl.fixmylinks.data.repository

import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import kotlinx.coroutines.flow.Flow

interface RulesRepository {
    suspend fun saveRule(rule: BaseMutationModel)

    suspend fun deleteAllRules()

    suspend fun deleteByBaseRuleId(baseRuleId: Long)

    fun getAllRules(): Flow<List<BaseMutationModel>>
}