package com.suvanl.fixmylinks.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.ui.graphics.vector.ImageVector
import com.suvanl.fixmylinks.R

sealed class FmlScreen(
    val route: String,
    @StringRes val label: Int,
    val selectedIcon: ImageVector = Icons.Filled.Star,
    val unselectedIcon: ImageVector = Icons.Outlined.StarOutline
) {
    object Home : FmlScreen(
        route = "home",
        label = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Rules : FmlScreen(
        route = "rules",
        label = R.string.rules,
        selectedIcon = Icons.Filled.LibraryBooks,
        unselectedIcon = Icons.Outlined.LibraryBooks
    )

    object Saved : FmlScreen(
        route = "saved",
        label = R.string.saved,
        selectedIcon = Icons.Filled.Bookmarks,
        unselectedIcon = Icons.Outlined.Bookmarks
    )

    object AddRule : FmlScreen(
        route = "add_rule",
        label = R.string.add_new_rule
    )
}

val allFmlScreens = listOf(
    FmlScreen.Home,
    FmlScreen.Rules,
    FmlScreen.Saved,
    FmlScreen.AddRule
)