package com.suvanl.fixmylinks.viewmodel.search

import androidx.lifecycle.ViewModel
import com.suvanl.fixmylinks.ui.components.search.SearchBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RulesSearchBarViewModel @Inject constructor() : ViewModel() {

    private val _searchBarState = MutableStateFlow(SearchBarState())
    val searchBarState = _searchBarState.asStateFlow()

    fun setIsActive(isActive: Boolean) {
        _searchBarState.value = _searchBarState.value.copy(active = isActive)
    }

    fun setQuery(text: String) {
        _searchBarState.value = _searchBarState.value.copy(query = text)
    }

    fun resetState() {
        _searchBarState.value = SearchBarState()
    }
}