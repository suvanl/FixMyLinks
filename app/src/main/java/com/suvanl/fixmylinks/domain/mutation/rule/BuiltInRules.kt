package com.suvanl.fixmylinks.domain.mutation.rule

import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo

object BuiltInRules {
    private val SpotifyTrackingParamsRule = SpecificUrlParamsMutationModel(
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

    private val InstagramTrackingParamsRule = SpecificUrlParamsMutationModel(
        name = "Instagram - tracking parameters",
        mutationType = MutationType.URL_PARAMS_SPECIFIC,
        triggerDomain = "instagram.com",
        isLocalOnly = true,
        mutationInfo = SpecificUrlParamsMutationInfo(removableParams = listOf("igshid"))
    )

    private val XtoTwitterRule = DomainNameAndAllUrlParamsMutationModel(
        name = "X.com to Twitter.com",
        mutationType = MutationType.DOMAIN_NAME,
        triggerDomain = "x.com",
        isLocalOnly = true,
        mutationInfo = DomainNameMutationInfo(initialDomain = "x.com", targetDomain = "twitter.com")
    )

    val all = listOf(
        SpotifyTrackingParamsRule,
        InstagramTrackingParamsRule,
        XtoTwitterRule
    )
}