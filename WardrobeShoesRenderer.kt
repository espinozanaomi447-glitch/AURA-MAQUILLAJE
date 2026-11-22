package com.example.ui.canvas

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.*

internal fun DrawScope.drawOutfit(
    fig: Rect,
    style: OutfitStyle,
    color: Color,
    accent: Color
) {
    val darker = color.copy(
        red = (color.red * 0.64f).coerceIn(0f, 1f),
        green = (color.green * 0.64f).coerceIn(0f, 1f),
        blue = (color.blue * 0.64f).coerceIn(0f, 1f)
    )
    val deepShadow = color.copy(
        red = (color.red * 0.44f).coerceIn(0f, 1f),
        green = (color.green * 0.44f).coerceIn(0f, 1f),
        blue = (color.blue * 0.44f).coerceIn(0f, 1f)
    )
    val lighter = color.copy(
        red = (color.red + (1f - color.red) * 0.28f).coerceIn(0f, 1f),
        green = (color.green + (1f - color.green) * 0.28f).coerceIn(0f, 1f),
        blue = (color.blue + (1f - color.blue) * 0.28f).coerceIn(0f, 1f)
    )
    val fabricBrush = Brush.horizontalGradient(
        colors = listOf(darker, color, lighter, color, darker),
        startX = fig.left + fig.width * 0.22f,
        endX = fig.left + fig.width * 0.78f
    )
    val seamStroke = Stroke(width = 1.6f, cap = StrokeCap.Round)
    val edgeColor = deepShadow.copy(alpha = 0.55f)

    when (style) {
        OutfitStyle.GALA_GOWN -> {
            val bodice = Path().apply {
                moveTo(fw(fig, 0.32f, 0.36f).x, fw(fig, 0.32f, 0.36f).y)
                quadraticTo(fw(fig, 0.41f, 0.338f).x, fw(fig, 0.41f, 0.338f).y, fw(fig, 0.50f, 0.372f).x, fw(fig, 0.50f, 0.372f).y)
                quadraticTo(fw(fig, 0.59f, 0.338f).x, fw(fig, 0.59f, 0.338f).y, fw(fig, 0.68f, 0.36f).x, fw(fig, 0.68f, 0.36f).y)
                lineTo(fw(fig, 0.655f, 0.482f).x, fw(fig, 0.655f, 0.482f).y)
                lineTo(fw(fig, 0.345f, 0.482f).x, fw(fig, 0.345f, 0.482f).y)
                close()
            }
            drawPath(bodice, fabricBrush)
            drawPath(bodice, edgeColor, style = seamStroke)
            // Princess bust seams
            for (side in listOf(-1f, 1f)) {
                drawLine(
                    deepShadow.copy(alpha = 0.42f),
                    fw(fig, 0.50f + side * 0.08f, 0.36f),
                    fw(fig, 0.50f + side * 0.06f, 0.475f),
                    strokeWidth = 1.5f
                )
            }
            val skirt = Path().apply {
                moveTo(fw(fig, 0.345f, 0.48f).x, fw(fig, 0.345f, 0.48f).y)
                lineTo(fw(fig, 0.655f, 0.48f).x, fw(fig, 0.655f, 0.48f).y)
                cubicTo(fw(fig, 0.78f, 0.60f).x, fw(fig, 0.78f, 0.60f).y, fw(fig, 0.85f, 0.78f).x, fw(fig, 0.85f, 0.78f).y, fw(fig, 0.82f, 0.94f).x, fw(fig, 0.82f, 0.94f).y)
                lineTo(fw(fig, 0.59f, 0.94f).x, fw(fig, 0.59f, 0.94f).y)
                lineTo(fw(fig, 0.555f, 0.66f).x, fw(fig, 0.555f, 0.66f).y)
                lineTo(fw(fig, 0.48f, 0.94f).x, fw(fig, 0.48f, 0.94f).y)
                lineTo(fw(fig, 0.18f, 0.94f).x, fw(fig, 0.18f, 0.94f).y)
                cubicTo(fw(fig, 0.16f, 0.78f).x, fw(fig, 0.16f, 0.78f).y, fw(fig, 0.22f, 0.60f).x, fw(fig, 0.22f, 0.60f).y, fw(fig, 0.345f, 0.48f).x, fw(fig, 0.345f, 0.48f).y)
                close()
            }
            drawPath(skirt, fabricBrush)
            drawPath(skirt, edgeColor, style = seamStroke)
            // Satin drape folds
            listOf(
                Pair(Offset(0.39f, 0.49f), Offset(0.28f, 0.92f)),
                Pair(Offset(0.45f, 0.49f), Offset(0.40f, 0.92f)),
                Pair(Offset(0.60f, 0.49f), Offset(0.70f, 0.92f))
            ).forEach { (start, end) ->
                drawLine(deepShadow.copy(alpha = 0.26f), fw(fig, start.x, start.y), fw(fig, end.x, end.y), strokeWidth = 2.4f, cap = StrokeCap.Round)
            }
            drawRoundRect(accent, fw(fig, 0.34f, 0.47f), Size(fig.width * 0.32f, fig.height * 0.02f), CornerRadius(6f, 6f))
        }

        OutfitStyle.STREETWEAR_BLAZER -> {
            drawRect(Color(0xFFF7F2EE), fw(fig, 0.38f, 0.34f), Size(fig.width * 0.24f, fig.height * 0.13f))
            for (side in listOf(-1f, 1f)) {
                val pant = Path().apply {
                    moveTo(fig.center.x, fig.top + fig.height * 0.47f)
                    lineTo(fig.center.x + side * fig.width * 0.205f, fig.top + fig.height * 0.47f)
                    lineTo(fig.center.x + side * fig.width * 0.215f, fig.top + fig.height * 0.91f)
                    lineTo(fig.center.x + side * fig.width * 0.015f, fig.top + fig.height * 0.91f)
                    close()
                }
                drawPath(pant, fabricBrush)
                drawPath(pant, edgeColor, style = seamStroke)
                // Tailored trouser crease
                drawLine(
                    lighter.copy(alpha = 0.38f),
                    Offset(fig.center.x + side * fig.width * 0.11f, fig.top + fig.height * 0.50f),
                    Offset(fig.center.x + side * fig.width * 0.115f, fig.top + fig.height * 0.89f),
                    strokeWidth = 1.6f
                )
                val flap = Path().apply {
                    moveTo(fig.center.x + side * fig.width * 0.075f, fig.top + fig.height * 0.31f)
                    lineTo(fig.center.x + side * fig.width * 0.245f, fig.top + fig.height * 0.318f)
                    lineTo(fig.center.x + side * fig.width * 0.235f, fig.top + fig.height * 0.575f)
                    lineTo(fig.center.x + side * fig.width * 0.035f, fig.top + fig.height * 0.575f)
                    close()
                }
                drawPath(flap, fabricBrush)
                drawPath(flap, edgeColor, style = seamStroke)
                // Structured lapel highlight
                drawLine(
                    lighter.copy(alpha = 0.65f),
                    Offset(fig.center.x + side * fig.width * 0.08f, fig.top + fig.height * 0.315f),
                    Offset(fig.center.x + side * fig.width * 0.045f, fig.top + fig.height * 0.465f),
                    strokeWidth = 2.6f,
                    cap = StrokeCap.Round
                )
                val sleeve = Path().apply {
                    moveTo(fig.center.x + side * fig.width * 0.225f, fig.top + fig.height * 0.312f)
                    cubicTo(
                        fig.center.x + side * fig.width * 0.318f, fig.top + fig.height * 0.355f,
                        fig.center.x + side * fig.width * 0.342f, fig.top + fig.height * 0.445f,
                        fig.center.x + side * fig.width * 0.326f, fig.top + fig.height * 0.542f
                    )
                    lineTo(fig.center.x + side * fig.width * 0.274f, fig.top + fig.height * 0.542f)
                    cubicTo(
                        fig.center.x + side * fig.width * 0.28f, fig.top + fig.height * 0.455f,
                        fig.center.x + side * fig.width * 0.24f, fig.top + fig.height * 0.38f,
                        fig.center.x + side * fig.width * 0.175f, fig.top + fig.height * 0.345f
                    )
                    close()
                }
                drawPath(sleeve, Brush.horizontalGradient(listOf(darker, color, darker)))
                drawPath(sleeve, edgeColor, style = seamStroke)
            }
            drawRoundRect(accent, fw(fig, 0.465f, 0.475f), Size(fig.width * 0.07f, fig.height * 0.018f), CornerRadius(4f, 4f))
        }

        OutfitStyle.BOHO_FLORAL -> {
            val top = Path().apply {
                moveTo(fw(fig, 0.26f, 0.355f).x, fw(fig, 0.26f, 0.355f).y)
                quadraticTo(fw(fig, 0.50f, 0.39f).x, fw(fig, 0.50f, 0.39f).y, fw(fig, 0.74f, 0.355f).x, fw(fig, 0.74f, 0.355f).y)
                lineTo(fw(fig, 0.655f, 0.482f).x, fw(fig, 0.655f, 0.482f).y)
                lineTo(fw(fig, 0.345f, 0.482f).x, fw(fig, 0.345f, 0.482f).y)
                close()
            }
            drawPath(top, fabricBrush)
            drawPath(top, edgeColor, style = seamStroke)
            val skirt = Path().apply {
                moveTo(fw(fig, 0.345f, 0.48f).x, fw(fig, 0.345f, 0.48f).y)
                lineTo(fw(fig, 0.655f, 0.48f).x, fw(fig, 0.655f, 0.48f).y)
                cubicTo(fw(fig, 0.77f, 0.60f).x, fw(fig, 0.77f, 0.60f).y, fw(fig, 0.80f, 0.74f).x, fw(fig, 0.80f, 0.74f).y, fw(fig, 0.77f, 0.84f).x, fw(fig, 0.77f, 0.84f).y)
                quadraticTo(fw(fig, 0.50f, 0.87f).x, fw(fig, 0.50f, 0.87f).y, fw(fig, 0.23f, 0.84f).x, fw(fig, 0.23f, 0.84f).y)
                cubicTo(fw(fig, 0.20f, 0.74f).x, fw(fig, 0.20f, 0.74f).y, fw(fig, 0.23f, 0.60f).x, fw(fig, 0.23f, 0.60f).y, fw(fig, 0.345f, 0.48f).x, fw(fig, 0.345f, 0.48f).y)
                close()
            }
            drawPath(skirt, fabricBrush)
            drawPath(skirt, edgeColor, style = seamStroke)
            // Tiered ruffle seam
            drawLine(deepShadow.copy(alpha = 0.3f), fw(fig, 0.24f, 0.68f), fw(fig, 0.76f, 0.68f), strokeWidth = 1.8f)
            listOf(Offset(0.38f, 0.56f), Offset(0.60f, 0.58f), Offset(0.47f, 0.65f), Offset(0.32f, 0.74f), Offset(0.66f, 0.74f), Offset(0.51f, 0.79f)).forEach { pt ->
                val c = fw(fig, pt.x, pt.y)
                drawCircle(accent.copy(alpha = 0.9f), fig.width * 0.017f, c)
                drawCircle(Color.White.copy(alpha = 0.65f), fig.width * 0.006f, c)
            }
        }

        OutfitStyle.COUTURE_CORSET -> {
            val corset = Path().apply {
                moveTo(fw(fig, 0.33f, 0.36f).x, fw(fig, 0.33f, 0.36f).y)
                lineTo(fw(fig, 0.50f, 0.388f).x, fw(fig, 0.50f, 0.388f).y)
                lineTo(fw(fig, 0.67f, 0.36f).x, fw(fig, 0.67f, 0.36f).y)
                lineTo(fw(fig, 0.645f, 0.49f).x, fw(fig, 0.645f, 0.49f).y)
                lineTo(fw(fig, 0.50f, 0.525f).x, fw(fig, 0.50f, 0.525f).y)
                lineTo(fw(fig, 0.355f, 0.49f).x, fw(fig, 0.355f, 0.49f).y)
                close()
            }
            drawPath(corset, fabricBrush)
            drawPath(corset, edgeColor, style = seamStroke)
            for (bx in listOf(0.415f, 0.50f, 0.585f)) {
                drawLine(accent.copy(alpha = 0.75f), fw(fig, bx, 0.382f), fw(fig, bx, 0.496f), strokeWidth = 2f, cap = StrokeCap.Round)
            }
            val skirt = Path().apply {
                moveTo(fw(fig, 0.355f, 0.49f).x, fw(fig, 0.355f, 0.49f).y)
                lineTo(fw(fig, 0.50f, 0.522f).x, fw(fig, 0.50f, 0.522f).y)
                lineTo(fw(fig, 0.645f, 0.49f).x, fw(fig, 0.645f, 0.49f).y)
                lineTo(fw(fig, 0.75f, 0.83f).x, fw(fig, 0.75f, 0.83f).y)
                lineTo(fw(fig, 0.27f, 0.68f).x, fw(fig, 0.27f, 0.68f).y)
                close()
            }
            drawPath(skirt, fabricBrush)
            drawPath(skirt, edgeColor, style = seamStroke)
        }

        OutfitStyle.ROMANTIC_TULLE -> {
            drawLine(darker, fw(fig, 0.385f, 0.308f), fw(fig, 0.385f, 0.37f), strokeWidth = 3.8f, cap = StrokeCap.Round)
            drawLine(darker, fw(fig, 0.615f, 0.308f), fw(fig, 0.615f, 0.37f), strokeWidth = 3.8f, cap = StrokeCap.Round)
            val bodice = Path().apply {
                moveTo(fw(fig, 0.34f, 0.36f).x, fw(fig, 0.34f, 0.36f).y)
                quadraticTo(fw(fig, 0.50f, 0.385f).x, fw(fig, 0.50f, 0.385f).y, fw(fig, 0.66f, 0.36f).x, fw(fig, 0.66f, 0.36f).y)
                lineTo(fw(fig, 0.635f, 0.482f).x, fw(fig, 0.635f, 0.482f).y)
                lineTo(fw(fig, 0.365f, 0.482f).x, fw(fig, 0.365f, 0.482f).y)
                close()
            }
            drawPath(bodice, fabricBrush)
            drawPath(bodice, edgeColor, style = seamStroke)
            val tulleOuter = Path().apply {
                moveTo(fw(fig, 0.355f, 0.48f).x, fw(fig, 0.355f, 0.48f).y)
                lineTo(fw(fig, 0.645f, 0.48f).x, fw(fig, 0.645f, 0.48f).y)
                cubicTo(fw(fig, 0.82f, 0.56f).x, fw(fig, 0.82f, 0.56f).y, fw(fig, 0.85f, 0.72f).x, fw(fig, 0.85f, 0.72f).y, fw(fig, 0.80f, 0.79f).x, fw(fig, 0.80f, 0.79f).y)
                quadraticTo(fw(fig, 0.50f, 0.83f).x, fw(fig, 0.50f, 0.83f).y, fw(fig, 0.20f, 0.79f).x, fw(fig, 0.20f, 0.79f).y)
                cubicTo(fw(fig, 0.15f, 0.72f).x, fw(fig, 0.15f, 0.72f).y, fw(fig, 0.18f, 0.56f).x, fw(fig, 0.18f, 0.56f).y, fw(fig, 0.355f, 0.48f).x, fw(fig, 0.355f, 0.48f).y)
                close()
            }
            drawPath(tulleOuter, color.copy(alpha = 0.76f))
            // Soft sheer tulle pleat lines
            for (px in listOf(0.41f, 0.50f, 0.59f)) {
                drawLine(lighter.copy(alpha = 0.42f), fw(fig, px, 0.485f), fw(fig, 0.50f + (px - 0.50f) * 2.4f, 0.79f), strokeWidth = 2.2f)
            }
            drawPath(tulleOuter, color = accent.copy(alpha = 0.52f), style = Stroke(width = 1.8f))
        }

        OutfitStyle.MINIMAL_JUMPSUIT -> {
            val halter = Path().apply {
                moveTo(fw(fig, 0.44f, 0.295f).x, fw(fig, 0.44f, 0.295f).y)
                lineTo(fw(fig, 0.56f, 0.295f).x, fw(fig, 0.56f, 0.295f).y)
                lineTo(fw(fig, 0.66f, 0.41f).x, fw(fig, 0.66f, 0.41f).y)
                lineTo(fw(fig, 0.635f, 0.485f).x, fw(fig, 0.635f, 0.485f).y)
                lineTo(fw(fig, 0.365f, 0.485f).x, fw(fig, 0.365f, 0.485f).y)
                lineTo(fw(fig, 0.34f, 0.41f).x, fw(fig, 0.34f, 0.41f).y)
                close()
            }
            drawPath(halter, fabricBrush)
            drawPath(halter, edgeColor, style = seamStroke)
            for (side in listOf(-1f, 1f)) {
                val leg = Path().apply {
                    moveTo(fig.center.x, fig.top + fig.height * 0.48f)
                    lineTo(fig.center.x + side * fig.width * 0.195f, fig.top + fig.height * 0.48f)
                    lineTo(fig.center.x + side * fig.width * 0.165f, fig.top + fig.height * 0.90f)
                    lineTo(fig.center.x + side * fig.width * 0.015f, fig.top + fig.height * 0.90f)
                    close()
                }
                drawPath(leg, fabricBrush)
                drawPath(leg, edgeColor, style = seamStroke)
            }
            drawLine(accent, fw(fig, 0.365f, 0.485f), fw(fig, 0.635f, 0.485f), strokeWidth = 4f)
        }

        OutfitStyle.COCKTAIL_MINI -> {
            val dress = Path().apply {
                moveTo(fw(fig, 0.34f, 0.352f).x, fw(fig, 0.34f, 0.352f).y)
                quadraticTo(fw(fig, 0.50f, 0.39f).x, fw(fig, 0.50f, 0.39f).y, fw(fig, 0.67f, 0.33f).x, fw(fig, 0.67f, 0.33f).y)
                lineTo(fw(fig, 0.635f, 0.48f).x, fw(fig, 0.635f, 0.48f).y)
                quadraticTo(fw(fig, 0.72f, 0.58f).x, fw(fig, 0.72f, 0.58f).y, fw(fig, 0.68f, 0.67f).x, fw(fig, 0.67f, 0.67f).y)
                lineTo(fw(fig, 0.32f, 0.69f).x, fw(fig, 0.32f, 0.69f).y)
                quadraticTo(fw(fig, 0.28f, 0.58f).x, fw(fig, 0.28f, 0.58f).y, fw(fig, 0.365f, 0.48f).x, fw(fig, 0.365f, 0.48f).y)
                close()
            }
            drawPath(dress, fabricBrush)
            drawPath(dress, edgeColor, style = seamStroke)
            drawLine(lighter.copy(alpha = 0.65f), fw(fig, 0.36f, 0.50f), fw(fig, 0.65f, 0.58f), strokeWidth = 2.4f, cap = StrokeCap.Round)
            drawLine(deepShadow.copy(alpha = 0.30f), fw(fig, 0.35f, 0.53f), fw(fig, 0.66f, 0.61f), strokeWidth = 2.0f, cap = StrokeCap.Round)
            drawLine(lighter.copy(alpha = 0.55f), fw(fig, 0.34f, 0.565f), fw(fig, 0.66f, 0.645f), strokeWidth = 2.2f, cap = StrokeCap.Round)
        }

        OutfitStyle.DENIM_CROP -> {
            drawRect(Color(0xFFFDFBF9), fw(fig, 0.375f, 0.35f), Size(fig.width * 0.25f, fig.height * 0.11f))
            for (side in listOf(-1f, 1f)) {
                val jPanel = Path().apply {
                    moveTo(fig.center.x + side * fig.width * 0.08f, fig.top + fig.height * 0.31f)
                    lineTo(fig.center.x + side * fig.width * 0.245f, fig.top + fig.height * 0.32f)
                    lineTo(fig.center.x + side * fig.width * 0.22f, fig.top + fig.height * 0.435f)
                    lineTo(fig.center.x + side * fig.width * 0.08f, fig.top + fig.height * 0.435f)
                    close()
                }
                drawPath(jPanel, fabricBrush)
                drawPath(jPanel, edgeColor, style = seamStroke)
            }
            val denim = Color(0xFF3E5E84)
            val denimDark = Color(0xFF28415E)
            val denimLight = Color(0xFF5D81AA)
            for (side in listOf(-1f, 1f)) {
                val jeanLeg = Path().apply {
                    moveTo(fig.center.x, fig.top + fig.height * 0.47f)
                    lineTo(fig.center.x + side * fig.width * 0.195f, fig.top + fig.height * 0.47f)
                    lineTo(fig.center.x + side * fig.width * 0.17f, fig.top + fig.height * 0.90f)
                    lineTo(fig.center.x + side * fig.width * 0.02f, fig.top + fig.height * 0.90f)
                    close()
                }
                drawPath(jeanLeg, Brush.horizontalGradient(listOf(denimDark, denim, denimLight, denimDark)))
                drawPath(jeanLeg, denimDark.copy(alpha = 0.65f), style = seamStroke)
                // Contrast topstitching
                drawLine(Color(0xFFD6A86E).copy(alpha = 0.65f), Offset(fig.center.x + side * fig.width * 0.185f, fig.top + fig.height * 0.485f), Offset(fig.center.x + side * fig.width * 0.16f, fig.top + fig.height * 0.89f), strokeWidth = 1.3f)
            }
        }

        OutfitStyle.SILK_SLIP -> {
            drawLine(darker, fw(fig, 0.39f, 0.308f), fw(fig, 0.39f, 0.37f), strokeWidth = 2.4f, cap = StrokeCap.Round)
            drawLine(darker, fw(fig, 0.61f, 0.308f), fw(fig, 0.61f, 0.37f), strokeWidth = 2.4f, cap = StrokeCap.Round)
            val slip = Path().apply {
                moveTo(fw(fig, 0.365f, 0.36f).x, fw(fig, 0.365f, 0.36f).y)
                quadraticTo(fw(fig, 0.50f, 0.402f).x, fw(fig, 0.50f, 0.402f).y, fw(fig, 0.635f, 0.36f).x, fw(fig, 0.635f, 0.36f).y)
                cubicTo(fw(fig, 0.665f, 0.52f).x, fw(fig, 0.665f, 0.52f).y, fw(fig, 0.72f, 0.68f).x, fw(fig, 0.72f, 0.68f).y, fw(fig, 0.69f, 0.80f).x, fw(fig, 0.69f, 0.80f).y)
                quadraticTo(fw(fig, 0.50f, 0.822f).x, fw(fig, 0.50f, 0.822f).y, fw(fig, 0.31f, 0.80f).x, fw(fig, 0.31f, 0.80f).y)
                cubicTo(fw(fig, 0.28f, 0.68f).x, fw(fig, 0.28f, 0.68f).y, fw(fig, 0.335f, 0.52f).x, fw(fig, 0.335f, 0.52f).y, fw(fig, 0.365f, 0.36f).x, fw(fig, 0.365f, 0.36f).y)
                close()
            }
            drawPath(slip, Brush.horizontalGradient(listOf(darker, lighter, color, lighter, darker)))
            drawPath(slip, edgeColor, style = seamStroke)
            drawLine(lighter.copy(alpha = 0.55f), fw(fig, 0.45f, 0.43f), fw(fig, 0.42f, 0.77f), strokeWidth = 2.6f, cap = StrokeCap.Round)
        }

        OutfitStyle.TRENCH_COAT -> {
            val coat = Path().apply {
                moveTo(fw(fig, 0.27f, 0.31f).x, fw(fig, 0.27f, 0.31f).y)
                lineTo(fw(fig, 0.73f, 0.31f).x, fw(fig, 0.73f, 0.31f).y)
                lineTo(fw(fig, 0.76f, 0.76f).x, fw(fig, 0.76f, 0.76f).y)
                lineTo(fw(fig, 0.24f, 0.76f).x, fw(fig, 0.24f, 0.76f).y)
                close()
            }
            drawPath(coat, fabricBrush)
            drawPath(coat, edgeColor, style = seamStroke)
            for (side in listOf(-1f, 1f)) {
                val sl = Path().apply {
                    moveTo(fig.center.x + side * fig.width * 0.225f, fig.top + fig.height * 0.312f)
                    cubicTo(
                        fig.center.x + side * fig.width * 0.318f, fig.top + fig.height * 0.355f,
                        fig.center.x + side * fig.width * 0.342f, fig.top + fig.height * 0.445f,
                        fig.center.x + side * fig.width * 0.326f, fig.top + fig.height * 0.542f
                    )
                    lineTo(fig.center.x + side * fig.width * 0.274f, fig.top + fig.height * 0.542f)
                    cubicTo(
                        fig.center.x + side * fig.width * 0.28f, fig.top + fig.height * 0.455f,
                        fig.center.x + side * fig.width * 0.24f, fig.top + fig.height * 0.38f,
                        fig.center.x + side * fig.width * 0.18f, fig.top + fig.height * 0.35f
                    )
                    close()
                }
                drawPath(sl, Brush.horizontalGradient(listOf(darker, color, darker)))
                drawPath(sl, edgeColor, style = seamStroke)
                // Double-breasted buttons
                for (by in listOf(0.41f, 0.53f, 0.60f)) {
                    drawCircle(deepShadow, fig.width * 0.011f, fw(fig, 0.50f + side * 0.06f, by))
                }
            }
            drawLine(deepShadow.copy(alpha = 0.65f), fw(fig, 0.50f, 0.36f), fw(fig, 0.50f, 0.76f), strokeWidth = 2.2f)
            drawRoundRect(darker, fw(fig, 0.315f, 0.47f), Size(fig.width * 0.37f, fig.height * 0.022f), CornerRadius(5f, 5f))
            drawRect(accent, fw(fig, 0.47f, 0.468f), Size(fig.width * 0.06f, fig.height * 0.026f))
        }

        OutfitStyle.MERMAID_SEQUIN -> {
            val gown = Path().apply {
                moveTo(fw(fig, 0.34f, 0.36f).x, fw(fig, 0.34f, 0.36f).y)
                quadraticTo(fw(fig, 0.50f, 0.392f).x, fw(fig, 0.50f, 0.392f).y, fw(fig, 0.66f, 0.36f).x, fw(fig, 0.66f, 0.36f).y)
                cubicTo(fw(fig, 0.665f, 0.54f).x, fw(fig, 0.665f, 0.54f).y, fw(fig, 0.64f, 0.68f).x, fw(fig, 0.64f, 0.68f).y, fw(fig, 0.84f, 0.94f).x, fw(fig, 0.84f, 0.94f).y)
                lineTo(fw(fig, 0.16f, 0.94f).x, fw(fig, 0.16f, 0.94f).y)
                cubicTo(fw(fig, 0.36f, 0.68f).x, fw(fig, 0.36f, 0.68f).y, fw(fig, 0.335f, 0.54f).x, fw(fig, 0.335f, 0.54f).y, fw(fig, 0.34f, 0.36f).x, fw(fig, 0.34f, 0.36f).y)
                close()
            }
            drawPath(gown, fabricBrush)
            drawPath(gown, edgeColor, style = seamStroke)
            listOf(
                Offset(0.42f, 0.44f), Offset(0.56f, 0.42f), Offset(0.48f, 0.53f),
                Offset(0.41f, 0.62f), Offset(0.58f, 0.63f), Offset(0.35f, 0.78f),
                Offset(0.64f, 0.80f), Offset(0.50f, 0.86f), Offset(0.44f, 0.72f), Offset(0.55f, 0.74f)
            ).forEach { pt ->
                val c = fw(fig, pt.x, pt.y)
                drawCircle(accent.copy(alpha = 0.65f), fig.width * 0.014f, c)
                drawCircle(Color.White.copy(alpha = 0.86f), fig.width * 0.008f, c)
            }
        }

        OutfitStyle.ATHLEISURE_SET -> {
            val top = Path().apply {
                moveTo(fw(fig, 0.40f, 0.31f).x, fw(fig, 0.40f, 0.31f).y)
                lineTo(fw(fig, 0.60f, 0.31f).x, fw(fig, 0.60f, 0.31f).y)
                lineTo(fw(fig, 0.64f, 0.43f).x, fw(fig, 0.64f, 0.43f).y)
                lineTo(fw(fig, 0.36f, 0.43f).x, fw(fig, 0.36f, 0.43f).y)
                close()
            }
            drawPath(top, fabricBrush)
            drawPath(top, edgeColor, style = seamStroke)
            val skirt = Path().apply {
                moveTo(fw(fig, 0.355f, 0.48f).x, fw(fig, 0.355f, 0.48f).y)
                lineTo(fw(fig, 0.645f, 0.48f).x, fw(fig, 0.645f, 0.48f).y)
                lineTo(fw(fig, 0.74f, 0.64f).x, fw(fig, 0.74f, 0.64f).y)
                lineTo(fw(fig, 0.26f, 0.64f).x, fw(fig, 0.26f, 0.64f).y)
                close()
            }
            drawPath(skirt, fabricBrush)
            drawPath(skirt, edgeColor, style = seamStroke)
            for (px in listOf(0.41f, 0.47f, 0.53f, 0.59f)) {
                drawLine(deepShadow.copy(alpha = 0.30f), fw(fig, px, 0.485f), fw(fig, 0.50f + (px - 0.50f) * 1.45f, 0.635f), strokeWidth = 1.8f)
            }
            drawLine(Color.White.copy(alpha = 0.78f), fw(fig, 0.27f, 0.625f), fw(fig, 0.73f, 0.625f), strokeWidth = 3f)
        }
    }
}

