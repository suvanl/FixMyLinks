package com.suvanl.fixmylinks.ui.components.form.common

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.suvanl.fixmylinks.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DomainNameFieldTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var addWildcardButtonContentDescription: String

    @Before
    fun setup() {
        addWildcardButtonContentDescription =
            composeTestRule.activity.getString(R.string.cd_add_wildcard_operator)
    }

    private fun setDomainNameField(value: String = "") {
        composeTestRule.setContent {
            DomainNameField(
                value = value,
                errorMessage = null,
                showHints = true,
                isLastFieldInForm = false,
                onValueChange = {},
                onClickAddWildcard = {}
            )
        }
    }

    @Test
    fun domainNameField_addWildcardButton_isDisplayed_whenValueIsEmpty() {
        setDomainNameField()

        composeTestRule
            .onNodeWithContentDescription(addWildcardButtonContentDescription)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun domainNameField_addWildcardButton_isDisplayed_whenValueIsBlank() {
        setDomainNameField(value = "      ")

        composeTestRule
            .onNodeWithContentDescription(addWildcardButtonContentDescription)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun domainNameField_addWildcardButton_isNotDisplayed_whenValueIsNotBlank() {
        setDomainNameField(value = "w")

        composeTestRule
            .onNodeWithContentDescription(addWildcardButtonContentDescription)
            .assertDoesNotExist()
    }
}