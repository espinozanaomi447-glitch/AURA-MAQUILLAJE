package com.example.ui.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import com.example.model.*

internal fun DrawScope.drawRealisticFaceAndMakeup(
    face: Rect,
    model: WomanModel,
    hairColor: Color,
    makeup: MakeupState,
    metal: JewelryMetal
) {
    drawHeadAndFaceBase(face, model)
    drawBaseAndContour(face, makeup)
    drawBlush(face, makeup)
    drawEyeshadow(face, makeup)
    drawEyeliner(face, makeup)
    drawEyesAndLashes(face, model, makeup.browLashStyle)
    drawBrows(face, hairColor, makeup.browLashStyle)
    drawLips(face, model, makeup)
    drawFaceGems(face, makeup.gemStyle, metal)
}

internal fun DrawScope.drawRealisticPortraitMakeupOverlay(
    face: Rect,
    model: WomanModel,
    hairColor: Color,
    makeup: MakeupState,
    metal: JewelryMetal
) {
    // Translucent layers carefully calibrated to preserve the illustrated portrait's skin texture, eyes, and lip contours
    drawBaseAndContour(face, makeup)
    drawBlush(face, makeup)
    drawPortraitEyeshadow(face, makeup)
    drawEyeliner(face, makeup)
    drawPortraitBrowLashEnhancement(face, hairColor, makeup.browLashStyle)
    drawPortraitLips(face, model, makeup)
    drawFaceGems(face, makeup.gemStyle, metal)
}

