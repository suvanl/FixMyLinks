package com.suvanl.fixmylinks.domain.mutation.util

import com.suvanl.fixmylinks.domain.mutation.MutationMap
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.util.StringUtils

internal object MutationUtils {
    /**
     * Returns the [MutationType] associated with the first URL found in the [content] string
     */
    fun determineMutationType(content: String?): MutationType {
        if (content == null) return MutationType.FALLBACK

        // Extract the first URL in the content string
        val extractedUrl = StringUtils.extractUrl(content) ?: return MutationType.FALLBACK

        return MutationMap.UriToMutationType.getOrDefault(
            key = extractedUrl.host,
            defaultValue = MutationType.FALLBACK
        )
    }
}