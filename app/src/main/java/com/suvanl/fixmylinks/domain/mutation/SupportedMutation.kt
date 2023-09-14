package com.suvanl.fixmylinks.domain.mutation

data class MutationInfo(
    val from: String,
    val to: String
)

enum class SupportedMutation(val info: MutationInfo) {
    /**
     * `x.com` to `twitter.com`
     */
    X_TO_TWITTER(
        MutationInfo(from = "x.com", to = "twitter.com")
    )
}