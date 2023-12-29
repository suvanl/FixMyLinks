package com.suvanl.fixmylinks.ui.components.appbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.components.appbar.menu.OverflowMenu
import com.suvanl.fixmylinks.ui.theme.LetterSpacingDefaults
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot

enum class TopAppBarSize { SMALL, MEDIUM, LARGE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FmlTopAppBar(
    title: String,
    size: TopAppBarSize,
    scrollBehavior: TopAppBarScrollBehavior,
    currentBackStackEntryFlow: Flow<NavBackStackEntry>,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentContentBackStackEntry by produceState(
        initialValue = null as NavBackStackEntry?,
        producer = {
            currentBackStackEntryFlow
                .filterNot { it.destination is FloatingWindow }
                .collect { value = it }
        }
    )

    TopAppBarBody(
        title = title,
        size = size,
        scrollBehavior = scrollBehavior,
        onNavigateUp = onNavigateUp,
        modifier = modifier,
    ) {
        AppBarActionRow(navBackStackEntry = currentContentBackStackEntry)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarBody(
    title: String,
    size: TopAppBarSize,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    when (size) {
        TopAppBarSize.SMALL -> {
            TopAppBar(
                title = { TopAppBarTitle(text = title) },
                navigationIcon = { TopAppBarNavigationIcon(onNavigateUp = onNavigateUp) },
                actions = actions,
                scrollBehavior = scrollBehavior,
                modifier = modifier
            )
        }

        TopAppBarSize.MEDIUM -> {
            MediumTopAppBar(
                title = { TopAppBarTitle(text = title) },
                navigationIcon = { TopAppBarNavigationIcon(onNavigateUp = onNavigateUp) },
                actions = actions,
                scrollBehavior = scrollBehavior,
                modifier = modifier
            )
        }

        TopAppBarSize.LARGE -> {
            LargeTopAppBar(
                title = { TopAppBarTitle(text = title) },
                navigationIcon = { TopAppBarNavigationIcon(onNavigateUp = onNavigateUp) },
                actions = actions,
                scrollBehavior = scrollBehavior,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun TopAppBarTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit? = null
) {
    val letterSpacing = LetterSpacingDefaults.ExtraTight

    if (fontSize == null) {
        Text(
            text = text,
            letterSpacing = letterSpacing,
            modifier = modifier
        )
        return
    }

    Text(
        text = text,
        letterSpacing = letterSpacing,
        fontSize = fontSize,
        modifier = modifier
    )
}

@Composable
private fun TopAppBarNavigationIcon(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onNavigateUp,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(id = R.string.navigate_up)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(widthDp = 320)
@Preview(
    name = "Dark",
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FmlTopAppBarPreview() {
    PreviewContainer {
        TopAppBarBody(
            title = "Home",
            size = TopAppBarSize.SMALL,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onNavigateUp = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(widthDp = 320)
@Preview(
    name = "Dark",
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FmlTopAppBarWithOverflowMenuPreview() {
    PreviewContainer {
        TopAppBarBody(
            title = "Home",
            size = TopAppBarSize.SMALL,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onNavigateUp = {},
        ) {
            OverflowMenu {
                DropdownMenuItem(
                    text = { Text(text = "Test option") },
                    onClick = {}
                )
            }
        }
    }
}