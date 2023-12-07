package com.suvanl.fixmylinks.ui.components.appbar.menu

import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.theme.TextStyleDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer

@Composable
fun HintsDropdownItem(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenuItem(
        text = {
            Text(
                text = stringResource(id = R.string.show_hints),
                style = TextStyleDefaults.dropdownItemStyle
            )
        },
        onClick = { onCheckedChange(!isChecked) },
        trailingIcon = {
            Checkbox(
                checked = isChecked,
                onCheckedChange = null  // null recommended for accessibility with screen readers
            )
        },
        modifier = modifier
    )
}

@Preview(widthDp = 150)
@Composable
private fun HintsDropdownItemPreview() {
    PreviewContainer {
        HintsDropdownItem(
            isChecked = true,
            onCheckedChange = {}
        )
    }
}