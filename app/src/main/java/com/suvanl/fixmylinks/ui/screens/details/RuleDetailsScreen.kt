package com.suvanl.fixmylinks.ui.screens.details

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.ui.components.list.RuleDetailsList
import com.suvanl.fixmylinks.ui.components.list.RuleDetailsListItemState
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData
import com.suvanl.fixmylinks.ui.util.getShapeForRule

/**
 * Displays additional info about the rule compared to that of
 * [com.suvanl.fixmylinks.ui.screens.RulesScreen].
 *
 * @param rule The rule data to be displayed on this screen.
 * @param showDeleteConfirmation Whether to display the "Delete rule" [AlertDialog].
 * @param onDismissDeleteConfirmation When the "Delete rule" [AlertDialog] is dismissed.
 * @param onDelete When the user clicks the `confirmButton` on the "Delete rule" dialog.
 * @param modifier The default modifier for this screen.
 */
@Composable
fun RuleDetailsScreen(
    rule: BaseMutationModel?,
    showDeleteConfirmation: Boolean,
    onDismissDeleteConfirmation: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (rule == null) {
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .semantics { contentDescription = "Rule Details Screen" }
    ) {
        if (showDeleteConfirmation) {
            DeleteConfirmationDialog(
                onDismissRequest = { onDismissDeleteConfirmation() },
                onConfirmDelete = { onDelete() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rule type description
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = getShapeForRule(rule.mutationType).shape
                    )
            )

            Column(
                modifier = Modifier.weight(1F)
            ) {
                val ruleTypeInfo = getRuleTypeInPresentSimpleTense(ruleType = rule.mutationType)
                var lineCount by remember { mutableIntStateOf(0) }

                if (lineCount < 2) {
                    Text(text = "This rule", style = MaterialTheme.typography.bodyMedium)
                }

                Text(
                    text = ruleTypeInfo,
                    maxLines = 2,
                    letterSpacing = when (ruleTypeInfo.length) {
                        in 20..29 -> LetterSpacingDefaults.Tight
                        in 30..39 -> LetterSpacingDefaults.Tighter
                        in 40..Int.MAX_VALUE -> LetterSpacingDefaults.ExtraTight
                        else -> 0.sp
                    },
                    onTextLayout = { lineCount = it.lineCount }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // General details
        RuleDetailsExpandingCard(rule = rule)
    }
}

@Composable
private fun RuleDetailsExpandingCard(
    rule: BaseMutationModel,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(true) }

    val arrowRotationAngle by animateFloatAsState(
        targetValue = if (!expanded) 0F else 180F,
        label = "arrow rotation",
        animationSpec = tween(
            durationMillis = 400,
            easing = EaseOutExpo,
        )
    )

    val cardShapeCornerRadius by animateDpAsState(
        targetValue = if (expanded) 50.dp else 30.dp,
        label = "card shape corner radius",
        animationSpec = tween(
            durationMillis = 700,
            delayMillis = 150
        )
    )

    Card(
        shape = RoundedCornerShape(cardShapeCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow,
                    )
                )
        ) {
            // Heading / expand button row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
                    .clickable(
                        // Hide ripple effect
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        expanded = !expanded
                    }
            ) {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
                Text(
                    text = "General",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1F)
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        )
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ExpandMore,
                        contentDescription = if (expanded) {
                            stringResource(R.string.collapse_details)
                        } else {
                            stringResource(R.string.expand_details)
                        },
                        modifier = Modifier.rotate(arrowRotationAngle)
                    )
                }
            }

            // Details content
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                RuleDetailsList(
                    details = listOf(
                        RuleDetailsListItemState(
                            headlineText = rule.name,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Description,
                                    contentDescription = stringResource(R.string.content_desc_rule_name)
                                )
                            },
                        ),
                        RuleDetailsListItemState(
                            headlineText = rule.triggerDomain,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Language,
                                    contentDescription = stringResource(R.string.content_desc_domain_name)
                                )
                            },
                        ),
                        RuleDetailsListItemState(
                            headlineText = if (rule.isLocalOnly) {
                                stringResource(R.string.not_backed_up)
                            } else {
                                stringResource(R.string.backed_up)
                            },
                            leadingIcon = {
                                if (rule.isLocalOnly) {
                                    Icon(
                                        imageVector = Icons.Outlined.CloudOff,
                                        contentDescription = stringResource(R.string.content_desc_backup_off)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.Backup,
                                        contentDescription = stringResource(R.string.content_desc_backup_on)
                                    )
                                }
                            },
                        ),
                        RuleDetailsListItemState(
                            headlineText = "A week ago",
                            supportingText = rule.dateModifiedTimestamp.toString(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Schedule,
                                    contentDescription = stringResource(R.string.content_desc_last_modified)
                                )
                            },
                        ),
                    )
                )
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Delete rule",
                letterSpacing = LetterSpacingDefaults.Tighter,
            )
        },
        text = {
            Text(text = "Are you sure you want to delete this rule? This action can't be undone.")
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
        modifier = modifier.semantics { testTag = "Delete Confirmation Dialog" }
    )
}

@Composable
private fun getRuleTypeInPresentSimpleTense(ruleType: MutationType) = when (ruleType) {
    MutationType.DOMAIN_NAME -> {
        stringResource(id = R.string.mt_domain_name_present_simple_tense)
    }

    MutationType.URL_PARAMS_ALL -> {
        stringResource(id = R.string.mt_url_params_all_present_simple_tense)
    }

    MutationType.URL_PARAMS_SPECIFIC -> {
        stringResource(id = R.string.mt_url_params_specific_present_simple_tense)
    }

    MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
        stringResource(id = R.string.mt_domain_name_and_url_params_all_present_simple_tense)
    }

    MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC -> {
        TODO("Not user-selectable yet")
    }

    MutationType.FALLBACK -> {
        throw IllegalArgumentException("MutationType.FALLBACK should not be user-selectable")
    }
}

@Preview(
    showBackground = true,
    widthDp = 400
)
@Preview(
    name = "Dark",
    showBackground = true,
    widthDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RuleDetailsScreenPreview() {
    PreviewContainer {
        RuleDetailsScreen(
            rule = PreviewData.previewRules.first(),
            showDeleteConfirmation = false,
            onDismissDeleteConfirmation = { },
            onDelete = { },
        )
    }
}