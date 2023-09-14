package com.suvanl.fixmylinks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import com.suvanl.fixmylinks.ui.screens.FixMyLinksApp
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FixMyLinksTheme {
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                FixMyLinksApp(windowSize = windowSizeClass)
            }
        }
    }
}
