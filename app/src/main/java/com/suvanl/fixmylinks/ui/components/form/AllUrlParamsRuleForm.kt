package com.suvanl.fixmylinks.ui.components.form

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.ui.components.form.common.DomainNameField
import com.suvanl.fixmylinks.ui.components.form.common.RuleNameField
import com.suvanl.fixmylinks.ui.util.PreviewContainer

@Composable
fun AllUrlParamsRuleForm(
    showHints: Boolean,
    interFieldSpacing: Dp,
    onRuleNameChange: (String) -> Unit,
    onDomainNameChange: (String) -> Unit,
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
    }
}

@Preview
@Preview(
    name = "Dark",
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun AllUrlParamsRuleFormPreview() {
    PreviewContainer {
        AllUrlParamsRuleForm(
            showHints = true,
            interFieldSpacing = 16.dp,
            onRuleNameChange = {},
            onDomainNameChange = {},
        )
    }
}