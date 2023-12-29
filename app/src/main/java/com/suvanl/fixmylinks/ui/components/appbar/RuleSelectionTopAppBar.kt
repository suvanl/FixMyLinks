package com.suvanl.fixmylinks.ui.components.appbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot

@Composable
fun RuleSelectionTopAppBar(
    selectedItemsSize: Int,
    currentBackStackEntryFlow: Flow<NavBackStackEntry>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentContentBackStackEntry by produceState(
        initialValue = null as NavBackStackEntry?,
        producer = {
            currentBackStackEntryFlow
                .filterNot { it.destination is FloatingWindow }
                .collect { value = it }
        }
    )

    RuleSelectionTopAppBarBody(
        selectedItemsSize = selectedItemsSize,
        onDismiss = onDismiss,
        actions = { AppBarActionRow(navBackStackEntry = currentContentBackStackEntry) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RuleSelectionTopAppBarBody(
    selectedItemsSize: Int,
    onDismiss: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            if (selectedItemsSize > 0) {
                Text(text = selectedItemsSize.toString())
            }
        },
        navigationIcon = {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Clear selection"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
        actions = actions,
        modifier = modifier
    )
}

@Preview
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RuleSelectionTopAppBarPreview() {
    PreviewContainer {
        RuleSelectionTopAppBarBody(
            selectedItemsSize = 3,
            onDismiss = {},
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete selected"
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.SelectAll,
                        contentDescription = "Select all"
                    )
                }
            },
        )
    }
}