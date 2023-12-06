package com.suvanl.fixmylinks.ui.graphics

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.RoundedPolygon

/**
 * Represents a [RoundedPolygon] as a compose-friendly [Shape] object that has an [Outline]
 * which can be used to clip a composable or to be used as its shape (e.g., the shape of a `Box`
 * composable).
 * @param polygon The [RoundedPolygon] used to create the underlying path for this shape.
 */
class RoundedPolygonShape(private val polygon: RoundedPolygon) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val matrix = calculateMatrix(polygon.bounds, size.width, size.height)
        polygon.transform(matrix)
        return Outline.Generic(polygon.toPath().asComposePath())
    }
}