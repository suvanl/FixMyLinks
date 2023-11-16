package com.suvanl.fixmylinks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suvanl.fixmylinks.di.AppViewModelProvider
import com.suvanl.fixmylinks.ui.screens.ShareScreen
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme
import com.suvanl.fixmylinks.util.shareTextContent
import com.suvanl.fixmylinks.viewmodel.ShareViewModel

class ShareActivity : ComponentActivity() {

    private val viewModel: ShareViewModel by viewModels { AppViewModelProvider.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            FixMyLinksTheme {
                // Only show ShareScreen if mutatedUri is null. If mutatedUri has a value, there's
                // no need to show the contents of this screen since all that is required will be
                // the share sheet for the mutated URI. This share sheet gets launched in onResume
                // and the tinted backdrop will be the original application the ShareActivity was
                // started from (via a Send intent).
                val mutatedUri by viewModel.mutatedUri.collectAsStateWithLifecycle()
                if (mutatedUri == null) {
                    ShareScreen(shareViewModel = viewModel)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        val intentContent = intent.getStringExtra(Intent.EXTRA_TEXT)
        viewModel.apply {
            updateUri(intentContent)
            generateMutatedUri(intentContent)

            mutatedUri.value?.let { link -> shareTextContent(content = link) }
        }

        Log.d(
            TAG,
            "Content: ${viewModel.receivedContent.value}\nMutated: ${viewModel.mutatedUri.value}"
        )
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    companion object {
        private const val TAG = "ShareActivity"
    }
}
