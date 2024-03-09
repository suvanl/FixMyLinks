package com.suvanl.fixmylinks.ui.components.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.suvanl.fixmylinks.ui.navigation.FmlScreen
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.topLevelScreens

@Composable
fun FmlNavigationBar(
    navItems: List<FmlScreen>,
    currentDestination: NavDestination?,
    onNavItemClick: (FmlScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        navItems.forEach { screen ->
            val isSelected =
                currentDestination?.hierarchy?.any { it.route == screen.route } == true

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
                    Text(
                        text = stringResource(id = screen.label),
                        letterSpacing = LetterSpacingDefaults.Tight,
                        fontWeight = if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                        fontSize = 13.sp,
                    )
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun NavigationBarPreview() {
    PreviewContainer {
        FmlNavigationBar(navItems = topLevelScreens, currentDestination = null, onNavItemClick = {})
    }
}