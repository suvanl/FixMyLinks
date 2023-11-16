package com.suvanl.fixmylinks.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.util.shareTextContent
import com.suvanl.fixmylinks.viewmodel.ShareViewModel

@Composable
fun ShareScreen(
    modifier: Modifier = Modifier,
    shareViewModel: ShareViewModel,
    shareSheetButtonIsEnabled: Boolean =
        shareViewModel.mutatedUri.collectAsStateWithLifecycle().value != null
) {
    val context = LocalContext.current

    ShareScreenBody(
        shareSheetButtonIsEnabled = shareSheetButtonIsEnabled,
        onClickOpenShareSheet = {
            shareViewModel.mutatedUri.value?.let { content ->
                context.shareTextContent(content)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun ShareScreenBody(
    shareSheetButtonIsEnabled: Boolean,
    onClickOpenShareSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            Button(
                onClick = onClickOpenShareSheet,
                enabled = shareSheetButtonIsEnabled
            ) {
                Text(text = stringResource(id = R.string.open_share_sheet))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ShareScreenPreview() {
    PreviewContainer {
        ShareScreenBody(
            shareSheetButtonIsEnabled = true,
            onClickOpenShareSheet = {}
        )
    }
}