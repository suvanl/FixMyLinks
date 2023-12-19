package com.suvanl.fixmylinks.ui.components.list

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suvanl.fixmylinks.ui.util.PreviewContainer

data class SwitchListItemState(
    val headlineText: String,
    val supportingText: String,
    val leadingIcon: ImageVector,
    val isSwitchChecked: Boolean,
    val onSwitchCheckedChange: (Boolean) -> Unit,
    val comingSoon: Boolean = false,
    val isSwitchEnabled: Boolean = !comingSoon,
)

@Composable
fun SwitchListItem(
    listItemState: SwitchListItemState,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = listItemState.headlineText,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (listItemState.comingSoon) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Coming soon".uppercase(),
                            maxLines = 1,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
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
fun SwitchListItemComingSoonPreview() {
    PreviewContainer {
        SwitchListItem(
            listItemState = SwitchListItemState(
                headlineText = "Headline",
                supportingText = "Supporting text",
                leadingIcon = Icons.Outlined.AutoAwesome,
                isSwitchChecked = false,
                onSwitchCheckedChange = {},
                comingSoon = true
            )
        )
    }
}