private fun DrawScope.drawPortraitEyeshadow(face: Rect, makeup: MakeupState) {
    if (makeup.eyeshadowPattern == EyeshadowPattern.NONE) return
    val p = makeup.eyeshadowPrimary
    val s = makeup.eyeshadowSecondary
    val alpha = makeup.eyeshadowIntensity * 0.78f

    for (side in listOf(-1f, 1f)) {
        val eyeCenter = uv(face, 0.50f + side * 0.165f, 0.405f)
        val lidCenter = Offset(eyeCenter.x, eyeCenter.y - face.height * 0.026f)
        val eyeW = face.width * 0.175f
        val eyeH = face.height * 0.095f

        when (makeup.eyeshadowPattern) {
            EyeshadowPattern.NONE -> Unit
            EyeshadowPattern.SOFT_WASH -> {
                drawOval(
                    brush = Brush.radialGradient(listOf(p.copy(alpha = 0.58f * alpha), Color.Transparent), lidCenter, eyeW * 0.78f),
                    topLeft = Offset(lidCenter.x - eyeW * 0.70f, lidCenter.y - eyeH * 0.70f),
                    size = Size(eyeW * 1.40f, eyeH * 1.15f)
                )
            }
            EyeshadowPattern.GRADIENT_SUNSET -> {
                val grad = Brush.horizontalGradient(
                    colors = if (side < 0) listOf(s.copy(alpha = 0.72f * alpha), p.copy(alpha = 0.58f * alpha), Color.Transparent)
                    else listOf(Color.Transparent, p.copy(alpha = 0.58f * alpha), s.copy(alpha = 0.72f * alpha)),
                    startX = lidCenter.x - eyeW * 0.78f,
                    endX = lidCenter.x + eyeW * 0.78f
                )
                drawOval(
                    brush = grad,
                    topLeft = Offset(lidCenter.x - eyeW * 0.75f, lidCenter.y - eyeH * 0.75f),
                    size = Size(eyeW * 1.50f, eyeH * 1.20f)
                )
            }
            EyeshadowPattern.SMOKY_HALO -> {
                drawOval(
                    brush = Brush.radialGradient(listOf(s.copy(alpha = 0.78f * alpha), Color.Transparent), lidCenter, eyeW * 0.88f),
                    topLeft = Offset(lidCenter.x - eyeW * 0.80f, lidCenter.y - eyeH * 0.78f),
                    size = Size(eyeW * 1.60f, eyeH * 1.32f)
                )
                drawCircle(
                    brush = Brush.radialGradient(listOf(p.copy(alpha = 0.82f * alpha), Color.Transparent), lidCenter, eyeW * 0.34f),
                    radius = eyeW * 0.34f,
                    center = lidCenter
                )
            }
            EyeshadowPattern.CUT_CREASE -> {
                val creasePath = Path().apply {
                    moveTo(lidCenter.x - eyeW * 0.70f, lidCenter.y + eyeH * 0.12f)
                    quadraticTo(lidCenter.x, lidCenter.y - eyeH * 1.10f, lidCenter.x + eyeW * 0.70f, lidCenter.y + eyeH * 0.12f)
                }
                drawPath(creasePath, s.copy(alpha = 0.76f * alpha), style = Stroke(width = eyeH * 0.38f, cap = StrokeCap.Round))
                drawOval(
                    brush = Brush.radialGradient(listOf(p.copy(alpha = 0.75f * alpha), Color.Transparent), lidCenter, eyeW * 0.55f),
                    topLeft = Offset(lidCenter.x - eyeW * 0.54f, lidCenter.y - eyeH * 0.48f),
                    size = Size(eyeW * 1.08f, eyeH * 0.76f)
                )
            }
            EyeshadowPattern.GRAPHIC_DUO -> {
                val block = Path().apply {
                    moveTo(lidCenter.x - eyeW * 0.62f, lidCenter.y + eyeH * 0.15f)
                    lineTo(lidCenter.x - eyeW * 0.42f, lidCenter.y - eyeH * 0.68f)
                    lineTo(lidCenter.x + eyeW * 0.75f, lidCenter.y - eyeH * 0.58f)
                    lineTo(lidCenter.x + eyeW * 0.62f, lidCenter.y + eyeH * 0.18f)
                    close()
                }
                drawPath(block, p.copy(alpha = 0.68f * alpha))
                drawLine(
                    color = s.copy(alpha = 0.85f * alpha),
                    start = Offset(lidCenter.x - eyeW * 0.45f, lidCenter.y - eyeH * 0.68f),
                    end = Offset(lidCenter.x + eyeW * 0.75f, lidCenter.y - eyeH * 0.58f),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
            }
            EyeshadowPattern.FOXY_WING_SMOKE -> {
                val wingStart = Offset(lidCenter.x, lidCenter.y + eyeH * 0.08f)
                val wingTip = Offset(lidCenter.x + side * eyeW * 1.05f, lidCenter.y - eyeH * 0.80f)
                drawLine(
                    brush = Brush.linearGradient(listOf(s.copy(alpha = 0.78f * alpha), Color.Transparent), wingStart, wingTip),
                    start = wingStart,
                    end = wingTip,
                    strokeWidth = eyeH * 0.85f,
                    cap = StrokeCap.Round
                )
                drawOval(
                    brush = Brush.radialGradient(listOf(p.copy(alpha = 0.58f * alpha), Color.Transparent), lidCenter, eyeW * 0.62f),
                    topLeft = Offset(lidCenter.x - eyeW * 0.58f, lidCenter.y - eyeH * 0.58f),
                    size = Size(eyeW * 1.16f, eyeH * 0.96f)
                )
            }
            EyeshadowPattern.GLITTER_GLAM -> {
                drawOval(
                    brush = Brush.radialGradient(listOf(p.copy(alpha = 0.62f * alpha), Color.Transparent), lidCenter, eyeW * 0.75f),
                    topLeft = Offset(lidCenter.x - eyeW * 0.68f, lidCenter.y - eyeH * 0.68f),
                    size = Size(eyeW * 1.36f, eyeH * 1.10f)
                )
                for (dx in listOf(-0.28f, -0.10f, 0.08f, 0.25f)) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.90f),
                        radius = face.width * 0.0075f,
                        center = Offset(lidCenter.x + dx * eyeW, lidCenter.y - eyeH * 0.16f)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawPortraitBrowLashEnhancement(
    face: Rect,
    hairColor: Color,
    style: BrowLashStyle
) {
    if (style == BrowLashStyle.NATURAL) return
    val browTint = hairColor.copy(alpha = 0.36f)
    for (side in listOf(-1f, 1f)) {
        val eyeCenter = uv(face, 0.50f + side * 0.165f, 0.405f)
        val halfW = face.width * 0.088f
        val eyeH = face.height * 0.044f

        when (style) {
            BrowLashStyle.NATURAL -> Unit
            BrowLashStyle.LAMINATED_FLUFFY -> {
                val inner = uv(face, 0.50f + side * 0.085f, 0.332f)
                for (h in 0..6) {
                    val hx = inner.x + side * h * face.width * 0.025f
                    drawLine(
                        color = browTint,
                        start = Offset(hx, inner.y + 4f),
                        end = Offset(hx + side * 3f, inner.y - face.height * 0.026f),
                        strokeWidth = 1.8f,
                        cap = StrokeCap.Round
                    )
                }
            }
            BrowLashStyle.ARCHED_SCULPTED -> {
                val arch = uv(face, 0.50f + side * 0.185f, 0.302f)
                drawLine(
                    brush = Brush.linearGradient(
                        listOf(Color.Transparent, Color(0xFFFFF4E3).copy(alpha = 0.45f), Color.Transparent),
                        start = uv(face, 0.50f + side * 0.10f, 0.34f),
                        end = uv(face, 0.50f + side * 0.26f, 0.34f)
                    ),
                    start = uv(face, 0.50f + side * 0.10f, 0.34f),
                    end = Offset(arch.x, arch.y + face.height * 0.024f),
                    strokeWidth = face.height * 0.014f,
                    cap = StrokeCap.Round
                )
            }
            BrowLashStyle.DOLL_VOLUME, BrowLashStyle.FOX_EYE_LIFT, BrowLashStyle.WISPY_MANGA -> {
                val count = if (style == BrowLashStyle.DOLL_VOLUME) 9 else 7
                for (i in 0 until count) {
                    val t = -0.60f + (i.toFloat() / (count - 1)) * 1.42f
                    val bx = eyeCenter.x + side * t * halfW * 0.90f
                    val by = eyeCenter.y - eyeH * (1f - t * t) * 0.78f
                    val lenFactor = when {
                        style == BrowLashStyle.FOX_EYE_LIFT && t > 0f -> 1.75f
                        style == BrowLashStyle.WISPY_MANGA && i % 2 == 0 -> 1.80f
                        style == BrowLashStyle.DOLL_VOLUME -> 1.45f
                        else -> 0.95f
                    }
                    drawLine(
                        color = Color(0xFF161013).copy(alpha = 0.85f),
                        start = Offset(bx, by),
                        end = Offset(bx + side * face.width * 0.022f * lenFactor, by - face.height * 0.028f * lenFactor),
                        strokeWidth = 1.9f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawPortraitLips(face: Rect, model: WomanModel, makeup: MakeupState) {
    if (makeup.lipPattern == LipPattern.NONE) return
    val mainColor = makeup.lipColor
    val alpha = makeup.lipIntensity * 0.72f

    val upperLipColor = mainColor.copy(
        red = (mainColor.red * 0.84f).coerceIn(0f, 1f),
        green = (mainColor.green * 0.84f).coerceIn(0f, 1f),
        blue = (mainColor.blue * 0.84f).coerceIn(0f, 1f),
        alpha = alpha
    )
    val lowerLipColor = mainColor.copy(alpha = alpha)

    val leftCorner = uv(face, 0.398f, 0.690f)
    val rightCorner = uv(face, 0.602f, 0.690f)
    val leftPeak = uv(face, 0.464f, 0.662f)
    val cupidDip = uv(face, 0.50f, 0.674f)
    val rightPeak = uv(face, 0.536f, 0.662f)
    val mouthCenter = uv(face, 0.50f, 0.692f)
    val lowerCenter = uv(face, 0.50f, 0.734f)

    val upperLipPath = Path().apply {
        moveTo(leftCorner.x, leftCorner.y)
        quadraticTo(uv(face, 0.435f, 0.670f).x, uv(face, 0.435f, 0.670f).y, leftPeak.x, leftPeak.y)
        lineTo(cupidDip.x, cupidDip.y)
        lineTo(rightPeak.x, rightPeak.y)
        quadraticTo(uv(face, 0.565f, 0.670f).x, uv(face, 0.565f, 0.670f).y, rightCorner.x, rightCorner.y)
        quadraticTo(uv(face, 0.54f, 0.690f).x, uv(face, 0.54f, 0.690f).y, mouthCenter.x, mouthCenter.y)
        quadraticTo(uv(face, 0.46f, 0.690f).x, uv(face, 0.46f, 0.690f).y, leftCorner.x, leftCorner.y)
        close()
    }

    val lowerLipPath = Path().apply {
        moveTo(leftCorner.x, leftCorner.y)
        quadraticTo(uv(face, 0.46f, 0.690f).x, uv(face, 0.46f, 0.690f).y, mouthCenter.x, mouthCenter.y)
        quadraticTo(uv(face, 0.54f, 0.690f).x, uv(face, 0.54f, 0.690f).y, rightCorner.x, rightCorner.y)
        cubicTo(
            uv(face, 0.565f, 0.728f).x, uv(face, 0.565f, 0.728f).y,
            uv(face, 0.53f, 0.734f).x, lowerCenter.y,
            lowerCenter.x, lowerCenter.y
        )
        cubicTo(
            uv(face, 0.47f, 0.734f).x, lowerCenter.y,
            uv(face, 0.435f, 0.728f).x, uv(face, 0.435f, 0.728f).y,
            leftCorner.x, leftCorner.y
        )
        close()
    }

    when (makeup.lipPattern) {
        LipPattern.NONE -> Unit
        LipPattern.VELVET_MATTE -> {
            drawPath(upperLipPath, upperLipColor)
            drawPath(lowerLipPath, lowerLipColor)
        }
        LipPattern.MIRROR_GLOSS -> {
            drawPath(upperLipPath, upperLipColor)
            drawPath(lowerLipPath, lowerLipColor)
            drawOval(
                color = Color.White.copy(alpha = 0.62f),
                topLeft = uv(face, 0.455f, 0.703f),
                size = Size(face.width * 0.09f, face.height * 0.017f)
            )
            drawCircle(Color.White.copy(alpha = 0.55f), face.width * 0.011f, uv(face, 0.53f, 0.709f))
        }
        LipPattern.OMBRE_GLAM -> {
            drawPath(upperLipPath, upperLipColor)
            drawPath(
                lowerLipPath,
                Brush.radialGradient(
                    colors = listOf(makeup.lipSecondaryColor.copy(alpha = alpha), lowerLipColor),
                    center = uv(face, 0.50f, 0.705f),
                    radius = face.width * 0.10f
                )
            )
        }
        LipPattern.LINED_90S -> {
            drawPath(upperLipPath, makeup.lipSecondaryColor.copy(alpha = alpha * 0.85f))
            drawPath(lowerLipPath, makeup.lipSecondaryColor.copy(alpha = alpha * 0.85f))
            drawPath(upperLipPath, mainColor.copy(alpha = 0.90f), style = Stroke(width = face.width * 0.013f, join = StrokeJoin.Round))
            drawPath(lowerLipPath, mainColor.copy(alpha = 0.90f), style = Stroke(width = face.width * 0.013f, join = StrokeJoin.Round))
        }
        LipPattern.BLURRED_KOREAN -> {
            drawPath(
                upperLipPath,
                Brush.radialGradient(listOf(upperLipColor, Color.Transparent), uv(face, 0.50f, 0.688f), face.width * 0.095f)
            )
            drawPath(
                lowerLipPath,
                Brush.radialGradient(listOf(lowerLipColor, Color.Transparent), uv(face, 0.50f, 0.703f), face.width * 0.095f)
            )
        }
        LipPattern.METALLIC_FOIL -> {
            val foil = Brush.horizontalGradient(
                listOf(mainColor.copy(alpha = alpha), Color.White.copy(alpha = 0.65f), mainColor.copy(alpha = alpha)),
                startX = leftCorner.x,
                endX = rightCorner.x
            )
            drawPath(upperLipPath, foil)
            drawPath(lowerLipPath, foil)
        }
    }

    // Subtle mouth parting definition so the portrait smile remains sharp
    val mouthLine = Path().apply {
        moveTo(leftCorner.x + 2f, leftCorner.y)
        quadraticTo(uv(face, 0.46f, 0.691f).x, uv(face, 0.46f, 0.691f).y, mouthCenter.x, mouthCenter.y)
        quadraticTo(uv(face, 0.54f, 0.691f).x, uv(face, 0.54f, 0.691f).y, rightCorner.x - 2f, rightCorner.y)
    }
    drawPath(mouthLine, Color(0xFF3E1319).copy(alpha = 0.45f), style = Stroke(width = 1.5f, cap = StrokeCap.Round))
}

private fun DrawScope.drawHeadAndFaceBase(face: Rect, model: WomanModel) {
    val rimColor = model.skinShadow.copy(alpha = 0.62f)

    // Ears with inner concha shadow & subtle lobe highlight
    for (side in listOf(-1f, 1f)) {
        val earLeft = face.center.x + side * face.width * 0.405f - face.width * 0.048f
        val earTop = face.top + face.height * 0.37f
        drawOval(
            color = model.skinShadow,
            topLeft = Offset(earLeft, earTop),
            size = Size(face.width * 0.096f, face.height * 0.20f)
        )
        drawOval(
            color = model.skinTone,
            topLeft = Offset(earLeft + face.width * 0.014f, earTop + face.height * 0.025f),
            size = Size(face.width * 0.068f, face.height * 0.145f)
        )
    }

    val jawWidthFactor = when (model.faceShape) {
        FaceShape.OVAL -> 0.33f
        FaceShape.HEART -> 0.25f
        FaceShape.ROUND -> 0.39f
        FaceShape.DIAMOND -> 0.29f
        FaceShape.SQUARE -> 0.38f
    }
    val cheekWidthFactor = when (model.faceShape) {
        FaceShape.DIAMOND -> 0.44f
        FaceShape.ROUND -> 0.43f
        FaceShape.SQUARE -> 0.41f
        else -> 0.40f
    }
    val chinRoundness = when (model.faceShape) {
        FaceShape.SQUARE -> 0.22f
        FaceShape.HEART -> 0.11f
        else -> 0.16f
    }

    val headPath = Path().apply {
        moveTo(face.center.x, face.top + face.height * 0.025f)
        cubicTo(
            face.center.x + face.width * cheekWidthFactor, face.top + face.height * 0.025f,
            face.center.x + face.width * cheekWidthFactor, face.top + face.height * 0.45f,
            face.center.x + face.width * jawWidthFactor, face.top + face.height * 0.69f
        )
        quadraticTo(
            face.center.x + face.width * chinRoundness, face.bottom - face.height * 0.015f,
            face.center.x, face.bottom - face.height * 0.015f
        )
        quadraticTo(
            face.center.x - face.width * chinRoundness, face.bottom - face.height * 0.015f,
            face.center.x - face.width * jawWidthFactor, face.top + face.height * 0.69f
        )
        cubicTo(
            face.center.x - face.width * cheekWidthFactor, face.top + face.height * 0.45f,
            face.center.x - face.width * cheekWidthFactor, face.top + face.height * 0.025f,
            face.center.x, face.top + face.height * 0.025f
        )
        close()
    }

    // Multi-zone 3D Sculpted Skin Lighting
    drawPath(
        path = headPath,
        brush = Brush.radialGradient(
            colors = listOf(model.skinHighlight, model.skinTone, model.skinShadow),
            center = uv(face, 0.50f, 0.40f),
            radius = face.height * 0.65f
        )
    )
    // Soft jawline & hairline ambient occlusion
    drawPath(headPath, rimColor, style = Stroke(width = 1.5f, join = StrokeJoin.Round))

    clipPath(headPath) {
        // Temple, under-cheekbone & jaw perimeter shading
        for (side in listOf(-1f, 1f)) {
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(model.skinShadow.copy(alpha = 0.30f), Color.Transparent),
                    center = uv(face, 0.50f + side * 0.34f, 0.28f),
                    radius = face.width * 0.16f
                ),
                radius = face.width * 0.16f,
                center = uv(face, 0.50f + side * 0.34f, 0.28f)
            )
            // Subtle under-cheekbone anatomical hollow
            drawLine(
                brush = Brush.linearGradient(
                    listOf(model.skinShadow.copy(alpha = 0.24f), Color.Transparent),
                    start = uv(face, 0.50f + side * 0.36f, 0.52f),
                    end = uv(face, 0.50f + side * 0.18f, 0.62f)
                ),
                start = uv(face, 0.50f + side * 0.36f, 0.52f),
                end = uv(face, 0.50f + side * 0.18f, 0.62f),
                strokeWidth = face.width * 0.06f,
                cap = StrokeCap.Round
            )
            // Soft orbital eye-socket depth under brow bone
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(model.skinShadow.copy(alpha = 0.22f), Color.Transparent),
                    center = uv(face, 0.50f + side * 0.14f, 0.38f),
                    radius = face.width * 0.11f
                ),
                radius = face.width * 0.11f,
                center = uv(face, 0.50f + side * 0.14f, 0.38f)
            )
        }
    }

    // Realistic Sculpted Nose (bridge light, side-wall shadows, nasal tip shadow, curved alae/nostrils)
    for (side in listOf(-1f, 1f)) {
        // Bridge side shadow connecting up to inner brow
        val sideBridge = Path().apply {
            moveTo(uv(face, 0.50f + side * 0.07f, 0.34f).x, uv(face, 0.50f + side * 0.07f, 0.34f).y)
            quadraticTo(
                uv(face, 0.50f + side * 0.032f, 0.45f).x, uv(face, 0.50f + side * 0.032f, 0.45f).y,
                uv(face, 0.50f + side * 0.056f, 0.555f).x, uv(face, 0.50f + side * 0.056f, 0.555f).y
            )
        }
        drawPath(
            sideBridge,
            color = model.skinShadow.copy(alpha = 0.34f),
            style = Stroke(width = face.width * 0.018f, cap = StrokeCap.Round)
        )
        // Curved nostril wing (ala)
        val nostrilWing = Path().apply {
            moveTo(uv(face, 0.50f + side * 0.022f, 0.568f).x, uv(face, 0.50f + side * 0.022f, 0.568f).y)
            quadraticTo(
                uv(face, 0.50f + side * 0.055f, 0.576f).x, uv(face, 0.50f + side * 0.055f, 0.576f).y,
                uv(face, 0.50f + side * 0.062f, 0.550f).x, uv(face, 0.50f + side * 0.062f, 0.550f).y
            )
        }
        drawPath(nostrilWing, model.skinShadow.copy(alpha = 0.65f), style = Stroke(width = 1.6f, cap = StrokeCap.Round))
        // Delicate nostril opening shadow
        drawOval(
            color = model.skinShadow.copy(alpha = 0.72f),
            topLeft = uv(face, 0.50f + side * 0.032f - 0.011f, 0.558f),
            size = Size(face.width * 0.022f, face.height * 0.016f)
        )
    }
    // Bridge & nasal tip highlight
    drawLine(
        brush = Brush.verticalGradient(
            listOf(model.skinHighlight.copy(alpha = 0.75f), Color.Transparent),
            startY = uv(face, 0.50f, 0.38f).y,
            endY = uv(face, 0.50f, 0.535f).y
        ),
        start = uv(face, 0.50f, 0.38f),
        end = uv(face, 0.50f, 0.535f),
        strokeWidth = face.width * 0.022f,
        cap = StrokeCap.Round
    )
    drawCircle(
        brush = Brush.radialGradient(
            listOf(model.skinHighlight.copy(alpha = 0.75f), Color.Transparent),
            center = uv(face, 0.50f, 0.542f),
            radius = face.width * 0.030f
        ),
        radius = face.width * 0.030f,
        center = uv(face, 0.50f, 0.542f)
    )
    // Soft shadow under nasal tip
    drawOval(
        color = model.skinShadow.copy(alpha = 0.32f),
        topLeft = uv(face, 0.47f, 0.562f),
        size = Size(face.width * 0.06f, face.height * 0.020f)
    )

    // Philtrum & Chin anatomical shading
    drawLine(
        color = model.skinShadow.copy(alpha = 0.24f),
        start = uv(face, 0.50f, 0.585f),
        end = uv(face, 0.50f, 0.642f),
        strokeWidth = face.width * 0.015f,
        cap = StrokeCap.Round
    )
    // Shadow cast under lower lip onto chin
    drawOval(
        brush = Brush.verticalGradient(listOf(model.skinShadow.copy(alpha = 0.48f), Color.Transparent)),
        topLeft = uv(face, 0.43f, 0.742f),
        size = Size(face.width * 0.14f, face.height * 0.045f)
    )
    drawCircle(
        brush = Brush.radialGradient(
            listOf(model.skinHighlight.copy(alpha = 0.48f), Color.Transparent),
            center = uv(face, 0.50f, 0.845f),
            radius = face.width * 0.085f
        ),
        radius = face.width * 0.085f,
        center = uv(face, 0.50f, 0.845f)
    )
}

private fun DrawScope.drawBaseAndContour(face: Rect, makeup: MakeupState) {
    val alpha = makeup.baseIntensity
    when (makeup.baseStyle) {
        BaseContourStyle.NONE -> Unit
        BaseContourStyle.SOFT_GLOW -> {
            for (pt in listOf(Offset(0.50f, 0.24f), Offset(0.50f, 0.44f), Offset(0.28f, 0.48f), Offset(0.72f, 0.48f))) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(makeup.highlighterColor.copy(alpha = 0.48f * alpha), Color.Transparent),
                        center = uv(face, pt.x, pt.y),
                        radius = face.width * 0.13f
                    ),
                    radius = face.width * 0.13f,
                    center = uv(face, pt.x, pt.y)
                )
            }
        }
        BaseContourStyle.SCULPTED_CONTOUR -> {
            val contourColor = Color(0xFF6B422B).copy(alpha = 0.36f * alpha)
            for (side in listOf(-1f, 1f)) {
                val start = uv(face, 0.50f + side * 0.32f, 0.49f)
                val end = uv(face, 0.50f + side * 0.17f, 0.59f)
                drawLine(
                    brush = Brush.linearGradient(listOf(contourColor, Color.Transparent), start, end),
                    start = start,
                    end = end,
                    strokeWidth = face.width * 0.085f,
                    cap = StrokeCap.Round
                )
            }
        }
        BaseContourStyle.GOLDEN_BRONZER -> {
            val bronze = Color(0xFFB36E3C).copy(alpha = 0.28f * alpha)
            for (pt in listOf(Offset(0.32f, 0.20f), Offset(0.68f, 0.20f), Offset(0.28f, 0.53f), Offset(0.72f, 0.53f))) {
                drawCircle(
                    brush = Brush.radialGradient(listOf(bronze, Color.Transparent), uv(face, pt.x, pt.y), face.width * 0.15f),
                    radius = face.width * 0.15f,
                    center = uv(face, pt.x, pt.y)
                )
            }
        }
        BaseContourStyle.ARTISTIC_FRECKLES -> {
            val freckleColor = Color(0xFF6E432C).copy(alpha = 0.58f * alpha)
            val dots = listOf(
                Offset(0.29f, 0.50f), Offset(0.33f, 0.52f), Offset(0.36f, 0.49f), Offset(0.39f, 0.53f),
                Offset(0.46f, 0.51f), Offset(0.50f, 0.525f), Offset(0.54f, 0.505f),
                Offset(0.61f, 0.53f), Offset(0.64f, 0.49f), Offset(0.67f, 0.52f), Offset(0.71f, 0.50f)
            )
            dots.forEachIndexed { idx, p ->
                drawCircle(
                    color = freckleColor,
                    radius = if (idx % 2 == 0) face.width * 0.008f else face.width * 0.006f,
                    center = uv(face, p.x, p.y)
                )
            }
        }
        BaseContourStyle.GLASS_SKIN -> {
            for (pt in listOf(
                Offset(0.50f, 0.22f), Offset(0.26f, 0.47f), Offset(0.74f, 0.47f),
                Offset(0.50f, 0.54f), Offset(0.50f, 0.84f)
            )) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color.White.copy(alpha = 0.52f * alpha), makeup.highlighterColor.copy(alpha = 0.24f), Color.Transparent),
                        center = uv(face, pt.x, pt.y),
                        radius = face.width * 0.14f
                    ),
                    radius = face.width * 0.14f,
                    center = uv(face, pt.x, pt.y)
                )
            }
        }
        BaseContourStyle.EDITORIAL_BAKING -> {
            for (side in listOf(-1f, 1f)) {
                val c = uv(face, 0.50f + side * 0.17f, 0.48f)
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFFFFF6EB).copy(alpha = 0.48f * alpha), Color.Transparent),
                        center = c,
                        radius = face.width * 0.13f
                    ),
                    radius = face.width * 0.13f,
                    center = c
                )
            }
        }
    }
}

