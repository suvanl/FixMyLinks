package com.suvanl.fixmylinks.domain.mutation

object MutationMap {
    private val twitterParams = listOf("t", "s")

    val UriToMutationType: Map<String, MutationType> = mapOf(
        "x.com" to MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL,
        "open.spotify.com" to MutationType.URL_PARAMS_SPECIFIC,
        "instagram.com" to MutationType.URL_PARAMS_SPECIFIC
    )

    val UriToRemovableParametersList: Map<String, List<String>> = mapOf(
        "x.com" to twitterParams,
        "twitter.com" to twitterParams,
        "open.spotify.com" to listOf("si", "nd", "_branch_match_id", "_branch_referrer"),
        "instagram.com" to listOf("igshid")
    )
}