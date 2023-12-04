package com.suvanl.fixmylinks.domain.mutation.model

import com.suvanl.fixmylinks.data.local.db.entity.AllUrlParamsRule
import com.suvanl.fixmylinks.domain.mutation.MutationType

data class AllUrlParamsMutationModel(
    override val name: String,
    override val mutationType: MutationType = MutationType.URL_PARAMS_ALL,
    override val triggerDomain: String,
    override val dateModifiedTimestamp: Long? = null,
    override val isLocalOnly: Boolean,
    override val baseRuleId: Long = 0,
) : BaseMutationModel

/**
 * Transforms this domain model to a database entity.
 * @param baseRuleId The ID of the rule's related BaseRule.
 */
fun AllUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long) =
    AllUrlParamsRule(baseRuleId = baseRuleId)

/**
 * Transforms this domain model to a database entity.
 * @param baseRuleId The ID of the rule's related BaseRule.
 * @param ruleId The ID of the rule (its primary key).
 */
fun AllUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long, ruleId: Long) =
    AllUrlParamsRule(id = ruleId, baseRuleId = baseRuleId)
