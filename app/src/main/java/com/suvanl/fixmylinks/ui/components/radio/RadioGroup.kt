package com.suvanl.fixmylinks.ui.components.radio

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.ui.util.PreviewContainer

private val radioOptionHorizontalPadding = 8.dp

@Composable
private fun BaseRadioGroup(
    options: List<RadioOptionData>,
    modifier: Modifier = Modifier,
    radioOption: @Composable (Int, RadioOptionData) -> Unit
) {
    // Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(modifier = modifier.selectableGroup()) {
        options.forEachIndexed { index, optionData ->
            radioOption(index, optionData)
        }
    }
}

/**
 * Stateless RadioGroup composable
 */
@Composable
fun RadioGroup(
    options: List<RadioOptionData>,
    selectedOptionId: String,
    onOptionClick: (currentOption: RadioOptionData) -> Unit,
    modifier: Modifier = Modifier,
    customSpacerHeight: Dp? = null,
) {
    BaseRadioGroup(
        options = options,
        modifier = modifier
    ) { optionIndex, optionData ->
        RadioOption(
            data = optionData,
            isSelected = optionData.id == selectedOptionId,
            spacerHeight = calculateSpacerHeight(
                currentIndex = optionIndex,
                lastIndex = options.lastIndex,
                customHeight = customSpacerHeight
            ),
            onClick = { onOptionClick(optionData) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = radioOptionHorizontalPadding)
                .semantics { contentDescription = "${optionData.id} option" }
        )
    }
}

/**
 * Stateful RadioGroup composable
 */
@Composable
fun RadioGroup(
    options: List<RadioOptionData>,
    selectedState: MutableState<RadioOptionData>,
    modifier: Modifier = Modifier,
    customSpacerHeight: Dp? = null,
) {
    val (selectedOption, onOptionSelected) = selectedState

    BaseRadioGroup(
        options = options,
        modifier = modifier
    ) { optionIndex, optionData ->
        RadioOption(
            data = optionData,
            isSelected = optionData == selectedOption,
            spacerHeight = calculateSpacerHeight(
                optionIndex,
                options.lastIndex,
                customSpacerHeight
            ),
            onClick = {
                onOptionSelected(
                    options.find { it == optionData } ?: options.first()
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = radioOptionHorizontalPadding)
        )
    }
}

private fun calculateSpacerHeight(currentIndex: Int, lastIndex: Int, customHeight: Dp?): Dp {
    return if (currentIndex == lastIndex) {
        0.dp
    } else {
        customHeight ?: RadioOptionDefaults.spacerHeight
    }
}

@Preview(
    showBackground = true,
    widthDp = 320
)
@Preview(
    name = "Dark",
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RadioGroupPreview() {
    val radioOptions = listOf(
        RadioOptionData(
            id = "calls",
            title = "Calls",
            description = "Something pretty descriptive"
        ),
        RadioOptionData(
            id = "missed",
            title = "Missed",
            description = "Interesting description"
        ),
        RadioOptionData(
            id = "friends",
            title = "Friends",
            description = "Descriptions can definitely be longer than this!"
        ),
    )

    PreviewContainer {
        RadioGroup(
            options = radioOptions,
            selectedOptionId = "calls",
            onOptionClick = { /* do nothing */ },
            customSpacerHeight = 32.dp,
        )
    }
}