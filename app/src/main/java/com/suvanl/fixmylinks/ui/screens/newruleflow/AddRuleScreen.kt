package com.suvanl.fixmylinks.ui.screens.newruleflow

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.components.form.AllUrlParamsRuleForm
import com.suvanl.fixmylinks.ui.components.form.DomainNameRuleForm
import com.suvanl.fixmylinks.ui.components.form.SpecificUrlParamsRuleForm
import com.suvanl.fixmylinks.ui.components.form.common.ParameterNameField
import com.suvanl.fixmylinks.ui.components.list.SwitchList
import com.suvanl.fixmylinks.ui.components.list.SwitchListItemState
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddAllUrlParamsRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddDomainNameRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddSpecificUrlParamsRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.NewRuleFlowViewModel

@Composable
fun AddRuleScreen(
    showSaveButton: Boolean,
    showFormFieldHints: Boolean,
    mutationType: MutationType,
    viewModel: NewRuleFlowViewModel?,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRuleEnabled by rememberSaveable { mutableStateOf(true) }
    var isBackupEnabled by rememberSaveable { mutableStateOf(false) }

    val ruleOptions = listOf(
        SwitchListItemState(
            headlineText = stringResource(id = R.string.enable),
            supportingText = stringResource(R.string.enable_rule_supporting_text),
            leadingIcon = Icons.Outlined.CheckCircle,
            isSwitchChecked = isRuleEnabled,
            onSwitchCheckedChange = { isRuleEnabled = it },
        ),
        SwitchListItemState(
            headlineText = stringResource(R.string.backup_to_cloud),
            supportingText = stringResource(R.string.backup_to_cloud_supporting_text),
            leadingIcon = Icons.Outlined.Backup,
            isSwitchChecked = isBackupEnabled,
            onSwitchCheckedChange = { isBackupEnabled = it },
        ),
    )

    AddRuleScreenBody(
        mutationType = mutationType,
        showSaveButton = showSaveButton,
        ruleOptions = ruleOptions,
        onSaveClick = onSaveClick,
        modifier = modifier
    ) {
        when (viewModel) {
            is AddDomainNameRuleViewModel -> {
                if (mutationType == MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL) {
                    viewModel.setRemoveAllUrlParams(true)
                }

                val ruleNameText by viewModel.ruleName.collectAsStateWithLifecycle()
                val initialDomainNameText by viewModel.initialDomainName.collectAsStateWithLifecycle()
                val targetDomainNameText by viewModel.targetDomainName.collectAsStateWithLifecycle()

                DomainNameRuleForm(
                    showHints = showFormFieldHints,
                    ruleNameText = ruleNameText,
                    initialDomainNameText = initialDomainNameText,
                    targetDomainNameText = targetDomainNameText,
                    onRuleNameChange = viewModel::setRuleName,
                    onInitialDomainNameChange = viewModel::setInitialDomainName,
                    onTargetDomainNameChange = viewModel::setTargetDomainName,
                )
            }

            is AddAllUrlParamsRuleViewModel -> {
                val ruleNameText by viewModel.ruleName.collectAsStateWithLifecycle()
                val domainNameText by viewModel.domainName.collectAsStateWithLifecycle()

                AllUrlParamsRuleForm(
                    showHints = showFormFieldHints,
                    ruleNameText = ruleNameText,
                    domainNameText = domainNameText,
                    onRuleNameChange = viewModel::setRuleName,
                    onDomainNameChange = viewModel::setDomainName,
                )
            }

            is AddSpecificUrlParamsRuleViewModel -> {
                val ruleNameText by viewModel.ruleName.collectAsStateWithLifecycle()
                val domainNameText by viewModel.domainName.collectAsStateWithLifecycle()
                val addedParamNames by viewModel.removableParams.collectAsStateWithLifecycle()

                var openParamNameDialog by remember { mutableStateOf(false) }

                SpecificUrlParamsRuleForm(
                    showHints = showFormFieldHints,
                    addedParamNames = addedParamNames,
                    ruleNameText = ruleNameText,
                    domainNameText = domainNameText,
                    onRuleNameChange = viewModel::setRuleName,
                    onDomainNameChange = viewModel::setDomainName,
                    onClickAddParam = { openParamNameDialog = true },
                    onClickDismissParam = viewModel::removeParam,
                )

                if (openParamNameDialog) {
                    AddParameterNameDialog(
                        onConfirmation = {
                            viewModel.addParam(it)
                            openParamNameDialog = false
                        },
                        onDismissRequest = { openParamNameDialog = false }
                    )
                }
            }

            else -> {
                throw IllegalArgumentException(
                    "null or unexpected ViewModel implementing NewRuleFlowViewModel was passed to AddRuleScreen"
                )
            }
        }
    }
}

@Composable
private fun AddRuleScreenBody(
    mutationType: MutationType,
    showSaveButton: Boolean,
    ruleOptions: List<SwitchListItemState>,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    form: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .imePadding()
    ) {
        Text(
            text = "Let's create a new ${mutationType.name} rule",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        form()

        Spacer(modifier = Modifier.height(32.dp))

        SwitchList(
            items = ruleOptions,
            modifier = Modifier
                .then(
                    if (!showSaveButton) Modifier.padding(bottom = 32.dp) else Modifier
                )
        )

        if (showSaveButton) {
            Spacer(modifier = Modifier.weight(1F))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = 32.dp, horizontal = 8.dp)
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    }
}

@Composable
private fun AddParameterNameDialog(
    onConfirmation: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        title = {
            Text(
                text = "Add parameter",
                letterSpacing = LetterSpacingDefaults.Tighter
            )
        },
        text = {
            ParameterNameField(
                text = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth()
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = { onConfirmation(text) }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        },
        modifier = modifier
    )
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
private fun AddRuleScreenPreview() {
    PreviewContainer {
        val ruleOptions = listOf(
            SwitchListItemState(
                headlineText = stringResource(id = R.string.enable),
                supportingText = stringResource(R.string.enable_rule_supporting_text),
                leadingIcon = Icons.Outlined.CheckCircle,
                isSwitchChecked = true,
                onSwitchCheckedChange = {},
            ),
            SwitchListItemState(
                headlineText = stringResource(R.string.backup_to_cloud),
                supportingText = stringResource(R.string.backup_to_cloud_supporting_text),
                leadingIcon = Icons.Outlined.Backup,
                isSwitchChecked = false,
                onSwitchCheckedChange = {},
            )
        )

        AddRuleScreenBody(
            mutationType = MutationType.URL_PARAMS_SPECIFIC,
            showSaveButton = true,
            ruleOptions = ruleOptions,
            onSaveClick = {}
        ) {
            SpecificUrlParamsRuleForm(
                showHints = true,
                addedParamNames = listOf(),
                ruleNameText = "",
                domainNameText = "",
                onRuleNameChange = {},
                onDomainNameChange = {},
                onClickAddParam = {},
                onClickDismissParam = {},
            )
        }
    }
}

@Preview(widthDp = 400)
@Composable
private fun AddParameterNameDialogPreview() {
    PreviewContainer {
        AddParameterNameDialog(onConfirmation = {}, onDismissRequest = {})
    }
}