package com.suvanl.fixmylinks.domain.mutation

object MutationMap {
    val UriToMutationType: Map<String, MutationType> = mapOf(
        "x.com" to MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL,
        "open.spotify.com" to MutationType.URL_PARAMS_SPECIFIC,
        "instagram.com" to MutationType.URL_PARAMS_SPECIFIC
    )
}