package com.suvanl.fixmylinks.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.suvanl.fixmylinks.ui.components.list.RulesList
import com.suvanl.fixmylinks.ui.graphics.CustomShapes.ScallopPolygon
import com.suvanl.fixmylinks.ui.layout.Polygon
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData

data class RulesScreenUiState(
    val rules: List<BaseMutationModel> = listOf()
)

@Composable
fun RulesScreen(
    uiState: RulesScreenUiState,
    onClickRuleItem: (BaseMutationModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    RulesScreenBody(
        uiState = uiState,
        onClickItem = { rule ->
            onClickRuleItem(rule)
        },
        modifier = modifier
    )
}

@Composable
private fun RulesScreenBody(
    uiState: RulesScreenUiState,
    onClickItem: (BaseMutationModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val hasRules = uiState.rules.isNotEmpty()

    Column(
        horizontalAlignment = if (hasRules) Alignment.Start else Alignment.CenterHorizontally,
        verticalArrangement = if (hasRules) Arrangement.Top else Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .semantics { contentDescription = "Rules Screen" }
    ) {
        if (!hasRules) {
            EmptyRulesBody()
            return
        }

        Spacer(modifier = Modifier.height(16.dp))

        RulesList(
            uiState = uiState,
            onClickItem = onClickItem
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
            shape = ScallopPolygon,
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
            uiState = RulesScreenUiState(rules = PreviewData.previewRules),
            onClickItem = { /* do nothing */ },
        )
    }
}