private fun DrawScope.drawBlush(face: Rect, makeup: MakeupState) {
    if (makeup.blushPattern == BlushPattern.NONE) return
    val baseColor = makeup.blushColor
    val intensity = makeup.blushIntensity

    when (makeup.blushPattern) {
        BlushPattern.NONE -> Unit
        BlushPattern.APPLE_CIRCULAR -> {
            for (side in listOf(-1f, 1f)) {
                val c = uv(face, 0.50f + side * 0.19f, 0.54f)
                val r = face.width * 0.13f
                drawCircle(
                    brush = Brush.radialGradient(listOf(baseColor.copy(alpha = 0.52f * intensity), Color.Transparent), c, r),
                    radius = r,
                    center = c
                )
            }
        }
        BlushPattern.LIFTED_TEMPLE -> {
            for (side in listOf(-1f, 1f)) {
                val c = uv(face, 0.50f + side * 0.24f, 0.48f)
                val r = face.width * 0.14f
                drawCircle(
                    brush = Brush.radialGradient(listOf(baseColor.copy(alpha = 0.52f * intensity), Color.Transparent), c, r),
                    radius = r,
                    center = c
                )
            }
        }
        BlushPattern.SUNKISSED_BRIDGE -> {
            for (pt in listOf(Offset(0.32f, 0.52f), Offset(0.50f, 0.50f), Offset(0.68f, 0.52f))) {
                val c = uv(face, pt.x, pt.y)
                val r = face.width * 0.135f
                drawCircle(
                    brush = Brush.radialGradient(listOf(baseColor.copy(alpha = 0.45f * intensity), Color.Transparent), c, r),
                    radius = r,
                    center = c
                )
            }
        }
        BlushPattern.DRAPING_EDITORIAL -> {
            for (side in listOf(-1f, 1f)) {
                val start = uv(face, 0.50f + side * 0.18f, 0.54f)
                val end = uv(face, 0.50f + side * 0.28f, 0.37f)
                drawLine(
                    brush = Brush.linearGradient(
                        listOf(baseColor.copy(alpha = 0.55f * intensity), baseColor.copy(alpha = 0.25f * intensity)),
                        start,
                        end
                    ),
                    start = start,
                    end = end,
                    strokeWidth = face.width * 0.12f,
                    cap = StrokeCap.Round
                )
            }
        }
        BlushPattern.DOLL_UNDER_EYE -> {
            for (side in listOf(-1f, 1f)) {
                val c = uv(face, 0.50f + side * 0.17f, 0.48f)
                val r = face.width * 0.115f
                drawCircle(
                    brush = Brush.radialGradient(listOf(baseColor.copy(alpha = 0.58f * intensity), Color.Transparent), c, r),
                    radius = r,
                    center = c
                )
            }
        }
    }
}

