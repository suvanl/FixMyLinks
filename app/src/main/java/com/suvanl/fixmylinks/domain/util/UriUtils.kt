package com.suvanl.fixmylinks.domain.util

import com.suvanl.fixmylinks.domain.mutation.MutatedUri
import java.net.URI

object UriUtils {

    /**
     * Removes a leading subdomain from a [URI]
     * @param subdomain The subdomain to remove from the [URI]. If the given subdomain is a wildcard
     *  operator (*), the subdomain to remove will be the first subdomain found in the `URI.host` string.
     * @return [URI] without the [subdomain]
     */
    fun URI.removeSubdomain(subdomain: String): URI {
        var removableSubdomain = subdomain
        if (subdomain == "*") {
            removableSubdomain = this.subdomain ?: subdomain
        }

        val hostWithoutSubdomain = host.removePrefix("${removableSubdomain}.")
        val hasUrlParams = !rawQuery.isNullOrEmpty()

        return MutatedUri(
            scheme = scheme,
            host = hostWithoutSubdomain,
            path = path,
            rawQuery = if (hasUrlParams) rawQuery else null
        ).build()
    }

    private val URI.subdomain: String?
        get() = findSubdomain()

    /**
     * Looks for a subdomain in the `URI.host` and if one or more exists, returns the first one.
     * Returns `null` if the `URI.host` doesn't appear to contain a subdomain.
     */
    fun URI.findSubdomain(): String? {
        if (host.isNullOrBlank()) return null

        val parts = host.split(".")
        return if (parts.size >= 3) parts[0] else null
    }
}