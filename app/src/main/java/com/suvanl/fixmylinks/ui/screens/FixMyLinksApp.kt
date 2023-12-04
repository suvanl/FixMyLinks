package com.suvanl.fixmylinks.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.components.button.AddNewRuleFab
import com.suvanl.fixmylinks.ui.components.nav.FmlNavigationBar
import com.suvanl.fixmylinks.ui.components.nav.FmlNavigationRail
import com.suvanl.fixmylinks.ui.components.appbar.FmlTopAppBar
import com.suvanl.fixmylinks.ui.components.appbar.TopAppBarSize
import com.suvanl.fixmylinks.ui.components.button.EditFab
import com.suvanl.fixmylinks.ui.navigation.FmlNavHost
import com.suvanl.fixmylinks.ui.navigation.FmlScreen
import com.suvanl.fixmylinks.ui.navigation.allFmlScreens
import com.suvanl.fixmylinks.ui.navigation.getBaseRoute
import com.suvanl.fixmylinks.ui.navigation.navigateSingleTop
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme
import com.suvanl.fixmylinks.ui.util.topLevelScreens
import com.suvanl.fixmylinks.ui.util.topLevelScreensWithFab

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FixMyLinksApp(windowSize: WindowSizeClass) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentBaseRoute = getBaseRoute(currentDestination?.route)
    val currentScreen = allFmlScreens.find { currentBaseRoute == it.route }

    // The screens on which the Navigation Bar should be shown
    val showNavBarOn = listOf(FmlScreen.Home, FmlScreen.Rules, FmlScreen.Saved)

    val topAppBarSize = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> TopAppBarSize.LARGE
        else -> TopAppBarSize.SMALL
    }

    val topAppBarScrollBehavior = when (topAppBarSize) {
        TopAppBarSize.SMALL -> TopAppBarDefaults.pinnedScrollBehavior()
        else -> TopAppBarDefaults.enterAlwaysScrollBehavior()
    }

    val shouldShowNavRail = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Medium -> true
        WindowWidthSizeClass.Expanded -> true
        else -> false
    } && showNavBarOn.any { it.route == currentBaseRoute }

    val shouldShowNavBar = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> true
        else -> false
    } && showNavBarOn.any { it.route == currentBaseRoute }

    val shouldShowTopAppBar = showNavBarOn.none { it.route == currentBaseRoute }
    val shouldShowAddNewRuleFab = topLevelScreensWithFab.any { it.route == currentBaseRoute }
    val shouldShowEditRuleFab = currentBaseRoute.startsWith(FmlScreen.RuleDetails.route)

    FixMyLinksTheme {
        Scaffold(
            topBar = {
                if (shouldShowTopAppBar) {
                    FmlTopAppBar(
                        title = stringResource(id = currentScreen?.label ?: R.string.app_name),
                        onNavigateUp = { navController.navigateUp() },
                        size = topAppBarSize,
                        scrollBehavior = topAppBarScrollBehavior,
                        currentBackStackEntryFlow = navController.currentBackStackEntryFlow
                    )
                }
            },
            bottomBar = {
                if (shouldShowNavBar) {
                    FmlNavigationBar(
                        navItems = topLevelScreens,
                        onNavItemClick = { screen ->
                            navController.navigateSingleTop(screen.route)
                        },
                        currentDestination = currentDestination
                    )
                }
            },
            floatingActionButton = {
                // Don't show the FAB if shouldShowNavRail is true as the navigation rail will contain
                // a FAB within it
                if (!shouldShowNavRail && shouldShowAddNewRuleFab) {
                    AddNewRuleFab(
                        onClick = {
                            navController.navigateSingleTop(
                                route = FmlScreen.SelectRuleType.route,
                                popUpToStartDestination = false
                            )
                        }
                    )
                } else if (shouldShowEditRuleFab) {
                    if (currentDestination != null
                        && currentDestination.route == FmlScreen.RuleDetails.routeWithArgs) {
                        val mutationTypeArg =
                            navBackStackEntry?.arguments?.getString(FmlScreen.RuleDetails.mutationTypeArg)

                        val mutationType = MutationType.entries.find { it.name == mutationTypeArg }
                            ?: MutationType.FALLBACK

                        val baseRuleId =
                            navBackStackEntry?.arguments?.getLong(FmlScreen.RuleDetails.baseRuleIdArg)
                                ?: throw NullPointerException("Expected base_rule_id to be non-null")

                        fun handleEditFabClick() {
                            val editActionRoute =
                                "${FmlScreen.AddRule.route}/${mutationType.name}/${FmlScreen.AddRule.Action.EDIT}/$baseRuleId"

                            navController.navigateSingleTop(
                                route = editActionRoute,
                                popUpToStartDestination = false
                            )
                        }

                        EditFab(
                            onClick = { handleEditFabClick() },
                            modifier = Modifier.navigationBarsPadding()
                        )
                    }

                }
            },
            contentWindowInsets = WindowInsets(
                left = 0,
                top = WindowInsets.safeDrawing.getTop(LocalDensity.current),
                right = 0,
                bottom = 0
            ),
            modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                        )
                ) {
                    AnimatedVisibility(
                        visible = shouldShowNavRail,
                        enter = slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        )
                    ) {
                        FmlNavigationRail(
                            navItems = topLevelScreens,
                            currentDestination = currentDestination,
                            onFabClick = {
                                navController.navigateSingleTop(
                                    route = FmlScreen.SelectRuleType.route,
                                    // we want to return to the screen the FAB was clicked on (i.e.
                                    // the previous destination) when popping the back stack rather
                                    // than popping all the way up to the startDestination.
                                    popUpToStartDestination = false
                                )
                            },
                            onNavItemClick = { screen ->
                                navController.navigateSingleTop(screen.route)
                            }
                        )
                    }

                    FmlNavHost(
                        navController = navController,
                        windowWidthSize = windowSize.widthSizeClass
                    )
                }
            }
        }
    }
}
