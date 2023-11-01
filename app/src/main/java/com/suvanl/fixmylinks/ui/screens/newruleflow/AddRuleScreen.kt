package com.suvanl.fixmylinks.ui.screens.newruleflow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.domain.mutation.MutationType

@Composable
fun AddRuleScreen(
    mutationType: MutationType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Add Rule Screen" }
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add a new ${mutationType.name} rule",
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
}