package com.suvanl.fixmylinks.ui.components.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.suvanl.fixmylinks.ui.components.button.AddNewRuleFab
import com.suvanl.fixmylinks.ui.navigation.FmlScreen
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults

@Composable
fun FmlNavigationRail(
    onFabClick: () -> Unit,
    navItems: List<FmlScreen>,
    onNavItemClick: (FmlScreen) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        header = {
            AddNewRuleFab(
                onClick = { onFabClick() },
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                modifier = modifier
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxHeight()
        ) {
            navItems.forEachIndexed { index, screen ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true

                NavigationRailItem(
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
                                letterSpacing = LetterSpacingDefaults.Tight,
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