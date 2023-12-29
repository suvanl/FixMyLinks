package com.suvanl.fixmylinks.ui.components.list

import android.content.res.Configuration
import android.view.SoundEffectConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.semantics
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
    selectedItems: Set<BaseMutationModel>,
    onUpdateSelectedItems: (Set<BaseMutationModel>) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = uiState.rules.sortedByDescending { it.dateModifiedTimestamp }

    fun toggleItemSelection(rule: BaseMutationModel) {
        if (selectedItems.contains(rule)) {
            onUpdateSelectedItems(selectedItems - rule)
        } else {
            onUpdateSelectedItems(selectedItems + rule)
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.semantics { selectableGroup() }
    ) {
        items(items = items) { rule ->
            val view = LocalView.current
            val haptics = LocalHapticFeedback.current
            val isInSelectionMode = selectedItems.isNotEmpty()

            RulesListItem(
                rule = rule,
                isSelected = selectedItems.contains(rule),
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            if (isInSelectionMode) toggleItemSelection(rule) else onClickItem(rule)
                            // Play click sound
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                        },
                        onLongClick = {
                            toggleItemSelection(rule)
                            // Perform long-press haptic feedback
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
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
            selectedItems = setOf(),
            onUpdateSelectedItems = {},
        )
    }
}