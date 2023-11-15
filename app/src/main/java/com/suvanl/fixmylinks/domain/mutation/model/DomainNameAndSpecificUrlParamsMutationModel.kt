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
    val mutationInfo: DomainNameAndSpecificUrlParamsMutationInfo,
) : BaseMutationModel

fun DomainNameAndSpecificUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long) =
    DomainNameAndSpecificUrlParamsRule(
        baseRuleId = baseRuleId,
        initialDomainName = mutationInfo.initialDomainName,
        targetDomainName = mutationInfo.targetDomainName,
        removableParams = mutationInfo.removableParams,
    )
