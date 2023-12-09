package com.suvanl.fixmylinks.ui.components.list

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.PreviewData

@Composable
fun RuleDetailsList(
    details: List<RuleDetailsListItemState>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier,
    ) {
        itemsIndexed(details) { index, item ->
            val shape = getItemShape(details, index)

            RuleDetailsListItem(
                state = item,
                shape = shape,
            )
        }
    }
}

private fun getItemShape(list: List<RuleDetailsListItemState>, currentIndex: Int): Shape {
    val itemCornerRadius = 5.dp
    val endItemCornerRadius = 32.dp

    return when (currentIndex) {
        0 -> {
            RoundedCornerShape(
                topStart = endItemCornerRadius,
                topEnd = endItemCornerRadius,
                bottomStart = itemCornerRadius,
                bottomEnd = itemCornerRadius,
            )
        }

        list.lastIndex -> {
            RoundedCornerShape(
                topStart = itemCornerRadius,
                topEnd = itemCornerRadius,
                bottomStart = endItemCornerRadius,
                bottomEnd = endItemCornerRadius,
            )
        }

        else -> {
            RoundedCornerShape(itemCornerRadius)
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
)
@Preview(
    showBackground = true,
    widthDp = 320,
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RuleDetailsListPreview() {
    val rules = PreviewData.previewRules

    PreviewContainer {
        Column(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            RuleDetailsList(
                details = listOf(
                    RuleDetailsListItemState(
                        headlineText = rules[0].name,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Description,
                                contentDescription = null,
                            )
                        },
                    ),
                    RuleDetailsListItemState(
                        headlineText = rules[0].triggerDomain,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                            )
                        },
                    ),
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}