private fun DrawScope.drawEyeshadow(face: Rect, makeup: MakeupState) {
    if (makeup.eyeshadowPattern == EyeshadowPattern.NONE) return
    val p = makeup.eyeshadowPrimary
    val s = makeup.eyeshadowSecondary
    val alpha = makeup.eyeshadowIntensity

    for (side in listOf(-1f, 1f)) {
        val eyeCenter = uv(face, 0.50f + side * 0.165f, 0.405f)
        val eyeW = face.width * 0.17f
        val eyeH = face.height * 0.11f

        when (makeup.eyeshadowPattern) {
            EyeshadowPattern.NONE -> Unit
            EyeshadowPattern.SOFT_WASH -> {
                drawOval(
                    brush = Brush.radialGradient(listOf(p.copy(alpha = 0.65f * alpha), Color.Transparent), eyeCenter, eyeW * 0.8f),
                    topLeft = Offset(eyeCenter.x - eyeW * 0.72f, eyeCenter.y - eyeH * 0.75f),
                    size = Size(eyeW * 1.44f, eyeH * 1.25f)
                )
            }
            EyeshadowPattern.GRADIENT_SUNSET -> {
                val grad = Brush.horizontalGradient(
                    colors = if (side < 0) listOf(s.copy(alpha = 0.78f * alpha), p.copy(alpha = 0.65f * alpha))
                    else listOf(p.copy(alpha = 0.65f * alpha), s.copy(alpha = 0.78f * alpha)),
                    startX = eyeCenter.x - eyeW * 0.75f,
                    endX = eyeCenter.x + eyeW * 0.75f
                )
                drawOval(
                    brush = grad,
                    topLeft = Offset(eyeCenter.x - eyeW * 0.75f, eyeCenter.y - eyeH * 0.80f),
                    size = Size(eyeW * 1.5f, eyeH * 1.3f)
                )
            }
            EyeshadowPattern.SMOKY_HALO -> {
                drawOval(
                    brush = Brush.radialGradient(listOf(s.copy(alpha = 0.82f * alpha), Color.Transparent), eyeCenter, eyeW * 0.9f),
                    topLeft = Offset(eyeCenter.x - eyeW * 0.80f, eyeCenter.y - eyeH * 0.85f),
                    size = Size(eyeW * 1.60f, eyeH * 1.45f)
                )
                drawCircle(
                    brush = Brush.radialGradient(listOf(p.copy(alpha = 0.85f * alpha), Color.Transparent), eyeCenter, eyeW * 0.35f),
                    radius = eyeW * 0.35f,
                    center = eyeCenter
                )
            }
            EyeshadowPattern.CUT_CREASE -> {
                val creasePath = Path().apply {
                    moveTo(eyeCenter.x - eyeW * 0.72f, eyeCenter.y)
                    quadraticTo(eyeCenter.x, eyeCenter.y - eyeH * 1.25f, eyeCenter.x + eyeW * 0.72f, eyeCenter.y)
                }
                drawPath(creasePath, s.copy(alpha = 0.85f * alpha), style = Stroke(width = eyeH * 0.45f, cap = StrokeCap.Round))
                drawOval(
                    color = p.copy(alpha = 0.88f * alpha),
                    topLeft = Offset(eyeCenter.x - eyeW * 0.56f, eyeCenter.y - eyeH * 0.55f),
                    size = Size(eyeW * 1.12f, eyeH * 0.85f)
                )
            }
            EyeshadowPattern.GRAPHIC_DUO -> {
                val block = Path().apply {
                    moveTo(eyeCenter.x - eyeW * 0.65f, eyeCenter.y + eyeH * 0.1f)
                    lineTo(eyeCenter.x - eyeW * 0.45f, eyeCenter.y - eyeH * 0.75f)
                    lineTo(eyeCenter.x + eyeW * 0.75f, eyeCenter.y - eyeH * 0.65f)
                    lineTo(eyeCenter.x + eyeW * 0.65f, eyeCenter.y + eyeH * 0.15f)
                    close()
                }
                drawPath(block, p.copy(alpha = 0.82f * alpha))
                drawLine(
                    color = s.copy(alpha = 0.9f * alpha),
                    start = Offset(eyeCenter.x - eyeW * 0.48f, eyeCenter.y - eyeH * 0.76f),
                    end = Offset(eyeCenter.x + eyeW * 0.75f, eyeCenter.y - eyeH * 0.66f),
                    strokeWidth = 3f
                )
            }
            EyeshadowPattern.FOXY_WING_SMOKE -> {
                val wingStart = Offset(eyeCenter.x, eyeCenter.y)
                val wingTip = Offset(eyeCenter.x + side * eyeW * 1.05f, eyeCenter.y - eyeH * 0.85f)
                drawLine(
                    brush = Brush.linearGradient(listOf(s.copy(alpha = 0.85f * alpha), Color.Transparent), wingStart, wingTip),
                    start = wingStart,
                    end = wingTip,
                    strokeWidth = eyeH * 0.9f,
                    cap = StrokeCap.Round
                )
                drawOval(
                    brush = Brush.radialGradient(listOf(p.copy(alpha = 0.65f * alpha), Color.Transparent), eyeCenter, eyeW * 0.65f),
                    topLeft = Offset(eyeCenter.x - eyeW * 0.6f, eyeCenter.y - eyeH * 0.65f),
                    size = Size(eyeW * 1.2f, eyeH * 1.05f)
                )
            }
            EyeshadowPattern.GLITTER_GLAM -> {
                drawOval(
                    brush = Brush.radialGradient(listOf(p.copy(alpha = 0.72f * alpha), Color.Transparent), eyeCenter, eyeW * 0.8f),
                    topLeft = Offset(eyeCenter.x - eyeW * 0.72f, eyeCenter.y - eyeH * 0.75f),
                    size = Size(eyeW * 1.44f, eyeH * 1.25f)
                )
                for (dx in listOf(-0.28f, -0.10f, 0.08f, 0.25f)) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.88f),
                        radius = face.width * 0.008f,
                        center = Offset(eyeCenter.x + dx * eyeW, eyeCenter.y - eyeH * 0.26f)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawEyeliner(face: Rect, makeup: MakeupState) {
    if (makeup.eyelinerPattern == EyelinerPattern.NONE) return
    val c = makeup.eyelinerColor

    for (side in listOf(-1f, 1f)) {
        val eyeCenter = uv(face, 0.50f + side * 0.165f, 0.405f)
        val halfW = face.width * 0.088f
        val innerX = eyeCenter.x - side * halfW
        val outerX = eyeCenter.x + side * halfW
        val upperY = eyeCenter.y - face.height * 0.022f

        val baseCurve = Path().apply {
            moveTo(innerX, eyeCenter.y + 1f)
            quadraticTo(eyeCenter.x, upperY - face.height * 0.025f, outerX, eyeCenter.y)
        }
        if (makeup.eyelinerPattern != EyelinerPattern.REVERSE_CAT) {
            drawPath(baseCurve, c, style = Stroke(width = face.width * 0.015f, cap = StrokeCap.Round))
        }

        when (makeup.eyelinerPattern) {
            EyelinerPattern.NONE, EyelinerPattern.CLASSIC_THIN -> Unit
            EyelinerPattern.CAT_EYE_WING -> {
                val wing = Path().apply {
                    moveTo(outerX - side * 2f, eyeCenter.y)
                    lineTo(outerX + side * face.width * 0.06f, eyeCenter.y - face.height * 0.045f)
                    lineTo(outerX - side * face.width * 0.02f, eyeCenter.y - face.height * 0.018f)
                    close()
                }
                drawPath(wing, c)
            }
            EyelinerPattern.DOUBLE_WING -> {
                drawLine(c, Offset(outerX, eyeCenter.y), Offset(outerX + side * face.width * 0.065f, eyeCenter.y - face.height * 0.048f), face.width * 0.014f, StrokeCap.Round)
                drawLine(c, Offset(outerX - side * face.width * 0.015f, eyeCenter.y - face.height * 0.032f), Offset(outerX + side * face.width * 0.055f, eyeCenter.y - face.height * 0.072f), face.width * 0.010f, StrokeCap.Round)
            }
            EyelinerPattern.SMOKED_LOWER -> {
                val lowerCurve = Path().apply {
                    moveTo(innerX, eyeCenter.y + 3f)
                    quadraticTo(eyeCenter.x, eyeCenter.y + face.height * 0.045f, outerX + side * face.width * 0.03f, eyeCenter.y - face.height * 0.01f)
                }
                drawPath(lowerCurve, c.copy(alpha = 0.72f), style = Stroke(width = face.width * 0.022f, cap = StrokeCap.Round))
            }
            EyelinerPattern.EDITORIAL_CONTRAST -> {
                val floatArc = Path().apply {
                    moveTo(innerX, eyeCenter.y - face.height * 0.055f)
                    quadraticTo(eyeCenter.x, eyeCenter.y - face.height * 0.095f, outerX + side * face.width * 0.04f, eyeCenter.y - face.height * 0.045f)
                }
                drawPath(floatArc, c, style = Stroke(width = face.width * 0.012f, cap = StrokeCap.Round))
            }
            EyelinerPattern.SIREN_INNER -> {
                drawLine(c, Offset(innerX + side * 2f, eyeCenter.y), Offset(innerX - side * face.width * 0.025f, eyeCenter.y + face.height * 0.014f), face.width * 0.012f, StrokeCap.Round)
                drawLine(c, Offset(outerX, eyeCenter.y), Offset(outerX + side * face.width * 0.058f, eyeCenter.y - face.height * 0.04f), face.width * 0.015f, StrokeCap.Round)
            }
            EyelinerPattern.REVERSE_CAT -> {
                val lowerWing = Path().apply {
                    moveTo(innerX, eyeCenter.y + face.height * 0.025f)
                    quadraticTo(eyeCenter.x, eyeCenter.y + face.height * 0.052f, outerX + side * face.width * 0.055f, eyeCenter.y - face.height * 0.025f)
                }
                drawPath(lowerWing, c, style = Stroke(width = face.width * 0.018f, cap = StrokeCap.Round))
            }
        }
    }
}

private fun DrawScope.drawEyesAndLashes(
    face: Rect,
    model: WomanModel,
    lashStyle: BrowLashStyle
) {
    for (side in listOf(-1f, 1f)) {
        val eyeCenter = uv(face, 0.50f + side * 0.165f, 0.405f)
        val halfW = face.width * 0.088f
        val eyeH = face.height * 0.046f
        val innerX = eyeCenter.x - side * halfW
        val outerX = eyeCenter.x + side * halfW

        // Upper eyelid crease fold
        val crease = Path().apply {
            moveTo(innerX + side * halfW * 0.08f, eyeCenter.y - eyeH * 0.90f)
            quadraticTo(eyeCenter.x, eyeCenter.y - eyeH * 1.78f, outerX - side * halfW * 0.05f, eyeCenter.y - eyeH * 1.05f)
        }
        drawPath(crease, model.skinShadow.copy(alpha = 0.62f), style = Stroke(width = 1.6f, cap = StrokeCap.Round))

        // Asymmetric realistic almond eye aperture
        val eyeShape = Path().apply {
            moveTo(innerX, eyeCenter.y + eyeH * 0.12f)
            cubicTo(
                innerX + side * halfW * 0.55f, eyeCenter.y - eyeH * 1.08f,
                outerX - side * halfW * 0.35f, eyeCenter.y - eyeH * 1.05f,
                outerX, eyeCenter.y - eyeH * 0.10f
            )
            cubicTo(
                outerX - side * halfW * 0.40f, eyeCenter.y + eyeH * 0.86f,
                innerX + side * halfW * 0.50f, eyeCenter.y + eyeH * 0.92f,
                innerX, eyeCenter.y + eyeH * 0.12f
            )
            close()
        }

        // Clip sclera + iris + upper lash shadow INSIDE the eye aperture!
        clipPath(eyeShape) {
            drawPath(
                eyeShape,
                Brush.verticalGradient(
                    listOf(Color(0xFFDAD0D5), Color(0xFFFDFBF9), Color(0xFFF6F1F3)),
                    startY = eyeCenter.y - eyeH,
                    endY = eyeCenter.y + eyeH
                )
            )

            // Pink inner tear duct (caruncle)
            drawCircle(
                color = Color(0xFFD99595).copy(alpha = 0.78f),
                radius = face.width * 0.015f,
                center = Offset(innerX + side * 2f, eyeCenter.y + eyeH * 0.08f)
            )

            // Iris positioned slightly tucked under upper eyelid (lifelike gaze!)
            val irisCenter = Offset(eyeCenter.x, eyeCenter.y - eyeH * 0.04f)
            val irisR = face.width * 0.043f
            val limbalColor = model.eyeColor.copy(
                red = (model.eyeColor.red * 0.42f).coerceIn(0f, 1f),
                green = (model.eyeColor.green * 0.42f).coerceIn(0f, 1f),
                blue = (model.eyeColor.blue * 0.42f).coerceIn(0f, 1f)
            )
            val innerIris = model.eyeColor.copy(
                red = (model.eyeColor.red + (1f - model.eyeColor.red) * 0.28f).coerceIn(0f, 1f),
                green = (model.eyeColor.green + (1f - model.eyeColor.green) * 0.28f).coerceIn(0f, 1f),
                blue = (model.eyeColor.blue + (1f - model.eyeColor.blue) * 0.28f).coerceIn(0f, 1f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(innerIris, model.eyeColor, limbalColor),
                    center = irisCenter,
                    radius = irisR
                ),
                radius = irisR,
                center = irisCenter
            )
            drawCircle(color = Color(0xFF120E11), radius = irisR * 0.42f, center = irisCenter)

            // Upper eyelid cast shadow across top of eye & iris
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color(0x551F141A), Color.Transparent),
                    startY = eyeCenter.y - eyeH,
                    endY = eyeCenter.y - eyeH * 0.05f
                ),
                topLeft = Offset(eyeCenter.x - halfW, eyeCenter.y - eyeH),
                size = Size(halfW * 2f, eyeH)
            )

            // Crisp studio catchlights
            drawCircle(
                color = Color.White.copy(alpha = 0.94f),
                radius = irisR * 0.23f,
                center = Offset(irisCenter.x - irisR * 0.34f, irisCenter.y - irisR * 0.30f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.55f),
                radius = irisR * 0.11f,
                center = Offset(irisCenter.x + irisR * 0.32f, irisCenter.y + irisR * 0.26f)
            )
        }

        // Upper lash line
        val lashStroke = when (lashStyle) {
            BrowLashStyle.DOLL_VOLUME -> face.width * 0.019f
            BrowLashStyle.FOX_EYE_LIFT, BrowLashStyle.WISPY_MANGA -> face.width * 0.015f
            else -> face.width * 0.012f
        }
        val upperLash = Path().apply {
            moveTo(innerX, eyeCenter.y + eyeH * 0.12f)
            cubicTo(
                innerX + side * halfW * 0.55f, eyeCenter.y - eyeH * 1.08f,
                outerX - side * halfW * 0.35f, eyeCenter.y - eyeH * 1.05f,
                outerX, eyeCenter.y - eyeH * 0.10f
            )
        }
        drawPath(upperLash, Color(0xFF181215), style = Stroke(width = lashStroke, cap = StrokeCap.Round))

        // Delicate lower waterline & lash shadow
        val lowerRim = Path().apply {
            moveTo(innerX + side * halfW * 0.12f, eyeCenter.y + eyeH * 0.18f)
            cubicTo(
                innerX + side * halfW * 0.52f, eyeCenter.y + eyeH * 0.90f,
                outerX - side * halfW * 0.38f, eyeCenter.y + eyeH * 0.84f,
                outerX - side * halfW * 0.04f, eyeCenter.y - eyeH * 0.05f
            )
        }
        drawPath(lowerRim, model.skinShadow.copy(alpha = 0.65f), style = Stroke(width = 1.4f, cap = StrokeCap.Round))

        // Individual curved eyelashes
        val lashCount = when (lashStyle) {
            BrowLashStyle.DOLL_VOLUME -> 8
            BrowLashStyle.WISPY_MANGA -> 7
            BrowLashStyle.FOX_EYE_LIFT -> 7
            else -> 5
        }
        for (i in 0 until lashCount) {
            val t = -0.65f + (i.toFloat() / (lashCount - 1)) * 1.38f
            val bx = eyeCenter.x + side * t * halfW * 0.88f
            val by = eyeCenter.y - eyeH * (1f - t * t) * 0.72f
            val lenFactor = when {
                lashStyle == BrowLashStyle.FOX_EYE_LIFT && t > 0f -> 1.55f
                lashStyle == BrowLashStyle.WISPY_MANGA && i % 2 == 0 -> 1.6f
                lashStyle == BrowLashStyle.DOLL_VOLUME -> 1.35f
                else -> 0.85f
            }
            drawLine(
                color = Color(0xFF1A1417),
                start = Offset(bx, by),
                end = Offset(bx + side * face.width * 0.019f * lenFactor, by - face.height * 0.026f * lenFactor),
                strokeWidth = 1.6f,
                cap = StrokeCap.Round
            )
        }
    }
}

