package com.suvanl.fixmylinks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.suvanl.fixmylinks.ui.screens.AddRuleScreen
import com.suvanl.fixmylinks.ui.screens.HomeScreen
import com.suvanl.fixmylinks.ui.screens.RulesScreen
import com.suvanl.fixmylinks.ui.screens.SavedScreen

@Composable
fun FmlNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = FmlScreen.Home.route,
        modifier = modifier
    ) {
        composable(route = FmlScreen.Home.route) {
            HomeScreen()
        }

        composable(route = FmlScreen.Rules.route) {
            RulesScreen()
        }

        composable(route = FmlScreen.Saved.route) {
            SavedScreen()
        }

        composable(route = FmlScreen.AddRule.route) {
            AddRuleScreen()
        }
    }
}

/**
 * Navigates to the given route with `launchSingleTop` enabled, to ensure that there will be at most
 * only one copy of a given destination on top of the back stack.
 */
fun NavHostController.navigateSingleTop(route: String) {
    this.navigate(route) {
        // Pop up to the start destination of the graph to avoid building up a large stack of
        // destinations on the back stack as users select items
        popUpTo(this@navigateSingleTop.graph.findStartDestination().id) {
            saveState = true
        }

        // Avoid multiple copies of the same destination when re-selecting the same item
        launchSingleTop = true

        // Restore state when re-selecting a previously selected item
        restoreState = true
    }
}