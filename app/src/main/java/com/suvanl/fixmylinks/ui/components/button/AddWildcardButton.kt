package com.suvanl.fixmylinks.ui.components.button

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.util.PreviewContainer

@Composable
fun AddWildcardButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonContentDescription = stringResource(id = R.string.cd_add_wildcard_operator)
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier.semantics { contentDescription = buttonContentDescription }
    ) {
        Text(text = "*.", fontWeight = FontWeight.ExtraBold)
    }
}

/**
 * Variant of [AddWildcardButton] with animated visibility.
 * @param visible Defines whether the button should be visible.
 * @param onClick Called when the button is clicked.
 */
@Composable
fun AnimatedAddWildcardButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut(),
    ) {
        AddWildcardButton(onClick = onClick, modifier = modifier)
    }
}

@Preview
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AddWildcardButtonPreview() {
    PreviewContainer {
        AddWildcardButton(onClick = {})
    }
}