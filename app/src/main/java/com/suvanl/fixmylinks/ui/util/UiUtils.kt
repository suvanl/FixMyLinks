package com.suvanl.fixmylinks.ui.util

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.graphics.CustomShapes

/**
 * A [Shape] with optional semantics properties for accessibility/testing purposes.
 */
data class UiShape(
    val shape: Shape,
    val semantics: SemanticsPropertyReceiver.() -> Unit = {},
)

/**
 * Returns the shape used to represent the given rule type in the UI.
 */
fun getShapeForRule(ruleType: MutationType): UiShape = when (ruleType) {
    MutationType.DOMAIN_NAME -> {
        UiShape(RoundedCornerShape(10.dp)) { contentDescription = "Square" }
    }

    MutationType.URL_PARAMS_ALL -> {
        UiShape(CustomShapes.ScallopShape) { contentDescription = "Scallop" }
    }

    MutationType.URL_PARAMS_SPECIFIC -> {
        UiShape(CustomShapes.DeltaShape) { contentDescription = "Delta" }
    }

    MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
        UiShape(CustomShapes.CloverShape) { contentDescription = "Clover" }
    }

    MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC -> {
        UiShape(CustomShapes.EightPointStarShape) { contentDescription = "Eight-point star" }
    }

    MutationType.FALLBACK -> {
        UiShape(CircleShape) { contentDescription = "Circle" }
    }
}