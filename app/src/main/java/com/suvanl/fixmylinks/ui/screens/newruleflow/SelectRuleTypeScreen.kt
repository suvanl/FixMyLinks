package com.suvanl.fixmylinks.ui.screens.newruleflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

    val selectedOption = rememberSaveable(stateSaver = RadioOptionData.saver) {
        mutableStateOf(radioOptions[0])
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp, horizontal = 8.dp)
            .semantics { contentDescription = "Select Rule Type Screen" },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        RuleTypeRadioGroup(
            options = radioOptions,
            selectedState = selectedOption,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(1F, fill = false)
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.next))
            }
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