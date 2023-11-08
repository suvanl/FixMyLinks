package com.suvanl.fixmylinks.ui.screens.newruleflow

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.components.RadioGroup
import com.suvanl.fixmylinks.ui.components.RadioOptionData
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.ui.util.StringResourceUtil

@Composable
fun SelectRuleTypeScreen(
    showNextButton: Boolean,
    onSelectedMutationTypeChanged: (RadioOptionData) -> Unit,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectableMutationTypes = setOf(
        MutationType.DOMAIN_NAME,
        MutationType.URL_PARAMS_ALL,
        MutationType.URL_PARAMS_SPECIFIC,
        MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL
    )

    val radioOptions = selectableMutationTypes.map {
        RadioOptionData(
            id = it.name,
            title = stringResource(
                id = StringResourceUtil.humanizedMutationTypeNames.getOrDefault(
                    key = it,
                    defaultValue = StringResourceUtil.StringResource(R.string.empty)
                ).id
            ),
            description = stringResource(
                id = StringResourceUtil.humanizedMutationTypeDescriptions.getOrDefault(
                    key = it,
                    defaultValue = StringResourceUtil.StringResource(R.string.empty)
                ).id
            )
        )
    }

    val selectedOption = rememberSaveable(stateSaver = RadioOptionData.saver) {
        mutableStateOf(radioOptions[0])
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp)
            .semantics { contentDescription = "Select Rule Type Screen" },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        RuleTypeRadioGroup(
            options = radioOptions,
            selectedState = selectedOption,
            onSelectionChanged = onSelectedMutationTypeChanged,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // The row containing the "Next" button is conditionally shown as it won't be required
        // in cases where this button is provided inside the Top App Bar instead, i.e.,
        // medium and expanded layouts.
        if (showNextButton) {
            Spacer(modifier = Modifier.weight(1F))

            Row(
                modifier = modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onNextButtonClick,
                    modifier = modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(bottom = 32.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }
}

@Composable
private fun RuleTypeRadioGroup(
    options: List<RadioOptionData>,
    selectedState: MutableState<RadioOptionData>,
    onSelectionChanged: (RadioOptionData) -> Unit,
    modifier: Modifier = Modifier
) {
    val (selectedOption, onOptionSelected) = selectedState

    RadioGroup(
        options = options,
        selectedOptionId = selectedOption.id,
        onOptionClick = { currentOption ->
            onOptionSelected(
                options.find { it == currentOption } ?: options.first()
            )

            onSelectionChanged(currentOption)
        },
        modifier = modifier
    )
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
fun SelectRuleTypeScreenPreview() {
    PreviewContainer {
        SelectRuleTypeScreen(
            showNextButton = true,
            onSelectedMutationTypeChanged = { /* do nothing */ },
            onNextButtonClick = { /* do nothing */ }
        )
    }
}