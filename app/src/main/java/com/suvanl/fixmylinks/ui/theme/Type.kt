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
private fun overusedGrotesk(
    variationSettings: FontVariation.Setting = FontVariation.weight(FontWeight.Normal.weight)
): FontFamily {
    return FontFamily(
        Font(
            resId = R.font.overusedgrotesk_vf,
            variationSettings = FontVariation.Settings(variationSettings)
        )
    )
}

private val defaultTypography = Typography()
val Typography = Typography(
    // Display
    displayLarge = defaultTypography.displayLarge.copy(
        fontFamily = overusedGrotesk()
    ),
    displayMedium = defaultTypography.displayMedium.copy(
        fontFamily = overusedGrotesk()
    ),
    displaySmall = defaultTypography.displaySmall.copy(
        fontFamily = overusedGrotesk()
    ),

    // Headline
    headlineLarge = defaultTypography.headlineLarge.copy(
        fontFamily = overusedGrotesk()
    ),
    headlineMedium = defaultTypography.headlineMedium.copy(
        fontFamily = overusedGrotesk()
    ),
    headlineSmall = defaultTypography.headlineSmall.copy(
        fontFamily = overusedGrotesk()
    ),

    // Title
    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = overusedGrotesk()
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = overusedGrotesk(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = overusedGrotesk(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),

    // Label
    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = overusedGrotesk(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),
    labelMedium = defaultTypography.labelMedium.copy(
        fontFamily = overusedGrotesk(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),
    labelSmall = defaultTypography.labelSmall.copy(
        fontFamily = overusedGrotesk(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),

    // Body
    bodyLarge = TextStyle(
        fontFamily = overusedGrotesk(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = overusedGrotesk()
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontFamily = overusedGrotesk()
    )
)