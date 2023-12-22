package com.suvanl.fixmylinks.ui.util

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.graphics.shapes.RoundedPolygon
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
 * A [RoundedPolygon] with optional semantics properties for accessibility/testing purposes.
 */
data class UiRoundedPolygon(
    val shape: RoundedPolygon,
    val semantics: SemanticsPropertyReceiver.() -> Unit = {},
)

/**
 * Returns the shape (with optional semantics) used to represent the given rule type in the UI.
 */
fun getShapeForRule(ruleType: MutationType): UiShape = when (ruleType) {
    MutationType.DOMAIN_NAME -> {
        UiShape(CustomShapes.SquareShape) { contentDescription = "Square" }
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
        UiShape(CustomShapes.CircleShape) { contentDescription = "Circle" }
    }
}

/**
 * Returns the rounded polygon (with optional semantics) to represent the given rule type in the UI.
 */
fun getRoundedPolygonForRule(ruleType: MutationType): UiRoundedPolygon = when (ruleType) {
    MutationType.DOMAIN_NAME -> {
        UiRoundedPolygon(CustomShapes.SquarePolygon) { contentDescription = "Square" }
    }

    MutationType.URL_PARAMS_ALL -> {
        UiRoundedPolygon(CustomShapes.ScallopPolygon) { contentDescription = "Scallop" }
    }

    MutationType.URL_PARAMS_SPECIFIC -> {
        UiRoundedPolygon(CustomShapes.DeltaPolygon) { contentDescription = "Delta" }
    }

    MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
        UiRoundedPolygon(CustomShapes.CloverPolygon) { contentDescription = "Clover" }
    }

    MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC -> {
        UiRoundedPolygon(CustomShapes.EightPointStarPolygon) { contentDescription = "Eight-point star" }
    }

    MutationType.FALLBACK -> {
        UiRoundedPolygon(CustomShapes.CirclePolygon) { contentDescription = "Circle" }
    }
}