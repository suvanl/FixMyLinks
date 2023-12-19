package com.suvanl.fixmylinks.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.suvanl.fixmylinks.viewmodel.FakeAppLevelViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupFmlNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)

            // Assign a ComposeNavigator to the navController so it can navigate through composables
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            FmlNavHost(
                navController = navController,
                windowWidthSize = WindowWidthSizeClass.Compact,
                mainViewModel = viewModel<FakeAppLevelViewModel>()
            )
        }
    }

    /**
     * Verify whether HomeScreen is the start destination
     */
    @Test
    fun fmlNavHost_verifyStartDestination() {
        composeTestRule
            .onNodeWithContentDescription("Home Screen")
            .assertIsDisplayed()
    }
}