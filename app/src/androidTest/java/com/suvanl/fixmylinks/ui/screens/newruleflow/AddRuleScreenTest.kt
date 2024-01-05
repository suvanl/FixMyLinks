package com.suvanl.fixmylinks.ui.screens.newruleflow

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.components.form.AllUrlParamsRuleForm
import com.suvanl.fixmylinks.ui.components.form.AllUrlParamsRuleFormState
import com.suvanl.fixmylinks.ui.components.form.DomainNameRuleForm
import com.suvanl.fixmylinks.ui.components.form.DomainNameRuleFormState
import com.suvanl.fixmylinks.ui.components.form.SpecificUrlParamsRuleForm
import com.suvanl.fixmylinks.ui.components.form.SpecificUrlParamsRuleFormState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddRuleScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Composable
    private fun DefaultAddRuleScreen(
        mutationType: MutationType,
        layoutClass: String = "Compact",
    ) {
        AddRuleScreenBody(
            mutationType = mutationType,
            showSaveButton = layoutClass.lowercase() == "compact",
            onSaveClick = {},
            ruleOptions = RuleOptionsState(),
            onOptionsChanged = {},
        ) {
            when (mutationType) {
                MutationType.URL_PARAMS_SPECIFIC -> {
                    SpecificUrlParamsRuleForm(
                        showHints = true,
                        formState = SpecificUrlParamsRuleFormState(),
                        onRuleNameChange = {},
                        onDomainNameChange = {},
                        onClickAddParam = {},
                        onClickDismissParam = {},
                        onClickAddWildcard = {},
                    )
                }

                MutationType.DOMAIN_NAME,
                MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
                    DomainNameRuleForm(
                        formState = DomainNameRuleFormState(),
                        showHints = true,
                        onRuleNameChange = {},
                        onInitialDomainNameChange = {},
                        onTargetDomainNameChange = {},
                        onClickAddInitialWildcard = {},
                        onClickAddTargetWildcard = {},
                    )
                }
                MutationType.URL_PARAMS_ALL -> {
                    AllUrlParamsRuleForm(
                        showHints = true,
                        formState = AllUrlParamsRuleFormState(),
                        onRuleNameChange = {},
                        onDomainNameChange = {},
                        onClickAddWildcard = {},
                    )
                }
                else -> {
                    throw IllegalArgumentException("Unsupported MutationType in AddRuleScreenBody")
                }
            }
        }
    }

    @Test
    fun addRuleScreen_isDisplayed() {
        composeTestRule.setContent {
            DefaultAddRuleScreen(
                mutationType = MutationType.DOMAIN_NAME
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Add Rule Screen")
            .assertIsDisplayed()
    }

    @Test
    fun domainNameRule_DomainNameRuleForm_isDisplayed() {
        composeTestRule.setContent {
            DefaultAddRuleScreen(
                mutationType = MutationType.DOMAIN_NAME
            )
        }

        composeTestRule
            .onNodeWithTag("DomainNameRuleForm")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun allUrlParamsRule_AllUrlParamsRuleForm_isDisplayed() {
        composeTestRule.setContent {
            DefaultAddRuleScreen(
                mutationType = MutationType.URL_PARAMS_ALL
            )
        }

        composeTestRule
            .onNodeWithTag("AllUrlParamsRuleForm")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun specificUrlParamsRule_SpecificUrlParamsRuleForm_isDisplayed() {
        composeTestRule.setContent {
            DefaultAddRuleScreen(
                mutationType = MutationType.URL_PARAMS_SPECIFIC
            )
        }

        composeTestRule
            .onNodeWithTag("SpecificUrlParamsRuleForm")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun specificUrlParamsRule_DomainNameAndAllUrlParamsRule_isDisplayed() {
        composeTestRule.setContent {
            DefaultAddRuleScreen(
                mutationType = MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL
            )
        }

        composeTestRule
            .onNodeWithTag("DomainNameRuleForm")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun mediumLayout_saveButton_doesNotExist() {
        composeTestRule.setContent {
            DefaultAddRuleScreen(
                mutationType = MutationType.URL_PARAMS_SPECIFIC,
                layoutClass = "Medium"
            )
        }

        composeTestRule
            .onNodeWithTag("Save Button")
            .assertDoesNotExist()
    }

    @Test
    fun expandedLayout_saveButton_doesNotExist() {
        composeTestRule.setContent {
            DefaultAddRuleScreen(
                mutationType = MutationType.URL_PARAMS_SPECIFIC,
                layoutClass = "Expanded"
            )
        }

        composeTestRule
            .onNodeWithTag("Save Button")
            .assertDoesNotExist()
    }
}