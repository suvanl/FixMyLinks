package com.suvanl.fixmylinks.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.suvanl.fixmylinks.R

sealed class FmlScreen(
    val route: String,
    @StringRes val label: Int,
    val selectedIcon: ImageVector = Icons.Filled.Circle,
    val unselectedIcon: ImageVector = Icons.Outlined.Circle
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
        selectedIcon = Icons.AutoMirrored.Filled.LibraryBooks,
        unselectedIcon = Icons.AutoMirrored.Outlined.LibraryBooks
    )

    data object Saved : FmlScreen(
        route = "saved",
        label = R.string.saved,
        selectedIcon = Icons.Filled.Bookmarks,
        unselectedIcon = Icons.Outlined.Bookmarks
    )

    data object RuleDetails : FmlScreen(
        route = "rule_details",
        label = R.string.rule_details
    ), FmlScreenWithArgs {
        const val mutationTypeArg = "mutation_type"
        const val baseRuleIdArg = "base_rule_id"
        override val args = listOf(
            navArgument(mutationTypeArg) { type = NavType.StringType },
            navArgument(baseRuleIdArg) { type = NavType.LongType }
        )
        override val routeWithArgs = "$route/{$mutationTypeArg}/{$baseRuleIdArg}"
    }

    data object SelectRuleType : FmlScreen(
        route = "select_rule_type",
        label = R.string.select_rule_type
    )

    data object AddRule : FmlScreen(
        route = "add_rule",
        label = R.string.add_new_rule
    ), FmlScreenWithArgs {
        const val mutationTypeArg = "mutation_type"
        const val actionArg = "action"
        const val baseRuleIdArg = "base_rule_id"

        override val args = listOf(
            navArgument(mutationTypeArg) { type = NavType.StringType },
            navArgument(actionArg) { type = NavType.StringType },
            navArgument(baseRuleIdArg) { type = NavType.LongType },
        )
        override val routeWithArgs = "$route/{$mutationTypeArg}/{$actionArg}/{$baseRuleIdArg}"

        /**
         * An action that can be performed on the AddRule screen (i.e., adding a new rule or
         * editing an existing rule).
         */
        enum class Action { ADD, EDIT }
    }
}

val allFmlScreens = setOf(
    FmlScreen.Home,
    FmlScreen.Rules,
    FmlScreen.Saved,
    FmlScreen.SelectRuleType,
    FmlScreen.AddRule,
    FmlScreen.RuleDetails,
)

/**
 * Screens that are part of the "add new rule" flow
 */
val addNewRuleFlowScreens = setOf(
    FmlScreen.SelectRuleType,
    FmlScreen.AddRule
)

/**
 * Screens that are part of the "rule details" flow, excluding top-level destinations (i.e.,
 * [FmlScreen.Rules]).
 */
val ruleDetailsFlowScreens = setOf(
    FmlScreen.RuleDetails,
)

/**
 * Returns the base of a route (i.e., the route without any arguments)
 */
fun getBaseRoute(route: String?): String {
    if (route == null) return ""
    return route.split("/")[0]
}
