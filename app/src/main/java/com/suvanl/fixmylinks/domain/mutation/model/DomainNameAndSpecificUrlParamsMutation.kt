package com.suvanl.fixmylinks.domain.mutation.model

import com.suvanl.fixmylinks.domain.mutation.MutationType

data class DomainNameAndSpecificUrlParamsMutationInfo(
    val initialDomainName: String,
    val targetDomainName: String,
    val removableParams: List<String>,
)

data class DomainNameAndSpecificUrlParamsMutation(
    override val name: String,
    override val mutationType: MutationType,
    override val triggerDomain: String,
    override val dateModifiedTimestamp: Long? = null,
    override val isLocalOnly: Boolean,
    val mutationInfo: DomainNameAndSpecificUrlParamsMutationInfo,
) : BaseMutation
