package com.suvanl.fixmylinks.domain.mutation.rule

import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel

object BuiltInRules {
    private val SpotifyTrackingParamsRule = SpecificUrlParamsMutationModel(
        name = "Spotify - tracking parameters",
        triggerDomain = "spotify.com",
        isLocalOnly = true,
        isEnabled = true,
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
        triggerDomain = "instagram.com",
        isLocalOnly = true,
        isEnabled = true,
        mutationInfo = SpecificUrlParamsMutationInfo(removableParams = listOf("igshid"))
    )

    private val XtoTwitterRule = DomainNameAndAllUrlParamsMutationModel(
        name = "X.com to Twitter.com",
        triggerDomain = "x.com",
        isLocalOnly = true,
        isEnabled = true,
        mutationInfo = DomainNameMutationInfo(initialDomain = "x.com", targetDomain = "twitter.com")
    )

    val all = listOf(
        SpotifyTrackingParamsRule,
        InstagramTrackingParamsRule,
        XtoTwitterRule
    )
}