package com.suvanl.fixmylinks.ui.graphics

import android.graphics.Matrix
import android.graphics.RectF
import kotlin.math.min

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