private fun DrawScope.drawBrows(face: Rect, hairColor: Color, style: BrowLashStyle) {
    val browColor = hairColor.copy(alpha = 0.88f)
    val softColor = hairColor.copy(alpha = 0.52f)
    val baseThick = when (style) {
        BrowLashStyle.LAMINATED_FLUFFY -> face.height * 0.028f
        BrowLashStyle.ARCHED_SCULPTED -> face.height * 0.023f
        else -> face.height * 0.020f
    }

    for (side in listOf(-1f, 1f)) {
        val archPeakY = if (style == BrowLashStyle.ARCHED_SCULPTED) 0.292f else 0.304f
        val inner = uv(face, 0.50f + side * 0.08f, 0.335f)
        val arch = uv(face, 0.50f + side * 0.185f, archPeakY)
        val tail = uv(face, 0.50f + side * 0.275f, 0.340f)

        // Tapered sculpted brow polygon (natural thick head -> crisp arch -> razor tail)
        val browShape = Path().apply {
            moveTo(inner.x, inner.y + baseThick * 0.45f)
            quadraticTo(arch.x, arch.y + baseThick * 0.35f, tail.x, tail.y)
            quadraticTo(arch.x, arch.y - baseThick * 0.65f, inner.x, inner.y - baseThick * 0.55f)
            close()
        }
        drawPath(
            browShape,
            Brush.horizontalGradient(
                colors = if (side < 0) listOf(browColor, softColor) else listOf(softColor, browColor),
                startX = minOf(inner.x, tail.x),
                endX = maxOf(inner.x, tail.x)
            )
        )

        // Micro hair-by-hair strokes at inner brow
        for (h in 0..4) {
            val hx = inner.x + side * h * face.width * 0.018f
            drawLine(
                color = browColor,
                start = Offset(hx, inner.y + 3f),
                end = Offset(hx + side * 1.5f, inner.y - baseThick * 1.15f),
                strokeWidth = 1.4f,
                cap = StrokeCap.Round
            )
        }
    }
}

