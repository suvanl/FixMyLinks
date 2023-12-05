package com.suvanl.fixmylinks.ui.components.search

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import com.suvanl.fixmylinks.viewmodel.search.RulesSearchBarViewModel

data class SearchBarState(
    val query: String = "",
    val active: Boolean = false,
)

@Composable
fun RulesSearchBar(
    docked: Boolean,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp,
    viewModel: RulesSearchBarViewModel = hiltViewModel()
) {
    val searchBarState by viewModel.searchBarState.collectAsStateWithLifecycle()

    val searchPadding: Dp by animateDpAsState(
        targetValue = if (!searchBarState.active) horizontalPadding else 0.dp,
        label = "searchbar padding"
    )

    StandardSearchBar(
        query = searchBarState.query,
        onQueryChange = viewModel::setQuery,
        onSearch = { /* TODO */ },
        active = searchBarState.active,
        onActiveChange = { isActive ->
            if (isActive) viewModel.setIsActive(true) else viewModel.resetState()
        },
        modifier = modifier.then(
            if (horizontalPadding.value > 0) {
                Modifier.padding(horizontal = searchPadding)
            } else {
                Modifier
            }
        )
    ) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StandardSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_rules)
            )
        },
        leadingIcon = {
            AnimatedVisibility(
                visible = !active,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            }

            AnimatedVisibility(
                visible = active,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_up),
                    modifier = Modifier.clickable { onActiveChange(false) }
                )
            }
        },
        trailingIcon = {
            if (!active) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = stringResource(id = R.string.account_and_settings),
                    modifier = Modifier.clickable { /* TODO: show account menu */ }
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val hasQuery = query.isNotBlank()
                    if (hasQuery) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Clear text",
                            modifier = Modifier.clickable { onQueryChange("") }
                        )
                    }

                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = "Start voice search",
                        modifier = Modifier
                            .clickable { /* TODO: implement voice search */ }
                            .then(
                                if (hasQuery) {
                                    Modifier.padding(end = 12.dp)
                                } else {
                                    Modifier
                                }
                            )
                    )
                }
            }
        },
        modifier = modifier,
        content = content,
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
private fun StandardSearchBarPreview() {
    PreviewContainer {
        StandardSearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            active = false,
            onActiveChange = {},
        ) {}
    }
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
private fun StandardSearchBarActivePreview() {
    PreviewContainer {
        StandardSearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            active = true,
            onActiveChange = {},
        ) {}
    }
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
private fun StandardSearchBarActiveWithQueryPreview() {
    PreviewContainer {
        StandardSearchBar(
            query = "hellooo",
            onQueryChange = {},
            onSearch = {},
            active = true,
            onActiveChange = {},
        ) {}
    }
}