package com.suvanl.fixmylinks.ui.components.appbar.menu

import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.suvanl.fixmylinks.R

@Composable
fun HintsDropdownItem(
    isChecked: Boolean,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenuItem(
        text = {
            Text(text = stringResource(id = R.string.show_hints))
        },
        onClick = onClick,
        trailingIcon = {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        },
        modifier = modifier
    )
}