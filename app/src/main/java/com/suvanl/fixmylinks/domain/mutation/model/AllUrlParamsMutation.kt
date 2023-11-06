package com.suvanl.fixmylinks.domain.mutation.model

import com.suvanl.fixmylinks.domain.mutation.MutationType

data class AllUrlParamsMutation(
    override val name: String,
    override val mutationType: MutationType,
    override val triggerDomain: String,
    override val dateModifiedTimestamp: Long? = null,
    override val isLocalOnly: Boolean
) : BaseMutation
