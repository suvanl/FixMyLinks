package com.suvanl.fixmylinks.ui.components.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.ui.components.button.AddNewRuleFab

@Composable
fun FmlNavigationRail(
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    navItemsColumn: @Composable () -> Unit,
) {
    NavigationRail(
        header = {
            AddNewRuleFab(
                onClick = { onFabClick() },
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxHeight()
        ) {
            navItemsColumn()
        }
    }
}