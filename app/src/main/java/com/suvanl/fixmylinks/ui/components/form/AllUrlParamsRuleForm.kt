package com.suvanl.fixmylinks.ui.components.form

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.ui.components.form.common.DomainNameField
import com.suvanl.fixmylinks.ui.components.form.common.RuleNameField
import com.suvanl.fixmylinks.ui.util.PreviewContainer

data class AllUrlParamsRuleFormState(
    val ruleName: String = "",
    val domainName: String = "",
    val domainNameError: String? = null,
)

@Composable
fun AllUrlParamsRuleForm(
    formState: AllUrlParamsRuleFormState,
    showHints: Boolean,
    onRuleNameChange: (String) -> Unit,
    onDomainNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    interFieldSpacing: Dp = FormDefaults.InterFieldSpacing,
) {
    Column(
        modifier = modifier
            .semantics { testTag = "AllUrlParamsRuleForm" }
    ) {
        // "Rule name"
        RuleNameField(
            text = formState.ruleName,
            onValueChange = onRuleNameChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(interFieldSpacing))

        // "Domain name"
        DomainNameField(
            value = formState.domainName,
            errorMessage = formState.domainNameError,
            showHints = showHints,
            onValueChange = onDomainNameChange,
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
            formState = AllUrlParamsRuleFormState(),
        )
    }
}