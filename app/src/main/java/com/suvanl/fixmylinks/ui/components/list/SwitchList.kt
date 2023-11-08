package com.suvanl.fixmylinks.ui.components.list

import android.content.res.Configuration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.ui.util.PreviewContainer

@Composable
fun SwitchList(
    items: List<SwitchListItemState>,
    modifier: Modifier = Modifier,
    elevation: CardElevation = CardDefaults.cardElevation(),
) {
    Card(
        elevation = elevation,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
    ) {
        items.forEachIndexed { index, item -> 
            SwitchListItem(listItemState = item)

            if (index != items.lastIndex) {
                Divider(
                    color = MaterialTheme.colorScheme.background,
                    thickness = 2.dp
                )
            }
        }
    }
}

@Preview(widthDp = 320)
@Preview(
    name = "Dark",
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SwitchListPreview() {
    PreviewContainer {
        val items = listOf(
            SwitchListItemState(
                headlineText = "Hello, world",
                supportingText = "Some supporting text",
                leadingIcon = Icons.Outlined.AutoAwesome,
                isSwitchChecked = true,
                onSwitchCheckedChange = {}
            ),
            SwitchListItemState(
                headlineText = "Lorem ipsum sit dolor",
                supportingText = "Some more supporting text!",
                leadingIcon = Icons.Outlined.NotificationAdd,
                isSwitchChecked = true,
                onSwitchCheckedChange = {}
            ),
            SwitchListItemState(
                headlineText = "Yet another item",
                supportingText = "With, you guessed it, more supporting text!",
                leadingIcon = Icons.Outlined.Share,
                isSwitchChecked = false,
                onSwitchCheckedChange = {}
            )
        )

        SwitchList(items = items)
    }
}