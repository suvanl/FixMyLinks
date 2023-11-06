package com.suvanl.fixmylinks.viewmodel

import android.webkit.URLUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.suvanl.fixmylinks.domain.mutation.MutateUriUseCase
import com.suvanl.fixmylinks.domain.util.StringUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShareViewModel(
    private val mutateUriUseCase: MutateUriUseCase
) : ViewModel() {
    private val _receivedContent = MutableStateFlow<String?>(null)
    val receivedContent = _receivedContent.asStateFlow()

    private val _mutatedUri = MutableStateFlow<String?>(null)
    val mutatedUri = _mutatedUri.asStateFlow()

    fun updateUri(content: String?) {
        _receivedContent.value = content
    }

    fun generateMutatedUri(content: String?) {
        if (content == null) {
            _mutatedUri.value = null
            return
        }

        // Extract the first URL found in the received content
        val extractedUrl = StringUtils.extractUrl(content)
        if (extractedUrl == null) {
            _mutatedUri.value = content
            return
        }

        // Check if the extracted URL is a valid URL. If it isn't, set mutatedUri to the original
        // content that was provided - it will not be modified.
        if (!URLUtil.isValidUrl(extractedUrl.toString())) {
            _mutatedUri.value = content
            return
        }

        _mutatedUri.value = mutateUriUseCase(extractedUrl).toString()
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