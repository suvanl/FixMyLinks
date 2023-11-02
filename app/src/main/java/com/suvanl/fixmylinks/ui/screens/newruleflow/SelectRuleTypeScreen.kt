package com.suvanl.fixmylinks.ui.screens.newruleflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.components.RadioGroup
import com.suvanl.fixmylinks.ui.components.RadioOptionData
import com.suvanl.fixmylinks.ui.util.StringResourceUtil

@Composable
fun SelectRuleTypeScreen(modifier: Modifier = Modifier) {
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

    val selectedOption = remember { mutableStateOf(radioOptions[0]) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(top = 16.dp)
            .semantics { contentDescription = "Select Rule Type Screen" },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        RuleTypeRadioGroup(
            options = radioOptions,
            selectedState = selectedOption,
            modifier = modifier.padding(horizontal = 8.dp)
        )
        
        Row(
            modifier = modifier.weight(1F, fill = false)
        ) {
            Text(text = "Heyy")
        }
    }
}

@Composable
private fun RuleTypeRadioGroup(
    options: List<RadioOptionData>,
    selectedState: MutableState<RadioOptionData>,
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
        },
        modifier = modifier
    )
}