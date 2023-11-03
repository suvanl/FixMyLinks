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
import androidx.compose.ui.unit.TextUnit
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
                scrollBehavior = scrollBehavior,
                modifier = modifier
            )
        }

        TopAppBarSize.MEDIUM -> {
            MediumTopAppBar(
                title = { TopAppBarTitle(text = title) },
                navigationIcon = { TopAppBarNavigationIcon(onNavigateUp = { onNavigateUp() }) },
                scrollBehavior = scrollBehavior,
                modifier = modifier
            )
        }

        TopAppBarSize.LARGE -> {
            LargeTopAppBar(
                title = { TopAppBarTitle(text = title) },
                navigationIcon = { TopAppBarNavigationIcon(onNavigateUp = { onNavigateUp() }) },
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
    val letterSpacing = TIGHT_LETTER_SPACING.times(16)

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