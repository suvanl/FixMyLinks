package com.suvanl.fixmylinks

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
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
     * Common actions in this flow before it diverges based on selections made by the user
     */
    @Before
    fun commonActions() {
        // click FAB
        composeTestRule
            .onNodeWithTag("Add New Rule FAB")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // arrive on SelectRuleTypeScreen
        composeTestRule
            .onNodeWithContentDescription("Select Rule Type Screen")
            .assertIsDisplayed()
    }

    /**
     * Navigate through the "add new rule" flow to create a DOMAIN_NAME rule
     */
    @Test
    @LargeTest
    fun addNewRuleFlow_createDomainNameRule() {
        // click "Change domain name" option
        composeTestRule
            .onNodeWithText("Change domain name")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // click "Next" button
        composeTestRule
            .onNodeWithTag("Next")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // arrive on AddRuleScreen
        composeTestRule
            .onNodeWithContentDescription("Add Rule Screen")
            .assertIsDisplayed()

        // verify whether DomainNameRuleForm composable is displayed
        composeTestRule
            .onNodeWithTag("DomainNameRuleForm")
            .assertExists()
            .assertIsDisplayed()
    }

    /**
     * Navigate through the "add new rule" flow to create a URL_PARAMS_ALL rule
     */
    @Test
    @LargeTest
    fun addNewRuleFlow_createAllUrlParamsRule() {
        // click "Remove all URL parameters" option
        composeTestRule
            .onNodeWithText("Remove all URL parameters")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // click "Next" button
        composeTestRule
            .onNodeWithTag("Next")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // arrive on AddRuleScreen
        composeTestRule
            .onNodeWithContentDescription("Add Rule Screen")
            .assertIsDisplayed()

        // verify whether DomainNameRuleForm composable is displayed
        composeTestRule
            .onNodeWithTag("AllUrlParamsRuleForm")
            .assertExists()
            .assertIsDisplayed()
    }

    /**
     * Navigate through the "add new rule" flow to create a URL_PARAMS_SPECIFIC rule
     */
    @Test
    @LargeTest
    fun addNewRuleFlow_createSpecificUrlParamsRule() {
        // click "Remove all URL parameters" option
        composeTestRule
            .onNodeWithText("Remove specific URL parameters")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // click "Next" button
        composeTestRule
            .onNodeWithTag("Next")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // arrive on AddRuleScreen
        composeTestRule
            .onNodeWithContentDescription("Add Rule Screen")
            .assertIsDisplayed()

        // verify whether DomainNameRuleForm composable is displayed
        composeTestRule
            .onNodeWithTag("SpecificUrlParamsRuleForm")
            .assertExists()
            .assertIsDisplayed()
    }

    /**
     * Navigate through the "add new rule" flow to create a DOMAIN_NAME_AND_URL_PARAMS_ALL rule
     */
    @Test
    @LargeTest
    fun addNewRuleFlow_createDomainNameAndAllUrlParamsRule() {
        // click "Change domain name and remove all URL parameters" option
        composeTestRule
            .onNodeWithText("Change domain name and remove all URL parameters")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // click "Next" button
        composeTestRule
            .onNodeWithTag("Next")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        // arrive on AddRuleScreen
        composeTestRule
            .onNodeWithContentDescription("Add Rule Screen")
            .assertIsDisplayed()

        // verify whether DomainNameRuleForm composable is displayed
        composeTestRule
            .onNodeWithTag("DomainNameRuleForm")
            .assertExists()
            .assertIsDisplayed()
    }
}