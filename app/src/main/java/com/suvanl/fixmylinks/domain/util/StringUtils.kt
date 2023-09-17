package com.suvanl.fixmylinks.domain.util

import java.net.URI
import java.util.regex.Pattern

internal object StringUtils {
    /**
     * Returns the first [URI] in the given content string if one exists. Otherwise returns null.
     */
    fun extractUrl(content: String): URI? {
        val urlPattern = Pattern.compile("https?://[A-Za-z0-9-_.~:/?#\\[\\]@!$&'()*+,;=%]+")
        val matcher = urlPattern.matcher(content)

        // Check if a URL is found
        if (matcher.find()) {
            // Extract the first URL found in the content string
            val url = matcher.group()

            // Remove trailing non-URL characters (i.e., punctuation)
            val cleanUrl = url.trimEnd{ !it.isLetterOrDigit() }

            // Return the extracted URL as a URI object
            return URI(cleanUrl)
        }

        // If a URL cannot be found in the content string, return null
        return null
    }
}