package com.suvanl.fixmylinks.ui.components.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.suvanl.fixmylinks.R

@Composable
fun EditFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
) {
    FloatingActionButton(
        onClick = onClick,
        elevation = elevation,
        modifier = modifier.semantics { testTag = "Edit FAB" }
    ) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = stringResource(id = R.string.edit)
        )
    }
}