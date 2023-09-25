package com.suvanl.fixmylinks.domain.util

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

        return URI("$scheme://${hostWithoutSubdomain}${path}${if (hasUrlParams) "?${rawQuery}" else ""}")
    }
}