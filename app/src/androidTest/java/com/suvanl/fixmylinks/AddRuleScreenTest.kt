package com.suvanl.fixmylinks

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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
import com.suvanl.fixmylinks.ui.components.list.SwitchListItemState
import com.suvanl.fixmylinks.ui.screens.newruleflow.AddRuleScreenBody
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
        val ruleOptions = listOf(
            SwitchListItemState(
                headlineText = stringResource(id = R.string.enable),
                supportingText = stringResource(R.string.enable_rule_supporting_text),
                leadingIcon = Icons.Outlined.CheckCircle,
                isSwitchChecked = true,
                onSwitchCheckedChange = {},
            ),
            SwitchListItemState(
                headlineText = stringResource(R.string.backup_to_cloud),
                supportingText = stringResource(R.string.backup_to_cloud_supporting_text),
                leadingIcon = Icons.Outlined.Backup,
                isSwitchChecked = false,
                onSwitchCheckedChange = {},
            )
        )

        AddRuleScreenBody(
            mutationType = mutationType,
            showSaveButton = layoutClass.lowercase() == "compact",
            ruleOptions = ruleOptions,
            onSaveClick = {}
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
                    )
                }
                MutationType.URL_PARAMS_ALL -> {
                    AllUrlParamsRuleForm(
                        showHints = true,
                        formState = AllUrlParamsRuleFormState(),
                        onRuleNameChange = {},
                        onDomainNameChange = {}
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