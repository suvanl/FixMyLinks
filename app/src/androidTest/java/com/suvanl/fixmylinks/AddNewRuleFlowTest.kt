package com.suvanl.fixmylinks

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test suite for the "Add New Rule" user flow, in which users can create and save a new custom rule
 */
@RunWith(AndroidJUnit4::class)
class AddNewRuleFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Verify that we end up on the SelectRuleTypeScreen upon clicking the FAB
     */
    @Test
    fun clickFab_navigatesToSelectRuleType() {
        composeTestRule
            .onNodeWithTag("Add New Rule FAB")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Select Rule Type Screen")
            .assertIsDisplayed()
    }
}