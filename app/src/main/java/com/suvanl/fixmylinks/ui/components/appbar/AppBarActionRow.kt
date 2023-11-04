package com.suvanl.fixmylinks.ui.components.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.LocalOwnersProvider

private class TopAppBarViewModel : ViewModel() {
    var actionState by mutableStateOf<(@Composable RowScope.() -> Unit)?>(
        value = null,
        policy = referentialEqualityPolicy()
    )
}

@Composable
fun ProvideAppBarActions(actions: @Composable RowScope.() -> Unit) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    if (viewModelStoreOwner == null || viewModelStoreOwner !is NavBackStackEntry) return

    val actionViewModel = viewModel(initializer = { TopAppBarViewModel() })
    SideEffect {
        actionViewModel.actionState = actions
    }
}

@Composable
fun RowScope.AppBarActionRow(navBackStackEntry: NavBackStackEntry?) {
    val stateHolder = rememberSaveableStateHolder()

    navBackStackEntry?.LocalOwnersProvider(stateHolder) {
        val actionViewModel = viewModel(initializer = { TopAppBarViewModel() })
        actionViewModel.actionState?.let { it() }
    }
}