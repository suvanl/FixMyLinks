package com.suvanl.fixmylinks.ui.components.list

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.suvanl.fixmylinks.ui.util.PreviewContainer

data class SwitchListItemState(
    val headlineText: String,
    val supportingText: String,
    val leadingIcon: ImageVector,
    val isSwitchChecked: Boolean,
    val onSwitchCheckedChange: (Boolean) -> Unit,
    val isSwitchEnabled: Boolean = true
)

@Composable
fun SwitchListItem(
    listItemState: SwitchListItemState,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = listItemState.headlineText,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        supportingContent = {
            Text(
                text = listItemState.supportingText,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingContent = {
            Icon(
                imageVector = listItemState.leadingIcon,
                contentDescription = null  // decorative
            )
        },
        trailingContent = {
            Switch(
                checked = listItemState.isSwitchChecked,
                onCheckedChange = listItemState.onSwitchCheckedChange,
                enabled = listItemState.isSwitchEnabled
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
    widthDp = 320
)
@Preview(
    name = "Dark",
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SwitchListItemOnPreview() {
    PreviewContainer {
        SwitchListItem(
            listItemState = SwitchListItemState(
                headlineText = "Headline",
                supportingText = "Supporting text",
                leadingIcon = Icons.Outlined.AutoAwesome,
                isSwitchChecked = true,
                onSwitchCheckedChange = {}
            )
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 320
)
@Preview(
    name = "Dark",
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SwitchListItemOffPreview() {
    PreviewContainer {
        SwitchListItem(
            listItemState = SwitchListItemState(
                headlineText = "Headline",
                supportingText = "Supporting text",
                leadingIcon = Icons.Outlined.AutoAwesome,
                isSwitchChecked = false,
                onSwitchCheckedChange = {}
            )
        )
    }
}