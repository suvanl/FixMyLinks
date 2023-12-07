package com.suvanl.fixmylinks.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.ui.components.layout.PolygonShape
import com.suvanl.fixmylinks.ui.graphics.CustomShapes.ScallopPolygon
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.viewmodel.RulesViewModel
import kotlinx.coroutines.launch

data class RulesScreenUiState(
    val rules: List<BaseMutationModel> = listOf()
)

@Composable
fun RulesScreen(
    onClickRuleItem: (MutationType, Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RulesViewModel = hiltViewModel()
) {
    val uiState by viewModel.rulesScreenUiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    RulesScreenBody(
        uiState = uiState,
        onClickItem = { mutationType, baseRuleId ->
            onClickRuleItem(mutationType, baseRuleId)
        },
        onClickDeleteAll = {
            coroutineScope.launch {
                viewModel.deleteAll()
            }
        },
        onClickDeleteRule = { baseRuleId ->
            coroutineScope.launch {
                viewModel.deleteSingleRule(baseRuleId)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun RulesScreenBody(
    uiState: RulesScreenUiState,
    onClickItem: (MutationType, Long) -> Unit,
    onClickDeleteAll: () -> Unit,
    onClickDeleteRule: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val hasRules = uiState.rules.isNotEmpty()

    Column(
        horizontalAlignment = if (hasRules) Alignment.Start else Alignment.CenterHorizontally,
        verticalArrangement = if (hasRules) Arrangement.Top else Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = "Rules Screen" }
    ) {
        if (!hasRules) {
            EmptyRulesBody()
            return
        }

        RulesList(
            uiState = uiState,
            onClickDeleteAll = onClickDeleteAll,
            onClickDeleteRule = onClickDeleteRule,
            onClickItem = onClickItem
        )
    }
}

@Composable
private fun RulesList(
    uiState: RulesScreenUiState,
    onClickDeleteAll: () -> Unit,
    onClickDeleteRule: (Long) -> Unit,
    onClickItem: (MutationType, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onClickDeleteAll
    ) {
        Text(text = "Delete all")
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(items = uiState.rules.sortedByDescending { it.dateModifiedTimestamp }) { rule ->
            Column {
                Text(
                    text = rule.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "${rule.mutationType.name} on ${rule.triggerDomain}"
                )

                Text(
                    text = "Last modified ${rule.dateModifiedTimestamp}",
                    style = MaterialTheme.typography.labelMedium
                )

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    FilledTonalButton(onClick = { onClickDeleteRule(rule.baseRuleId) }) {
                        Text(text = "Delete this")
                    }
                    FilledTonalButton(
                        onClick = {
                            onClickItem(rule.mutationType, rule.baseRuleId)
                        }
                    ) {
                        Text(text = "View this")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun EmptyRulesBody(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        PolygonShape(
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
        RulesScreenBody(
            uiState = RulesScreenUiState(
                rules = listOf(
                    DomainNameAndAllUrlParamsMutationModel(
                        name = "Google rule",
                        triggerDomain = "google.com",
                        isLocalOnly = true,
                        dateModifiedTimestamp = 1700174822,
                        mutationInfo = DomainNameMutationInfo(
                            initialDomain = "google.com",
                            targetDomain = "google.co.uk"
                        )
                    )
                )
            ),
            onClickItem = { _, _ -> /* do nothing */ },
            onClickDeleteAll = {},
            onClickDeleteRule = {},
        )
    }
}