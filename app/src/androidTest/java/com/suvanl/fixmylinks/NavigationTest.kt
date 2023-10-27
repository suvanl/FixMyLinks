package com.suvanl.fixmylinks

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.suvanl.fixmylinks.ui.navigation.FmlNavHost
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

            FmlNavHost(navController = navController)
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