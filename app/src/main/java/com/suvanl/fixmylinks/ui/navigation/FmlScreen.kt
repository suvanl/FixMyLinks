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
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.suvanl.fixmylinks.R

sealed class FmlScreen(
    val route: String,
    @StringRes val label: Int,
    val selectedIcon: ImageVector = Icons.Filled.Star,
    val unselectedIcon: ImageVector = Icons.Outlined.StarOutline
) {
    private interface FmlScreenWithArgs {
        /**
         * A list of navigation arguments required by this screen
         */
        val args: List<NamedNavArgument>

        /**
         * The full route with arguments appended, such as
         * `"myScreen/{firstArgument}"`
         */
        val routeWithArgs: String
    }

    data object Home : FmlScreen(
        route = "home",
        label = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Rules : FmlScreen(
        route = "rules",
        label = R.string.rules,
        selectedIcon = Icons.Filled.LibraryBooks,
        unselectedIcon = Icons.Outlined.LibraryBooks
    )

    data object Saved : FmlScreen(
        route = "saved",
        label = R.string.saved,
        selectedIcon = Icons.Filled.Bookmarks,
        unselectedIcon = Icons.Outlined.Bookmarks
    )

    data object SelectRuleType : FmlScreen(
        route = "select_rule_type",
        label = R.string.select_rule_type
    )

    data object AddRule : FmlScreen(
        route = "add_rule",
        label = R.string.add_new_rule
    ), FmlScreenWithArgs {
        const val mutationTypeArg = "mutation_type"

        override val args: List<NamedNavArgument> = listOf(
            navArgument(mutationTypeArg) { type = NavType.StringType },
        )

        override val routeWithArgs = "$route/{${mutationTypeArg}}"
    }
}

val allFmlScreens = setOf(
    FmlScreen.Home,
    FmlScreen.Rules,
    FmlScreen.Saved,
    FmlScreen.SelectRuleType,
    FmlScreen.AddRule
)
