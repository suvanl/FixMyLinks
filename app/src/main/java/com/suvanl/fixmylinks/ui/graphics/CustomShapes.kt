package com.suvanl.fixmylinks.ui.graphics

import androidx.graphics.shapes.RoundedPolygon

/**
 * Custom Material 3-style shapes, such as 'scallop', 'clover', wavy circle' etc.
 */
object CustomShapes {
    val ScallopPolygon = genPolygon(PolygonParams.ScallopParams, ShapeParameters.ShapeId.Star)
    val ScallopShape = RoundedPolygonShape(ScallopPolygon)

    val CloverPolygon = genPolygon(PolygonParams.CloverParams, ShapeParameters.ShapeId.Star)
    val CloverShape = RoundedPolygonShape(CloverPolygon)

    val DeltaPolygon = genPolygon(PolygonParams.DeltaParams, ShapeParameters.ShapeId.Triangle)
    val DeltaShape = RoundedPolygonShape(DeltaPolygon)

    val EightPointStarPolygon =
        genPolygon(PolygonParams.EightPointStarParams, ShapeParameters.ShapeId.Star)
    val EightPointStarShape = RoundedPolygonShape(EightPointStarPolygon)

    val WavyCirclePolygon = genPolygon(PolygonParams.WavyCircleParams, ShapeParameters.ShapeId.Star)
    val WavyCircleShape = RoundedPolygonShape(WavyCirclePolygon)

    val TiltedPillPolygon = genPolygon(PolygonParams.TiltedPillParams, ShapeParameters.ShapeId.Blob)
    val TiltedPillShape = RoundedPolygonShape(TiltedPillPolygon)
}

private object PolygonParams {
    val ScallopParams = ShapeParameters(sides = 12, innerRadius = 0.928F, roundness = 0.1F)
    val CloverParams = ShapeParameters(
        sides = 4,
        innerRadius = 0.352F,
        roundness = 0.32F,
        rotation = 45F,
    )
    val DeltaParams = ShapeParameters(innerRadius = 0.1F, roundness = 0.22F)
    val EightPointStarParams = ShapeParameters(sides = 8, innerRadius = 0.784F, roundness = 0.16F)
    val WavyCircleParams = ShapeParameters(sides = 15, innerRadius = 0.892F, roundness = 1F)
    val TiltedPillParams = ShapeParameters(innerRadius = 0.19F, roundness = 0.86F, rotation = 45F)
}

private fun genPolygon(
    shapeParams: ShapeParameters,
    shapeId: ShapeParameters.ShapeId
): RoundedPolygon {
    val shapeItem = shapeParams.getShapeItemById(shapeId)
    return shapeParams.genShape(shapeItem).normalized()
}