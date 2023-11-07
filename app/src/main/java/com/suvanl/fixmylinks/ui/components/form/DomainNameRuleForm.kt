package com.suvanl.fixmylinks.ui.components.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.animation.TransitionDefaults
import com.suvanl.fixmylinks.ui.components.form.common.RuleNameField

@Composable
fun DomainNameRuleForm(
    showHints: Boolean,
    interFieldSpacing: Dp,
    onRuleNameChange: (String) -> Unit,
    onInitialDomainNameChange: (String) -> Unit,
    onTargetDomainNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var ruleNameText by rememberSaveable { mutableStateOf("") }
    var initialDomainNameText by rememberSaveable { mutableStateOf("") }
    var targetDomainNameText by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier) {
        // "Rule name"
        RuleNameField(
            text = ruleNameText,
            onValueChange = {
                ruleNameText = it
                onRuleNameChange(it)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(interFieldSpacing))

        // "Initial domain name"
        OutlinedTextField(
            value = initialDomainNameText,
            onValueChange = {
                initialDomainNameText = it
                onInitialDomainNameChange(it)
            },
            singleLine = true,
            label = {
                Text(text = stringResource(id = R.string.initial_domain_name))
            },
            supportingText = {
                AnimatedVisibility(
                    visible = showHints,
                    enter = TransitionDefaults.supportingTextEnterTransition
                ) {
                    Text(text = stringResource(id = R.string.initial_domain_supporting_text))
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Language,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(interFieldSpacing))

        // "Target domain name"
        OutlinedTextField(
            value = targetDomainNameText,
            onValueChange = {
                targetDomainNameText = it
                onTargetDomainNameChange(it)
            },
            singleLine = true,
            label = {
                Text(text = stringResource(id = R.string.target_domain_name))
            },
            supportingText = {
                AnimatedVisibility(
                    visible = showHints,
                    enter = TransitionDefaults.supportingTextEnterTransition
                ) {
                    Text(text = stringResource(id = R.string.target_domain_supporting_text))
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Language,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}