package com.suvanl.fixmylinks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.suvanl.fixmylinks.ui.screens.ShareScreen
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme
import com.suvanl.fixmylinks.util.shareTextContent
import com.suvanl.fixmylinks.viewmodel.ShareViewModel

class ShareActivity : ComponentActivity() {

    private val viewModel by viewModels<ShareViewModel> { ShareViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FixMyLinksTheme {
                // Only show ShareScreen if mutatedUri is null. If mutatedUri has a value, there's
                // no need to show the contents of this screen since all that is required will be
                // the share sheet for the mutated URI. This share sheet gets launched in onResume
                // and the tinted backdrop will be the original application the ShareActivity was
                // started from (via a Send intent).
                if (viewModel.mutatedUri == null) {
                    ShareScreen()
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
            updateMutatedContent(intentContent)

            mutatedUri?.let { link -> shareTextContent(content = link) }
        }

        Log.d(TAG, "Content: ${viewModel.receivedContent}\nMutated: ${viewModel.mutatedUri}")
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
