package com.suvanl.fixmylinks.domain.mutation.model

import com.suvanl.fixmylinks.data.local.db.entity.DomainNameRule
import com.suvanl.fixmylinks.domain.mutation.MutationType

data class DomainNameMutationInfo(
    val initialDomain: String,
    val targetDomain: String
)

data class DomainNameMutationModel(
    override val name: String,
    override val mutationType: MutationType = MutationType.DOMAIN_NAME,
    override val triggerDomain: String,
    override val dateModifiedTimestamp: Long? = null,
    override val isLocalOnly: Boolean,
    override val baseRuleId: Long = 0,
    val mutationInfo: DomainNameMutationInfo,
) : BaseMutationModel

fun DomainNameMutationModel.toDatabaseEntity(baseRuleId: Long) = DomainNameRule(
    baseRuleId = baseRuleId,
    initialDomainName = mutationInfo.initialDomain,
    targetDomainName = mutationInfo.targetDomain,
)