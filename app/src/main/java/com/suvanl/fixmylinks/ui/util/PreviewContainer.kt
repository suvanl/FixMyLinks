package com.suvanl.fixmylinks.ui.util

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme

/**
 * A simple container that displays the given [content] within a [Surface], which itself is within
 * a [FixMyLinksTheme] composable. This should be used in composable previews to ensure an accurate
 * (in terms of how it'll look at runtime) representation of the design is displayed in the preview.
 */
@Composable
fun PreviewContainer(
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    FixMyLinksTheme {
        Surface {
            content(modifier)
        }
    }
}