private fun DrawScope.drawLips(face: Rect, model: WomanModel, makeup: MakeupState) {
    val isNatural = makeup.lipPattern == LipPattern.NONE
    val mainColor = if (isNatural) model.naturalLipColor else makeup.lipColor
    val alpha = if (isNatural) 0.90f else makeup.lipIntensity

    // Upper lip darker for realistic downward-angled lighting
    val upperLipColor = mainColor.copy(
        red = (mainColor.red * 0.84f).coerceIn(0f, 1f),
        green = (mainColor.green * 0.84f).coerceIn(0f, 1f),
        blue = (mainColor.blue * 0.84f).coerceIn(0f, 1f),
        alpha = alpha
    )
    val lowerLipColor = mainColor.copy(alpha = alpha)

    val leftCorner = uv(face, 0.392f, 0.688f)
    val rightCorner = uv(face, 0.608f, 0.688f)
    val leftPeak = uv(face, 0.462f, 0.654f)
    val cupidDip = uv(face, 0.50f, 0.669f)
    val rightPeak = uv(face, 0.538f, 0.654f)
    val mouthCenter = uv(face, 0.50f, 0.692f)
    val lowerCenter = uv(face, 0.50f, 0.742f)

    val upperLipPath = Path().apply {
        moveTo(leftCorner.x, leftCorner.y)
        quadraticTo(uv(face, 0.43f, 0.665f).x, uv(face, 0.43f, 0.665f).y, leftPeak.x, leftPeak.y)
        lineTo(cupidDip.x, cupidDip.y)
        lineTo(rightPeak.x, rightPeak.y)
        quadraticTo(uv(face, 0.57f, 0.665f).x, uv(face, 0.57f, 0.665f).y, rightCorner.x, rightCorner.y)
        quadraticTo(uv(face, 0.54f, 0.688f).x, uv(face, 0.54f, 0.688f).y, mouthCenter.x, mouthCenter.y)
        quadraticTo(uv(face, 0.46f, 0.688f).x, uv(face, 0.46f, 0.688f).y, leftCorner.x, leftCorner.y)
        close()
    }

    val lowerLipPath = Path().apply {
        moveTo(leftCorner.x, leftCorner.y)
        quadraticTo(uv(face, 0.46f, 0.688f).x, uv(face, 0.46f, 0.688f).y, mouthCenter.x, mouthCenter.y)
        quadraticTo(uv(face, 0.54f, 0.688f).x, uv(face, 0.54f, 0.688f).y, rightCorner.x, rightCorner.y)
        cubicTo(
            uv(face, 0.57f, 0.735f).x, uv(face, 0.57f, 0.735f).y,
            uv(face, 0.53f, 0.742f).x, lowerCenter.y,
            lowerCenter.x, lowerCenter.y
        )
        cubicTo(
            uv(face, 0.47f, 0.742f).x, lowerCenter.y,
            uv(face, 0.43f, 0.735f).x, uv(face, 0.43f, 0.735f).y,
            leftCorner.x, leftCorner.y
        )
        close()
    }

    when (makeup.lipPattern) {
        LipPattern.NONE, LipPattern.VELVET_MATTE -> {
            drawPath(upperLipPath, upperLipColor)
            drawPath(lowerLipPath, lowerLipColor)
            drawOval(
                color = Color.White.copy(alpha = 0.18f),
                topLeft = uv(face, 0.455f, 0.708f),
                size = Size(face.width * 0.09f, face.height * 0.018f)
            )
        }
        LipPattern.MIRROR_GLOSS -> {
            drawPath(upperLipPath, upperLipColor)
            drawPath(lowerLipPath, lowerLipColor)
            drawOval(
                color = Color.White.copy(alpha = 0.68f),
                topLeft = uv(face, 0.455f, 0.706f),
                size = Size(face.width * 0.09f, face.height * 0.020f)
            )
            drawCircle(Color.White.copy(alpha = 0.58f), face.width * 0.012f, uv(face, 0.53f, 0.712f))
        }
        LipPattern.OMBRE_GLAM -> {
            drawPath(upperLipPath, upperLipColor)
            drawPath(
                lowerLipPath,
                Brush.radialGradient(
                    colors = listOf(makeup.lipSecondaryColor.copy(alpha = alpha), lowerLipColor),
                    center = uv(face, 0.50f, 0.708f),
                    radius = face.width * 0.11f
                )
            )
        }
        LipPattern.LINED_90S -> {
            drawPath(upperLipPath, makeup.lipSecondaryColor.copy(alpha = alpha))
            drawPath(lowerLipPath, makeup.lipSecondaryColor.copy(alpha = alpha))
            drawPath(upperLipPath, mainColor.copy(alpha = 0.95f), style = Stroke(width = face.width * 0.015f, join = StrokeJoin.Round))
            drawPath(lowerLipPath, mainColor.copy(alpha = 0.95f), style = Stroke(width = face.width * 0.015f, join = StrokeJoin.Round))
        }
        LipPattern.BLURRED_KOREAN -> {
            drawPath(
                upperLipPath,
                Brush.radialGradient(listOf(upperLipColor, model.naturalLipColor.copy(alpha = 0.45f)), uv(face, 0.50f, 0.685f), face.width * 0.10f)
            )
            drawPath(
                lowerLipPath,
                Brush.radialGradient(listOf(lowerLipColor, model.naturalLipColor.copy(alpha = 0.45f)), uv(face, 0.50f, 0.705f), face.width * 0.10f)
            )
        }
        LipPattern.METALLIC_FOIL -> {
            val foil = Brush.horizontalGradient(
                listOf(mainColor.copy(alpha = alpha), Color.White.copy(alpha = 0.85f), mainColor.copy(alpha = alpha)),
                startX = leftCorner.x,
                endX = rightCorner.x
            )
            drawPath(upperLipPath, foil)
            drawPath(lowerLipPath, foil)
        }
    }

    // Natural curved mouth parting line + soft corner dimples
    val mouthLine = Path().apply {
        moveTo(leftCorner.x + 2f, leftCorner.y)
        quadraticTo(uv(face, 0.46f, 0.690f).x, uv(face, 0.46f, 0.690f).y, mouthCenter.x, mouthCenter.y)
        quadraticTo(uv(face, 0.54f, 0.690f).x, uv(face, 0.54f, 0.690f).y, rightCorner.x - 2f, rightCorner.y)
    }
    drawPath(mouthLine, Color(0xFF3E1319).copy(alpha = 0.68f), style = Stroke(width = 1.8f, cap = StrokeCap.Round))
    drawCircle(model.skinShadow.copy(alpha = 0.55f), face.width * 0.009f, leftCorner)
    drawCircle(model.skinShadow.copy(alpha = 0.55f), face.width * 0.009f, rightCorner)
}

