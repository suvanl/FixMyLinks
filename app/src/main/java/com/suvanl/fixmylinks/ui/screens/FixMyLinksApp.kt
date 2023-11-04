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
import com.suvanl.fixmylinks.ui.components.button.AddNewRuleFab
import com.suvanl.fixmylinks.ui.components.nav.FmlNavigationBar
import com.suvanl.fixmylinks.ui.components.nav.FmlNavigationRail
import com.suvanl.fixmylinks.ui.components.nav.FmlTopAppBar
import com.suvanl.fixmylinks.ui.components.nav.TopAppBarSize
import com.suvanl.fixmylinks.ui.navigation.FmlNavHost
import com.suvanl.fixmylinks.ui.navigation.FmlScreen
import com.suvanl.fixmylinks.ui.navigation.allFmlScreens
import com.suvanl.fixmylinks.ui.navigation.navigateSingleTop
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme

private val topLevelNavItems = listOf(
    FmlScreen.Home,
    FmlScreen.Rules,
    FmlScreen.Saved,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FixMyLinksApp(windowSize: WindowSizeClass) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = allFmlScreens.find { currentDestination?.route == it.route }

    // The screens on which the Floating Action Button (FAB) should be displayed
    val displayFabOn = listOf(FmlScreen.Home, FmlScreen.Rules)

    // The screens on which the Navigation Bar should be hidden
    val hideNavBarOn = listOf(FmlScreen.SelectRuleType, FmlScreen.AddRule)

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
    } && hideNavBarOn.none { it.route == currentDestination?.route }

    val shouldShowNavBar = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> true
        else -> false
    } && hideNavBarOn.none { it.route == currentDestination?.route }

    val shouldShowTopAppBar = hideNavBarOn.any { it.route == currentDestination?.route }
    val shouldShowFab = displayFabOn.any { it.route == currentDestination?.route }

    FixMyLinksTheme {
        Scaffold(
            topBar = {
                if (shouldShowTopAppBar) {
                    FmlTopAppBar(
                        title = stringResource(id = currentScreen?.label ?: R.string.app_name),
                        onNavigateUp = { navController.navigateUp() },
                        size = topAppBarSize,
                        scrollBehavior = topAppBarScrollBehavior
                    )
                }
            },
            bottomBar = {
                if (shouldShowNavBar) {
                    FmlNavigationBar(
                        navItems = topLevelNavItems,
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
                if (!shouldShowNavRail && shouldShowFab) {
                    AddNewRuleFab(onClick = {
                        navController.navigateSingleTop(
                            route = FmlScreen.SelectRuleType.route,
                            popUpToStartDestination = false
                        )
                    })
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
                            navItems = topLevelNavItems,
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

                    FmlNavHost(navController = navController)
                }
            }
        }
    }
}