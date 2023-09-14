package com.suvanl.fixmylinks.viewmodel

import android.webkit.URLUtil
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.suvanl.fixmylinks.domain.mutation.MutateUriUseCase
import java.net.URI

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

        _mutatedUri = mutateUriUseCase(URI(content)).toString()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ShareViewModel(
                    mutateUriUseCase = MutateUriUseCase()
                ) as T
            }
        }
    }
}