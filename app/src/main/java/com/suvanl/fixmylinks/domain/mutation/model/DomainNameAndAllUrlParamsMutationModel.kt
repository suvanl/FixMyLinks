package com.suvanl.fixmylinks.domain.mutation.model

import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndAllUrlParamsRule
import com.suvanl.fixmylinks.domain.mutation.MutationType

data class DomainNameAndAllUrlParamsMutationModel(
    override val name: String,
    override val mutationType: MutationType = MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL,
    override val triggerDomain: String,
    override val dateModifiedTimestamp: Long? = null,
    override val isLocalOnly: Boolean,
    override val isEnabled: Boolean,
    override val baseRuleId: Long = 0,
    val mutationInfo: DomainNameMutationInfo
) : BaseMutationModel

/**
 * Transforms this domain model to a database entity.
 * @param baseRuleId The ID of the rule's related BaseRule.
 */
fun DomainNameAndAllUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long) =
    DomainNameAndAllUrlParamsRule(
        baseRuleId = baseRuleId,
        initialDomainName = mutationInfo.initialDomain,
        targetDomainName = mutationInfo.targetDomain,
    )

/**
 * Transforms this domain model to a database entity.
 * @param baseRuleId The ID of the rule's related BaseRule.
 * @param ruleId The ID of the rule (its primary key).
 */
fun DomainNameAndAllUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long, ruleId: Long) =
    DomainNameAndAllUrlParamsRule(
        id = ruleId,
        baseRuleId = baseRuleId,
        initialDomainName = mutationInfo.initialDomain,
        targetDomainName = mutationInfo.targetDomain,
    )