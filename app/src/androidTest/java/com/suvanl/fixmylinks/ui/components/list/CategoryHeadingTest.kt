package com.suvanl.fixmylinks.ui.components.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class CategoryHeadingTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun categoryHeading_indicatorDot_isInitiallyShown_thenAnimatesOut() {
        val indicatorTestTag = "indicator dot"
        composeTestRule.setContent {
            CategoryHeading(category = RulesListCategory.ACTIVE)
        }

        // Pause animations
        composeTestRule.mainClock.autoAdvance = false

        // Assert that the indicator dot is displayed
        composeTestRule
            .onNodeWithTag(indicatorTestTag)
            .assertExists()
            .assertIsDisplayed()

        // Let the animation proceed (allow additional 0.5 sec for it to animate out)
        composeTestRule.mainClock.advanceTimeBy(INDICATOR_DOT_DISPLAY_DURATION + 500)

        // Assert that the indicator is no longer displayed (as it has animated out)
        composeTestRule
            .onNodeWithTag(indicatorTestTag)
            .assertDoesNotExist()
    }
}