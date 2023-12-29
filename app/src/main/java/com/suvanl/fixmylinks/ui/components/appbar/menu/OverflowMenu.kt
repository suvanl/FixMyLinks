package com.suvanl.fixmylinks.ui.components.appbar.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.suvanl.fixmylinks.R

/**
 * Stateful [DropdownMenu] containing `DropdownItem`s, for use as an app bar's overflow menu
 */
@Composable
fun OverflowMenu(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    OverflowMenu(
        showMenu = showMenu,
        onOverflowIconClick = { showMenu = !showMenu },
        onDismissRequest = { showMenu = false },
        modifier = modifier.semantics { contentDescription = "More options" }
    ) {
        content()
    }
}

/**
 * Stateless [DropdownMenu] containing `DropdownItem`s, for use as an app bar's overflow menu
 */
@Composable
fun OverflowMenu(
    showMenu: Boolean,
    onOverflowIconClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    IconButton(onClick = onOverflowIconClick) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = stringResource(id = R.string.content_description_more_options)
        )
    }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        content()
    }
}