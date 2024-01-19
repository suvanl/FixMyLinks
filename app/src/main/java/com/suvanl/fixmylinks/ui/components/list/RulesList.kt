package com.suvanl.fixmylinks.ui.components.list

import android.content.res.Configuration
import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.ui.screens.RulesScreenUiState
import com.suvanl.fixmylinks.ui.theme.active_status
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData
import kotlinx.coroutines.delay

enum class RulesListCategory { ACTIVE, INACTIVE }

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
        val activeRules = items.filter { it.isEnabled }
        val inactiveRules = items.filter { !it.isEnabled }
        val isInSelectionMode = selectedItems.isNotEmpty()

        @Composable
        fun Modifier.clickableItem(rule: BaseMutationModel): Modifier {
            val view = LocalView.current
            val haptics = LocalHapticFeedback.current

            return combinedClickable(
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
        }

        if (activeRules.isNotEmpty()) {
            item { CategoryHeading(category = RulesListCategory.ACTIVE) }
            items(items = activeRules) { rule ->
                RulesListItem(
                    rule = rule,
                    isSelected = selectedItems.contains(rule),
                    modifier = Modifier.clickableItem(rule)
                )
            }
        }

        if (inactiveRules.isNotEmpty()) {
            item { CategoryHeading(category = RulesListCategory.INACTIVE) }
            items(items = inactiveRules) { rule ->
                RulesListItem(
                    rule = rule,
                    isSelected = selectedItems.contains(rule),
                    modifier = Modifier.clickableItem(rule)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CategoryHeading(
    category: RulesListCategory,
    modifier: Modifier = Modifier
) {
    val isActive = category == RulesListCategory.ACTIVE

    var showStatusCircle by remember { mutableStateOf(true) }
    val rowSpacing by animateDpAsState(
        targetValue = if (showStatusCircle) 8.dp else 0.dp,
        animationSpec = tween(easing = EaseOutQuint),
        label = "row arrangement spacedBy value"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "CategoryHeading infiniteTransition")
    val activeCircleAlpha by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 700,
                delayMillis = 200,
                easing = EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CategoryHeading active circle alpha"
    )
    val activeCircleColor = active_status.copy(alpha = activeCircleAlpha)
    val inactiveCircleColor = MaterialTheme.colorScheme.outline

    LaunchedEffect(key1 = Unit) {
        delay(7000)
        showStatusCircle = false
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(rowSpacing),
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        AnimatedVisibility(
            visible = showStatusCircle,
            enter = fadeIn()
                    + slideInHorizontally(initialOffsetX = { w -> -w * 2 })
                    + expandHorizontally(expandFrom = Alignment.Start, clip = false),
            exit = fadeOut()
                    + slideOutHorizontally(targetOffsetX = { w -> -w * 2 })
                    + shrinkHorizontally(shrinkTowards = Alignment.Start, clip = false),
        ) {
            Canvas(modifier = Modifier.size(8.dp)) {
                drawCircle(color = if (isActive) activeCircleColor else inactiveCircleColor)
            }
        }

        Text(
            text = stringResource(if (isActive) R.string.active_rules else R.string.inactive_rules),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
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