package com.suvanl.fixmylinks.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.navigation.FmlNavHost
import com.suvanl.fixmylinks.ui.navigation.FmlScreen
import com.suvanl.fixmylinks.ui.navigation.navigateSingleTop
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme

val navItems = listOf(
    FmlScreen.Home,
    FmlScreen.Rules,
    FmlScreen.Saved,
)

@Composable
fun FixMyLinksAppPortrait(navController: NavHostController) {
    FixMyLinksTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    navItems.forEach { screen ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigateSingleTop(screen.route)
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Column {
                                    Text(
                                        text = stringResource(id = screen.label),
                                        letterSpacing = (-0.035F).sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(top = 64.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            },
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            FmlNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun FixMyLinksAppLandscape(navController: NavHostController) {
    FixMyLinksTheme {
        @Composable
        fun NavRail() {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationRail(
                header = {
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
                        // remove elevation from FAB
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(id = R.string.add_new_rule)
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    navItems.forEachIndexed { index, screen ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true

                        NavigationRailItem(
                            selected = isSelected,
                            onClick = { navController.navigateSingleTop(screen.route) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Column {
                                    Text(
                                        text = stringResource(id = screen.label),
                                        letterSpacing = (-0.035F).sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
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
                NavRail()
                FmlNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun FixMyLinksApp(windowSize: WindowSizeClass) {
    val navController = rememberNavController()

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> FixMyLinksAppPortrait(navController = navController)
        WindowWidthSizeClass.Medium -> FixMyLinksAppLandscape(navController = navController)
        WindowWidthSizeClass.Expanded -> FixMyLinksAppLandscape(navController = navController)
    }
}