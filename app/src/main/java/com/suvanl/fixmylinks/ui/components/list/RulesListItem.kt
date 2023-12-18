package com.suvanl.fixmylinks.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData
import com.suvanl.fixmylinks.ui.util.getShapeForRule

@Composable
fun RulesListItem(
    rule: BaseMutationModel,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (!isSelected) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.tertiaryContainer
            }
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.semantics {
            selected = isSelected
            testTag = "Rules List Item ${rule.baseRuleId}"
        }
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
                            color = if (!isSelected) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.tertiary
                            }
                        )
                        .size(40.dp)
                        .semantics { testTag = "Shape for ${rule.mutationType.name}" }
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Preview(widthDp = 380, showBackground = true)
@Composable
private fun ItemPreview() {
    PreviewContainer {
        RulesListItem(
            rule = PreviewData.previewRules[1],
            isSelected = false,
            modifier = Modifier.padding(8.dp)
        )
    }
}