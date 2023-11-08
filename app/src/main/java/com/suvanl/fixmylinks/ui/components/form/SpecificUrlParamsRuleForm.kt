package com.suvanl.fixmylinks.ui.components.form

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.components.form.common.DomainNameField
import com.suvanl.fixmylinks.ui.components.form.common.RuleNameField
import com.suvanl.fixmylinks.ui.util.PreviewContainer

@Composable
fun SpecificUrlParamsRuleForm(
    showHints: Boolean,
    interFieldSpacing: Dp,
    addedParamNames: List<String>,
    onRuleNameChange: (String) -> Unit,
    onDomainNameChange: (String) -> Unit,
    onClickAddParam: () -> Unit,
    onClickDismissParam: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var ruleNameText by rememberSaveable { mutableStateOf("") }
    var domainNameText by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier) {
        // "Rule name"
        RuleNameField(
            text = ruleNameText,
            onValueChange = {
                ruleNameText = it
                onRuleNameChange(it)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(interFieldSpacing))

        // "Domain name"
        DomainNameField(
            value = domainNameText,
            showHints = showHints,
            onValueChange = {
                domainNameText = it
                onDomainNameChange(it)
            },
            isLastFieldInForm = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(interFieldSpacing))

        // "Parameters to remove"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Add names of parameters to remove",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )

            FilledTonalIconButton(
                onClick = onClickAddParam,
                modifier = Modifier.semantics { contentDescription = "Add" }
            ) {
                Icon(imageVector = Icons.Outlined.AddCircleOutline, contentDescription = "Add")
            }
        }

        ParamsChipGroup(
            paramNames = addedParamNames,
            onChipDismiss = onClickDismissParam
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ParamsChipGroup(
    paramNames: List<String>,
    onChipDismiss: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        paramNames.forEachIndexed { index, name ->
            var selected by remember { mutableStateOf(false) }

            InputChip(
                selected = selected,
                onClick = {
                    selected = !selected
                    onChipDismiss(index)
                },
                label = { Text(text = name) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(id = R.string.delete),
                        modifier = Modifier.size(InputChipDefaults.AvatarSize)
                    )
                }
            )
        }
    }
}

@Preview
@Preview(
    name = "Dark",
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun SpecificUrlParamsRuleFormPreview() {
    PreviewContainer {
        SpecificUrlParamsRuleForm(
            interFieldSpacing = 16.dp,
            showHints = true,
            addedParamNames = listOf("param1", "ok", "cool", "t", "calm", "g", "igshid"),
            onRuleNameChange = {},
            onDomainNameChange = {},
            onClickAddParam = {},
            onClickDismissParam = {},
        )
    }
}