package com.suvanl.fixmylinks.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.rule.BuiltInRules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RulesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var noRulesAddedYetString: String
    private lateinit var builtInTabLabel: String

    @Before
    fun setup() {
        noRulesAddedYetString = composeTestRule.activity.getString(R.string.no_rules_added_yet)
        builtInTabLabel = composeTestRule.activity.getString(R.string.built_in)
    }

    @Test
    fun rulesScreen_withNoSavedRules_displaysEmptyRulesBody() {
        composeTestRule.setContent { EmptyRulesScreen() }

        composeTestRule.onNodeWithTag("Empty Rules Body").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText(noRulesAddedYetString).assertExists().assertIsDisplayed()
    }

    @Test
    fun rulesScreen_customRules_withTwoSavedRules_displaysRulesListItem_forEachOne() {
        val rules = fakeRules.take(2)

        composeTestRule.setContent {
            CustomRulesBody(
                uiState = RulesScreenUiState(rules = rules),
                hasRules = rules.isNotEmpty(),
                onClickRuleItem = {},
                selectedItems = setOf(),
                onUpdateSelectedItems = {},
            )
        }

        composeTestRule.onNodeWithTag("Empty Rules Body").assertDoesNotExist()

        rules.forEach { rule ->
            composeTestRule
                .onNodeWithTag("Rules List Item ${rule.baseRuleId}", useUnmergedTree = true)
                .assertExists()
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText(rule.name, useUnmergedTree = true)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun rulesScreen_customRules_itemsAreSelectable_and_singleSelectedRule_isSelected() {
        val selectedRule = fakeRules[2]

        composeTestRule.setContent {
            CustomRulesBody(
                uiState = RulesScreenUiState(rules = fakeRules),
                hasRules = fakeRules.isNotEmpty(),
                onClickRuleItem = {},
                selectedItems = setOf(selectedRule),
                onUpdateSelectedItems = {},
            )
        }

        // Unselected items
        fakeRules.minus(selectedRule).forEach { rule ->
            composeTestRule
                .onNodeWithTag("Rules List Item ${rule.baseRuleId}")
                .assertExists()
                .assertIsDisplayed()
                .assertIsSelectable()
                .assertIsNotSelected()
        }

        // Selected item
        composeTestRule
            .onNodeWithTag("Rules List Item ${selectedRule.baseRuleId}")
            .assertExists()
            .assertIsDisplayed()
            .assertIsSelectable()
            .assertIsSelected()
    }

    @Test
    fun rulesScreen_customRules_shapeRepresentingRuleType_isDisplayed() {
        composeTestRule.setContent {
            CustomRulesBody(
                uiState = RulesScreenUiState(rules = fakeRules),
                hasRules = fakeRules.isNotEmpty(),
                onClickRuleItem = {},
                selectedItems = setOf(),
                onUpdateSelectedItems = {},
            )
        }

        fakeRules.forEach { rule ->
            val testTag = "Shape for ${rule.mutationType}"

            composeTestRule
                .onNodeWithTag(testTag, useUnmergedTree = true)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun rulesScreen_customRules_correctShapeForEachRuleType_isDisplayed() {
        composeTestRule.setContent {
            CustomRulesBody(
                uiState = RulesScreenUiState(rules = fakeRules),
                hasRules = fakeRules.isNotEmpty(),
                onClickRuleItem = {},
                selectedItems = setOf(),
                onUpdateSelectedItems = {},
            )
        }

        fakeRules.forEach { rule ->
            when (rule.mutationType) {
                MutationType.DOMAIN_NAME -> {
                    composeTestRule.onNodeWithContentDescription("Square", useUnmergedTree = true)
                        .assertExists()
                        .assertIsDisplayed()
                }

                MutationType.URL_PARAMS_ALL -> {
                    composeTestRule.onNodeWithContentDescription("Scallop", useUnmergedTree = true)
                        .assertExists()
                        .assertIsDisplayed()
                }

                MutationType.URL_PARAMS_SPECIFIC -> {
                    composeTestRule.onNodeWithContentDescription("Delta", useUnmergedTree = true)
                        .assertExists()
                        .assertIsDisplayed()
                }

                MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
                    composeTestRule.onNodeWithContentDescription("Clover", useUnmergedTree = true)
                        .assertExists()
                        .assertIsDisplayed()
                }

                MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC -> {
                    composeTestRule.onNodeWithContentDescription("Eight-point star", useUnmergedTree = true)
                        .assertExists()
                        .assertIsDisplayed()
                }

                MutationType.FALLBACK -> {
                    composeTestRule.onNodeWithContentDescription("Circle", useUnmergedTree = true)
                        .assertExists()
                        .assertIsDisplayed()
                }
            }
        }
    }

    @Test
    fun rulesScreen_builtInRules_allRulesAreDisplayed() {
        composeTestRule.setContent { EmptyRulesScreen() }

        composeTestRule
            .onNodeWithText(builtInTabLabel)
            .performClick()

        BuiltInRules.all.forEach {
            composeTestRule
                .onNodeWithText(it.name, useUnmergedTree = true)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun rulesScreen_selectedTabState_isPersistedAcrossActivityRecreation() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent { EmptyRulesScreen() }

        // Switch to "Built-in" tab
        composeTestRule
            .onNodeWithText(builtInTabLabel)
            .performClick()
            .assertIsSelected()

        // Trigger recreation and state restoration
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // Assert that the "Built-in" tab is still selected
        composeTestRule
            .onNodeWithText(builtInTabLabel)
            .assertIsSelected()
    }

    companion object {
        @Composable
        private fun EmptyRulesScreen() {
            RulesScreen(
                uiState = RulesScreenUiState(),
                onClickRuleItem = {},
                selectedItems = setOf(),
                onUpdateSelectedItems = {},
            )
        }

        private val fakeRules = listOf(
            DomainNameAndAllUrlParamsMutationModel(
                name = "Google rule",
                triggerDomain = "google.com",
                isLocalOnly = true,
                isEnabled = true,
                dateModifiedTimestamp = 1700174822,
                mutationInfo = DomainNameMutationInfo(
                    initialDomain = "google.com",
                    targetDomain = "google.co.uk"
                ),
                baseRuleId = 1,
            ),
            SpecificUrlParamsMutationModel(
                name = "YouTube - remove playlist association and timestamp, but nothing else",
                triggerDomain = "youtube.com",
                isLocalOnly = true,
                isEnabled = true,
                dateModifiedTimestamp = 1701970463,
                mutationInfo = SpecificUrlParamsMutationInfo(
                    removableParams = listOf("list", "t")
                ),
                baseRuleId = 2,
            ),
            DomainNameMutationModel(
                name = "Google rule 2",
                triggerDomain = "google.com",
                isLocalOnly = true,
                isEnabled = true,
                dateModifiedTimestamp = 1700174823,
                mutationInfo = DomainNameMutationInfo(
                    initialDomain = "google.fr",
                    targetDomain = "google.ca"
                ),
                baseRuleId = 3,
            ),
            AllUrlParamsMutationModel(
                name = "remove all params from reddit links",
                triggerDomain = "reddit.com",
                isLocalOnly = true,
                isEnabled = true,
                dateModifiedTimestamp = 1702899122,
                baseRuleId = 4,
            ),
        )
    }
}