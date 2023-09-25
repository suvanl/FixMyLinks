package com.suvanl.fixmylinks.domain.mutation

import java.net.URI

data class MutatedUri(
    val scheme: String,
    val host: String,
    val path: String? = null,
    val rawQuery: String? = null
) {
    /**
     * Builds a [java.net.URI] with the data provided in the [MutatedUri] primary constructor
     */
    fun build(): URI {
        if (scheme.isEmpty()) throw IllegalArgumentException("URI scheme must be provided")
        if (host.isEmpty()) throw IllegalArgumentException("URI host must be provided")
        if (!path.isNullOrEmpty() && !path.startsWith("/")) {
            throw IllegalArgumentException("Path must start with '/'")
        }

        val pathStr = if (path.isNullOrEmpty()) "" else path
        val queryStr = if (rawQuery.isNullOrEmpty()) "" else "?$rawQuery"

        return URI("$scheme://$host$pathStr$queryStr")
    }
}