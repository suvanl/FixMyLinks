package com.suvanl.fixmylinks.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon
import com.suvanl.fixmylinks.ui.graphics.CustomShapes.ScallopPolygon
import com.suvanl.fixmylinks.ui.graphics.scaled
import com.suvanl.fixmylinks.ui.graphics.toPath
import com.suvanl.fixmylinks.ui.util.PreviewContainer
import kotlin.math.min

/**
 * [Box]-like layout composable composable that draws a [RoundedPolygon] with optional content
 * overlaid within it.
 */
@Composable
fun Polygon(
    polygon: RoundedPolygon,
    modifier: Modifier = Modifier,
    color: Color = Color.Transparent,
    content: @Composable (BoxScope.() -> Unit)? = null,
) {
    val sizedShape = remember(polygon) { mutableMapOf<Size, List<Cubic>>() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .drawWithContent {
                val scale = min(size.width, size.height)
                val shape = sizedShape.getOrPut(size) { polygon.cubics.scaled(scale) }
                drawPath(shape.toPath(), color)

                // Draw content after drawing the polygon shape to ensure children (`content`)
                // appear on top of the shape
                drawContent()
            }
    ) {
        if (content != null) {
            content()
        }
    }
}

@Preview(
    name = "Scallop shape",
    widthDp = 320
)
@Composable
private fun PolygonPreview() {
    PreviewContainer {
        Polygon(
            polygon = ScallopPolygon,
            modifier = Modifier.aspectRatio(1F),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(
    name = "Scallop shape with content",
    widthDp = 320
)
@Composable
private fun PolygonWithContentPreview() {
    PreviewContainer {
        Polygon(
            polygon = ScallopPolygon,
            modifier = Modifier.aspectRatio(1F),
            color = MaterialTheme.colorScheme.primary
        ) {
            Text(
                text = "Hello",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 100.sp,
                softWrap = false
            )
        }
    }
}