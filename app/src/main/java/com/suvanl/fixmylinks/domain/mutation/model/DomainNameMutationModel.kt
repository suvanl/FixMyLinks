package com.suvanl.fixmylinks.domain.mutation.model

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
    val mutationInfo: DomainNameMutationInfo,
) : BaseMutationModel