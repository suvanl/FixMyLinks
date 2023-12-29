package com.suvanl.fixmylinks.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.graphics.shapes.Morph
import com.suvanl.fixmylinks.ui.graphics.toComposePath
import kotlin.math.min

/**
 * A [Polygon] layout composable that supports morphing between `RoundedPolygon`s.
 */
@Composable
fun MorphingPolygon(
    sizedMorph: Morph,
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = Color.Transparent,
    content: @Composable (() -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .drawWithContent {
                val scale = min(size.width, size.height)
                val path = sizedMorph.toComposePath(progress, scale)
                drawPath(path, color)

                // Draw content after drawing the morphing polygon shape to ensure children (`content`)
                // appear on top of the shape
                drawContent()
            }
    ) {
        if (content != null) {
            content()
        }
    }
}