package com.suvanl.fixmylinks.ui.components.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.graphics.shapes.Morph
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.ui.graphics.CustomShapes
import com.suvanl.fixmylinks.ui.layout.MorphingPolygon
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData
import com.suvanl.fixmylinks.ui.util.getRoundedPolygonForRule

@Composable
fun RulesListItem(
    rule: BaseMutationModel,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val (polygon, polygonSemantics) = getRoundedPolygonForRule(rule.mutationType)

    val shapeMorphProgress = remember { Animatable(0f) }
    val morphed by remember {
        derivedStateOf {
            Morph(polygon, CustomShapes.CirclePolygon)
        }
    }

    LaunchedEffect(key1 = isSelected) {
        doAnimation(shapeMorphProgress, isSelected)
    }

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
                MorphingPolygon(
                    sizedMorph = morphed,
                    progress = shapeMorphProgress.value,
                    color = if (!isSelected) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .semantics {
                            polygonSemantics()
                            testTag = "Shape for ${rule.mutationType.name}"
                        }
                ) {
                    AnimatedVisibility(
                        visible = isSelected,
                        enter = scaleIn(),
                        exit = scaleOut(),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.padding(4.dp)
        )
    }
}

private suspend fun doAnimation(
    progress: Animatable<Float, AnimationVector1D>,
    isSelected: Boolean
) {
    progress.animateTo(
        targetValue = if (isSelected) 1F else 0F,
        animationSpec = spring(
            dampingRatio = 0.6F,
            stiffness = 50F,
        )
    )
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

@Preview(widthDp = 380, showBackground = true)
@Composable
private fun ItemSelectedPreview() {
    PreviewContainer {
        RulesListItem(
            rule = SpecificUrlParamsMutationModel(
                name = "YouTube - remove playlist association and timestamp, but nothing else",
                mutationType = MutationType.FALLBACK,
                triggerDomain = "youtube.com",
                isLocalOnly = true,
                dateModifiedTimestamp = 1701970463,
                mutationInfo = SpecificUrlParamsMutationInfo(
                    removableParams = listOf("list", "t")
                )
            ),
            isSelected = true,
            modifier = Modifier.padding(8.dp)
        )
    }
}