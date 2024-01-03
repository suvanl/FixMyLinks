package com.suvanl.fixmylinks.domain.mutation

import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.util.UriUtils.findSubdomain
import java.net.URI

class ReplaceDomainNameUseCase {
    operator fun invoke(
        uri: URI,
        mutationInfo: DomainNameMutationInfo,
        useWwwSubdomain: Boolean
    ): URI {
        uri.apply {
            val hasWildcardSubdomain = mutationInfo.initialDomain.startsWith("*.")

            val domain = if (hasWildcardSubdomain) {
                val subdomain = findSubdomain()
                if (!subdomain.isNullOrBlank() && mutationInfo.targetDomain.startsWith("*.")) {
                    // Retain the subdomain present in `uri` if the targetDomain starts with the
                    // wildcard operator (*.)
                    "${subdomain}.${mutationInfo.targetDomain.removePrefix("*.")}"
                } else {
                    mutationInfo.targetDomain
                }
            } else if (useWwwSubdomain) {
                "www.${mutationInfo.targetDomain}"
            } else {
                mutationInfo.targetDomain
            }

            return UriBuilder(
                scheme = scheme,
                host = domain,
                path = path,
                rawQuery = rawQuery,
                fragment = fragment,
            ).build()
        }
    }
}