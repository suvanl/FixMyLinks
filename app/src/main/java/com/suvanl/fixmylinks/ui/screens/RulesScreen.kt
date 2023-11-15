package com.suvanl.fixmylinks.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suvanl.fixmylinks.di.AppViewModelProvider
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.viewmodel.RulesViewModel
import kotlinx.coroutines.launch

data class RulesScreenUiState(
    val rules: List<BaseMutationModel> = listOf()
)

@Composable
fun RulesScreen(
    modifier: Modifier = Modifier,
    viewModel: RulesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.rulesScreenUiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = "Rules Screen" }
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.deleteAll()
                }
            }
        ) {
            Text(text = "Delete all")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = uiState.rules.sortedByDescending { it.dateModifiedTimestamp }) { rule ->
                Column {
                    Text(
                        text = rule.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "${rule.mutationType.name} on ${rule.triggerDomain}"
                    )

                    Text(
                        text = "Last modified ${rule.dateModifiedTimestamp}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}