internal fun DrawScope.drawShoes(
    fig: Rect,
    style: ShoeStyle,
    color: Color,
    metal: JewelryMetal
) {
    val darker = color.copy(
        red = (color.red * 0.60f).coerceIn(0f, 1f),
        green = (color.green * 0.60f).coerceIn(0f, 1f),
        blue = (color.blue * 0.60f).coerceIn(0f, 1f)
    )
    val lighter = color.copy(
        red = (color.red + (1f - color.red) * 0.28f).coerceIn(0f, 1f),
        green = (color.green + (1f - color.green) * 0.28f).coerceIn(0f, 1f),
        blue = (color.blue + (1f - color.blue) * 0.28f).coerceIn(0f, 1f)
    )
    val shoeBrush = Brush.verticalGradient(listOf(lighter, color, darker))
    val rim = Stroke(width = 1.4f, cap = StrokeCap.Round)

    for (side in listOf(-1f, 1f)) {
        val cx = fig.center.x + side * fig.width * 0.082f
        val ankleY = fig.top + fig.height * 0.915f
        val soleY = fig.top + fig.height * 0.975f
        val w = fig.width * 0.068f

        when (style) {
            ShoeStyle.STILETTO -> {
                drawLine(metal.primary, Offset(cx + side * w * 0.2f, ankleY + fig.height * 0.02f), Offset(cx + side * w * 0.25f, soleY), strokeWidth = 3f, cap = StrokeCap.Round)
                val pump = Path().apply {
                    moveTo(cx - w * 0.55f, ankleY + fig.height * 0.028f)
                    quadraticTo(cx, soleY + 4f, cx + w * 0.62f, soleY)
                    quadraticTo(cx + w * 0.35f, ankleY + fig.height * 0.012f, cx - w * 0.55f, ankleY + fig.height * 0.028f)
                    close()
                }
                drawPath(pump, shoeBrush)
                drawPath(pump, darker.copy(alpha = 0.6f), style = rim)
                drawLine(color, Offset(cx - w * 0.4f, ankleY + 4f), Offset(cx + w * 0.4f, ankleY + 4f), strokeWidth = 2.8f, cap = StrokeCap.Round)
            }

            ShoeStyle.COMBAT_BOOTS -> {
                val boot = Path().apply {
                    moveTo(cx - w * 0.52f, ankleY - fig.height * 0.065f)
                    lineTo(cx + w * 0.52f, ankleY - fig.height * 0.065f)
                    lineTo(cx + w * 0.65f, soleY)
                    lineTo(cx - w * 0.62f, soleY)
                    close()
                }
                drawPath(boot, shoeBrush)
                drawPath(boot, darker.copy(alpha = 0.6f), style = rim)
                // Lace criss-cross detail
                for (i in 0..2) {
                    val ly = ankleY - fig.height * 0.05f + i * fig.height * 0.015f
                    drawLine(lighter.copy(alpha = 0.7f), Offset(cx - w * 0.2f, ly), Offset(cx + w * 0.2f, ly + 4f), strokeWidth = 1.6f)
                }
                drawLine(Color(0xFF120E10), Offset(cx - w * 0.64f, soleY), Offset(cx + w * 0.67f, soleY), strokeWidth = 5.5f, cap = StrokeCap.Round)
            }

            ShoeStyle.JEWEL_SANDALS -> {
                drawLine(color, Offset(cx - w * 0.5f, soleY), Offset(cx + w * 0.55f, soleY), strokeWidth = 4f, cap = StrokeCap.Round)
                drawLine(metal.primary, Offset(cx - w * 0.4f, ankleY + 6f), Offset(cx + w * 0.4f, ankleY + 14f), strokeWidth = 2.5f)
                drawCircle(metal.primary, radius = 4.4f, center = Offset(cx, ankleY + 18f))
                drawCircle(metal.highlight, radius = 2.5f, center = Offset(cx, ankleY + 18f))
            }

            ShoeStyle.PLATFORM_SNEAKERS -> {
                drawRoundRect(shoeBrush, Offset(cx - w * 0.58f, ankleY + fig.height * 0.01f), Size(w * 1.24f, fig.height * 0.042f), CornerRadius(8f, 8f))
                drawRoundRect(Color(0xFFFDFBF9), Offset(cx - w * 0.62f, soleY - fig.height * 0.015f), Size(w * 1.32f, fig.height * 0.018f), CornerRadius(6f, 6f))
                drawLine(Color(0xFFDAD3CE), Offset(cx - w * 0.58f, soleY - fig.height * 0.005f), Offset(cx + w * 0.66f, soleY - fig.height * 0.005f), strokeWidth = 1.5f)
            }

            ShoeStyle.POINTED_MULES -> {
                val mule = Path().apply {
                    moveTo(cx - w * 0.55f, soleY - 6f)
                    lineTo(cx + w * 0.68f, soleY)
                    lineTo(cx + w * 0.2f, ankleY + fig.height * 0.022f)
                    lineTo(cx - w * 0.48f, ankleY + fig.height * 0.026f)
                    close()
                }
                drawPath(mule, shoeBrush)
                drawPath(mule, darker.copy(alpha = 0.6f), style = rim)
            }

            ShoeStyle.OVER_KNEE_BOOTS -> {
                val boot = Path().apply {
                    moveTo(cx - w * 0.72f, fig.top + fig.height * 0.73f)
                    lineTo(cx + w * 0.72f, fig.top + fig.height * 0.73f)
                    lineTo(cx + w * 0.62f, soleY)
                    lineTo(cx - w * 0.58f, soleY)
                    close()
                }
                drawPath(boot, Brush.horizontalGradient(listOf(darker, color, lighter, darker)))
                drawPath(boot, darker.copy(alpha = 0.6f), style = rim)
                drawLine(metal.highlight.copy(alpha = 0.65f), Offset(cx - w * 0.1f, fig.top + fig.height * 0.74f), Offset(cx - w * 0.1f, soleY - 4f), strokeWidth = 2f)
            }

            ShoeStyle.MARY_JANES -> {
                drawRoundRect(shoeBrush, Offset(cx - w * 0.58f, ankleY + fig.height * 0.022f), Size(w * 1.22f, fig.height * 0.036f), CornerRadius(10f, 10f))
                drawLine(color, Offset(cx - w * 0.45f, ankleY + fig.height * 0.012f), Offset(cx + w * 0.45f, ankleY + fig.height * 0.012f), strokeWidth = 3f)
                drawCircle(metal.primary, radius = 3f, center = Offset(cx + side * w * 0.3f, ankleY + fig.height * 0.012f))
            }

            ShoeStyle.GLADIATOR_HEELS -> {
                drawLine(color, Offset(cx - w * 0.5f, soleY), Offset(cx + w * 0.55f, soleY), strokeWidth = 3.5f, cap = StrokeCap.Round)
                for (step in 0..4) {
                    val y1 = ankleY - step * fig.height * 0.016f
                    drawLine(color, Offset(cx - w * 0.42f, y1), Offset(cx + w * 0.42f, y1 - 6f), strokeWidth = 2.2f)
                    drawLine(color, Offset(cx + w * 0.42f, y1), Offset(cx - w * 0.42f, y1 - 6f), strokeWidth = 2.2f)
                }
            }

            ShoeStyle.ESPADRILLE_WEDGES -> {
                val wedge = Path().apply {
                    moveTo(cx - w * 0.55f, ankleY + fig.height * 0.02f)
                    lineTo(cx + w * 0.60f, soleY - fig.height * 0.012f)
                    lineTo(cx + w * 0.60f, soleY)
                    lineTo(cx - w * 0.55f, soleY)
                    close()
                }
                drawPath(wedge, Brush.verticalGradient(listOf(Color(0xFFE5CFA4), Color(0xFFB8965E))))
                drawRoundRect(shoeBrush, Offset(cx - w * 0.48f, ankleY + fig.height * 0.018f), Size(w * 1.0f, fig.height * 0.020f), CornerRadius(6f, 6f))
            }

            ShoeStyle.ANKLE_BOOTIES -> {
                val bootie = Path().apply {
                    moveTo(cx - w * 0.50f, ankleY - fig.height * 0.015f)
                    lineTo(cx + w * 0.50f, ankleY - fig.height * 0.015f)
                    lineTo(cx + w * 0.64f, soleY)
                    lineTo(cx - w * 0.55f, soleY)
                    close()
                }
                drawPath(bootie, shoeBrush)
                drawPath(bootie, darker.copy(alpha = 0.6f), style = rim)
                drawLine(metal.primary, Offset(cx - w * 0.48f, ankleY + fig.height * 0.015f), Offset(cx - w * 0.48f, soleY), strokeWidth = 4f, cap = StrokeCap.Round)
            }
        }
    }
}

