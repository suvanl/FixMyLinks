package com.suvanl.fixmylinks.ui.components.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.ui.screens.RulesScreenUiState
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData

@Composable
fun RulesList(
    uiState: RulesScreenUiState,
    onClickItem: (BaseMutationModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = uiState.rules.sortedByDescending { it.dateModifiedTimestamp }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(items = items) { rule ->
            RulesListItem(
                rule = rule,
                onClick = { onClickItem(it) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 380
)
@Preview(
    name = "Dark",
    showBackground = true,
    widthDp = 380,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RulesListPreview() {
    PreviewContainer {
        RulesList(
            uiState = RulesScreenUiState(rules = PreviewData.previewRules),
            onClickItem = { /* do nothing */ }
        )
    }
}