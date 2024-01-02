package com.suvanl.fixmylinks.ui.components.form.common

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.animation.TransitionDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer

@Composable
fun DomainNameField(
    value: String,
    errorMessage: String?,
    showHints: Boolean,
    isLastFieldInForm: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = {
            Text(text = stringResource(id = R.string.domain_name))
        },
        isError = errorMessage != null,
        supportingText = {
            Column {
                AnimatedContent(
                    targetState = errorMessage,
                    transitionSpec = { TransitionDefaults.errorMessageTransition },
                    label = "form field error message"
                ) { errorMessage ->
                    if (errorMessage != null) {
                        FormFieldErrorMessage(text = errorMessage)
                    }
                }

                AnimatedVisibility(
                    visible = showHints,
                    enter = TransitionDefaults.supportingTextEnterTransition
                ) {
                    Text(text = stringResource(id = R.string.domain_name_supporting_text))
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
            imeAction = if (!isLastFieldInForm) ImeAction.Next else ImeAction.Done
        ),
        modifier = modifier
    )
}

@Preview
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DomainNameFieldPreview() {
    PreviewContainer {
        DomainNameField(
            value = "",
            errorMessage = null,
            showHints = true,
            isLastFieldInForm = false,
            onValueChange = {},
        )
    }
}

@Preview
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DomainNameFieldWithErrorPreview() {
    PreviewContainer {
        DomainNameField(
            value = "Some invalid input",
            errorMessage = "Error: error message",
            showHints = true,
            isLastFieldInForm = false,
            onValueChange = {},
        )
    }
}