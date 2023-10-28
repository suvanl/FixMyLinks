package com.suvanl.fixmylinks.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.components.button.AddNewRuleFab
import com.suvanl.fixmylinks.ui.components.nav.FmlNavigationRail
import com.suvanl.fixmylinks.ui.components.nav.FmlTopAppBar
import com.suvanl.fixmylinks.ui.navigation.FmlNavHost
import com.suvanl.fixmylinks.ui.navigation.FmlScreen
import com.suvanl.fixmylinks.ui.navigation.allFmlScreens
import com.suvanl.fixmylinks.ui.navigation.navigateSingleTop
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme
import com.suvanl.fixmylinks.ui.theme.TIGHT_LETTER_SPACING

private val navItems = listOf(
    FmlScreen.Home,
    FmlScreen.Rules,
    FmlScreen.Saved,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixMyLinksAppPortrait(
    navHost: @Composable (padding: PaddingValues) -> Unit,
    onNavItemClick: (screen: FmlScreen) -> Unit,
    navItemSelectedFn: (screen: FmlScreen) -> Boolean,
    onFabClick: () -> Unit,
    onNavigateUp: () -> Unit,
    topAppBarTitle: String,
    showFab: Boolean = true,
    showNavBar: Boolean = true,
    showTopAppBar: Boolean = false,
) {
    FixMyLinksTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            topBar = {
                if (showTopAppBar) {
                    FmlTopAppBar(
                        title = topAppBarTitle,
                        onNavigateUp = { onNavigateUp() },
                        scrollBehavior = scrollBehavior
                    )
                }
            },
            bottomBar = {
                val density = LocalDensity.current

                AnimatedVisibility(
                    visible = showNavBar,
                    enter = slideInVertically(
                        initialOffsetY = {
                            it / 2
                            with(density) { 128.dp.roundToPx() }
                        },
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = {
                            it / 2
                            with(density) { 128.dp.roundToPx() }
                        }
                    )
                ) {
                    NavigationBar {
                        navItems.forEach { screen ->
                            val isSelected = navItemSelectedFn(screen)

                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { onNavItemClick(screen) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) {
                                            screen.selectedIcon
                                        } else {
                                            screen.unselectedIcon
                                        },
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Column {
                                        Text(
                                            text = stringResource(id = screen.label),
                                            letterSpacing = TIGHT_LETTER_SPACING,
                                            fontWeight = if (isSelected) {
                                                FontWeight.Bold
                                            } else {
                                                FontWeight.Normal
                                            },
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(top = 64.dp)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (showFab) {
                    AddNewRuleFab(onClick = { onFabClick() })
                }
            },
            contentWindowInsets = WindowInsets.safeDrawing,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            navHost(innerPadding)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixMyLinksAppLandscape(
    navHost: @Composable () -> Unit,
    onItemClick: (screen: FmlScreen) -> Unit,
    selectedFn: (screen: FmlScreen) -> Boolean,
    onFabClick: () -> Unit,
    onNavigateUp: () -> Unit,
    topAppBarTitle: String,
    showTopAppBar: Boolean = false,
    showNavRail: Boolean = true
) {
    FixMyLinksTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        @Composable
        fun NavRail() {
            FmlNavigationRail(onFabClick = { onFabClick() }) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    navItems.forEachIndexed { index, screen ->
                        val isSelected = selectedFn(screen)

                        NavigationRailItem(
                            selected = isSelected,
                            onClick = { onItemClick(screen) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) {
                                        screen.selectedIcon
                                    } else {
                                        screen.unselectedIcon
                                    },
                                    contentDescription = null
                                )
                            },
                            label = {
                                Column {
                                    Text(
                                        text = stringResource(id = screen.label),
                                        letterSpacing = TIGHT_LETTER_SPACING,
                                        fontWeight = if (isSelected) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Normal
                                        },
                                    )
                                }
                            }
                        )

                        if (index != navItems.lastIndex) {
                            Spacer(modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.statusBarsPadding()) {
                AnimatedVisibility(
                    visible = showNavRail,
                    enter = slideInHorizontally(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                ) {
                    NavRail()
                }

                Column(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    if (showTopAppBar) {
                        FmlTopAppBar(
                            title = topAppBarTitle,
                            onNavigateUp = { onNavigateUp() },
                            scrollBehavior = scrollBehavior
                        )
                    }
                    navHost()
                }
            }
        }
    }
}

@Composable
fun FixMyLinksApp(windowSize: WindowSizeClass) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = allFmlScreens.find { currentDestination?.route == it.route }

    // The screens on which the Floating Action Button (FAB) should be displayed
    val displayFabOn = listOf(FmlScreen.Home, FmlScreen.Rules)

    // The screens on which the Navigation Bar should be hidden
    val hideNavBarOn = listOf(FmlScreen.AddRule)

    @Composable
    fun PortraitLayout() {
        FixMyLinksAppPortrait(
            navHost = { innerPadding ->
                FmlNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            },
            onNavItemClick = { screen ->
                navController.navigateSingleTop(screen.route)
            },
            navItemSelectedFn = { screen ->
                currentDestination?.hierarchy?.any { it.route == screen.route } == true
            },
            onFabClick = {
                navController.navigateSingleTop(
                    route = FmlScreen.AddRule.route,
                    popUpToStartDestination = false
                )
            },
            onNavigateUp = { navController.navigateUp() },
            topAppBarTitle = stringResource(id = currentScreen?.label ?: R.string.app_name),
            showFab = displayFabOn.any { it.route == currentDestination?.route },
            showNavBar = hideNavBarOn.none { it.route == currentDestination?.route },
            showTopAppBar = hideNavBarOn.any { it.route == currentDestination?.route }
        )
    }

    @Composable
    fun LandscapeLayout() {
        FixMyLinksAppLandscape(
            navHost = {
                FmlNavHost(navController = navController)
            },
            onItemClick = { screen ->
                navController.navigateSingleTop(screen.route)
            },
            selectedFn = { screen ->
                currentDestination?.hierarchy?.any { it.route == screen.route } == true
            },
            onFabClick = {
                navController.navigateSingleTop(
                    route = FmlScreen.AddRule.route,
                    popUpToStartDestination = false
                )
            },
            showNavRail = hideNavBarOn.none {
                it.route == currentDestination?.route
            },
            onNavigateUp = { navController.navigateUp() },
            topAppBarTitle = stringResource(id = currentScreen?.label ?: R.string.app_name),
            showTopAppBar = hideNavBarOn.any { it.route == currentDestination?.route }
        )
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> PortraitLayout()
        WindowWidthSizeClass.Medium -> LandscapeLayout()
        WindowWidthSizeClass.Expanded -> LandscapeLayout()
    }
}