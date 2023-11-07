package com.suvanl.fixmylinks.ui.screens.newruleflow

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.components.form.DomainNameRuleForm
import com.suvanl.fixmylinks.ui.components.list.SwitchList
import com.suvanl.fixmylinks.ui.components.list.SwitchListItemState
import com.suvanl.fixmylinks.ui.util.PreviewContainer

/**
 * The amount of vertical space between form fields
 */
private val interFieldSpacing = 12.dp

@Composable
fun AddRuleScreen(
    showSaveButton: Boolean,
    mutationType: MutationType,
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
            onSwitchCheckedChange = { isRuleEnabled = it }
        ),
        SwitchListItemState(
            headlineText = stringResource(R.string.backup_to_cloud),
            supportingText = stringResource(R.string.backup_to_cloud_supporting_text),
            leadingIcon = Icons.Outlined.Backup,
            isSwitchChecked = isBackupEnabled,
            onSwitchCheckedChange = { isBackupEnabled = it },
        )
    )

    AddRuleScreenBody(
        mutationType = mutationType,
        showSaveButton = showSaveButton,
        ruleOptions = ruleOptions,
        onSaveClick = onSaveClick,
        modifier = modifier
    ) {
        DomainNameRuleForm(
            interFieldSpacing = interFieldSpacing,
            onRuleNameChange = {},
            onInitialDomainNameChange = {},
            onTargetDomainNameChange = {},
            modifier = Modifier.fillMaxWidth()
        )
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
    ) {
        Text(
            text = "Let's create a new ${mutationType.name} rule",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        form()

        Spacer(modifier = Modifier.height(32.dp))

        SwitchList(items = ruleOptions)

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
        AddRuleScreen(
            mutationType = MutationType.URL_PARAMS_SPECIFIC,
            showSaveButton = true,
            onSaveClick = { },
        )
    }
}