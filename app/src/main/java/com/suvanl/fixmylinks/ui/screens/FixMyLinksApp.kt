package com.suvanl.fixmylinks.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.suvanl.fixmylinks.ui.components.BottomNavigationBar

@Composable
fun FixMyLinksAppPortrait() {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { padding ->
        HomeScreen(modifier = Modifier.padding(padding))
    }
}

@Composable
fun FixMyLinksAppLandscape() {
    TODO("Not yet implemented")
}

@Composable
fun FixMyLinksApp(windowSize: WindowSizeClass) {
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> FixMyLinksAppPortrait()
        WindowWidthSizeClass.Medium -> FixMyLinksAppLandscape()
        WindowWidthSizeClass.Expanded -> FixMyLinksAppLandscape()
    }
}