internal fun DrawScope.drawNecklace(
    fig: Rect,
    face: Rect,
    style: NecklaceStyle,
    metal: JewelryMetal
) {
    val neckY = face.bottom + fig.height * 0.020f
    val cx = face.center.x
    val halfW = face.width * 0.165f

    when (style) {
        NecklaceStyle.NONE -> Unit
        NecklaceStyle.DIAMOND_CHOKER -> {
            drawLine(metal.primary, Offset(cx - halfW, neckY + 1.5f), Offset(cx + halfW, neckY + 1.5f), strokeWidth = 3.2f, cap = StrokeCap.Round)
            for (i in -3..3) {
                val x = cx + i * (halfW / 3.5f)
                drawCircle(metal.primary, radius = 3.2f, center = Offset(x, neckY + 1.5f))
                drawCircle(metal.highlight, radius = 1.9f, center = Offset(x - 0.5f, neckY + 1.0f))
            }
        }
        NecklaceStyle.PENDANT_CHAIN -> {
            val chain = Path().apply {
                moveTo(cx - halfW, neckY)
                quadraticTo(cx, neckY + fig.height * 0.044f, cx + halfW, neckY)
            }
            drawPath(chain, metal.primary, style = Stroke(width = 2.2f, cap = StrokeCap.Round))
            val pendantPos = Offset(cx, neckY + fig.height * 0.025f)
            drawCircle(metal.primary, radius = 5.4f, center = pendantPos)
            drawCircle(metal.highlight, radius = 3.2f, center = Offset(pendantPos.x - 0.8f, pendantPos.y - 0.8f))
        }
        NecklaceStyle.PEARL_STRAND -> {
            for (i in -5..5) {
                val t = i / 5f
                val px = cx + t * halfW
                val py = neckY + (1f - t * t) * fig.height * 0.022f
                drawCircle(Color(0xFFD9CEC7), radius = 3.4f, center = Offset(px, py + 0.8f))
                drawCircle(Color(0xFFFDF9F4), radius = 3.1f, center = Offset(px, py))
                drawCircle(Color.White, radius = 1.1f, center = Offset(px - 0.8f, py - 0.8f))
            }
        }
        NecklaceStyle.LAYERED_CHAINS -> {
            for (layer in 1..3) {
                val drop = layer * fig.height * 0.015f
                val chain = Path().apply {
                    moveTo(cx - halfW, neckY)
                    quadraticTo(cx, neckY + drop * 1.4f, cx + halfW, neckY)
                }
                drawPath(chain, metal.primary, style = Stroke(width = 1.8f, cap = StrokeCap.Round))
            }
        }
    }
}

