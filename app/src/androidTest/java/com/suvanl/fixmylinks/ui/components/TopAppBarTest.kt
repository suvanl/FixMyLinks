package com.suvanl.fixmylinks.ui.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.suvanl.fixmylinks.ui.components.appbar.TopAppBarBody
import com.suvanl.fixmylinks.ui.components.appbar.TopAppBarSize
import com.suvanl.fixmylinks.ui.components.appbar.menu.OverflowMenu
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun topAppBar_overflowMenuState_isNotRestored() {
        val restorationTester = StateRestorationTester(composeTestRule)
        val menuItemText = "Test option"

        restorationTester.setContent {
            // Top app bar with an overflow menu
            TopAppBarBody(
                title = "Home",
                size = TopAppBarSize.SMALL,
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                onNavigateUp = {},
            ) {
                OverflowMenu {
                    DropdownMenuItem(
                        text = { Text(text = menuItemText) },
                        onClick = {}
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithContentDescription("More options")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // Assert that "Test option" DropdownMenuItem is displayed
        composeTestRule
            .onNodeWithText(menuItemText)
            .assertExists()
            .assertIsDisplayed()

        // Trigger state restoration
        restorationTester.emulateSavedInstanceStateRestore()

        // Assert that the DropdownMenuItem in the OverflowMenu is no longer displayed, since
        // OverflowMenu's `showMenu` state should not survive configuration changes, and its default
        // value is false.
        composeTestRule
            .onNodeWithText(menuItemText)
            .assertDoesNotExist()
    }
}