package com.suvanl.fixmylinks.ui.graphics

/**
 * Custom Material 3-style shapes, such as 'scallop', 'clover', wavy circle' etc.
 */
object CustomShapes {
    val ScallopShape: RoundedPolygonShape = Scallop.shape
}

private interface CustomShape {
    val shape: RoundedPolygonShape
        get() = genShape()

    fun genShape(): RoundedPolygonShape
}

private object Scallop : CustomShape {
    override fun genShape(): RoundedPolygonShape {
        val scallopShapeParams = ShapeParameters(
            sides = 12,
            innerRadius = 0.928F,
            roundness = 0.1F,
        )

        val scallopShapeItem = scallopShapeParams.getShapeItemById(ShapeParameters.ShapeId.Star)
        val scallopRoundedPolygon = scallopShapeParams.genShape(scallopShapeItem).also { polygon ->
            val matrix = calculateMatrix(polygon.bounds, 1F, 1F)
            polygon.transform(matrix)
        }

        return RoundedPolygonShape(scallopRoundedPolygon)
    }
}