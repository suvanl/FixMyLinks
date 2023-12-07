package com.suvanl.fixmylinks.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.ui.graphics.CustomShapes.CloverShape
import com.suvanl.fixmylinks.ui.graphics.CustomShapes.DeltaShape
import com.suvanl.fixmylinks.ui.graphics.CustomShapes.EightPointStarShape
import com.suvanl.fixmylinks.ui.graphics.CustomShapes.ScallopShape
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData

/**
 * Returns the shape used to represent the given rule type in the UI.
 */
private fun getShapeForRule(ruleType: MutationType): Shape = when (ruleType) {
    MutationType.DOMAIN_NAME -> RoundedCornerShape(10.dp)
    MutationType.URL_PARAMS_ALL -> ScallopShape
    MutationType.URL_PARAMS_SPECIFIC -> DeltaShape
    MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> CloverShape
    MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC -> EightPointStarShape
    MutationType.FALLBACK -> CircleShape
}

@Composable
fun RulesListItem(
    rule: BaseMutationModel,
    onClick: (MutationType, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = rule.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    letterSpacing = LetterSpacingDefaults.Tight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = rule.triggerDomain,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            leadingContent = {
                Box(
                    modifier = Modifier
                        .background(
                            shape = getShapeForRule(rule.mutationType),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        .size(40.dp)
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .padding(4.dp)
                .clickable {
                    onClick(rule.mutationType, rule.baseRuleId)
                }
        )
    }
}

@Preview(widthDp = 380, showBackground = true)
@Composable
private fun ItemPreview() {
    PreviewContainer {
        RulesListItem(
            rule = PreviewData.previewRules[1],
            onClick = { _, _ -> /* do nothing */ },
            modifier = Modifier.padding(8.dp)
        )
    }
}