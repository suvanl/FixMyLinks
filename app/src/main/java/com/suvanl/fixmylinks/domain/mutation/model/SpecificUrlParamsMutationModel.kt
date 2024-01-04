package com.suvanl.fixmylinks.domain.mutation.model

import com.suvanl.fixmylinks.data.local.db.entity.SpecificUrlParamsRule
import com.suvanl.fixmylinks.domain.mutation.MutationType

data class SpecificUrlParamsMutationInfo(
    val removableParams: List<String>
)

data class SpecificUrlParamsMutationModel(
    override val name: String,
    override val mutationType: MutationType = MutationType.URL_PARAMS_SPECIFIC,
    override val triggerDomain: String,
    override val dateModifiedTimestamp: Long? = null,
    override val isLocalOnly: Boolean,
    override val isEnabled: Boolean,
    override val baseRuleId: Long = 0,
    val mutationInfo: SpecificUrlParamsMutationInfo,
) : BaseMutationModel

/**
 * Transforms this domain model to a database entity.
 * @param baseRuleId The ID of the rule's related BaseRule.
 */
fun SpecificUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long) = SpecificUrlParamsRule(
    baseRuleId = baseRuleId,
    removableParams = mutationInfo.removableParams,
)

/**
 * Transforms this domain model to a database entity.
 * @param baseRuleId The ID of the rule's related BaseRule.
 * @param ruleId The ID of the rule (its primary key).
 */
fun SpecificUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long, ruleId: Long) =
    SpecificUrlParamsRule(
        id = ruleId,
        baseRuleId = baseRuleId,
        removableParams = mutationInfo.removableParams,
    )