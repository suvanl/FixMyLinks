package com.suvanl.fixmylinks.ui.components.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.theme.TIGHT_LETTER_SPACING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FmlTopAppBar(
    title: String,
    onNavigateUp: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                letterSpacing = (TIGHT_LETTER_SPACING.times(16))
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_up)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}