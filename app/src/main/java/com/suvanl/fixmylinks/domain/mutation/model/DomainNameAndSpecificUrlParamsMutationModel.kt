package com.suvanl.fixmylinks.domain.mutation.model

import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndSpecificUrlParamsRule
import com.suvanl.fixmylinks.domain.mutation.MutationType

data class DomainNameAndSpecificUrlParamsMutationInfo(
    val initialDomainName: String,
    val targetDomainName: String,
    val removableParams: List<String>,
)

data class DomainNameAndSpecificUrlParamsMutationModel(
    override val name: String,
    override val mutationType: MutationType = MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC,
    override val triggerDomain: String,
    override val dateModifiedTimestamp: Long? = null,
    override val isLocalOnly: Boolean,
    override val isEnabled: Boolean,
    override val baseRuleId: Long = 0,
    val mutationInfo: DomainNameAndSpecificUrlParamsMutationInfo,
) : BaseMutationModel

/**
 * Transforms this domain model to a database entity.
 * @param baseRuleId The ID of the rule's related BaseRule.
 */
fun DomainNameAndSpecificUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long) =
    DomainNameAndSpecificUrlParamsRule(
        baseRuleId = baseRuleId,
        initialDomainName = mutationInfo.initialDomainName,
        targetDomainName = mutationInfo.targetDomainName,
        removableParams = mutationInfo.removableParams,
    )

/**
 * Transforms this domain model to a database entity.
 * @param baseRuleId The ID of the rule's related BaseRule.
 * @param ruleId The ID of the rule (its primary key).
 */
fun DomainNameAndSpecificUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long, ruleId: Long) =
    DomainNameAndSpecificUrlParamsRule(
        id = ruleId,
        baseRuleId = baseRuleId,
        initialDomainName = mutationInfo.initialDomainName,
        targetDomainName = mutationInfo.targetDomainName,
        removableParams = mutationInfo.removableParams,
    )