private fun DrawScope.drawFaceGems(face: Rect, style: FaceGemStyle, metal: JewelryMetal) {
    when (style) {
        FaceGemStyle.NONE -> Unit
        FaceGemStyle.INNER_PEARLS -> {
            for (side in listOf(-1f, 1f)) {
                drawCircle(Color(0xFFFDF8F5), radius = face.width * 0.018f, center = uv(face, 0.50f + side * 0.075f, 0.412f))
            }
        }
        FaceGemStyle.HALO_CRYSTALS -> {
            val offsets = listOf(Offset(0.11f, 0.36f), Offset(0.165f, 0.34f), Offset(0.22f, 0.355f))
            for (side in listOf(-1f, 1f)) {
                offsets.forEach { p ->
                    drawCircle(Color(0xFFEAF6FF), radius = face.width * 0.013f, center = uv(face, 0.50f + side * p.x, p.y))
                }
            }
        }
        FaceGemStyle.STAR_DUST -> {
            val spots = listOf(Offset(0.24f, 0.45f), Offset(0.27f, 0.48f), Offset(0.76f, 0.45f), Offset(0.73f, 0.48f))
            spots.forEach { pt ->
                val c = uv(face, pt.x, pt.y)
                val r = face.width * 0.017f
                drawLine(metal.highlight, Offset(c.x - r, c.y), Offset(c.x + r, c.y), strokeWidth = 2.2f)
                drawLine(metal.highlight, Offset(c.x, c.y - r), Offset(c.x, c.y + r), strokeWidth = 2.2f)
            }
        }
        FaceGemStyle.GLITTER_TEAR -> {
            for (side in listOf(-1f, 1f)) {
                for (i in 0..3) {
                    drawCircle(
                        color = Color(0xFFFFF4E3).copy(alpha = 0.85f),
                        radius = face.width * (0.014f - i * 0.002f),
                        center = uv(face, 0.50f + side * 0.165f, 0.445f + i * 0.025f)
                    )
                }
            }
        }
        FaceGemStyle.BUTTERFLY_GLAM -> {
            for (side in listOf(-1f, 1f)) {
                val c = uv(face, 0.50f + side * 0.28f, 0.38f)
                drawCircle(Color(0xFFEAF6FF), radius = face.width * 0.016f, center = c)
                drawCircle(metal.highlight, radius = face.width * 0.011f, center = Offset(c.x + side * face.width * 0.025f, c.y - face.height * 0.02f))
                drawCircle(metal.highlight, radius = face.width * 0.011f, center = Offset(c.x + side * face.width * 0.022f, c.y + face.height * 0.02f))
            }
        }
        FaceGemStyle.UNDER_EYE_STARS -> {
            for (side in listOf(-1f, 1f)) {
                for (dx in listOf(0.12f, 0.165f, 0.21f)) {
                    drawCircle(
                        color = metal.highlight,
                        radius = face.width * 0.011f,
                        center = uv(face, 0.50f + side * dx, 0.455f)
                    )
                }
            }
        }
    }
}

