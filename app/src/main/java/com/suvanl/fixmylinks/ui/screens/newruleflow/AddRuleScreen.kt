package com.suvanl.fixmylinks.ui.screens.newruleflow

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.util.PreviewContainer

@Composable
fun AddRuleScreen(
    mutationType: MutationType,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Add Rule Screen" }
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add a new ${mutationType.name} rule",
            modifier = modifier.padding(horizontal = 16.dp)
        )

        Button(onClick = onDoneClick) {
            Text(text = "Done")
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320
)
@Preview(
    name = "Dark",
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AddRuleScreenPreview() {
    PreviewContainer {
        AddRuleScreen(
            mutationType = MutationType.URL_PARAMS_SPECIFIC,
            onDoneClick = { }
        )
    }
}