package com.suvanl.fixmylinks.domain.mutation

data class DomainNameMutationInfo(
    val from: String,
    val to: String
)

enum class DomainNameMutation(val info: DomainNameMutationInfo) {
    /**
     * `x.com` to `twitter.com`
     */
    X_TO_TWITTER(
        DomainNameMutationInfo(from = "x.com", to = "twitter.com")
    )
}