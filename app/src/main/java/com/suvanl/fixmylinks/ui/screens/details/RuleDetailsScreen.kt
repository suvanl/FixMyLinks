package com.suvanl.fixmylinks.ui.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults

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
    ) {
        if (showDeleteConfirmation) {
            DeleteConfirmationDialog(
                onDismissRequest = { onDismissDeleteConfirmation() },
                onConfirmDelete = { onDelete() }
            )
        }

        Text(text = "Rule details")
        Text(text = "type: ${rule.mutationType}")
        Text(text = "base rule ID: ${rule.baseRuleId}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Name: ${rule.name}; On: ${rule.triggerDomain}")
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
        modifier = modifier
    )
}