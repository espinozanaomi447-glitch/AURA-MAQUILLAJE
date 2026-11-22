package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = RoseVelvet,
    onPrimary = Color.White,
    primaryContainer = BlushChampagne,
    onPrimaryContainer = DeepPlum,
    secondary = ChampagneGold,
    onSecondary = Color.White,
    secondaryContainer = SoftGold,
    onSecondaryContainer = CharcoalInk,
    tertiary = DeepPlum,
    onTertiary = Color.White,
    background = IvorySilk,
    onBackground = CharcoalInk,
    surface = IvorySilk,
    onSurface = CharcoalInk,
    surfaceVariant = WarmCream,
    onSurfaceVariant = MutedTaupe,
    outline = SoftOutline,
    outlineVariant = Color(0xFFEEE2DC)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF07DA1),
    onPrimary = DeepPlum,
    primaryContainer = Color(0xFF5D2138),
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = Color(0xFFE5BA73),
    onSecondary = DeepPlum,
    secondaryContainer = Color(0xFF4F3B1F),
    onSecondaryContainer = SoftGold,
    tertiary = BlushChampagne,
    onTertiary = DeepPlum,
    background = DeepPlum,
    onBackground = IvorySilk,
    surface = PlumSurface,
    onSurface = IvorySilk,
    surfaceVariant = Color(0xFF422536),
    onSurfaceVariant = Color(0xFFD6BEC8),
    outline = Color(0xFF5D3B4D)
)

val AtelierShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun AuraAtelierTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AtelierShapes,
        content = content
    )
}