internal fun DrawScope.drawEarrings(face: Rect, style: EarringStyle, metal: JewelryMetal) {
    if (style == EarringStyle.NONE) return
    for (side in listOf(-1f, 1f)) {
        val earBase = Offset(face.center.x + side * face.width * 0.405f, face.top + face.height * 0.54f)
        when (style) {
            EarringStyle.NONE -> Unit
            EarringStyle.GOLD_HOOPS -> {
                val hoopCenter = Offset(earBase.x, earBase.y + face.width * 0.045f)
                drawCircle(metal.primary, radius = face.width * 0.055f, center = hoopCenter, style = Stroke(width = 3.2f))
                drawCircle(metal.highlight.copy(alpha = 0.7f), radius = face.width * 0.055f, center = Offset(hoopCenter.x - 0.8f, hoopCenter.y - 0.8f), style = Stroke(width = 1.1f))
            }
            EarringStyle.CRYSTAL_DROPS -> {
                drawLine(metal.primary, earBase, Offset(earBase.x, earBase.y + face.height * 0.13f), strokeWidth = 2f)
                drawCircle(metal.primary, radius = face.width * 0.027f, center = Offset(earBase.x, earBase.y + face.height * 0.08f))
                drawCircle(metal.highlight, radius = face.width * 0.017f, center = Offset(earBase.x - 0.6f, earBase.y + face.height * 0.078f))
                drawCircle(metal.primary, radius = face.width * 0.035f, center = Offset(earBase.x, earBase.y + face.height * 0.14f))
                drawCircle(metal.highlight, radius = face.width * 0.022f, center = Offset(earBase.x - 0.8f, earBase.y + face.height * 0.138f))
            }
            EarringStyle.PEARL_STUDS -> {
                drawCircle(Color(0xFFD6C9C1), radius = face.width * 0.030f, center = Offset(earBase.x, earBase.y + 0.8f))
                drawCircle(Color(0xFFFDF8F5), radius = face.width * 0.028f, center = earBase)
                drawCircle(Color.White, radius = face.width * 0.010f, center = Offset(earBase.x - 1f, earBase.y - 1f))
            }
            EarringStyle.BOHO_TASSELS -> {
                drawCircle(metal.primary, radius = face.width * 0.026f, center = earBase)
                for (dx in listOf(-4f, 0f, 4f)) {
                    drawLine(Color(0xFFD98555), Offset(earBase.x + dx, earBase.y + 4f), Offset(earBase.x + dx * 1.4f, earBase.y + face.height * 0.16f), strokeWidth = 2.4f, cap = StrokeCap.Round)
                }
            }
        }
    }
}

