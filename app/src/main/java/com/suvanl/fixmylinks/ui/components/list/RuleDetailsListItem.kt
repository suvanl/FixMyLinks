package com.suvanl.fixmylinks.ui.components.list

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.ui.util.PreviewContainer

data class RuleDetailsListItemState(
    val headlineText: String,
    val leadingIcon: @Composable () -> Unit,
    val supportingText: String? = null,
)

@Composable
fun RuleDetailsListItem(
    state: RuleDetailsListItemState,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(5.dp),
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = shape,
        modifier = modifier
    ) {
        ListItem(
            headlineContent = {
                Text(text = state.headlineText, fontWeight = FontWeight.SemiBold)
            },
            supportingContent = {
                if (state.supportingText != null) {
                    Text(text = state.supportingText)
                }
            },
            leadingContent = {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                ) {
                    state.leadingIcon()
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
        )
    }
}

@Preview
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RuleDetailsListItemPreview() {
    PreviewContainer {
        RuleDetailsListItem(
            state = RuleDetailsListItemState(
                headlineText = "Change X.com to Twitter.com",
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Description, contentDescription = null)
                },
            )
        )
    }
}