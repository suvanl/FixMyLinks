package com.suvanl.fixmylinks.domain.mutation

import com.suvanl.fixmylinks.util.annotation.Unsupported

enum class MutationType {
    /**
     * Mutate the domain name only (e.g., x.com -> twitter.com)
     */
    DOMAIN_NAME,

    /**
     * Remove all URL parameters from the link
     */
    URL_PARAMS_ALL,

    /**
     * Strip any number of specific URL parameters from the link
     */
    URL_PARAMS_SPECIFIC,

    /**
     * Mutate the domain name and remove all URL parameters from the link
     */
    DOMAIN_NAME_AND_URL_PARAMS_ALL,

    /**
     * Mutate the domain name and strip any number of specific URL parameters from the link
     */
    @Unsupported
    DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC,

    /**
     * Don't mutate anything; return the content that was originally provided
     */
    FALLBACK
}
