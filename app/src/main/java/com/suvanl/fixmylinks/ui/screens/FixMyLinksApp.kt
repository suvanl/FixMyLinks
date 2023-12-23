package com.suvanl.fixmylinks.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.components.appbar.FmlTopAppBar
import com.suvanl.fixmylinks.ui.components.appbar.RuleSelectionTopAppBar
import com.suvanl.fixmylinks.ui.components.appbar.TopAppBarSize
import com.suvanl.fixmylinks.ui.components.button.AddNewRuleFab
import com.suvanl.fixmylinks.ui.components.button.EditFab
import com.suvanl.fixmylinks.ui.components.nav.FmlNavigationBar
import com.suvanl.fixmylinks.ui.components.nav.FmlNavigationRail
import com.suvanl.fixmylinks.ui.components.search.RulesSearchBar
import com.suvanl.fixmylinks.ui.navigation.FmlNavHost
import com.suvanl.fixmylinks.ui.navigation.FmlScreen
import com.suvanl.fixmylinks.ui.navigation.allFmlScreens
import com.suvanl.fixmylinks.ui.navigation.getBaseRoute
import com.suvanl.fixmylinks.ui.navigation.navigateSingleTop
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme
import com.suvanl.fixmylinks.ui.util.topLevelScreens
import com.suvanl.fixmylinks.ui.util.topLevelScreensWithFab
import com.suvanl.fixmylinks.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FixMyLinksApp(windowSize: WindowSizeClass) {
    val navController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()

    // Currently selected rule items on RulesScreen
    val multiSelectedRules by mainViewModel.multiSelectedRules.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentBaseRoute = getBaseRoute(currentDestination?.route)
    val currentScreen = allFmlScreens.find { currentBaseRoute == it.route }

    // The screens on which the Navigation Bar should be shown
    val showNavBarOn = listOf(FmlScreen.Home, FmlScreen.Rules, FmlScreen.Saved)

    // The screens on which the search bar (or docked search bar) should be shown
    val showSearchBarOn = listOf(FmlScreen.Home, FmlScreen.Rules, FmlScreen.Saved)

    val topAppBarSize = TopAppBarSize.SMALL
    // Note: for non-small TopAppBars, use enterAlwaysScrollBehavior()
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val shouldShowNavRail = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Medium -> true
        WindowWidthSizeClass.Expanded -> true
        else -> false
    } && showNavBarOn.any { it.route == currentBaseRoute }

    val shouldShowNavBar = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> true
        else -> false
    } && showNavBarOn.any { it.route == currentBaseRoute }

    val shouldShowSearchBar =
        showSearchBarOn.any { it.route == currentBaseRoute } && multiSelectedRules.isEmpty()
    val shouldShowDockedSearchBar = shouldShowSearchBar
            && windowSize.widthSizeClass != WindowWidthSizeClass.Compact

    val shouldShowTopAppBar = showNavBarOn.none { it.route == currentBaseRoute }
    val shouldShowAddNewRuleFab = topLevelScreensWithFab.any { it.route == currentBaseRoute }
    val shouldShowEditRuleFab = currentBaseRoute.startsWith(FmlScreen.RuleDetails.route)

    val shouldShowRulesMultiSelectTopAppBar =
        currentBaseRoute == FmlScreen.Rules.route && multiSelectedRules.isNotEmpty()

    FixMyLinksTheme {
        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = shouldShowRulesMultiSelectTopAppBar,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = EaseInOut
                        )
                    ) + scaleIn(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMedium
                        )
                    ),
                    exit = fadeOut() + scaleOut(),
                ) {
                    RuleSelectionTopAppBar(
                        selectedItemsSize = multiSelectedRules.size,
                        currentBackStackEntryFlow = navController.currentBackStackEntryFlow,
                        onDismiss = mainViewModel::clearMultiSelectedRules,
                    )

                    // Clear selection on system back gesture/press
                    BackHandler(enabled = multiSelectedRules.isNotEmpty()) {
                        mainViewModel.clearMultiSelectedRules()
                    }
                }

                AnimatedVisibility(
                    visible = shouldShowSearchBar && !shouldShowDockedSearchBar,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = EaseInOut
                        )
                    ) + scaleIn(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessHigh
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessHigh
                        )
                    ),
                ) {
                    // Standard (non-docked) search bar
                    RulesSearchBar(
                        docked = false,
                        horizontalPadding = 16.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

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
                val destinationIsRuleDetailsScreen = currentDestination != null
                        && currentDestination.route == FmlScreen.RuleDetails.routeWithArgs

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
                } else if (shouldShowEditRuleFab && destinationIsRuleDetailsScreen) {
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

                    Box {
                        if (shouldShowSearchBar && shouldShowDockedSearchBar) {
                            RulesSearchBar(
                                docked = true
                            )
                        }

                        FmlNavHost(
                            navController = navController,
                            windowWidthSize = windowSize.widthSizeClass,
                            mainViewModel = mainViewModel,
                        )
                    }
                }
            }
        }
    }
}
