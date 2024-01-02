package com.suvanl.fixmylinks.ui.components.form

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.animation.TransitionDefaults
import com.suvanl.fixmylinks.ui.components.form.common.FormFieldErrorMessage
import com.suvanl.fixmylinks.ui.components.form.common.RuleNameField
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.util.UiText

data class DomainNameRuleFormState(
    val ruleName: String = "",

    val initialDomainName: String = "",
    val initialDomainNameError: UiText? = null,

    val targetDomainName: String = "",
    val targetDomainNameError: UiText? = null,
)

@Composable
fun DomainNameRuleForm(
    formState: DomainNameRuleFormState,
    showHints: Boolean,
    onRuleNameChange: (String) -> Unit,
    onInitialDomainNameChange: (String) -> Unit,
    onTargetDomainNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    interFieldSpacing: Dp = FormDefaults.InterFieldSpacing,
) {
    Column(
        modifier = modifier
            .semantics { testTag = "DomainNameRuleForm" }
    ) {
        // "Rule name"
        RuleNameField(
            text = formState.ruleName,
            onValueChange = onRuleNameChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(interFieldSpacing))

        // "Initial domain name"
        OutlinedTextField(
            value = formState.initialDomainName,
            onValueChange = onInitialDomainNameChange,
            singleLine = true,
            label = {
                Text(text = stringResource(id = R.string.initial_domain_name))
            },
            supportingText = {
                Column {
                    // Error message
                    AnimatedContent(
                        targetState = formState.initialDomainNameError,
                        transitionSpec = { TransitionDefaults.errorMessageTransition },
                        label = "form field error message"
                    ) { errorMessage ->
                        if (errorMessage != null) {
                            FormFieldErrorMessage(text = errorMessage.asString())
                        }
                    }

                    // Hint text
                    AnimatedVisibility(
                        visible = showHints,
                        enter = TransitionDefaults.supportingTextEnterTransition
                    ) {
                        Text(text = stringResource(id = R.string.initial_domain_supporting_text))
                    }
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Language,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            ),
            isError = formState.initialDomainNameError != null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(interFieldSpacing))

        // "Target domain name"
        OutlinedTextField(
            value = formState.targetDomainName,
            onValueChange = onTargetDomainNameChange,
            singleLine = true,
            label = {
                Text(text = stringResource(id = R.string.target_domain_name))
            },
            supportingText = {
                Column {
                    AnimatedContent(
                        targetState = formState.targetDomainNameError,
                        transitionSpec = { TransitionDefaults.errorMessageTransition },
                        label = "form field error message"
                    ) { errorMessage ->
                        if (errorMessage != null) {
                            FormFieldErrorMessage(text = errorMessage.asString())
                        }
                    }

                    AnimatedVisibility(
                        visible = showHints,
                        enter = TransitionDefaults.supportingTextEnterTransition
                    ) {
                        Text(text = stringResource(id = R.string.target_domain_supporting_text))
                    }
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Language,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            isError = formState.targetDomainNameError != null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DomainNameRuleFormPreview() {
    PreviewContainer {
        DomainNameRuleForm(
            formState = DomainNameRuleFormState(),
            showHints = true,
            onRuleNameChange = {},
            onInitialDomainNameChange = {},
            onTargetDomainNameChange = {},
        )
    }
}