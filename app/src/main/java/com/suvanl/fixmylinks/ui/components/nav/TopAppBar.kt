package com.suvanl.fixmylinks.ui.components.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.theme.TIGHT_LETTER_SPACING

enum class TopAppBarSize { SMALL, MEDIUM, LARGE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FmlTopAppBar(
    title: String,
    onNavigateUp: () -> Unit,
    size: TopAppBarSize,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    when (size) {
        TopAppBarSize.SMALL -> {
            TopAppBar(
                title = { TopAppBarTitle(text = title) },
                navigationIcon = { TopAppBarNavigationIcon(onNavigateUp = { onNavigateUp() }) },
                scrollBehavior = scrollBehavior
            )
        }

        TopAppBarSize.MEDIUM -> {
            MediumTopAppBar(
                title = { TopAppBarTitle(text = title) },
                navigationIcon = { TopAppBarNavigationIcon(onNavigateUp = { onNavigateUp() }) },
                scrollBehavior = scrollBehavior
            )
        }

        TopAppBarSize.LARGE -> {
            LargeTopAppBar(
                title = { TopAppBarTitle(text = title) },
                navigationIcon = { TopAppBarNavigationIcon(onNavigateUp = { onNavigateUp() }) },
                scrollBehavior = scrollBehavior
            )
        }
    }
}

@Composable
private fun TopAppBarTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        letterSpacing = (TIGHT_LETTER_SPACING.times(16))
    )
}

@Composable
private fun TopAppBarNavigationIcon(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = { onNavigateUp() }) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(id = R.string.navigate_up)
        )
    }
}