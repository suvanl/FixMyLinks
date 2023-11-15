package com.suvanl.fixmylinks.domain.mutation.model

import com.suvanl.fixmylinks.data.local.db.entity.AllUrlParamsRule
import com.suvanl.fixmylinks.domain.mutation.MutationType

data class AllUrlParamsMutationModel(
    override val name: String,
    override val mutationType: MutationType,
    override val triggerDomain: String,
    override val dateModifiedTimestamp: Long? = null,
    override val isLocalOnly: Boolean
) : BaseMutationModel

fun AllUrlParamsMutationModel.toDatabaseEntity(baseRuleId: Long) =
    AllUrlParamsRule(baseRuleId = baseRuleId)
