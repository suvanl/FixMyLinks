package com.suvanl.fixmylinks.ui.screens.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RuleDetailsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // pst = present simple tense
    private lateinit var allUrlParamsPstString: String
    private lateinit var domainNameAndAllUrlParamsPstString: String
    private lateinit var domainNamePstString: String
    private lateinit var specificUrlParamsPstString: String

    @Before
    fun setup() {
        composeTestRule.apply {
            // Init string resources
            allUrlParamsPstString =
                activity.getString(R.string.mt_url_params_all_present_simple_tense)

            domainNameAndAllUrlParamsPstString =
                activity.getString(R.string.mt_domain_name_and_url_params_all_present_simple_tense)

            domainNamePstString =
                activity.getString(R.string.mt_domain_name_present_simple_tense)

            specificUrlParamsPstString =
                activity.getString(R.string.mt_url_params_specific_present_simple_tense)
        }
    }

    @Test
    fun ruleDetailsScreen_isDisplayed() {
        composeTestRule.setContent {
            RuleDetailsScreen(
                rule = AllUrlParamsMutationModel(
                    name = "My rule",
                    triggerDomain = "github.com",
                    dateModifiedTimestamp = 0,
                    isLocalOnly = true,
                    baseRuleId = 1
                ),
                showDeleteConfirmation = false,
                onDismissDeleteConfirmation = {},
                onDelete = {},
            )
        }

        composeTestRule.onNodeWithContentDescription("Rule Details Screen")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun ruleDetailsScreen_allUrlParamsRule_presentSimpleTenseDescription_isDisplayed() {
        composeTestRule.setContent {
            RuleDetailsScreen(
                rule = AllUrlParamsMutationModel(
                    name = "My rule",
                    triggerDomain = "github.com",
                    dateModifiedTimestamp = 0,
                    isLocalOnly = true,
                    baseRuleId = 1
                ),
                showDeleteConfirmation = false,
                onDismissDeleteConfirmation = {},
                onDelete = {},
            )
        }

        // Assert that the wrong text is not rendered
        composeTestRule.onNodeWithText(domainNameAndAllUrlParamsPstString).assertDoesNotExist()
        composeTestRule.onNodeWithText(domainNamePstString).assertDoesNotExist()
        composeTestRule.onNodeWithText(specificUrlParamsPstString).assertDoesNotExist()

        // Assert that the correct text is displayed
        composeTestRule.onNodeWithText(allUrlParamsPstString)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun ruleDetailsScreen_domainNameAndAllUrlParamsRule_presentSimpleTenseDescription_isDisplayed() {
        composeTestRule.setContent {
            RuleDetailsScreen(
                rule = DomainNameAndAllUrlParamsMutationModel(
                    name = "My rule",
                    triggerDomain = "google.com",
                    dateModifiedTimestamp = 0,
                    isLocalOnly = true,
                    baseRuleId = 1,
                    mutationInfo = DomainNameMutationInfo(
                        initialDomain = "google.com",
                        targetDomain = "google.fr",
                    )
                ),
                showDeleteConfirmation = false,
                onDismissDeleteConfirmation = {},
                onDelete = {},
            )
        }

        // Assert that the wrong text is not rendered
        composeTestRule.onNodeWithText(allUrlParamsPstString).assertDoesNotExist()
        composeTestRule.onNodeWithText(domainNamePstString).assertDoesNotExist()
        composeTestRule.onNodeWithText(specificUrlParamsPstString).assertDoesNotExist()

        // Assert that the correct text is displayed
        composeTestRule.onNodeWithText(domainNameAndAllUrlParamsPstString)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun ruleDetailsScreen_domainNameRule_presentSimpleTenseDescription_isDisplayed() {
        composeTestRule.setContent {
            RuleDetailsScreen(
                rule = DomainNameMutationModel(
                    name = "My rule",
                    triggerDomain = "google.com",
                    dateModifiedTimestamp = 0,
                    isLocalOnly = true,
                    baseRuleId = 1,
                    mutationInfo = DomainNameMutationInfo(
                        initialDomain = "google.com",
                        targetDomain = "google.fr",
                    )
                ),
                showDeleteConfirmation = false,
                onDismissDeleteConfirmation = {},
                onDelete = {},
            )
        }

        // Assert that the wrong text is not rendered
        composeTestRule.onNodeWithText(allUrlParamsPstString).assertDoesNotExist()
        composeTestRule.onNodeWithText(domainNameAndAllUrlParamsPstString).assertDoesNotExist()
        composeTestRule.onNodeWithText(specificUrlParamsPstString).assertDoesNotExist()

        // Assert that the correct text is displayed
        composeTestRule.onNodeWithText(domainNamePstString)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun ruleDetailsScreen_specificUrlParamsRule_presentSimpleTenseDescription_isDisplayed() {
        composeTestRule.setContent {
            RuleDetailsScreen(
                rule = SpecificUrlParamsMutationModel(
                    name = "My rule",
                    triggerDomain = "youtube.com",
                    dateModifiedTimestamp = 0,
                    isLocalOnly = true,
                    baseRuleId = 1,
                    mutationInfo = SpecificUrlParamsMutationInfo(
                        removableParams = listOf("list", "t")
                    )
                ),
                showDeleteConfirmation = false,
                onDismissDeleteConfirmation = {},
                onDelete = {},
            )
        }

        // Assert that the wrong text is not rendered
        composeTestRule.onNodeWithText(allUrlParamsPstString).assertDoesNotExist()
        composeTestRule.onNodeWithText(domainNameAndAllUrlParamsPstString).assertDoesNotExist()
        composeTestRule.onNodeWithText(domainNamePstString).assertDoesNotExist()

        // Assert that the correct text is displayed
        composeTestRule.onNodeWithText(specificUrlParamsPstString)
            .assertExists()
            .assertIsDisplayed()
    }
}