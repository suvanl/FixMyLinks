package com.suvanl.fixmylinks.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.suvanl.fixmylinks.R

@OptIn(ExperimentalTextApi::class)
private fun defaultCustomFont(
    variationSettings: FontVariation.Setting = FontVariation.weight(FontWeight.Normal.weight)
): FontFamily {
    return FontFamily(
        Font(
            resId = R.font.inter_vf,
            variationSettings = FontVariation.Settings(variationSettings)
        )
    )
}

object LetterSpacingDefaults {
    val Tight = (-0.035F).sp
    val Tighter = Tight.times(8)
    val ExtraTight = Tight.times(16)
}

private val defaultTypography = Typography()
val Typography = Typography(
    // Display
    displayLarge = defaultTypography.displayLarge.copy(
        fontFamily = defaultCustomFont()
    ),
    displayMedium = defaultTypography.displayMedium.copy(
        fontFamily = defaultCustomFont()
    ),
    displaySmall = defaultTypography.displaySmall.copy(
        fontFamily = defaultCustomFont()
    ),

    // Headline
    headlineLarge = defaultTypography.headlineLarge.copy(
        fontFamily = defaultCustomFont()
    ),
    headlineMedium = defaultTypography.headlineMedium.copy(
        fontFamily = defaultCustomFont()
    ),
    headlineSmall = defaultTypography.headlineSmall.copy(
        fontFamily = defaultCustomFont()
    ),

    // Title
    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = defaultCustomFont(),
        letterSpacing = LetterSpacingDefaults.Tighter
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = defaultCustomFont(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = defaultCustomFont(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),

    // Label
    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = defaultCustomFont(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),
    labelMedium = defaultTypography.labelMedium.copy(
        fontFamily = defaultCustomFont(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),
    labelSmall = defaultTypography.labelSmall.copy(
        fontFamily = defaultCustomFont(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),

    // Body
    bodyLarge = TextStyle(
        fontFamily = defaultCustomFont(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = LetterSpacingDefaults.Tight
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = defaultCustomFont(),
        letterSpacing = LetterSpacingDefaults.Tight
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontFamily = defaultCustomFont()
    )
)

object TextStyleDefaults {
    val dropdownItemStyle = Typography.bodyLarge
}

