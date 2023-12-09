package com.suvanl.fixmylinks.ui.util

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.graphics.CustomShapes

/**
 * Returns the shape used to represent the given rule type in the UI.
 */
fun getShapeForRule(ruleType: MutationType): Shape = when (ruleType) {
    MutationType.DOMAIN_NAME -> RoundedCornerShape(10.dp)
    MutationType.URL_PARAMS_ALL -> CustomShapes.ScallopShape
    MutationType.URL_PARAMS_SPECIFIC -> CustomShapes.DeltaShape
    MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> CustomShapes.CloverShape
    MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC -> CustomShapes.EightPointStarShape
    MutationType.FALLBACK -> CircleShape
}