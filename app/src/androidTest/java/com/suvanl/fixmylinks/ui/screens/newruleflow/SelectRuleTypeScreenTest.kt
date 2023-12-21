package com.suvanl.fixmylinks.ui.screens.newruleflow

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.suvanl.fixmylinks.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SelectRuleTypeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var changeDomainNameOptionString: String
    private lateinit var changeDomainNameDescriptionString: String

    private lateinit var removeAllUrlParamsOptionString: String
    private lateinit var removeAllUrlParamsDescriptionString: String

    private lateinit var removeSpecificUrlParamsOptionString: String
    private lateinit var removeSpecificUrlParamsDescriptionString: String

    private lateinit var changeDomainNameAndRemoveAllUrlParamsOptionString: String
    private lateinit var changeDomainNameAndRemoveAllUrlParamsDescriptionString: String

    @Composable
    private fun DefaultCompactLayout() {
        SelectRuleTypeScreen(
            showNextButton = true,
            onSelectedMutationTypeChanged = {},
            onNextButtonClick = {}
        )
    }

    @Composable
    private fun DefaultMediumExpandedLayout() {
        SelectRuleTypeScreen(
            showNextButton = false,
            onSelectedMutationTypeChanged = {},
            onNextButtonClick = {}
        )
    }

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            // initialise string resources
            changeDomainNameOptionString = getString(R.string.mt_domain_name)
            changeDomainNameDescriptionString = getString(R.string.mt_domain_name_desc)

            removeAllUrlParamsOptionString = getString(R.string.mt_url_params_all)
            removeAllUrlParamsDescriptionString = getString(R.string.mt_url_params_all_desc)

            removeSpecificUrlParamsOptionString = getString(R.string.mt_url_params_specific)
            removeSpecificUrlParamsDescriptionString =
                getString(R.string.mt_url_params_specific_desc)

            changeDomainNameAndRemoveAllUrlParamsOptionString =
                getString(R.string.mt_domain_name_and_url_params_all)
            changeDomainNameAndRemoveAllUrlParamsDescriptionString =
                getString(R.string.mt_domain_name_and_url_params_all_desc)
        }
    }

    @Test
    fun selectRuleTypeScreen_activityRecreation_restoresSelectedRuleTypeState() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent { DefaultCompactLayout() }

        // Modify the default state by selecting the "Specific URL parameters" option by clicking it
        composeTestRule
            .onNodeWithContentDescription("URL_PARAMS_SPECIFIC option")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("URL_PARAMS_SPECIFIC option", useUnmergedTree = true)
            .printToLog("SelectRuleTypeScreenTest")

        // Trigger recreation and state restoration
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // Assert that the "Specific URL parameters" option remains selected
        composeTestRule
            .onNode(
                matcher = isSelected() and hasParent(
                    hasContentDescription("URL_PARAMS_SPECIFIC option")
                ),
                useUnmergedTree = true
            )
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun selectRuleTypeScreen_compact_changeDomainNameOption_isDisplayed() {
        composeTestRule.setContent {
            DefaultCompactLayout()
        }

        composeTestRule
            .onNodeWithContentDescription("$changeDomainNameOptionString option")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(changeDomainNameDescriptionString)
            .assertIsDisplayed()
    }

    @Test
    fun selectRuleTypeScreen_compact_removeAllUrlParams_isDisplayed() {
        composeTestRule.setContent {
            DefaultCompactLayout()
        }

        composeTestRule
            .onNodeWithContentDescription("$removeAllUrlParamsOptionString option")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(removeAllUrlParamsDescriptionString)
            .assertIsDisplayed()
    }

    @Test
    fun selectRuleTypeScreen_compact_removeSpecificUrlParams_isDisplayed() {
        composeTestRule.setContent {
            DefaultCompactLayout()
        }

        composeTestRule
            .onNodeWithContentDescription("$removeSpecificUrlParamsOptionString option")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(removeSpecificUrlParamsDescriptionString)
            .assertIsDisplayed()
    }

    @Test
    fun selectRuleTypeScreen_compact_changeDomainNameAndRemoveAllUrlParams_isDisplayed() {
        composeTestRule.setContent {
            DefaultCompactLayout()
        }

        composeTestRule
            .onNodeWithContentDescription("$changeDomainNameAndRemoveAllUrlParamsOptionString option")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(changeDomainNameAndRemoveAllUrlParamsDescriptionString)
            .assertIsDisplayed()
    }

    @Test
    fun selectRuleTypeScreen_compact_nextButton_isDisplayed() {
        composeTestRule.setContent {
            DefaultCompactLayout()
        }

        composeTestRule
            .onNodeWithTag("Next")
            .assertIsDisplayed()
    }

    @Test
    fun selectRuleTypeScreen_mediumOrExpanded_nextButton_doesNotExist() {
        composeTestRule.setContent {
            DefaultMediumExpandedLayout()
        }

        composeTestRule
            .onNodeWithTag("Next")
            .assertDoesNotExist()
    }
}