internal fun DrawScope.drawPaintedStrokes(faceRect: Rect, strokes: List<PaintedBrushStroke>) {
    strokes.forEach { stroke ->
        drawSinglePaintedStroke(faceRect, stroke)
    }
}

internal fun DrawScope.drawSinglePaintedStroke(faceRect: Rect, stroke: PaintedBrushStroke) {
    if (stroke.points.isEmpty()) return
    val baseRadius = (stroke.brush.bristleRadiusDp / 140f) * faceRect.width
    val softAlpha = stroke.alpha * (1f - stroke.brush.softness * 0.45f)

    if (stroke.points.size == 1) {
        val c = uv(faceRect, stroke.points.first().x, stroke.points.first().y)
        drawCircle(
            brush = Brush.radialGradient(listOf(stroke.color.copy(alpha = softAlpha), Color.Transparent), c, baseRadius * 1.3f),
            radius = baseRadius * 1.3f,
            center = c
        )
        return
    }

    val path = Path().apply {
        val first = uv(faceRect, stroke.points.first().x, stroke.points.first().y)
        moveTo(first.x, first.y)
        for (i in 1 until stroke.points.size) {
            val pt = uv(faceRect, stroke.points[i].x, stroke.points[i].y)
            lineTo(pt.x, pt.y)
        }
    }
    drawPath(
        path = path,
        color = stroke.color.copy(alpha = softAlpha * 0.65f),
        style = Stroke(width = baseRadius * 1.45f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}
