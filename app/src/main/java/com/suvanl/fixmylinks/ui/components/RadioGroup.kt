package com.suvanl.fixmylinks.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.ui.util.PreviewContainer

/**
 * Stateless RadioGroup composable
 */
@Composable
fun RadioGroup(
    options: List<RadioOptionData>,
    selectedOptionId: String,
    onOptionClick: (currentOption: RadioOptionData) -> Unit,
    modifier: Modifier = Modifier
) {
    // Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(Modifier.selectableGroup()) {
        options.forEach { optionData ->
            RadioOption(
                data = optionData,
                isSelected = optionData.id == selectedOptionId,
                onClick = { onOptionClick(optionData) },
                modifier = modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

/**
 * Stateful RadioGroup composable
 */
@Composable
fun RadioGroup(
    options: List<RadioOptionData>,
    selectedState: MutableState<RadioOptionData>,
    modifier: Modifier = Modifier
) {
    val (selectedOption, onOptionSelected) = selectedState

    // Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(Modifier.selectableGroup()) {
        options.forEach { optionData ->
            RadioOption(
                data = optionData,
                isSelected = optionData == selectedOption,
                onClick = {
                    onOptionSelected(
                        options.find { it == optionData } ?: options.first()
                    )
                },
                modifier = modifier.padding(horizontal = 16.dp)
            )
        }
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
            onOptionClick = { /* do nothing */ }
        )
    }
}