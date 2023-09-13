package com.suvanl.fixmylinks.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme
import com.suvanl.fixmylinks.util.shareTextContent
import com.suvanl.fixmylinks.viewmodel.ShareViewModel

@Composable
fun ShareScreen(
    modifier: Modifier = Modifier,
    shareViewModel: ShareViewModel = viewModel(factory = ShareViewModel.Factory)
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Button(
            onClick = {
                shareViewModel.mutatedUri?.let { content ->
                    context.shareTextContent(content)
                }
            },
            enabled = shareViewModel.mutatedUri != null
        ) {
            Text(text = stringResource(id = R.string.open_share_sheet))
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ShareScreenPreview() {
    FixMyLinksTheme {
        ShareScreen()
    }
}