internal fun DrawScope.drawHeadAccessory(face: Rect, style: HeadAccessoryStyle, metal: JewelryMetal) {
    when (style) {
        HeadAccessoryStyle.NONE -> Unit
        HeadAccessoryStyle.PEARL_TIARA -> {
            val arc = Path().apply {
                moveTo(face.left + face.width * 0.16f, face.top + face.height * 0.12f)
                quadraticTo(face.center.x, face.top - face.height * 0.12f, face.right - face.width * 0.16f, face.top + face.height * 0.12f)
            }
            drawPath(arc, metal.primary, style = Stroke(width = 3f, cap = StrokeCap.Round))
            for (i in -2..2) {
                val x = face.center.x + i * face.width * 0.11f
                val y = face.top + face.height * 0.01f + (i * i) * face.height * 0.015f
                drawCircle(Color(0xFFD9CEC7), radius = face.width * 0.024f, center = Offset(x, y + 0.8f))
                drawCircle(Color(0xFFFDF8F5), radius = face.width * 0.022f, center = Offset(x, y))
            }
        }
        HeadAccessoryStyle.SATIN_HEADBAND -> {
            val band = Path().apply {
                moveTo(face.left + face.width * 0.08f, face.top + face.height * 0.20f)
                quadraticTo(face.center.x, face.top - face.height * 0.06f, face.right - face.width * 0.08f, face.top + face.height * 0.20f)
            }
            drawPath(band, metal.primary, style = Stroke(width = face.width * 0.06f, cap = StrokeCap.Round))
            drawPath(band, metal.highlight.copy(alpha = 0.45f), style = Stroke(width = face.width * 0.018f, cap = StrokeCap.Round))
        }
        HeadAccessoryStyle.GOLD_CLIPS -> {
            drawRoundRect(metal.primary, uv(face, 0.72f, 0.16f), Size(face.width * 0.12f, face.height * 0.028f), CornerRadius(4f, 4f))
            drawRoundRect(metal.highlight, uv(face, 0.70f, 0.21f), Size(face.width * 0.12f, face.height * 0.024f), CornerRadius(4f, 4f))
        }
    }
}

