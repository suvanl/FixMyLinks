package com.suvanl.fixmylinks.ui.navigation.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

enum class ScaleTransitionDirection { INWARDS, OUTWARDS }

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.95F else 1.05F
): EnterTransition {
    return scaleIn(
        animationSpec = tween(
            durationMillis = 120,
            delayMillis = 90,
            easing = EaseOut
        ),
        initialScale = initialScale,
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = 250,
            delayMillis = 90,
            easing = EaseOut
        )
    )
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.95F else 1.05F
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 120,
            delayMillis = 90
        ),
        targetScale = targetScale
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 0,
            easing = EaseOutExpo
        )
    )
}