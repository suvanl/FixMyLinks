package com.suvanl.fixmylinks.domain.mutation.rule

import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutation
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutation
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo

object BuiltInRules {
    private val SpotifyTrackingParamsRule = SpecificUrlParamsMutation(
        name = "Spotify - tracking parameters",
        mutationType = MutationType.URL_PARAMS_SPECIFIC,
        triggerDomain = "spotify.com",
        isLocalOnly = true,
        mutationInfo = SpecificUrlParamsMutationInfo(
            removableParams = listOf(
                "si",
                "nd",
                "_branch_match_id",
                "_branch_referrer"
            )
        )
    )

    private val InstagramTrackingParamsRule = SpecificUrlParamsMutation(
        name = "Instagram - tracking parameters",
        mutationType = MutationType.URL_PARAMS_SPECIFIC,
        triggerDomain = "instagram.com",
        isLocalOnly = true,
        mutationInfo = SpecificUrlParamsMutationInfo(removableParams = listOf("igshid"))
    )

    private val XtoTwitterRule = DomainNameAndAllUrlParamsMutation(
        name = "X.com to Twitter.com",
        mutationType = MutationType.DOMAIN_NAME,
        triggerDomain = "x.com",
        isLocalOnly = true,
        mutationInfo = DomainNameMutationInfo(from = "x.com", to = "twitter.com")
    )

    val all = listOf(
        SpotifyTrackingParamsRule,
        InstagramTrackingParamsRule,
        XtoTwitterRule
    )
}