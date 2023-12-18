package com.suvanl.fixmylinks.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
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
class RulesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var noRulesAddedYetString: String

    @Before
    fun setup() {
        noRulesAddedYetString = composeTestRule.activity.getString(R.string.no_rules_added_yet)
    }

    @Test
    fun rulesScreen_withNoSavedRules_displaysEmptyRulesBody() {
        composeTestRule.setContent { EmptyRulesScreen() }

        composeTestRule.onNodeWithTag("Empty Rules Body").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText(noRulesAddedYetString).assertExists().assertIsDisplayed()
    }

    @Test
    fun rulesScreen_withTwoSavedRules_displaysRulesListItem_forEachOne() {
        val rules = fakeRules.take(2)

        composeTestRule.setContent {
            RulesScreen(
                uiState = RulesScreenUiState(rules = rules),
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
                .onNodeWithText(rule.name)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun rulesScreen_itemsAreSelectable_and_singleSelectedRule_isSelected() {
        val selectedRule = fakeRules[2]

        composeTestRule.setContent {
            RulesScreen(
                uiState = RulesScreenUiState(rules = fakeRules),
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
    fun rulesScreen_shapeRepresentingRuleType_isDisplayed() {
        composeTestRule.setContent {
            RulesScreen(
                uiState = RulesScreenUiState(rules = fakeRules),
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
                dateModifiedTimestamp = 1702899122,
                baseRuleId = 4,
            ),
        )
    }
}