package com.suvanl.fixmylinks.data.repository

import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class FakeRulesRepository : RulesRepository {

    private val customRules = mutableListOf<BaseMutationModel>()
    private var customRulesStream = flowOf(customRules.toList())

    override suspend fun saveRule(rule: BaseMutationModel) {
        customRules += rule
        refreshFlow()
    }

    override suspend fun updateRule(baseRuleId: Long, newData: BaseMutationModel) {
        val ruleIndex = customRulesStream.first().indexOfFirst { it.baseRuleId == baseRuleId }
        if (ruleIndex == -1) {
            throw IllegalArgumentException("Couldn't find a rule with baseRuleId $baseRuleId")
        }

        customRules.removeAt(ruleIndex)
        customRules.add(ruleIndex, newData)

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

    override fun getRuleByBaseId(
        baseRuleId: Long,
        ruleType: MutationType
    ): Flow<BaseMutationModel> {
        runBlocking { refreshFlow() }

        return customRulesStream.map { rules ->
            rules.find { it.baseRuleId == baseRuleId && it.mutationType == ruleType }
                ?: throw NullPointerException("Couldn't find a rule with baseRuleId $baseRuleId or mutationType $ruleType")
        }
    }

    override fun getAllRules(): Flow<List<BaseMutationModel>> {
        runBlocking {
            refreshFlow()
        }

        return customRulesStream
    }

    suspend fun insertFakeData() {
        fakeRules.forEach {
            saveRule(it)
        }

        refreshFlow()
    }

    private suspend fun refreshFlow() {
        customRulesStream = flow { emit(customRules) }
    }

    companion object {
        private val fakeRules = listOf(
            AllUrlParamsMutationModel(
                baseRuleId = 1,
                name = "My rule",
                triggerDomain = "instagram.com",
                isLocalOnly = true,
            ),
            AllUrlParamsMutationModel(
                baseRuleId = 2,
                name = "My second rule",
                triggerDomain = "spotify.com",
                isLocalOnly = true,
            ),
            DomainNameMutationModel(
                baseRuleId = 3,
                name = "twitter to x",
                triggerDomain = "x.com",
                isLocalOnly = true,
                mutationInfo = DomainNameMutationInfo(
                    initialDomain = "twitter.com",
                    targetDomain = "x.com",
                ),
            ),
            AllUrlParamsMutationModel(
                baseRuleId = 4,
                name = "My second rule",
                triggerDomain = "spotify.com",
                isLocalOnly = true,
            ),
            DomainNameAndAllUrlParamsMutationModel(
                baseRuleId = 5,
                name = "Android Dev",
                triggerDomain = "developer.android.com",
                isLocalOnly = true,
                mutationInfo = DomainNameMutationInfo(
                    initialDomain = "developer.android.com",
                    targetDomain = "d.android.com",
                ),
            ),
            SpecificUrlParamsMutationModel(
                baseRuleId = 6,
                name = "YouTube - remove playlist association",
                triggerDomain = "youtube.com",
                isLocalOnly = true,
                mutationInfo = SpecificUrlParamsMutationInfo(
                    removableParams = listOf("list"),
                ),
            ),
        )
    }
}