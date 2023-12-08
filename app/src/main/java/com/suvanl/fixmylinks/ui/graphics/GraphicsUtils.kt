package com.suvanl.fixmylinks.ui.graphics

import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import androidx.core.graphics.plus
import androidx.core.graphics.times
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

val PointZero = PointF(0f, 0f)

fun calculateMatrix(bounds: RectF, width: Float, height: Float): Matrix {
    val originalWidth = bounds.right - bounds.left
    val originalHeight = bounds.bottom - bounds.top

    val scale = min(width / originalWidth, height / originalHeight)

    val newLeft = bounds.left - (width / scale - originalWidth) / 2
    val newTop = bounds.top - (height / scale - originalHeight) / 2

    return Matrix().apply {
        setTranslate(-newLeft, -newTop)
        postScale(scale, scale)
    }
}

fun radialToCartesian(
    radius: Float,
    angleRadians: Float,
    center: PointF = PointZero
) = directionVectorPointF(angleRadians) * radius + center

fun Float.toRadians() = this * PI.toFloat() / 180F

private fun directionVectorPointF(angleRadians: Float) =
    PointF(cos(angleRadians), sin(angleRadians))