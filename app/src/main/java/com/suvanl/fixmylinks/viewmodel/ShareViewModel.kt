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
import com.suvanl.fixmylinks.domain.mutation.util.MutationUtils
import com.suvanl.fixmylinks.domain.util.StringUtils

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
        if (content == null) {
            _mutatedUri = null
            return
        }

        // Extract the first URL found in the received content
        val extractedUrl = StringUtils.extractUrl(content)
        if (extractedUrl == null) {
            _mutatedUri = content
            return
        }

        // Check if the extracted URL is a valid URL. If it isn't, set mutatedUri to the original
        // content that was provided - it will not be modified.
        if (!URLUtil.isValidUrl(extractedUrl.toString())) {
            _mutatedUri = content
            return
        }

        _mutatedUri = mutateUriUseCase(
            uri = extractedUrl,
            mutationType = MutationUtils.determineMutationType(content)
        ).toString()
    }

    companion object {
        @Suppress("Unused")
        private const val TAG = "ShareViewModel"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ShareViewModel(mutateUriUseCase = MutateUriUseCase())
            }
        }
    }
}