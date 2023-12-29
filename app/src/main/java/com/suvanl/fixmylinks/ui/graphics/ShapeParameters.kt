package com.suvanl.fixmylinks.ui.graphics

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.graphics.Matrix
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Represents the parameters of a custom "Material 3"-style shape, such as a 'scallop', 'clover',
 * 'wavy circle' etc.
 *
 * **See also**: https://stackoverflow.com/a/76396178
 *
 * **Adapted from**: https://github.com/chethaase/ShapesDemo
 */
class ShapeParameters(
    sides: Int = 5,
    innerRadius: Float = 0.5F,
    roundness: Float = 0F,
    smooth: Float = 0F,
    innerRoundness: Float = roundness,
    innerSmooth: Float = smooth,
    rotation: Float = 0F
) {
    enum class ShapeId { Star, Polygon, Triangle, Blob }

    data class ShapeItem(
        val id: ShapeId,
        val usesSides: Boolean = true,
        val usesInnerRatio: Boolean = true,
        val usesRoundness: Boolean = true,
        val usesInnerParameters: Boolean = true,
        val genShape: () -> RoundedPolygon,
    )

    private val sidesState = mutableFloatStateOf(sides.toFloat())
    private val innerRadiusState = mutableFloatStateOf(innerRadius)
    private val roundnessState = mutableFloatStateOf(roundness)
    private val smoothState = mutableFloatStateOf(smooth)
    private val innerRoundnessState = mutableFloatStateOf(innerRoundness)
    private val innerSmoothState = mutableFloatStateOf(innerSmooth)
    private val rotationState = mutableFloatStateOf(rotation)

    /**
     * Primitive shapes that can be drawn
     */
    private val shapes = listOf(
        ShapeItem(id = ShapeId.Star) {
            RoundedPolygon.star(
                numVerticesPerRadius = sidesState.floatValue.roundToInt(),
                innerRadius = innerRadiusState.floatValue,
                rounding = CornerRounding(
                    roundnessState.floatValue,
                    smoothState.floatValue
                ),
                innerRounding = CornerRounding(
                    innerRoundnessState.floatValue,
                    innerSmoothState.floatValue
                )
            )
        },

        ShapeItem(
            id = ShapeId.Polygon,
            usesInnerRatio = false,
            usesInnerParameters = false
        ) {
            RoundedPolygon(
                numVertices = sidesState.floatValue.roundToInt(),
                rounding = CornerRounding(roundnessState.floatValue)
            )
        },

        ShapeItem(
            id = ShapeId.Triangle,
            usesSides = false,
            usesInnerParameters = false
        ) {
            val points = floatArrayOf(
                radialToCartesian(1F, 270F.toRadians()).x,
                radialToCartesian(1F, 270F.toRadians()).y,

                radialToCartesian(1F, 30F.toRadians()).x,
                radialToCartesian(1F, 30F.toRadians()).y,

                radialToCartesian(innerRadiusState.floatValue, 90F.toRadians()).x,
                radialToCartesian(innerRadiusState.floatValue, 90F.toRadians()).y,

                radialToCartesian(1F, 150F.toRadians()).x,
                radialToCartesian(1F, 150F.toRadians()).y,
            )

            RoundedPolygon(
                vertices = points,
                rounding = CornerRounding(roundnessState.floatValue, smoothState.floatValue),
                centerX = 0F,
                centerY = 0F,
            )
        },

        ShapeItem(
            id = ShapeId.Blob,
            usesSides = false,
            usesInnerParameters = false
        ) {
            // scale
            val sx = innerRadiusState.floatValue.coerceAtLeast(0.55F)
            val sy = roundnessState.floatValue.coerceAtLeast(0.1F)

            val vertices = floatArrayOf(
//                -sx, -sy,
                sx, -sy,
                sx, sy,
                -sx, sy,
            )

            RoundedPolygon(
                vertices = vertices,
                rounding = CornerRounding(min(sx, sy), smoothState.floatValue),
                centerX = 0F,
                centerY = 0F,
            )
        }
    )

    fun getShapeItemById(id: ShapeId) = shapes.find { it.id == id }
        ?: throw IllegalArgumentException("A shape with the id $id does not exist")

    fun genShape(shapeItem: ShapeItem, autoSize: Boolean = true): RoundedPolygon {
        return shapeItem.genShape().let { polygon ->
            polygon.transformedWithMatrix(Matrix().apply {
                if (autoSize) {
                    val bounds = polygon.getBounds()

                    // Move the center to the origin
                    translate(
                        x = -(bounds.left + bounds.right) / 2,
                        y = -(bounds.top + bounds.bottom) / 2
                    )

                    // Scale to the [-1, 1] range
                    val scale = 2F / max(bounds.width, bounds.height)
                    scale(x = scale, y = scale)
                }

                // Apply required rotation
                rotateZ(rotationState.floatValue)
            })
        }
    }
}