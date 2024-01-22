package com.suvanl.fixmylinks.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.domain.mutation.rule.BuiltInRules
import com.suvanl.fixmylinks.ui.components.list.RulesList
import com.suvanl.fixmylinks.ui.graphics.CustomShapes.ScallopPolygon
import com.suvanl.fixmylinks.ui.layout.Polygon
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData

data class RulesScreenUiState(
    val rules: List<BaseMutationModel> = listOf()
)

data class TabItem(
    val title: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RulesScreen(
    uiState: RulesScreenUiState,
    onClickRuleItem: (BaseMutationModel) -> Unit,
    selectedItems: Set<BaseMutationModel>,
    onUpdateSelectedItems: (Set<BaseMutationModel>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabItems = listOf(
        TabItem(title = stringResource(R.string.custom)),
        TabItem(title = stringResource(R.string.built_in)),
    )

    val hasRules = uiState.rules.isNotEmpty()

    Column(
        modifier = modifier.semantics { contentDescription = "Rules Screen" }
    ) {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val pagerState = rememberPagerState { tabItems.size }

        LaunchedEffect(selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }

        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            if (!pagerState.isScrollInProgress) {
                selectedTabIndex = pagerState.currentPage
            }
        }

        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.padding(vertical = 8.dp)) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = item.title,
                            letterSpacing = LetterSpacingDefaults.Tight
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) { tabIndex ->
            when (tabIndex) {
                0 -> {
                    CustomRulesBody(
                        uiState = uiState,
                        hasRules = hasRules,
                        onClickRuleItem = onClickRuleItem,
                        selectedItems = selectedItems,
                        onUpdateSelectedItems = onUpdateSelectedItems
                    )
                }

                1 -> {
                    BuiltInRulesBody(onClickRuleItem = onClickRuleItem)
                }
            }
        }
    }
}

@Composable
private fun BuiltInRulesBody(
    onClickRuleItem: (BaseMutationModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        RulesList(
            uiState = RulesScreenUiState(rules = BuiltInRules.all),
            onClickItem = onClickRuleItem,
            selectedItems = setOf(),
            onUpdateSelectedItems = {}
        )
    }
}

@Composable
fun CustomRulesBody(
    uiState: RulesScreenUiState,
    hasRules: Boolean,
    onClickRuleItem: (BaseMutationModel) -> Unit,
    selectedItems: Set<BaseMutationModel>,
    onUpdateSelectedItems: (Set<BaseMutationModel>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = if (hasRules) Alignment.Start else Alignment.CenterHorizontally,
        verticalArrangement = if (hasRules) Arrangement.Top else Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (!hasRules) {
            EmptyRulesBody()
            return
        }

        Spacer(modifier = Modifier.height(16.dp))

        RulesList(
            uiState = uiState,
            onClickItem = onClickRuleItem,
            selectedItems = selectedItems,
            onUpdateSelectedItems = onUpdateSelectedItems,
        )
    }
}

@Composable
private fun EmptyRulesBody(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.semantics { testTag = "Empty Rules Body" }
    ) {
        Polygon(
            polygon = ScallopPolygon,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
                .size(208.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Link,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(48.dp)
            )
        }

        Text(
            text = stringResource(R.string.no_rules_added_yet),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun DeleteSelectionConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.delete_selected_rules),
                letterSpacing = LetterSpacingDefaults.Tighter,
            )
        },
        text = {
            Text(text = stringResource(R.string.delete_selected_rules_description))
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmDelete() }
            ) {
                Text(text = stringResource(id = R.string.delete))
            }
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier.semantics { testTag = "Delete Selected Confirmation Dialog" }
    )
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
private fun RulesScreenPreview() {
    PreviewContainer {
        RulesScreen(
            uiState = RulesScreenUiState(rules = PreviewData.previewRules),
            onClickRuleItem = {},
            selectedItems = setOf(),
            onUpdateSelectedItems = {},
        )
    }
}