package com.suvanl.fixmylinks.viewmodel

import android.webkit.URLUtil
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.suvanl.fixmylinks.domain.mutation.MutateUriUseCase
import com.suvanl.fixmylinks.domain.mutation.MutationMap
import com.suvanl.fixmylinks.domain.mutation.MutationType
import java.net.URI
import java.util.regex.Pattern

class ShareViewModel(
    private val mutateUriUseCase: MutateUriUseCase
) : ViewModel() {
    private var _receivedContent: String? by mutableStateOf(null)
    val receivedContent: String?
        get() = _receivedContent

    private var _mutatedUri: String? by mutableStateOf(null)
    val mutatedUri: String?
        get() = _mutatedUri


    fun updateUri(content: String?) {
        _receivedContent = content
    }

    fun updateMutatedContent(content: String?) {
        // Check if the received content is a valid URL. If it isn't, set mutatedUri to the original
        // content that was provided - it will not be modified.
        if (!URLUtil.isValidUrl(content)) {
            _mutatedUri = content
            return
        }

        _mutatedUri = mutateUriUseCase(URI(content), determineMutationType(content)).toString()
    }

    /**
     * Returns the first [URI] in the given content string if one exists. Otherwise returns null.
     */
    private fun extractUrl(content: String?): URI? {
        if (content == null) return null

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

        // Return null if a URL cannot be found in the content string
        return null
    }

    private fun determineMutationType(content: String?): MutationType {
        if (content == null) return MutationType.FALLBACK

        // Extract the first URL in the content string
        val extractedUrl = extractUrl(content) ?: return MutationType.FALLBACK

        return MutationMap.UriToMutationType.getOrDefault(
            key = extractedUrl.host,
            defaultValue = MutationType.FALLBACK
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ShareViewModel(mutateUriUseCase = MutateUriUseCase())
            }
        }
    }
}