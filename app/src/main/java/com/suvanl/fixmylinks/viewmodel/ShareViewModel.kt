package com.suvanl.fixmylinks.viewmodel

import android.webkit.URLUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.mutation.MutateUriUseCase
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.domain.util.StringUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ShareViewModel @Inject constructor(
    private val mutateUriUseCase: MutateUriUseCase,
    private val rulesRepository: RulesRepository
) : ViewModel() {
    private val _receivedContent = MutableStateFlow<String?>(null)
    val receivedContent = _receivedContent.asStateFlow()

    private val _mutatedUri = MutableStateFlow<String?>(null)
    val mutatedUri = _mutatedUri.asStateFlow()

    // TODO: find a solution that avoids use of `runBlocking` that doesn't result in multiple share
    //  sheets being created, while successfully passing the latest list of custom rules to
    //  MutateUriUseCase. Perhaps ignore the first emission (when the list is empty rather than
    //  containing the saved custom rules) using `<Flow>.drop`? See
    //  https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/drop.html
    //  for more info.
    //  Since no application UI is rendered (just the system share sheet) in the activity that uses
    //  this ViewModel (ShareActivity), there isn't a possibility of application UI frame drops.
    //  However, we don't want this to somehow impact the share sheet's performance. Based on manual
    //  testing, no performance penalty is visible, although we might eventually run into situations
    //  where this is the case.
    private val _customRules: StateFlow<List<BaseMutationModel>> =
        rulesRepository.getAllRules()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = TIMEOUT_MILLIS),
                initialValue = runBlocking { rulesRepository.getAllRules().first() }
            )

    fun updateUri(content: String) {
        _receivedContent.value = content
    }

    fun generateMutatedUri(content: String) {
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

        _mutatedUri.value = mutateUriUseCase(extractedUrl, _customRules.value).toString()
    }

    companion object {
        @Suppress("Unused")
        private const val TAG = "ShareViewModel"

        private const val TIMEOUT_MILLIS = 5000L
    }
}