internal fun DrawScope.drawBodyPortraitOutfit(
    body: Rect,
    style: OutfitStyle,
    color: Color,
    accent: Color
) {
    val darker = color.copy(
        red = (color.red * 0.64f).coerceIn(0f, 1f),
        green = (color.green * 0.64f).coerceIn(0f, 1f),
        blue = (color.blue * 0.64f).coerceIn(0f, 1f)
    )
    val deepShadow = color.copy(
        red = (color.red * 0.44f).coerceIn(0f, 1f),
        green = (color.green * 0.44f).coerceIn(0f, 1f),
        blue = (color.blue * 0.44f).coerceIn(0f, 1f)
    )
    val lighter = color.copy(
        red = (color.red + (1f - color.red) * 0.28f).coerceIn(0f, 1f),
        green = (color.green + (1f - color.green) * 0.28f).coerceIn(0f, 1f),
        blue = (color.blue + (1f - color.blue) * 0.28f).coerceIn(0f, 1f)
    )
    val fabricBrush = Brush.horizontalGradient(
        colors = listOf(darker, color, lighter, color, darker),
        startX = body.left + body.width * 0.20f,
        endX = body.left + body.width * 0.80f
    )
    val seamStroke = Stroke(width = 1.6f, cap = StrokeCap.Round)
    val edgeColor = deepShadow.copy(alpha = 0.60f)

    // Torso reference landmarks on the extracted full-body WebP sprites (contrapposto pose)
    val waistY = 0.368f
    val bustTopY = 0.238f
    val shoulderY = 0.215f
    val hipMidY = 0.455f

    when (style) {
        OutfitStyle.GALA_GOWN -> {
            // Spaghetti/halter straps covering base bra straps
            drawLine(darker, fw(body, 0.36f, shoulderY), fw(body, 0.35f, bustTopY + 0.02f), strokeWidth = 4.5f, cap = StrokeCap.Round)
            drawLine(darker, fw(body, 0.65f, shoulderY), fw(body, 0.66f, bustTopY + 0.02f), strokeWidth = 4.5f, cap = StrokeCap.Round)
            val bodice = Path().apply {
                moveTo(fw(body, 0.30f, bustTopY).x, fw(body, 0.30f, bustTopY).y)
                quadraticTo(fw(body, 0.42f, bustTopY - 0.015f).x, fw(body, 0.42f, bustTopY - 0.015f).y, fw(body, 0.505f, bustTopY + 0.015f).x, fw(body, 0.505f, bustTopY + 0.015f).y)
                quadraticTo(fw(body, 0.59f, bustTopY - 0.015f).x, fw(body, 0.59f, bustTopY - 0.015f).y, fw(body, 0.71f, bustTopY).x, fw(body, 0.71f, bustTopY).y)
                lineTo(fw(body, 0.675f, waistY).x, fw(body, 0.675f, waistY).y)
                lineTo(fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                close()
            }
            drawPath(bodice, fabricBrush)
            drawPath(bodice, edgeColor, style = seamStroke)

            val skirt = Path().apply {
                moveTo(fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                lineTo(fw(body, 0.675f, waistY).x, fw(body, 0.675f, waistY).y)
                cubicTo(
                    fw(body, 0.85f, 0.49f).x, fw(body, 0.85f, 0.49f).y,
                    fw(body, 0.90f, 0.74f).x, fw(body, 0.90f, 0.74f).y,
                    fw(body, 0.86f, 0.94f).x, fw(body, 0.86f, 0.94f).y
                )
                lineTo(fw(body, 0.50f, 0.94f).x, fw(body, 0.50f, 0.94f).y)
                lineTo(fw(body, 0.43f, 0.61f).x, fw(body, 0.43f, 0.61f).y)
                lineTo(fw(body, 0.32f, 0.94f).x, fw(body, 0.32f, 0.94f).y)
                lineTo(fw(body, 0.06f, 0.93f).x, fw(body, 0.06f, 0.93f).y)
                cubicTo(
                    fw(body, 0.09f, 0.74f).x, fw(body, 0.09f, 0.74f).y,
                    fw(body, 0.16f, 0.49f).x, fw(body, 0.16f, 0.49f).y,
                    fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y
                )
                close()
            }
            drawPath(skirt, fabricBrush)
            drawPath(skirt, edgeColor, style = seamStroke)
            listOf(
                Pair(Offset(0.39f, waistY + 0.01f), Offset(0.18f, 0.91f)),
                Pair(Offset(0.61f, waistY + 0.01f), Offset(0.75f, 0.91f))
            ).forEach { (s, e) ->
                drawLine(deepShadow.copy(alpha = 0.28f), fw(body, s.x, s.y), fw(body, e.x, e.y), strokeWidth = 2.6f, cap = StrokeCap.Round)
            }
            drawRoundRect(accent, fw(body, 0.33f, waistY - 0.008f), Size(body.width * 0.35f, body.height * 0.018f), CornerRadius(6f, 6f))
        }

        OutfitStyle.STREETWEAR_BLAZER -> {
            // Crisp tee beneath blazer covering bra
            drawRect(Color(0xFFF7F2EE), fw(body, 0.34f, bustTopY), Size(body.width * 0.33f, body.height * 0.145f))
            // Left tailored trouser (contrapposto leg shifted left)
            val leftPant = Path().apply {
                moveTo(fw(body, 0.25f, waistY).x, fw(body, 0.25f, waistY).y)
                lineTo(fw(body, 0.50f, waistY).x, fw(body, 0.50f, waistY).y)
                lineTo(fw(body, 0.31f, 0.90f).x, fw(body, 0.31f, 0.90f).y)
                lineTo(fw(body, 0.11f, 0.90f).x, fw(body, 0.11f, 0.90f).y)
                cubicTo(fw(body, 0.18f, 0.65f).x, fw(body, 0.18f, 0.65f).y, fw(body, 0.18f, 0.46f).x, fw(body, 0.18f, 0.46f).y, fw(body, 0.25f, waistY).x, fw(body, 0.25f, waistY).y)
                close()
            }
            drawPath(leftPant, fabricBrush)
            drawPath(leftPant, edgeColor, style = seamStroke)

            // Right tailored trouser (straighter standing leg)
            val rightPant = Path().apply {
                moveTo(fw(body, 0.48f, waistY).x, fw(body, 0.48f, waistY).y)
                lineTo(fw(body, 0.74f, waistY).x, fw(body, 0.74f, waistY).y)
                cubicTo(fw(body, 0.83f, 0.46f).x, fw(body, 0.83f, 0.46f).y, fw(body, 0.74f, 0.66f).x, fw(body, 0.74f, 0.66f).y, fw(body, 0.68f, 0.90f).x, fw(body, 0.68f, 0.90f).y)
                lineTo(fw(body, 0.48f, 0.90f).x, fw(body, 0.48f, 0.90f).y)
                close()
            }
            drawPath(rightPant, fabricBrush)
            drawPath(rightPant, edgeColor, style = seamStroke)

            // Structured blazer flaps
            for (side in listOf(-1f, 1f)) {
                val sx = 0.505f
                val flap = Path().apply {
                    moveTo(fw(body, sx + side * 0.11f, shoulderY).x, fw(body, sx + side * 0.11f, shoulderY).y)
                    lineTo(fw(body, sx + side * 0.26f, shoulderY + 0.015f).x, fw(body, sx + side * 0.26f, shoulderY + 0.015f).y)
                    lineTo(fw(body, sx + side * 0.24f, 0.49f).x, fw(body, sx + side * 0.24f, 0.49f).y)
                    lineTo(fw(body, sx + side * 0.04f, 0.49f).x, fw(body, sx + side * 0.04f, 0.49f).y)
                    close()
                }
                drawPath(flap, fabricBrush)
                drawPath(flap, edgeColor, style = seamStroke)
                drawLine(lighter.copy(alpha = 0.65f), fw(body, sx + side * 0.115f, shoulderY + 0.01f), fw(body, sx + side * 0.055f, 0.37f), strokeWidth = 2.6f, cap = StrokeCap.Round)
            }
            drawRoundRect(accent, fw(body, 0.45f, waistY + 0.005f), Size(body.width * 0.11f, body.height * 0.016f), CornerRadius(4f, 4f))
        }

        OutfitStyle.BOHO_FLORAL -> {
            drawLine(darker, fw(body, 0.36f, shoulderY), fw(body, 0.35f, bustTopY + 0.02f), strokeWidth = 5.5f, cap = StrokeCap.Round)
            drawLine(darker, fw(body, 0.65f, shoulderY), fw(body, 0.66f, bustTopY + 0.02f), strokeWidth = 5.5f, cap = StrokeCap.Round)
            val top = Path().apply {
                moveTo(fw(body, 0.29f, bustTopY).x, fw(body, 0.29f, bustTopY).y)
                quadraticTo(fw(body, 0.505f, bustTopY + 0.032f).x, fw(body, 0.505f, bustTopY + 0.032f).y, fw(body, 0.72f, bustTopY).x, fw(body, 0.72f, bustTopY).y)
                lineTo(fw(body, 0.675f, waistY).x, fw(body, 0.675f, waistY).y)
                lineTo(fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                close()
            }
            drawPath(top, fabricBrush)
            drawPath(top, edgeColor, style = seamStroke)

            val skirt = Path().apply {
                moveTo(fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                lineTo(fw(body, 0.675f, waistY).x, fw(body, 0.675f, waistY).y)
                cubicTo(fw(body, 0.85f, 0.48f).x, fw(body, 0.85f, 0.48f).y, fw(body, 0.87f, 0.68f).x, fw(body, 0.87f, 0.68f).y, fw(body, 0.82f, 0.82f).x, fw(body, 0.82f, 0.82f).y)
                quadraticTo(fw(body, 0.49f, 0.85f).x, fw(body, 0.49f, 0.85f).y, fw(body, 0.13f, 0.82f).x, fw(body, 0.13f, 0.82f).y)
                cubicTo(fw(body, 0.12f, 0.68f).x, fw(body, 0.12f, 0.68f).y, fw(body, 0.17f, 0.48f).x, fw(body, 0.17f, 0.48f).y, fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                close()
            }
            drawPath(skirt, fabricBrush)
            drawPath(skirt, edgeColor, style = seamStroke)
            drawLine(deepShadow.copy(alpha = 0.30f), fw(body, 0.17f, 0.62f), fw(body, 0.83f, 0.62f), strokeWidth = 1.8f)
            listOf(Offset(0.38f, 0.46f), Offset(0.62f, 0.48f), Offset(0.49f, 0.56f), Offset(0.28f, 0.68f), Offset(0.71f, 0.69f), Offset(0.52f, 0.76f)).forEach { pt ->
                val c = fw(body, pt.x, pt.y)
                drawCircle(accent.copy(alpha = 0.9f), body.width * 0.025f, c)
                drawCircle(Color.White.copy(alpha = 0.65f), body.width * 0.009f, c)
            }
        }

        OutfitStyle.COUTURE_CORSET -> {
            val corset = Path().apply {
                moveTo(fw(body, 0.305f, bustTopY).x, fw(body, 0.305f, bustTopY).y)
                lineTo(fw(body, 0.505f, bustTopY + 0.02f).x, fw(body, 0.505f, bustTopY + 0.02f).y)
                lineTo(fw(body, 0.705f, bustTopY).x, fw(body, 0.705f, bustTopY).y)
                lineTo(fw(body, 0.675f, waistY + 0.02f).x, fw(body, 0.675f, waistY + 0.02f).y)
                lineTo(fw(body, 0.505f, waistY + 0.05f).x, fw(body, 0.505f, waistY + 0.05f).y)
                lineTo(fw(body, 0.335f, waistY + 0.02f).x, fw(body, 0.335f, waistY + 0.02f).y)
                close()
            }
            drawPath(corset, fabricBrush)
            drawPath(corset, edgeColor, style = seamStroke)
            for (bx in listOf(0.41f, 0.505f, 0.60f)) {
                drawLine(accent.copy(alpha = 0.75f), fw(body, bx, bustTopY + 0.02f), fw(body, bx, waistY + 0.02f), strokeWidth = 2.0f, cap = StrokeCap.Round)
            }
            val skirt = Path().apply {
                moveTo(fw(body, 0.335f, waistY + 0.02f).x, fw(body, 0.335f, waistY + 0.02f).y)
                lineTo(fw(body, 0.505f, waistY + 0.05f).x, fw(body, 0.505f, waistY + 0.05f).y)
                lineTo(fw(body, 0.675f, waistY + 0.02f).x, fw(body, 0.675f, waistY + 0.02f).y)
                lineTo(fw(body, 0.84f, 0.80f).x, fw(body, 0.84f, 0.80f).y)
                lineTo(fw(body, 0.16f, 0.62f).x, fw(body, 0.16f, 0.62f).y)
                close()
            }
            drawPath(skirt, fabricBrush)
            drawPath(skirt, edgeColor, style = seamStroke)
        }

        OutfitStyle.ROMANTIC_TULLE -> {
            drawLine(darker, fw(body, 0.365f, shoulderY), fw(body, 0.365f, bustTopY + 0.02f), strokeWidth = 5f, cap = StrokeCap.Round)
            drawLine(darker, fw(body, 0.645f, shoulderY), fw(body, 0.645f, bustTopY + 0.02f), strokeWidth = 5f, cap = StrokeCap.Round)
            val bodice = Path().apply {
                moveTo(fw(body, 0.31f, bustTopY).x, fw(body, 0.31f, bustTopY).y)
                quadraticTo(fw(body, 0.505f, bustTopY + 0.025f).x, fw(body, 0.505f, bustTopY + 0.025f).y, fw(body, 0.70f, bustTopY).x, fw(body, 0.70f, bustTopY).y)
                lineTo(fw(body, 0.675f, waistY).x, fw(body, 0.675f, waistY).y)
                lineTo(fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                close()
            }
            drawPath(bodice, fabricBrush)
            drawPath(bodice, edgeColor, style = seamStroke)
            val tulleOuter = Path().apply {
                moveTo(fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                lineTo(fw(body, 0.675f, waistY).x, fw(body, 0.675f, waistY).y)
                cubicTo(fw(body, 0.89f, 0.45f).x, fw(body, 0.89f, 0.45f).y, fw(body, 0.92f, 0.65f).x, fw(body, 0.92f, 0.65f).y, fw(body, 0.86f, 0.76f).x, fw(body, 0.86f, 0.76f).y)
                quadraticTo(fw(body, 0.49f, 0.80f).x, fw(body, 0.49f, 0.80f).y, fw(body, 0.11f, 0.76f).x, fw(body, 0.11f, 0.76f).y)
                cubicTo(fw(body, 0.08f, 0.65f).x, fw(body, 0.08f, 0.65f).y, fw(body, 0.13f, 0.45f).x, fw(body, 0.13f, 0.45f).y, fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                close()
            }
            drawPath(tulleOuter, color.copy(alpha = 0.86f))
            for (px in listOf(0.41f, 0.50f, 0.59f)) {
                drawLine(lighter.copy(alpha = 0.42f), fw(body, px, waistY + 0.01f), fw(body, 0.50f + (px - 0.50f) * 3.0f, 0.76f), strokeWidth = 2.2f)
            }
            drawPath(tulleOuter, color = accent.copy(alpha = 0.60f), style = Stroke(width = 1.8f))
        }

        OutfitStyle.MINIMAL_JUMPSUIT -> {
            val halter = Path().apply {
                moveTo(fw(body, 0.41f, 0.205f).x, fw(body, 0.41f, 0.205f).y)
                lineTo(fw(body, 0.60f, 0.205f).x, fw(body, 0.60f, 0.205f).y)
                lineTo(fw(body, 0.71f, 0.30f).x, fw(body, 0.71f, 0.30f).y)
                lineTo(fw(body, 0.675f, waistY).x, fw(body, 0.675f, waistY).y)
                lineTo(fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                lineTo(fw(body, 0.30f, 0.30f).x, fw(body, 0.30f, 0.30f).y)
                close()
            }
            drawPath(halter, fabricBrush)
            drawPath(halter, edgeColor, style = seamStroke)

            val leftLeg = Path().apply {
                moveTo(fw(body, 0.25f, waistY).x, fw(body, 0.25f, waistY).y)
                lineTo(fw(body, 0.50f, waistY).x, fw(body, 0.50f, waistY).y)
                lineTo(fw(body, 0.29f, 0.89f).x, fw(body, 0.29f, 0.89f).y)
                lineTo(fw(body, 0.12f, 0.89f).x, fw(body, 0.12f, 0.89f).y)
                close()
            }
            val rightLeg = Path().apply {
                moveTo(fw(body, 0.49f, waistY).x, fw(body, 0.49f, waistY).y)
                lineTo(fw(body, 0.75f, waistY).x, fw(body, 0.75f, waistY).y)
                lineTo(fw(body, 0.67f, 0.89f).x, fw(body, 0.67f, 0.89f).y)
                lineTo(fw(body, 0.49f, 0.89f).x, fw(body, 0.49f, 0.89f).y)
                close()
            }
            drawPath(leftLeg, fabricBrush)
            drawPath(leftLeg, edgeColor, style = seamStroke)
            drawPath(rightLeg, fabricBrush)
            drawPath(rightLeg, edgeColor, style = seamStroke)
            drawLine(accent, fw(body, 0.335f, waistY), fw(body, 0.675f, waistY), strokeWidth = 4f)
        }

        OutfitStyle.COCKTAIL_MINI -> {
            drawLine(darker, fw(body, 0.64f, shoulderY), fw(body, 0.66f, bustTopY + 0.02f), strokeWidth = 6f, cap = StrokeCap.Round)
            val dress = Path().apply {
                moveTo(fw(body, 0.305f, bustTopY + 0.01f).x, fw(body, 0.305f, bustTopY + 0.01f).y)
                quadraticTo(fw(body, 0.505f, bustTopY + 0.04f).x, fw(body, 0.505f, bustTopY + 0.04f).y, fw(body, 0.715f, bustTopY - 0.01f).x, fw(body, 0.715f, bustTopY - 0.01f).y)
                lineTo(fw(body, 0.68f, waistY).x, fw(body, 0.68f, waistY).y)
                cubicTo(fw(body, 0.84f, hipMidY).x, fw(body, 0.84f, hipMidY).y, fw(body, 0.80f, 0.56f).x, fw(body, 0.80f, 0.56f).y, fw(body, 0.74f, 0.60f).x, fw(body, 0.74f, 0.60f).y)
                lineTo(fw(body, 0.21f, 0.61f).x, fw(body, 0.21f, 0.61f).y)
                cubicTo(fw(body, 0.19f, 0.56f).x, fw(body, 0.19f, 0.56f).y, fw(body, 0.19f, hipMidY).x, fw(body, 0.19f, hipMidY).y, fw(body, 0.335f, waistY).x, fw(body, 0.335f, waistY).y)
                close()
            }
            drawPath(dress, fabricBrush)
            drawPath(dress, edgeColor, style = seamStroke)
            drawLine(lighter.copy(alpha = 0.62f), fw(body, 0.32f, 0.42f), fw(body, 0.74f, 0.50f), strokeWidth = 2.4f, cap = StrokeCap.Round)
            drawLine(deepShadow.copy(alpha = 0.32f), fw(body, 0.29f, 0.47f), fw(body, 0.74f, 0.54f), strokeWidth = 2.0f, cap = StrokeCap.Round)
        }

        OutfitStyle.DENIM_CROP -> {
            drawRect(Color(0xFFFDFBF9), fw(body, 0.33f, bustTopY), Size(body.width * 0.35f, body.height * 0.125f))
            for (side in listOf(-1f, 1f)) {
                val sx = 0.505f
                val jPanel = Path().apply {
                    moveTo(fw(body, sx + side * 0.10f, shoulderY).x, fw(body, sx + side * 0.10f, shoulderY).y)
                    lineTo(fw(body, sx + side * 0.25f, shoulderY + 0.01f).x, fw(body, sx + side * 0.25f, shoulderY + 0.01f).y)
                    lineTo(fw(body, sx + side * 0.21f, 0.34f).x, fw(body, sx + side * 0.21f, 0.34f).y)
                    lineTo(fw(body, sx + side * 0.08f, 0.34f).x, fw(body, sx + side * 0.08f, 0.34f).y)
                    close()
                }
                drawPath(jPanel, fabricBrush)
                drawPath(jPanel, edgeColor, style = seamStroke)
            }
            val denim = Color(0xFF3E5E84)
            val denimDark = Color(0xFF28415E)
            val denimLight = Color(0xFF5D81AA)
            val denimBrush = Brush.horizontalGradient(listOf(denimDark, denim, denimLight, denimDark))
            val leftJean = Path().apply {
                moveTo(fw(body, 0.25f, waistY).x, fw(body, 0.25f, waistY).y)
                lineTo(fw(body, 0.50f, waistY).x, fw(body, 0.50f, waistY).y)
                lineTo(fw(body, 0.28f, 0.89f).x, fw(body, 0.28f, 0.89f).y)
                lineTo(fw(body, 0.12f, 0.89f).x, fw(body, 0.12f, 0.89f).y)
                close()
            }
            val rightJean = Path().apply {
                moveTo(fw(body, 0.49f, waistY).x, fw(body, 0.49f, waistY).y)
                lineTo(fw(body, 0.74f, waistY).x, fw(body, 0.74f, waistY).y)
                lineTo(fw(body, 0.67f, 0.89f).x, fw(body, 0.67f, 0.89f).y)
                lineTo(fw(body, 0.49f, 0.89f).x, fw(body, 0.49f, 0.89f).y)
                close()
            }
            drawPath(leftJean, denimBrush)
            drawPath(leftJean, denimDark.copy(alpha = 0.65f), style = seamStroke)
            drawPath(rightJean, denimBrush)
            drawPath(rightJean, denimDark.copy(alpha = 0.65f), style = seamStroke)
        }

        OutfitStyle.SILK_SLIP -> {
            drawLine(darker, fw(body, 0.365f, shoulderY), fw(body, 0.365f, bustTopY + 0.02f), strokeWidth = 4.2f, cap = StrokeCap.Round)
            drawLine(darker, fw(body, 0.645f, shoulderY), fw(body, 0.645f, bustTopY + 0.02f), strokeWidth = 4.2f, cap = StrokeCap.Round)
            val slip = Path().apply {
                moveTo(fw(body, 0.31f, bustTopY).x, fw(body, 0.31f, bustTopY).y)
                quadraticTo(fw(body, 0.505f, bustTopY + 0.04f).x, fw(body, 0.505f, bustTopY + 0.04f).y, fw(body, 0.70f, bustTopY).x, fw(body, 0.70f, bustTopY).y)
                cubicTo(fw(body, 0.74f, 0.42f).x, fw(body, 0.74f, 0.42f).y, fw(body, 0.82f, 0.60f).x, fw(body, 0.82f, 0.60f).y, fw(body, 0.77f, 0.77f).x, fw(body, 0.77f, 0.77f).y)
                quadraticTo(fw(body, 0.48f, 0.79f).x, fw(body, 0.48f, 0.79f).y, fw(body, 0.17f, 0.77f).x, fw(body, 0.17f, 0.77f).y)
                cubicTo(fw(body, 0.18f, 0.60f).x, fw(body, 0.18f, 0.60f).y, fw(body, 0.27f, 0.42f).x, fw(body, 0.27f, 0.42f).y, fw(body, 0.31f, bustTopY).x, fw(body, 0.31f, bustTopY).y)
                close()
            }
            drawPath(slip, Brush.horizontalGradient(listOf(darker, lighter, color, lighter, darker)))
            drawPath(slip, edgeColor, style = seamStroke)
            drawLine(lighter.copy(alpha = 0.55f), fw(body, 0.42f, 0.34f), fw(body, 0.36f, 0.74f), strokeWidth = 2.6f, cap = StrokeCap.Round)
        }

        OutfitStyle.TRENCH_COAT -> {
            val coat = Path().apply {
                moveTo(fw(body, 0.26f, shoulderY).x, fw(body, 0.26f, shoulderY).y)
                lineTo(fw(body, 0.75f, shoulderY).x, fw(body, 0.75f, shoulderY).y)
                lineTo(fw(body, 0.84f, 0.73f).x, fw(body, 0.84f, 0.73f).y)
                lineTo(fw(body, 0.15f, 0.73f).x, fw(body, 0.15f, 0.73f).y)
                close()
            }
            drawPath(coat, fabricBrush)
            drawPath(coat, edgeColor, style = seamStroke)
            for (side in listOf(-1f, 1f)) {
                for (by in listOf(0.30f, 0.44f, 0.54f)) {
                    drawCircle(deepShadow, body.width * 0.016f, fw(body, 0.505f + side * 0.08f, by))
                }
            }
            drawLine(deepShadow.copy(alpha = 0.65f), fw(body, 0.505f, 0.26f), fw(body, 0.505f, 0.73f), strokeWidth = 2.2f)
            drawRoundRect(darker, fw(body, 0.28f, waistY - 0.008f), Size(body.width * 0.45f, body.height * 0.020f), CornerRadius(5f, 5f))
            drawRect(accent, fw(body, 0.46f, waistY - 0.010f), Size(body.width * 0.09f, body.height * 0.024f))
        }

        OutfitStyle.MERMAID_SEQUIN -> {
            drawLine(darker, fw(body, 0.365f, shoulderY), fw(body, 0.365f, bustTopY + 0.02f), strokeWidth = 4.2f, cap = StrokeCap.Round)
            drawLine(darker, fw(body, 0.645f, shoulderY), fw(body, 0.645f, bustTopY + 0.02f), strokeWidth = 4.2f, cap = StrokeCap.Round)
            val gown = Path().apply {
                moveTo(fw(body, 0.305f, bustTopY).x, fw(body, 0.305f, bustTopY).y)
                quadraticTo(fw(body, 0.505f, bustTopY + 0.028f).x, fw(body, 0.505f, bustTopY + 0.028f).y, fw(body, 0.705f, bustTopY).x, fw(body, 0.705f, bustTopY).y)
                cubicTo(fw(body, 0.75f, 0.46f).x, fw(body, 0.75f, 0.46f).y, fw(body, 0.70f, 0.64f).x, fw(body, 0.70f, 0.64f).y, fw(body, 0.89f, 0.94f).x, fw(body, 0.89f, 0.94f).y)
                lineTo(fw(body, 0.08f, 0.94f).x, fw(body, 0.08f, 0.94f).y)
                cubicTo(fw(body, 0.24f, 0.64f).x, fw(body, 0.24f, 0.64f).y, fw(body, 0.24f, 0.46f).x, fw(body, 0.24f, 0.46f).y, fw(body, 0.305f, bustTopY).x, fw(body, 0.305f, bustTopY).y)
                close()
            }
            drawPath(gown, fabricBrush)
            drawPath(gown, edgeColor, style = seamStroke)
            listOf(
                Offset(0.41f, 0.33f), Offset(0.59f, 0.31f), Offset(0.49f, 0.44f),
                Offset(0.39f, 0.55f), Offset(0.61f, 0.56f), Offset(0.28f, 0.76f),
                Offset(0.71f, 0.78f), Offset(0.49f, 0.85f), Offset(0.42f, 0.68f), Offset(0.58f, 0.70f)
            ).forEach { pt ->
                val c = fw(body, pt.x, pt.y)
                drawCircle(accent.copy(alpha = 0.70f), body.width * 0.020f, c)
                drawCircle(Color.White.copy(alpha = 0.88f), body.width * 0.011f, c)
            }
        }

        OutfitStyle.ATHLEISURE_SET -> {
            drawLine(darker, fw(body, 0.365f, shoulderY), fw(body, 0.365f, bustTopY + 0.02f), strokeWidth = 6f, cap = StrokeCap.Round)
            drawLine(darker, fw(body, 0.645f, shoulderY), fw(body, 0.645f, bustTopY + 0.02f), strokeWidth = 6f, cap = StrokeCap.Round)
            val top = Path().apply {
                moveTo(fw(body, 0.32f, bustTopY).x, fw(body, 0.32f, bustTopY).y)
                lineTo(fw(body, 0.69f, bustTopY).x, fw(body, 0.69f, bustTopY).y)
                lineTo(fw(body, 0.675f, 0.335f).x, fw(body, 0.675f, 0.335f).y)
                lineTo(fw(body, 0.335f, 0.335f).x, fw(body, 0.335f, 0.335f).y)
                close()
            }
            drawPath(top, fabricBrush)
            drawPath(top, edgeColor, style = seamStroke)
            val skirt = Path().apply {
                moveTo(fw(body, 0.28f, waistY).x, fw(body, 0.28f, waistY).y)
                lineTo(fw(body, 0.72f, waistY).x, fw(body, 0.72f, waistY).y)
                lineTo(fw(body, 0.82f, 0.56f).x, fw(body, 0.82f, 0.56f).y)
                lineTo(fw(body, 0.18f, 0.56f).x, fw(body, 0.18f, 0.56f).y)
                close()
            }
            drawPath(skirt, fabricBrush)
            drawPath(skirt, edgeColor, style = seamStroke)
            for (px in listOf(0.38f, 0.46f, 0.54f, 0.62f)) {
                drawLine(deepShadow.copy(alpha = 0.30f), fw(body, px, waistY + 0.01f), fw(body, 0.50f + (px - 0.50f) * 1.85f, 0.555f), strokeWidth = 1.8f)
            }
            drawLine(Color.White.copy(alpha = 0.82f), fw(body, 0.19f, 0.545f), fw(body, 0.81f, 0.545f), strokeWidth = 3f)
        }
    }
}

internal fun DrawScope.drawBodyPortraitShoes(
    body: Rect,
    style: ShoeStyle,
    color: Color,
    metal: JewelryMetal
) {
    val darker = color.copy(
        red = (color.red * 0.60f).coerceIn(0f, 1f),
        green = (color.green * 0.60f).coerceIn(0f, 1f),
        blue = (color.blue * 0.60f).coerceIn(0f, 1f)
    )
    val lighter = color.copy(
        red = (color.red + (1f - color.red) * 0.28f).coerceIn(0f, 1f),
        green = (color.green + (1f - color.green) * 0.28f).coerceIn(0f, 1f),
        blue = (color.blue + (1f - color.blue) * 0.28f).coerceIn(0f, 1f)
    )
    val shoeBrush = Brush.verticalGradient(listOf(lighter, color, darker))
    val rim = Stroke(width = 1.4f, cap = StrokeCap.Round)

    // Left contrapposto foot center ~0.180f, Right standing foot center ~0.585f
    val feet = listOf(
        Pair(-1f, 0.180f),
        Pair(1f, 0.585f)
    )
    for ((side, footFx) in feet) {
        val cx = body.left + body.width * footFx
        val ankleY = body.top + body.height * 0.905f
        val soleY = body.top + body.height * 0.985f
        val w = body.width * 0.092f

        when (style) {
            ShoeStyle.STILETTO -> {
                drawLine(metal.primary, Offset(cx + side * w * 0.24f, ankleY + body.height * 0.02f), Offset(cx + side * w * 0.30f, soleY), strokeWidth = 3f, cap = StrokeCap.Round)
                val pump = Path().apply {
                    moveTo(cx - w * 0.62f, ankleY + body.height * 0.030f)
                    quadraticTo(cx, soleY + 3f, cx + w * 0.66f, soleY)
                    quadraticTo(cx + w * 0.36f, ankleY + body.height * 0.012f, cx - w * 0.62f, ankleY + body.height * 0.030f)
                    close()
                }
                drawPath(pump, shoeBrush)
                drawPath(pump, darker.copy(alpha = 0.65f), style = rim)
                drawLine(color, Offset(cx - w * 0.45f, ankleY + 4f), Offset(cx + w * 0.45f, ankleY + 4f), strokeWidth = 2.8f, cap = StrokeCap.Round)
            }

            ShoeStyle.COMBAT_BOOTS -> {
                val boot = Path().apply {
                    moveTo(cx - w * 0.56f, ankleY - body.height * 0.075f)
                    lineTo(cx + w * 0.56f, ankleY - body.height * 0.075f)
                    lineTo(cx + w * 0.72f, soleY)
                    lineTo(cx - w * 0.68f, soleY)
                    close()
                }
                drawPath(boot, shoeBrush)
                drawPath(boot, darker.copy(alpha = 0.65f), style = rim)
                for (i in 0..2) {
                    val ly = ankleY - body.height * 0.055f + i * body.height * 0.016f
                    drawLine(lighter.copy(alpha = 0.72f), Offset(cx - w * 0.22f, ly), Offset(cx + w * 0.22f, ly + 4f), strokeWidth = 1.6f)
                }
                drawLine(Color(0xFF120E10), Offset(cx - w * 0.70f, soleY), Offset(cx + w * 0.74f, soleY), strokeWidth = 5.5f, cap = StrokeCap.Round)
            }

            ShoeStyle.JEWEL_SANDALS -> {
                drawLine(color, Offset(cx - w * 0.55f, soleY), Offset(cx + w * 0.60f, soleY), strokeWidth = 4f, cap = StrokeCap.Round)
                drawLine(metal.primary, Offset(cx - w * 0.42f, ankleY + 6f), Offset(cx + w * 0.42f, ankleY + 14f), strokeWidth = 2.5f)
                drawCircle(metal.primary, radius = 4.5f, center = Offset(cx, ankleY + 20f))
                drawCircle(metal.highlight, radius = 2.5f, center = Offset(cx, ankleY + 20f))
            }

            ShoeStyle.PLATFORM_SNEAKERS -> {
                drawRoundRect(shoeBrush, Offset(cx - w * 0.64f, ankleY + body.height * 0.012f), Size(w * 1.32f, body.height * 0.048f), CornerRadius(8f, 8f))
                drawRoundRect(Color(0xFFFDFBF9), Offset(cx - w * 0.68f, soleY - body.height * 0.018f), Size(w * 1.40f, body.height * 0.020f), CornerRadius(6f, 6f))
            }

            ShoeStyle.POINTED_MULES -> {
                val mule = Path().apply {
                    moveTo(cx - w * 0.60f, soleY - 6f)
                    lineTo(cx + w * 0.72f, soleY)
                    lineTo(cx + w * 0.22f, ankleY + body.height * 0.025f)
                    lineTo(cx - w * 0.50f, ankleY + body.height * 0.028f)
                    close()
                }
                drawPath(mule, shoeBrush)
                drawPath(mule, darker.copy(alpha = 0.65f), style = rim)
            }

            ShoeStyle.OVER_KNEE_BOOTS -> {
                val kneeCx = body.left + body.width * (if (side < 0) 0.295f else 0.610f)
                val boot = Path().apply {
                    moveTo(kneeCx - w * 0.75f, body.top + body.height * 0.66f)
                    lineTo(kneeCx + w * 0.75f, body.top + body.height * 0.66f)
                    lineTo(cx + w * 0.68f, soleY)
                    lineTo(cx - w * 0.64f, soleY)
                    close()
                }
                drawPath(boot, Brush.horizontalGradient(listOf(darker, color, lighter, darker)))
                drawPath(boot, darker.copy(alpha = 0.65f), style = rim)
                drawLine(metal.highlight.copy(alpha = 0.65f), Offset(kneeCx - w * 0.1f, body.top + body.height * 0.67f), Offset(cx - w * 0.1f, soleY - 4f), strokeWidth = 2f)
            }

            ShoeStyle.MARY_JANES -> {
                drawRoundRect(shoeBrush, Offset(cx - w * 0.62f, ankleY + body.height * 0.026f), Size(w * 1.28f, body.height * 0.042f), CornerRadius(10f, 10f))
                drawLine(color, Offset(cx - w * 0.48f, ankleY + body.height * 0.015f), Offset(cx + w * 0.48f, ankleY + body.height * 0.015f), strokeWidth = 3f)
                drawCircle(metal.primary, radius = 3f, center = Offset(cx + side * w * 0.32f, ankleY + body.height * 0.015f))
            }

            ShoeStyle.GLADIATOR_HEELS -> {
                drawLine(color, Offset(cx - w * 0.55f, soleY), Offset(cx + w * 0.60f, soleY), strokeWidth = 3.5f, cap = StrokeCap.Round)
                for (step in 0..4) {
                    val y1 = ankleY - step * body.height * 0.016f
                    drawLine(color, Offset(cx - w * 0.44f, y1), Offset(cx + w * 0.44f, y1 - 6f), strokeWidth = 2.2f)
                    drawLine(color, Offset(cx + w * 0.44f, y1), Offset(cx - w * 0.44f, y1 - 6f), strokeWidth = 2.2f)
                }
            }

            ShoeStyle.ESPADRILLE_WEDGES -> {
                val wedge = Path().apply {
                    moveTo(cx - w * 0.60f, ankleY + body.height * 0.024f)
                    lineTo(cx + w * 0.64f, soleY - body.height * 0.014f)
                    lineTo(cx + w * 0.64f, soleY)
                    lineTo(cx - w * 0.60f, soleY)
                    close()
                }
                drawPath(wedge, Brush.verticalGradient(listOf(Color(0xFFE5CFA4), Color(0xFFB8965E))))
                drawRoundRect(shoeBrush, Offset(cx - w * 0.52f, ankleY + body.height * 0.020f), Size(w * 1.08f, body.height * 0.022f), CornerRadius(6f, 6f))
            }

            ShoeStyle.ANKLE_BOOTIES -> {
                val bootie = Path().apply {
                    moveTo(cx - w * 0.54f, ankleY - body.height * 0.018f)
                    lineTo(cx + w * 0.54f, ankleY - body.height * 0.018f)
                    lineTo(cx + w * 0.68f, soleY)
                    lineTo(cx - w * 0.60f, soleY)
                    close()
                }
                drawPath(bootie, shoeBrush)
                drawPath(bootie, darker.copy(alpha = 0.65f), style = rim)
                drawLine(metal.primary, Offset(cx - w * 0.52f, ankleY + body.height * 0.018f), Offset(cx - w * 0.52f, soleY), strokeWidth = 4f, cap = StrokeCap.Round)
            }
        }
    }
}

internal fun DrawScope.drawBodyPortraitNecklace(
    body: Rect,
    style: NecklaceStyle,
    metal: JewelryMetal
) {
    if (style == NecklaceStyle.NONE) return
    val neckY = body.top + body.height * 0.195f
    val cx = body.left + body.width * 0.505f
    val halfW = body.width * 0.095f

    when (style) {
        NecklaceStyle.NONE -> Unit
        NecklaceStyle.DIAMOND_CHOKER -> {
            drawLine(metal.primary, Offset(cx - halfW, neckY), Offset(cx + halfW, neckY), strokeWidth = 2.6f, cap = StrokeCap.Round)
            for (i in -3..3) {
                val x = cx + i * (halfW / 3.2f)
                drawCircle(metal.primary, radius = 2.6f, center = Offset(x, neckY))
                drawCircle(metal.highlight, radius = 1.5f, center = Offset(x - 0.4f, neckY - 0.4f))
            }
        }
        NecklaceStyle.PENDANT_CHAIN -> {
            val chain = Path().apply {
                moveTo(cx - halfW, neckY - 2f)
                quadraticTo(cx, neckY + body.height * 0.032f, cx + halfW, neckY - 2f)
            }
            drawPath(chain, metal.primary, style = Stroke(width = 1.8f, cap = StrokeCap.Round))
            val pendantPos = Offset(cx, neckY + body.height * 0.018f)
            drawCircle(metal.primary, radius = 4.2f, center = pendantPos)
            drawCircle(metal.highlight, radius = 2.4f, center = Offset(pendantPos.x - 0.5f, pendantPos.y - 0.5f))
        }
        NecklaceStyle.PEARL_STRAND -> {
            for (i in -4..4) {
                val t = i / 4f
                val px = cx + t * halfW
                val py = neckY + (1f - t * t) * body.height * 0.016f
                drawCircle(Color(0xFFFDF9F4), radius = 2.5f, center = Offset(px, py))
            }
        }
        NecklaceStyle.LAYERED_CHAINS -> {
            for (layer in 1..3) {
                val drop = layer * body.height * 0.011f
                val chain = Path().apply {
                    moveTo(cx - halfW, neckY - 2f)
                    quadraticTo(cx, neckY + drop * 1.4f, cx + halfW, neckY - 2f)
                }
                drawPath(chain, metal.primary, style = Stroke(width = 1.5f, cap = StrokeCap.Round))
            }
        }
    }
}

