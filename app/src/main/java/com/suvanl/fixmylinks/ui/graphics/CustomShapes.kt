package com.suvanl.fixmylinks.ui.graphics

import androidx.graphics.shapes.RoundedPolygon

/**
 * Custom Material 3-style shapes, such as 'scallop', 'clover', wavy circle' etc.
 */
object CustomShapes {
    val ScallopPolygon = genPolygon(PolygonParams.ScallopParams, ShapeParameters.ShapeId.Star)
    val ScallopShape = RoundedPolygonShape(ScallopPolygon)
}

private object PolygonParams {
    val ScallopParams = ShapeParameters(sides = 12, innerRadius = 0.928F, roundness = 0.1F)
}

private fun genPolygon(
    shapeParams: ShapeParameters,
    shapeId: ShapeParameters.ShapeId
): RoundedPolygon {
    val shapeItem = shapeParams.getShapeItemById(shapeId)
    return shapeParams.genShape(shapeItem).also { polygon ->
        val matrix = calculateMatrix(polygon.bounds, 1F, 1F)
        polygon.transform(matrix)
    }
}