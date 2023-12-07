package com.suvanl.fixmylinks.ui.graphics

import androidx.graphics.shapes.RoundedPolygon

/**
 * Custom Material 3-style shapes, such as 'scallop', 'clover', wavy circle' etc.
 */
object CustomShapes {
    val ScallopPolygon = Scallop.polygon
    val ScallopShape = Scallop.shape
}

private interface CustomShape {
    val shape: RoundedPolygonShape
        get() = genShape()

    val polygon: RoundedPolygon
        get() = genPolygon()

    fun genPolygon(): RoundedPolygon

    fun genShape(): RoundedPolygonShape
}

private object Scallop : CustomShape {
    override fun genPolygon(): RoundedPolygon {
        val shapeParams = ShapeParameters(
            sides = 12,
            innerRadius = 0.928F,
            roundness = 0.1F,
        )

        val shapeItem = shapeParams.getShapeItemById(ShapeParameters.ShapeId.Star)
        val roundedPolygon = shapeParams.genShape(shapeItem).also { polygon ->
            val matrix = calculateMatrix(polygon.bounds, 1F, 1F)
            polygon.transform(matrix)
        }

        return roundedPolygon
    }

    override fun genShape(): RoundedPolygonShape = RoundedPolygonShape(polygon)
}