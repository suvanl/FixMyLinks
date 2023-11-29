package com.suvanl.fixmylinks.ui.components.form.common

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.util.PreviewContainer

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ParameterNameField(
    text: String,
    errorMessage: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        label = {
            Text(text = stringResource(id = R.string.url_parameter_name))
        },
        supportingText = {
            if (errorMessage == null) return@OutlinedTextField
            FormFieldErrorMessage(text = errorMessage)
        },
        isError = errorMessage != null,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        modifier = modifier
    )
}

@Preview(widthDp = 320)
@Preview(
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ParameterNameFieldPreview() {
    PreviewContainer {
        ParameterNameField(text = "", errorMessage = null, onValueChange = {})
    }
}