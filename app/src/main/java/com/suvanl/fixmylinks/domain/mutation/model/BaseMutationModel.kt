package com.suvanl.fixmylinks.domain.mutation.model

import com.suvanl.fixmylinks.data.local.db.entity.BaseRule
import com.suvanl.fixmylinks.domain.mutation.MutationType

// TODO: maybe make mutationInfo a property of BaseMutation rather than having every data class
//  implementing this interface define its own mutationInfo property. May then need to use
//  annotations + KSP to ensure that only certain types can be used as allowed types of mutationInfo
//  (such as a `@MutationInfo`) annotation, perhaps with a parameter that takes in the class this
//  mutationInfo belongs to, such as `@MutationInfo(AllUrlParamsMutation::class)`.
interface BaseMutationModel {
    /**
     * The user-defined name of the rule for easy identification
     */
    val name: String

    /**
     * The type of mutation that should be performed on the URI
     */
    val mutationType: MutationType

    /**
     * The domain name that should trigger the link mutation rule, e.g. `www.google.com`
     */
    val triggerDomain: String

    /**
     * Unix timestamp of when the rule was last modified
     */
    val dateModifiedTimestamp: Long?

    /**
     * Whether the rule should only be stored locally. If `false`, the rule can be sent to the
     * server over the network for backup purposes.
     */
    val isLocalOnly: Boolean
}

fun BaseMutationModel.toDatabaseEntity() = BaseRule(
    title = name,
    mutationType = mutationType,
    triggerDomain = triggerDomain,
    dateModified = dateModifiedTimestamp ?: (System.currentTimeMillis() / 1000),
    isLocalOnly = isLocalOnly,
    authorId = "local_user"
)