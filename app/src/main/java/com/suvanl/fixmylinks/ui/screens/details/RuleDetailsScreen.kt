package com.suvanl.fixmylinks.ui.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.viewmodel.rule.RuleDetailsViewModel

@Composable
fun RuleDetailsScreen(
    mutationType: MutationType,
    baseRuleId: Long,
    onClickEdit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RuleDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.updateSelectedRule(baseRuleId, mutationType)
    }

    val rule by viewModel.selectedRule.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        Text(text = "Rule details")
        Text(text = "type: $mutationType")
        Text(text = "base rule ID: $baseRuleId")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Name: ${rule?.name}; On: ${rule?.triggerDomain}")
    }
}