package com.suvanl.fixmylinks.ui.graphics

import android.graphics.PointF
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.core.graphics.plus
import androidx.core.graphics.times
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.TransformResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/*
 * These utility functions are adapted from the AndroidX Graphics-Shapes demo repo at
 * https://github.com/chethaase/ShapesDemo.
 *
 * The implementations of the Material 3-style shapes in this project are heavily based on that of
 * the demo project.
 */

val PointZero = PointF(0f, 0f)

fun calculateMatrix(bounds: RectF, width: Float, height: Float): Matrix {
    val originalWidth = bounds.right - bounds.left
    val originalHeight = bounds.bottom - bounds.top

    val scale = min(width / originalWidth, height / originalHeight)

    val newLeft = bounds.left - (width / scale - originalWidth) / 2
    val newTop = bounds.top - (height / scale - originalHeight) / 2

    return Matrix().apply {
        translate(-newLeft, -newTop)
        scale(scale, scale)
    }
}

fun radialToCartesian(
    radius: Float,
    angleRadians: Float,
    center: PointF = PointZero
) = directionVectorPointF(angleRadians) * radius + center

fun directionVectorPointF(angleRadians: Float) = PointF(cos(angleRadians), sin(angleRadians))

fun Float.toRadians() = this * PI.toFloat() / 180F

/**
 * Scales a given shape (as a List), creating a new List of [Cubic]s.
 */
fun List<Cubic>.scaled(scale: Float) = map {
    it.transformed { x, y -> TransformResult(x * scale, y * scale) }
}

/**
 * Creates a path from a list of [Cubic]s.
 */
fun List<Cubic>.toPath(path: Path = Path()): Path {
    path.rewind()

    firstOrNull()?.let { first ->
        path.moveTo(first.anchor0X, first.anchor0Y)
    }

    for (bezier in this) {
        path.cubicTo(
            x1 = bezier.control0X, y1 = bezier.control0Y,
            x2 = bezier.control1X, y2 = bezier.control1Y,
            x3 = bezier.anchor1X,  y3 = bezier.anchor1Y,
        )
    }

    path.close()
    return path
}

/**
 * Calculates and returns the bounds of this [RoundedPolygon] as a [Rect].
 */
fun RoundedPolygon.getBounds() = calculateBounds().let { Rect(it[0], it[1], it[2], it[3]) }

/**
 * Transforms a [RoundedPolygon] with the given [Matrix].
 */
fun RoundedPolygon.transformedWithMatrix(matrix: Matrix): RoundedPolygon {
    return transformed { x, y ->
        val transformedPoint = matrix.map(Offset(x, y))
        TransformResult(transformedPoint.x, transformedPoint.y)
    }
}