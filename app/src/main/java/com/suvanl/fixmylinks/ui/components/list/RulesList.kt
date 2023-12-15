package com.suvanl.fixmylinks.ui.components.list

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.ui.screens.RulesScreenUiState
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RulesList(
    uiState: RulesScreenUiState,
    onClickItem: (BaseMutationModel) -> Unit,
    onUpdateSelectedItems: (Set<BaseMutationModel>) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = uiState.rules.sortedByDescending { it.dateModifiedTimestamp }

    // TODO: hoist this state higher?
    var selectedRules by remember { mutableStateOf(setOf<BaseMutationModel>()) }

    fun toggleItemSelection(rule: BaseMutationModel) {
        if (selectedRules.contains(rule)) selectedRules -= rule else selectedRules += rule
        onUpdateSelectedItems(selectedRules)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(items = items) { rule ->
            RulesListItem(
                rule = rule,
                isSelected = selectedRules.contains(rule),
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            val isInSelectionMode = selectedRules.isNotEmpty()
                            if (isInSelectionMode) toggleItemSelection(rule) else onClickItem(rule)
                        },
                        onLongClick = { toggleItemSelection(rule) }
                    )
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
            onClickItem = {},
            onUpdateSelectedItems = {},
        )
    }
}