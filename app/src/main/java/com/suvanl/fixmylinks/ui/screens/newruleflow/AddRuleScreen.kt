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
import androidx.compose.material.icons.outlined.Segment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.components.form.AllUrlParamsRuleForm
import com.suvanl.fixmylinks.ui.components.form.DomainNameRuleForm
import com.suvanl.fixmylinks.ui.components.form.SpecificUrlParamsRuleForm
import com.suvanl.fixmylinks.ui.components.form.SpecificUrlParamsRuleFormState
import com.suvanl.fixmylinks.ui.components.form.common.ParameterNameField
import com.suvanl.fixmylinks.ui.components.list.SwitchList
import com.suvanl.fixmylinks.ui.components.list.SwitchListItemState
import com.suvanl.fixmylinks.ui.navigation.FmlScreen
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddAllUrlParamsRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddDomainNameRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddSpecificUrlParamsRuleViewModel

data class AddRuleScreenUiState(
    val showSaveButton: Boolean,
    val showFormFieldHints: Boolean,
    val mutationType: MutationType,
    val ruleOptions: RuleOptionsState,
)

data class RuleOptionsState(
    val ruleEnabled: Boolean = true,
    val keepContent: Boolean = false,
    val backupEnabled: Boolean = false,
)

@Composable
fun AddRuleScreen(
    uiState: AddRuleScreenUiState,
    viewModel: AddRuleViewModel,
    onSaveClick: () -> Unit,
    onOptionsChanged: (options: RuleOptionsState) -> Unit,
    modifier: Modifier = Modifier,
    action: FmlScreen.AddRule.Action = FmlScreen.AddRule.Action.ADD,
    baseRuleId: Long = 0,
) {
    AddRuleScreenBody(
        mutationType = uiState.mutationType,
        showSaveButton = uiState.showSaveButton,
        onSaveClick = onSaveClick,
        ruleOptions = uiState.ruleOptions,
        onOptionsChanged = onOptionsChanged,
        modifier = modifier
    ) {
        LaunchedEffect(action, baseRuleId) {
            if (action != FmlScreen.AddRule.Action.EDIT || baseRuleId == 0L) return@LaunchedEffect
            viewModel.setInitialFormUiState(uiState.mutationType, baseRuleId)
        }

        when (viewModel) {
            is AddDomainNameRuleViewModel -> {
                if (uiState.mutationType == MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL) {
                    viewModel.setRemoveAllUrlParams(true)
                }

                val formUiState by viewModel.formUiState.collectAsStateWithLifecycle()

                DomainNameRuleForm(
                    formState = formUiState,
                    showHints = uiState.showFormFieldHints,
                    onRuleNameChange = viewModel::setRuleName,
                    onInitialDomainNameChange = viewModel::setInitialDomainName,
                    onTargetDomainNameChange = viewModel::setTargetDomainName,
                    onClickAddInitialWildcard = { viewModel.setInitialDomainName(formUiState.initialDomainName + "*.") },
                    onClickAddTargetWildcard = { viewModel.setTargetDomainName(formUiState.targetDomainName + "*.") }
                )
            }

            is AddAllUrlParamsRuleViewModel -> {
                val formUiState by viewModel.formUiState.collectAsStateWithLifecycle()

                AllUrlParamsRuleForm(
                    formState = formUiState,
                    showHints = uiState.showFormFieldHints,
                    onRuleNameChange = viewModel::setRuleName,
                    onDomainNameChange = viewModel::setDomainName,
                    onClickAddWildcard = { viewModel.setDomainName(formUiState.domainName + "*.") }
                )
            }

            is AddSpecificUrlParamsRuleViewModel -> {
                var openParamNameDialog by remember { mutableStateOf(false) }
                val formUiState by viewModel.formUiState.collectAsStateWithLifecycle()

                SpecificUrlParamsRuleForm(
                    showHints = uiState.showFormFieldHints,
                    formState = formUiState,
                    onRuleNameChange = viewModel::setRuleName,
                    onDomainNameChange = viewModel::setDomainName,
                    onClickAddParam = { openParamNameDialog = true },
                    onClickDismissParam = viewModel::removeParam,
                    onClickAddWildcard = { viewModel.setDomainName(formUiState.domainName + "*.") }
                )

                if (openParamNameDialog) {
                    AddParameterNameDialog(
                        textFieldErrorMessage = formUiState.urlParamKeyError?.asString(),
                        onConfirmation = {
                            if (!viewModel.validateUrlParamKey(it)) return@AddParameterNameDialog

                            viewModel.addParam(it)
                            openParamNameDialog = false
                        },
                        onDismissRequest = {
                            viewModel.resetUrlParamKeyValidationState()
                            openParamNameDialog = false
                        }
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
fun AddRuleScreenBody(
    mutationType: MutationType,
    showSaveButton: Boolean,
    onSaveClick: () -> Unit,
    ruleOptions: RuleOptionsState,
    onOptionsChanged: (options: RuleOptionsState) -> Unit,
    modifier: Modifier = Modifier,
    form: @Composable () -> Unit,
) {
    val switchListItems = listOf(
        SwitchListItemState(
            headlineText = stringResource(id = R.string.enable),
            supportingText = stringResource(R.string.enable_rule_supporting_text),
            leadingIcon = Icons.Outlined.CheckCircle,
            isSwitchChecked = ruleOptions.ruleEnabled,
            onSwitchCheckedChange = { onOptionsChanged(ruleOptions.copy(ruleEnabled = it)) },
        ),
        SwitchListItemState(
            headlineText = stringResource(R.string.keep_content),
            supportingText = stringResource(R.string.keep_content_supporting_text),
            leadingIcon = Icons.Outlined.Segment,
            isSwitchChecked = ruleOptions.keepContent,
            onSwitchCheckedChange = { onOptionsChanged(ruleOptions.copy(keepContent = it)) },
            comingSoon = true,
        ),
        SwitchListItemState(
            headlineText = stringResource(id = R.string.backup_to_cloud),
            supportingText = stringResource(R.string.backup_to_cloud_supporting_text),
            leadingIcon = Icons.Outlined.Backup,
            isSwitchChecked = ruleOptions.backupEnabled,
            onSwitchCheckedChange = { onOptionsChanged(ruleOptions.copy(backupEnabled = it)) },
            comingSoon = true,
        ),
    )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .imePadding()
            .semantics { contentDescription = "Add Rule Screen" }
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
            items = switchListItems,
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
                        .semantics { testTag = "Save Button" }
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    }
}

@Composable
private fun AddParameterNameDialog(
    textFieldErrorMessage: String?,
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
                errorMessage = textFieldErrorMessage,
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
fun AddRuleScreenPreview() {
    PreviewContainer {
        AddRuleScreenBody(
            mutationType = MutationType.URL_PARAMS_SPECIFIC,
            showSaveButton = true,
            onSaveClick = {},
            ruleOptions = RuleOptionsState(),
            onOptionsChanged = {},
        ) {
            SpecificUrlParamsRuleForm(
                showHints = true,
                formState = SpecificUrlParamsRuleFormState(),
                onRuleNameChange = {},
                onDomainNameChange = {},
                onClickAddParam = {},
                onClickDismissParam = {},
                onClickAddWildcard = {},
            )
        }
    }
}

@Preview(widthDp = 400)
@Composable
private fun AddParameterNameDialogPreview() {
    PreviewContainer {
        AddParameterNameDialog(
            textFieldErrorMessage = null,
            onConfirmation = {},
            onDismissRequest = {}
        )
    }
}