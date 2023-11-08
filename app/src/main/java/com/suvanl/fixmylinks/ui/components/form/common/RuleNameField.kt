package com.suvanl.fixmylinks.ui.components.form.common

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.util.PreviewContainer

@Composable
fun RuleNameField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        label = {
            Text(text = stringResource(id = R.string.rule_name))
        },
        supportingText = {
            Text(stringResource(id = R.string.rule_name_is_optional_supporting_text))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = modifier
    )
}

@Preview(widthDp = 320)
@Preview(
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RuleNameFieldPreview() {
    PreviewContainer {
        RuleNameField(text = "", onValueChange = {})
    }
}