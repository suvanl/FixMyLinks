package com.suvanl.fixmylinks.domain.util

import com.suvanl.fixmylinks.domain.mutation.MutatedUri
import java.net.URI

object UriUtils {

    /**
     * Removes a leading subdomain from a [URI]
     * @param subdomain The subdomain to remove from the [URI]
     * @return [URI] without the [subdomain]
     */
    fun URI.removeSubdomain(subdomain: String): URI {
        val hostWithoutSubdomain = host.removePrefix("${subdomain}.")
        val hasUrlParams = rawQuery.isNotEmpty()

        return MutatedUri(
            scheme = scheme,
            host = hostWithoutSubdomain,
            path = path,
            rawQuery = if (hasUrlParams) rawQuery else null
        ).build()
    }
}