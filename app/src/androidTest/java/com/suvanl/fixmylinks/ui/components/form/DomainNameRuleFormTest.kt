package com.suvanl.fixmylinks.ui.components.form

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.suvanl.fixmylinks.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DomainNameRuleFormTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var addWildcardButtonContentDescription: String
    private lateinit var initialDomainNameLabel: String
    private lateinit var targetDomainNameLabel: String
    private lateinit var ruleNameLabel: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            addWildcardButtonContentDescription = getString(R.string.cd_add_wildcard_operator)
            initialDomainNameLabel = getString(R.string.initial_domain_name)
            targetDomainNameLabel = getString(R.string.target_domain_name)
            ruleNameLabel = getString(R.string.rule_name)
        }
    }

    private fun setDomainNameRuleForm(formState: DomainNameRuleFormState = DomainNameRuleFormState()) {
        composeTestRule.setContent {
            DomainNameRuleForm(
                formState = formState,
                showHints = true,
                onRuleNameChange = {},
                onInitialDomainNameChange = {},
                onTargetDomainNameChange = {},
                onClickAddInitialWildcard = {},
                onClickAddTargetWildcard = {},
            )
        }
    }

    @Test
    fun domainNameRuleForm_ruleNameField_isDisplayed() {
        setDomainNameRuleForm()

        composeTestRule
            .onNodeWithText(ruleNameLabel)
            .assertExists()
            .assertIsDisplayed()
    }

    // Assert that the "add wildcard operator" button is displayed on the "initial domain name" and
    // "target domain name" fields in DomainNameRuleForm. No need to test this in the other forms
    // (AllUrlParamsRuleForm and SpecificUrlParamsRuleForm), since these use the reusable
    // DomainNameField composable, which already has tests to assert this (see DomainNameFieldTest).

    @Test
    fun domainNameRuleForm_displaysAddWildcardButton_inInitialDomainNameField_whenEmpty() {
        setDomainNameRuleForm()

        composeTestRule
            .onNode(
                matcher = hasContentDescription(addWildcardButtonContentDescription) and hasParent(
                    hasText(initialDomainNameLabel)
                )
            )
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun domainNameRuleForm_displaysAddWildcardButton_inTargetDomainNameField_whenEmpty() {
        setDomainNameRuleForm()

        composeTestRule
            .onNode(
                matcher = hasContentDescription(addWildcardButtonContentDescription) and hasParent(
                    hasText(targetDomainNameLabel)
                )
            )
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun domainNameRuleForm_displaysAddWildcardButton_inInitialDomainNameField_whenBlank() {
        setDomainNameRuleForm(DomainNameRuleFormState(initialDomainName = "   "))

        composeTestRule
            .onNode(
                matcher = hasContentDescription(addWildcardButtonContentDescription) and hasParent(
                    hasText(initialDomainNameLabel)
                )
            )
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun domainNameRuleForm_displaysAddWildcardButton_inTargetDomainNameField_whenBlank() {
        setDomainNameRuleForm(DomainNameRuleFormState(targetDomainName = "   "))

        composeTestRule
            .onNode(
                matcher = hasContentDescription(addWildcardButtonContentDescription) and hasParent(
                    hasText(targetDomainNameLabel)
                )
            )
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun domainNameRuleForm_doesNotDisplayAddWildcardButton_inInitialDomainNameField_whenNotBlank() {
        setDomainNameRuleForm(DomainNameRuleFormState(initialDomainName = "a"))

        composeTestRule
            .onNode(
                matcher = hasContentDescription(addWildcardButtonContentDescription) and hasParent(
                    hasText(initialDomainNameLabel)
                )
            )
            .assertDoesNotExist()
    }

    @Test
    fun domainNameRuleForm_doesNotDisplayAddWildcardButton_inTargetDomainNameField_whenNotBlank() {
        setDomainNameRuleForm(DomainNameRuleFormState(targetDomainName = "a"))

        composeTestRule
            .onNode(
                matcher = hasContentDescription(addWildcardButtonContentDescription) and hasParent(
                    hasText(targetDomainNameLabel)
                )
            )
            .assertDoesNotExist()
    }
}