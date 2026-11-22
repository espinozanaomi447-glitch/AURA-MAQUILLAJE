package com.example.ui.canvas

import android.graphics.BitmapFactory
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.model.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun ModelCanvas(
    model: WomanModel,
    hairStyle: HairStyle,
    hairColor: Color,
    makeup: MakeupState,
    wardrobe: WardrobeState,
    zoomMode: ViewZoomMode,
    activeBrush: BrushTool,
    activePigment: Color,
    isInteractivePaintingEnabled: Boolean,
    showGuideOverlay: Boolean,
    guidePoints: List<Offset>,
    guideMotion: BrushMotionPattern?,
    modifier: Modifier = Modifier,
    portraitPoseMode: PortraitPoseMode = model.defaultPortraitPose,
    onStrokeCompleted: (List<Offset>, BrushTool) -> Unit = { _, _ -> },
    onTapPoint: (Offset, BrushTool) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val assets = model.visualAssets

    val (portraitRes, portraitLandmarks) = remember(assets, portraitPoseMode, wardrobe.headAccessoryStyle) {
        if (assets != null) {
            resolvePortraitSpec(assets, portraitPoseMode, wardrobe.headAccessoryStyle)
        } else {
            null to null
        }
    }

    val portraitBitmap: ImageBitmap? = remember(portraitRes) {
        portraitRes?.let { resId ->
            runCatching { BitmapFactory.decodeResource(context.resources, resId)?.asImageBitmap() }.getOrNull()
        }
    }
    val bodyBitmap: ImageBitmap? = remember(assets?.bodyRes) {
        assets?.bodyRes?.let { resId ->
            runCatching { BitmapFactory.decodeResource(context.resources, resId)?.asImageBitmap() }.getOrNull()
        }
    }

    val useIllustratedPortrait = zoomMode == ViewZoomMode.FACE_CLOSEUP &&
        portraitBitmap != null &&
        portraitLandmarks != null
    val useIllustratedBody = zoomMode == ViewZoomMode.FULL_BODY &&
        bodyBitmap != null

    val zoomFactor by animateFloatAsState(
        targetValue = if (zoomMode == ViewZoomMode.FACE_CLOSEUP) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.84f, stiffness = 240f),
        label = "canvasZoomFactor"
    )
    val zoomScale = 1f + zoomFactor * 1.85f // Used for vector fallback mode

    val infiniteTransition = rememberInfiniteTransition(label = "brushGuideAnim")
    val guideProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "guideProgress"
    )

    val currentDragPoints = remember { mutableStateListOf<Offset>() }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(
                    isInteractivePaintingEnabled,
                    zoomMode,
                    activeBrush,
                    activePigment,
                    portraitBitmap,
                    portraitLandmarks
                ) {
                    if (!isInteractivePaintingEnabled) return@pointerInput
                    detectDragGestures(
                        onDragStart = { startOffset ->
                            currentDragPoints.clear()
                            val uv = resolveInputToFaceUv(
                                pos = startOffset,
                                canvasSize = Size(size.width.toFloat(), size.height.toFloat()),
                                portraitBitmap = if (useIllustratedPortrait) portraitBitmap else null,
                                portraitLandmarks = if (useIllustratedPortrait) portraitLandmarks else null,
                                zoomScale = zoomScale,
                                zoomFactor = zoomFactor
                            )
                            if (uv != null) currentDragPoints.add(uv)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val uv = resolveInputToFaceUv(
                                pos = change.position,
                                canvasSize = Size(size.width.toFloat(), size.height.toFloat()),
                                portraitBitmap = if (useIllustratedPortrait) portraitBitmap else null,
                                portraitLandmarks = if (useIllustratedPortrait) portraitLandmarks else null,
                                zoomScale = zoomScale,
                                zoomFactor = zoomFactor
                            )
                            if (uv != null) currentDragPoints.add(uv)
                        },
                        onDragEnd = {
                            if (currentDragPoints.size >= 2) {
                                onStrokeCompleted(currentDragPoints.toList(), activeBrush)
                            } else if (currentDragPoints.size == 1) {
                                onTapPoint(currentDragPoints.first(), activeBrush)
                            }
                            currentDragPoints.clear()
                        },
                        onDragCancel = {
                            currentDragPoints.clear()
                        }
                    )
                }
                .pointerInput(
                    isInteractivePaintingEnabled,
                    zoomMode,
                    activeBrush,
                    activePigment,
                    portraitBitmap,
                    portraitLandmarks
                ) {
                    if (!isInteractivePaintingEnabled) return@pointerInput
                    detectTapGestures { tapOffset ->
                        val uv = resolveInputToFaceUv(
                            pos = tapOffset,
                            canvasSize = Size(size.width.toFloat(), size.height.toFloat()),
                            portraitBitmap = if (useIllustratedPortrait) portraitBitmap else null,
                            portraitLandmarks = if (useIllustratedPortrait) portraitLandmarks else null,
                            zoomScale = zoomScale,
                            zoomFactor = zoomFactor
                        )
                        if (uv != null) {
                            onTapPoint(uv, activeBrush)
                        }
                    }
                }
        ) {
            when {
                useIllustratedPortrait -> {
                    val bmp = portraitBitmap!!
                    val landmarks = portraitLandmarks!!
                    val portraitAspect = bmp.width.toFloat() / bmp.height.toFloat()
                    val portraitRect = computePortraitRect(size, portraitAspect)
                    val faceRect = computePortraitFaceRect(portraitRect, landmarks)

                    // Soft studio rim glow behind portrait
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x38FFF1E2), Color.Transparent),
                            center = portraitRect.center,
                            radius = portraitRect.width * 0.65f
                        ),
                        radius = portraitRect.width * 0.65f,
                        center = portraitRect.center
                    )

                    drawImage(
                        image = bmp,
                        srcOffset = IntOffset.Zero,
                        srcSize = IntSize(bmp.width, bmp.height),
                        dstOffset = IntOffset(portraitRect.left.roundToInt(), portraitRect.top.roundToInt()),
                        dstSize = IntSize(portraitRect.width.roundToInt().coerceAtLeast(1), portraitRect.height.roundToInt().coerceAtLeast(1)),
                        filterQuality = FilterQuality.High
                    )

                    // Subtle necklace on visible collarbones when portrait extends low enough
                    if (wardrobe.necklaceStyle != NecklaceStyle.NONE &&
                        portraitRect.bottom > faceRect.bottom + faceRect.height * 0.14f
                    ) {
                        drawNecklace(
                            fig = Rect(portraitRect.left, portraitRect.top, portraitRect.right, portraitRect.bottom * 1.15f),
                            face = faceRect,
                            style = wardrobe.necklaceStyle,
                            metal = wardrobe.jewelryMetal
                        )
                    }

                    // Landmark-aligned interactive makeup overlays
                    drawRealisticPortraitMakeupOverlay(
                        face = faceRect,
                        model = model,
                        hairColor = hairColor,
                        makeup = makeup,
                        metal = wardrobe.jewelryMetal
                    )

                    // Live user-painted finger strokes
                    drawPaintedStrokes(faceRect, makeup.paintedStrokes)
                    if (currentDragPoints.size >= 2) {
                        drawSinglePaintedStroke(
                            faceRect = faceRect,
                            stroke = PaintedBrushStroke(
                                points = currentDragPoints.toList(),
                                color = activePigment,
                                brush = activeBrush,
                                alpha = 0.65f,
                                category = MakeupCategory.EYESHADOW
                            )
                        )
                    }

                    // Extra drop/tassel earrings & crown accessories
                    if (wardrobe.earringStyle == EarringStyle.CRYSTAL_DROPS ||
                        wardrobe.earringStyle == EarringStyle.BOHO_TASSELS
                    ) {
                        drawEarrings(faceRect, wardrobe.earringStyle, wardrobe.jewelryMetal)
                    }
                    if (wardrobe.headAccessoryStyle == HeadAccessoryStyle.PEARL_TIARA ||
                        wardrobe.headAccessoryStyle == HeadAccessoryStyle.GOLD_CLIPS
                    ) {
                        drawHeadAccessory(faceRect, wardrobe.headAccessoryStyle, wardrobe.jewelryMetal)
                    }

                    // Animated brush trajectory guide on the real model face
                    if (showGuideOverlay && guidePoints.isNotEmpty()) {
                        drawBrushGuideOverlay(
                            faceRect = faceRect,
                            points = guidePoints,
                            motion = guideMotion,
                            progress = guideProgress
                        )
                    }
                }

                useIllustratedBody -> {
                    val bmp = bodyBitmap!!
                    val bodyAspect = bmp.width.toFloat() / bmp.height.toFloat()
                    val bodyRect = computeIllustratedBodyRect(size, bodyAspect)

                    // Realistic soft studio floor shadow beneath feet
                    drawOval(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x4823111D), Color(0x1823111D), Color.Transparent),
                            center = Offset(bodyRect.left + bodyRect.width * 0.40f, bodyRect.bottom + 2f),
                            radius = bodyRect.width * 0.56f
                        ),
                        topLeft = Offset(bodyRect.left - bodyRect.width * 0.08f, bodyRect.bottom - 10f),
                        size = Size(bodyRect.width * 1.02f, 28f)
                    )

                    // Full-body illustrated sprite
                    drawImage(
                        image = bmp,
                        srcOffset = IntOffset.Zero,
                        srcSize = IntSize(bmp.width, bmp.height),
                        dstOffset = IntOffset(bodyRect.left.roundToInt(), bodyRect.top.roundToInt()),
                        dstSize = IntSize(bodyRect.width.roundToInt().coerceAtLeast(1), bodyRect.height.roundToInt().coerceAtLeast(1)),
                        filterQuality = FilterQuality.High
                    )

                    // Subtle makeup preview on the full-body model's face
                    val miniFaceRect = Rect(
                        left = bodyRect.left + bodyRect.width * 0.34f,
                        top = bodyRect.top + bodyRect.height * 0.030f,
                        right = bodyRect.left + bodyRect.width * 0.67f,
                        bottom = bodyRect.top + bodyRect.height * 0.175f
                    )
                    drawRealisticPortraitMakeupOverlay(
                        face = miniFaceRect,
                        model = model,
                        hairColor = hairColor,
                        makeup = makeup,
                        metal = wardrobe.jewelryMetal
                    )

                    // Contrapposto-aligned shoes, outfits, and necklace
                    drawBodyPortraitShoes(bodyRect, wardrobe.shoeStyle, wardrobe.shoeColor, wardrobe.jewelryMetal)
                    drawBodyPortraitOutfit(bodyRect, wardrobe.outfitStyle, wardrobe.outfitColor, wardrobe.outfitAccentColor)
                    drawBodyPortraitNecklace(bodyRect, wardrobe.necklaceStyle, wardrobe.jewelryMetal)
                }

                else -> {
                    // Fallback vector pipeline if visualAssets is null or unavailable in headless tests
                    val figureRect = computeFigureRect(size)
                    val faceRect = computeFaceRect(figureRect)
                    val facePivot = Offset(faceRect.center.x, faceRect.center.y)
                    val shiftY = (size.height * 0.49f - facePivot.y) * zoomFactor

                    withTransform({
                        translate(left = 0f, top = shiftY)
                        scale(scaleX = zoomScale, scaleY = zoomScale, pivot = facePivot)
                    }) {
                        if (zoomFactor < 0.35f) {
                            drawOval(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color(0x4523111D), Color(0x1823111D), Color.Transparent),
                                    center = Offset(figureRect.center.x, figureRect.bottom + 4f),
                                    radius = figureRect.width * 0.44f
                                ),
                                topLeft = Offset(figureRect.center.x - figureRect.width * 0.38f, figureRect.bottom - 10f),
                                size = Size(figureRect.width * 0.76f, 28f)
                            )
                        }

                        drawBackHair(figureRect, faceRect, hairStyle, hairColor)
                        drawRealisticBody(figureRect, faceRect, model)
                        drawShoes(figureRect, wardrobe.shoeStyle, wardrobe.shoeColor, wardrobe.jewelryMetal)
                        drawOutfit(figureRect, wardrobe.outfitStyle, wardrobe.outfitColor, wardrobe.outfitAccentColor)
                        drawNecklace(figureRect, faceRect, wardrobe.necklaceStyle, wardrobe.jewelryMetal)
                        drawRealisticFaceAndMakeup(faceRect, model, hairColor, makeup, wardrobe.jewelryMetal)

                        drawPaintedStrokes(faceRect, makeup.paintedStrokes)
                        if (currentDragPoints.size >= 2) {
                            drawSinglePaintedStroke(
                                faceRect = faceRect,
                                stroke = PaintedBrushStroke(
                                    points = currentDragPoints.toList(),
                                    color = activePigment,
                                    brush = activeBrush,
                                    alpha = 0.65f,
                                    category = MakeupCategory.EYESHADOW
                                )
                            )
                        }

                        drawEarrings(faceRect, wardrobe.earringStyle, wardrobe.jewelryMetal)
                        drawFrontHair(faceRect, hairStyle, hairColor)
                        drawHeadAccessory(faceRect, wardrobe.headAccessoryStyle, wardrobe.jewelryMetal)

                        if (showGuideOverlay && guidePoints.isNotEmpty()) {
                            drawBrushGuideOverlay(
                                faceRect = faceRect,
                                points = guidePoints,
                                motion = guideMotion,
                                progress = guideProgress
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun resolvePortraitSpec(
    assets: ModelVisualAssets,
    poseMode: PortraitPoseMode,
    headAccessoryStyle: HeadAccessoryStyle
): Pair<Int, PortraitLandmarks> {
    return when {
        (poseMode == PortraitPoseMode.SPA_HEADBAND || headAccessoryStyle == HeadAccessoryStyle.SATIN_HEADBAND) &&
            assets.spaRes != null && assets.spaLandmarks != null ->
            assets.spaRes to assets.spaLandmarks
        poseMode == PortraitPoseMode.HIGH_BUN ->
            assets.bunRes to assets.bunLandmarks
        else ->
            assets.faceRes to assets.faceLandmarks
    }
}

internal fun computePortraitRect(canvasSize: Size, aspectRatio: Float): Rect {
    val safeAspect = aspectRatio.coerceIn(0.35f, 1.4f)
    val availH = canvasSize.height * 0.90f
    val availW = canvasSize.width * 0.92f
    val h = min(availH, availW / safeAspect)
    val w = h * safeAspect
    val left = (canvasSize.width - w) / 2f
    val top = canvasSize.height * 0.04f + (availH - h) * 0.45f
    return Rect(left, top, left + w, top + h)
}

internal fun computePortraitFaceRect(portraitRect: Rect, landmarks: PortraitLandmarks): Rect {
    val eyeMidU = (landmarks.eyeLeft.x + landmarks.eyeRight.x) * 0.5f
    val eyeMidV = (landmarks.eyeLeft.y + landmarks.eyeRight.y) * 0.5f
    val eyeMidX = portraitRect.left + eyeMidU * portraitRect.width
    val eyeMidY = portraitRect.top + eyeMidV * portraitRect.height
    val ipdPx = ((landmarks.eyeRight.x - landmarks.eyeLeft.x) * portraitRect.width).coerceAtLeast(12f)
    val faceW = ipdPx / 0.330f
    val eyeToMouthPx = ((landmarks.mouthCenter.y - eyeMidV) * portraitRect.height).coerceAtLeast(12f)
    val faceH = eyeToMouthPx / 0.287f
    return Rect(
        left = eyeMidX - faceW * 0.50f,
        top = eyeMidY - faceH * 0.405f,
        right = eyeMidX + faceW * 0.50f,
        bottom = eyeMidY + faceH * 0.595f
    )
}

internal fun computeIllustratedBodyRect(canvasSize: Size, aspectRatio: Float): Rect {
    val safeAspect = aspectRatio.coerceIn(0.24f, 0.55f)
    val availH = canvasSize.height * 0.91f
    val availW = canvasSize.width * 0.88f
    val h = min(availH, availW / safeAspect)
    val w = h * safeAspect
    val left = (canvasSize.width - w) / 2f
    val top = (canvasSize.height - h) * 0.48f
    return Rect(left, top, left + w, top + h)
}

private fun resolveInputToFaceUv(
    pos: Offset,
    canvasSize: Size,
    portraitBitmap: ImageBitmap?,
    portraitLandmarks: PortraitLandmarks?,
    zoomScale: Float,
    zoomFactor: Float
): Offset? {
    if (portraitBitmap != null && portraitLandmarks != null) {
        val portraitAspect = portraitBitmap.width.toFloat() / portraitBitmap.height.toFloat()
        val portraitRect = computePortraitRect(canvasSize, portraitAspect)
        val faceRect = computePortraitFaceRect(portraitRect, portraitLandmarks)
        val u = (pos.x - faceRect.left) / faceRect.width
        val v = (pos.y - faceRect.top) / faceRect.height
        return if (u in -0.10f..1.10f && v in -0.10f..1.10f) {
            Offset(u.coerceIn(0f, 1f), v.coerceIn(0f, 1f))
        } else null
    }
    return screenToFaceUv(pos, canvasSize, zoomScale, zoomFactor)
}

internal fun computeFigureRect(canvasSize: Size): Rect {
    val targetAspect = 0.43f
    val availableH = canvasSize.height * 0.91f
    val availableW = canvasSize.width * 0.88f
    val h = min(availableH, availableW / targetAspect)
    val w = h * targetAspect
    val left = (canvasSize.width - w) / 2f
    val top = (canvasSize.height - h) / 2f
    return Rect(left, top, left + w, top + h)
}

internal fun computeFaceRect(figureRect: Rect): Rect {
    // Sleek realistic head-to-body ratio (roughly 1:7.5 fashion figure proportion)
    val faceW = figureRect.width * 0.44f
    val faceH = figureRect.height * 0.21f
    val left = figureRect.center.x - faceW / 2f
    val top = figureRect.top + figureRect.height * 0.055f
    return Rect(left, top, left + faceW, top + faceH)
}

private fun screenToFaceUv(
    pos: Offset,
    canvasSize: Size,
    zoomScale: Float,
    zoomFactor: Float
): Offset? {
    val figureRect = computeFigureRect(canvasSize)
    val faceRect = computeFaceRect(figureRect)
    val facePivot = Offset(faceRect.center.x, faceRect.center.y)
    val shiftY = (canvasSize.height * 0.49f - facePivot.y) * zoomFactor

    val unscaledX = (pos.x - facePivot.x) / zoomScale + facePivot.x
    val unscaledY = (pos.y - shiftY - facePivot.y) / zoomScale + facePivot.y

    val u = (unscaledX - faceRect.left) / faceRect.width
    val v = (unscaledY - faceRect.top) / faceRect.height
    return if (u in -0.08f..1.08f && v in -0.08f..1.08f) {
        Offset(u.coerceIn(0f, 1f), v.coerceIn(0f, 1f))
    } else null
}

internal fun uv(faceRect: Rect, u: Float, v: Float): Offset =
    Offset(faceRect.left + u * faceRect.width, faceRect.top + v * faceRect.height)

internal fun fw(figureRect: Rect, fx: Float, fy: Float): Offset =
    Offset(figureRect.left + fx * figureRect.width, figureRect.top + fy * figureRect.height)

private fun DrawScope.drawBackHair(
    fig: Rect,
    face: Rect,
    style: HairStyle,
    color: Color
) {
    val deepShadow = color.copy(
        red = (color.red * 0.56f).coerceIn(0f, 1f),
        green = (color.green * 0.56f).coerceIn(0f, 1f),
        blue = (color.blue * 0.56f).coerceIn(0f, 1f)
    )
    val midTone = color
    val sheen = color.copy(
        red = (color.red + (1f - color.red) * 0.28f).coerceIn(0f, 1f),
        green = (color.green + (1f - color.green) * 0.28f).coerceIn(0f, 1f),
        blue = (color.blue + (1f - color.blue) * 0.28f).coerceIn(0f, 1f)
    )

    when (style) {
        HairStyle.LONG_WAVES -> {
            val path = Path().apply {
                moveTo(face.left - face.width * 0.16f, face.top + face.height * 0.26f)
                cubicTo(
                    face.left - face.width * 0.30f, face.bottom + fig.height * 0.08f,
                    face.left - face.width * 0.16f, fig.top + fig.height * 0.42f,
                    face.left + face.width * 0.02f, fig.top + fig.height * 0.47f
                )
                quadraticTo(face.center.x, fig.top + fig.height * 0.49f, face.right - face.width * 0.02f, fig.top + fig.height * 0.47f)
                cubicTo(
                    face.right + face.width * 0.16f, fig.top + fig.height * 0.42f,
                    face.right + face.width * 0.30f, face.bottom + fig.height * 0.08f,
                    face.right + face.width * 0.16f, face.top + face.height * 0.26f
                )
                cubicTo(
                    face.right + face.width * 0.06f, face.top - face.height * 0.10f,
                    face.left - face.width * 0.06f, face.top - face.height * 0.10f,
                    face.left - face.width * 0.16f, face.top + face.height * 0.26f
                )
                close()
            }
            drawPath(path, Brush.verticalGradient(listOf(midTone, sheen, deepShadow)))
            // Layered sculpted wave highlights & shadow strands
            for (side in listOf(-1f, 1f)) {
                val darkLock = Path().apply {
                    moveTo(face.center.x + side * face.width * 0.36f, face.bottom - face.height * 0.10f)
                    cubicTo(
                        face.center.x + side * face.width * 0.50f, face.bottom + fig.height * 0.05f,
                        face.center.x + side * face.width * 0.32f, fig.top + fig.height * 0.36f,
                        face.center.x + side * face.width * 0.40f, fig.top + fig.height * 0.46f
                    )
                }
                drawPath(darkLock, deepShadow.copy(alpha = 0.55f), style = Stroke(width = 4.5f, cap = StrokeCap.Round))

                val brightLock = Path().apply {
                    moveTo(face.center.x + side * face.width * 0.45f, face.top + face.height * 0.32f)
                    cubicTo(
                        face.center.x + side * face.width * 0.56f, face.bottom + fig.height * 0.04f,
                        face.center.x + side * face.width * 0.38f, fig.top + fig.height * 0.38f,
                        face.center.x + side * face.width * 0.46f, fig.top + fig.height * 0.45f
                    )
                }
                drawPath(brightLock, sheen.copy(alpha = 0.52f), style = Stroke(width = 2.6f, cap = StrokeCap.Round))
            }
        }

        HairStyle.ASYMMETRIC_BOB -> {
            val path = Path().apply {
                moveTo(face.left - face.width * 0.14f, face.top + face.height * 0.25f)
                quadraticTo(
                    face.left - face.width * 0.20f, face.bottom + face.height * 0.08f,
                    face.left + face.width * 0.12f, face.bottom + face.height * 0.13f
                )
                lineTo(face.right - face.width * 0.08f, face.bottom + face.height * 0.03f)
                quadraticTo(
                    face.right + face.width * 0.20f, face.bottom - face.height * 0.02f,
                    face.right + face.width * 0.14f, face.top + face.height * 0.25f
                )
                cubicTo(
                    face.right, face.top - face.height * 0.09f,
                    face.left, face.top - face.height * 0.09f,
                    face.left - face.width * 0.14f, face.top + face.height * 0.25f
                )
                close()
            }
            drawPath(path, Brush.verticalGradient(listOf(midTone, sheen, deepShadow)))
        }

        HairStyle.AFRO_CURLS -> {
            val center = Offset(face.center.x, face.top + face.height * 0.32f)
            val baseR = face.width * 0.62f
            for (i in 0 until 18) {
                val angle = (i * 2 * PI / 18).toFloat()
                val cx = center.x + cos(angle) * baseR * 0.74f
                val cy = center.y + sin(angle) * baseR * 0.70f
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(if (i % 3 == 0) sheen else midTone, deepShadow),
                        center = Offset(cx, cy),
                        radius = baseR * 0.36f
                    ),
                    radius = baseR * 0.36f,
                    center = Offset(cx, cy)
                )
            }
            drawCircle(color = midTone, radius = baseR * 0.76f, center = center)
        }

        HairStyle.SLEEK_PONY -> {
            val tailPath = Path().apply {
                moveTo(face.center.x - face.width * 0.14f, face.top - face.height * 0.08f)
                quadraticTo(
                    face.right + face.width * 0.28f, face.top + face.height * 0.15f,
                    face.right + face.width * 0.18f, fig.top + fig.height * 0.40f
                )
                quadraticTo(
                    face.right + face.width * 0.02f, fig.top + fig.height * 0.36f,
                    face.center.x + face.width * 0.12f, face.top + face.height * 0.18f
                )
                close()
            }
            drawPath(tailPath, Brush.verticalGradient(listOf(midTone, sheen, deepShadow)))
            drawOval(
                brush = Brush.radialGradient(listOf(sheen, midTone, deepShadow), Offset(face.center.x, face.top + face.height * 0.08f), face.width * 0.45f),
                topLeft = Offset(face.left + face.width * 0.10f, face.top - face.height * 0.07f),
                size = Size(face.width * 0.80f, face.height * 0.46f)
            )
        }

        HairStyle.BOHO_BRAIDS -> {
            val crownPath = Path().apply {
                addOval(Rect(face.left - face.width * 0.06f, face.top - face.height * 0.05f, face.right + face.width * 0.06f, face.top + face.height * 0.58f))
            }
            drawPath(crownPath, midTone)
            for (side in listOf(-1f, 1f)) {
                val baseX = face.center.x + side * face.width * 0.38f
                for (seg in 0..8) {
                    val cy = face.top + face.height * 0.46f + seg * face.height * 0.14f
                    drawOval(
                        brush = Brush.horizontalGradient(listOf(deepShadow, if (seg % 2 == 0) sheen else midTone, deepShadow)),
                        topLeft = Offset(baseX - face.width * 0.075f, cy),
                        size = Size(face.width * 0.15f, face.height * 0.17f)
                    )
                }
            }
        }

        HairStyle.SILKY_BANGS -> {
            val path = Path().apply {
                moveTo(face.left - face.width * 0.14f, face.top + face.height * 0.22f)
                lineTo(face.left - face.width * 0.16f, fig.top + fig.height * 0.41f)
                lineTo(face.right + face.width * 0.16f, fig.top + fig.height * 0.41f)
                lineTo(face.right + face.width * 0.14f, face.top + face.height * 0.22f)
                cubicTo(
                    face.right, face.top - face.height * 0.08f,
                    face.left, face.top - face.height * 0.08f,
                    face.left - face.width * 0.14f, face.top + face.height * 0.22f
                )
                close()
            }
            drawPath(path, Brush.verticalGradient(listOf(midTone, sheen, deepShadow)))
        }

        HairStyle.HALF_UP_GLAM -> {
            drawOval(
                brush = Brush.radialGradient(listOf(sheen, midTone, deepShadow), Offset(face.center.x, face.top - face.height * 0.02f), face.width * 0.32f),
                topLeft = Offset(face.center.x - face.width * 0.26f, face.top - face.height * 0.15f),
                size = Size(face.width * 0.52f, face.height * 0.28f)
            )
            val path = Path().apply {
                moveTo(face.left - face.width * 0.16f, face.top + face.height * 0.26f)
                cubicTo(
                    face.left - face.width * 0.28f, face.bottom + fig.height * 0.06f,
                    face.left - face.width * 0.10f, fig.top + fig.height * 0.40f,
                    face.left + face.width * 0.08f, fig.top + fig.height * 0.44f
                )
                lineTo(face.right - face.width * 0.08f, fig.top + fig.height * 0.44f)
                cubicTo(
                    face.right + face.width * 0.10f, fig.top + fig.height * 0.40f,
                    face.right + face.width * 0.28f, face.bottom + fig.height * 0.06f,
                    face.right + face.width * 0.16f, face.top + face.height * 0.26f
                )
                cubicTo(
                    face.right, face.top - face.height * 0.07f,
                    face.left, face.top - face.height * 0.07f,
                    face.left - face.width * 0.16f, face.top + face.height * 0.26f
                )
                close()
            }
            drawPath(path, Brush.verticalGradient(listOf(midTone, sheen, deepShadow)))
        }

        HairStyle.PIXIE_CHIC -> {
            val pixie = Path().apply {
                moveTo(face.left - face.width * 0.05f, face.top + face.height * 0.38f)
                cubicTo(
                    face.left - face.width * 0.10f, face.top - face.height * 0.08f,
                    face.right + face.width * 0.10f, face.top - face.height * 0.08f,
                    face.right + face.width * 0.05f, face.top + face.height * 0.38f
                )
                close()
            }
            drawPath(pixie, Brush.verticalGradient(listOf(sheen, midTone, deepShadow)))
        }
    }
}

private fun DrawScope.drawFrontHair(face: Rect, style: HairStyle, color: Color) {
    val highlight = color.copy(
        red = (color.red + (1f - color.red) * 0.24f).coerceIn(0f, 1f),
        green = (color.green + (1f - color.green) * 0.24f).coerceIn(0f, 1f),
        blue = (color.blue + (1f - color.blue) * 0.24f).coerceIn(0f, 1f)
    )
    val shadow = color.copy(
        red = (color.red * 0.72f).coerceIn(0f, 1f),
        green = (color.green * 0.72f).coerceIn(0f, 1f),
        blue = (color.blue * 0.72f).coerceIn(0f, 1f)
    )
    when (style) {
        HairStyle.SILKY_BANGS -> {
            val bangs = Path().apply {
                moveTo(face.left + face.width * 0.07f, face.top + face.height * 0.27f)
                quadraticTo(face.left + face.width * 0.10f, face.top - face.height * 0.03f, face.center.x, face.top - face.height * 0.02f)
                quadraticTo(face.right - face.width * 0.10f, face.top - face.height * 0.03f, face.right - face.width * 0.07f, face.top + face.height * 0.27f)
                lineTo(face.right - face.width * 0.14f, face.top + face.height * 0.245f)
                lineTo(face.left + face.width * 0.14f, face.top + face.height * 0.245f)
                close()
            }
            drawPath(bangs, Brush.verticalGradient(listOf(color, highlight, shadow)))
            // Gloss band across bangs
            drawLine(
                color = highlight.copy(alpha = 0.45f),
                start = uv(face, 0.24f, 0.15f),
                end = uv(face, 0.76f, 0.15f),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
        HairStyle.PIXIE_CHIC -> {
            val sideSweep = Path().apply {
                moveTo(face.left + face.width * 0.07f, face.top + face.height * 0.20f)
                quadraticTo(face.center.x, face.top - face.height * 0.04f, face.right - face.width * 0.08f, face.top + face.height * 0.28f)
                quadraticTo(face.center.x + face.width * 0.12f, face.top + face.height * 0.14f, face.left + face.width * 0.07f, face.top + face.height * 0.20f)
                close()
            }
            drawPath(sideSweep, Brush.horizontalGradient(listOf(color, highlight)))
        }
        else -> {
            for (side in listOf(-1f, 1f)) {
                val frame = Path().apply {
                    moveTo(face.center.x, face.top + face.height * 0.035f)
                    quadraticTo(
                        face.center.x + side * face.width * 0.34f, face.top + face.height * 0.015f,
                        face.center.x + side * face.width * 0.41f, face.top + face.height * 0.39f
                    )
                    quadraticTo(
                        face.center.x + side * face.width * 0.46f, face.top + face.height * 0.14f,
                        face.center.x + side * face.width * 0.24f, face.top - face.height * 0.03f
                    )
                    close()
                }
                drawPath(frame, Brush.verticalGradient(listOf(color, shadow)))
                // Glossy crown sheen arc
                drawLine(
                    color = highlight.copy(alpha = 0.48f),
                    start = uv(face, 0.50f + side * 0.10f, 0.04f),
                    end = uv(face, 0.50f + side * 0.28f, 0.11f),
                    strokeWidth = 2.6f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

private fun DrawScope.drawRealisticBody(fig: Rect, face: Rect, model: WomanModel) {
    val skinBrush = Brush.horizontalGradient(
        colors = listOf(model.skinShadow, model.skinTone, model.skinHighlight, model.skinTone, model.skinShadow),
        startX = fig.left + fig.width * 0.25f,
        endX = fig.left + fig.width * 0.75f
    )
    val rimStroke = Stroke(width = 1.4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val rimColor = model.skinShadow.copy(alpha = 0.55f)

    // Neck flowing smoothly into trapezius & shoulders
    val neckPath = Path().apply {
        moveTo(face.center.x - face.width * 0.16f, face.bottom - face.height * 0.12f)
        lineTo(face.center.x + face.width * 0.16f, face.bottom - face.height * 0.12f)
        cubicTo(
            face.center.x + face.width * 0.165f, fig.top + fig.height * 0.27f,
            face.center.x + face.width * 0.22f, fig.top + fig.height * 0.295f,
            fig.center.x + fig.width * 0.24f, fig.top + fig.height * 0.315f
        )
        lineTo(fig.center.x - fig.width * 0.24f, fig.top + fig.height * 0.315f)
        cubicTo(
            face.center.x - face.width * 0.22f, fig.top + fig.height * 0.295f,
            face.center.x - face.width * 0.165f, fig.top + fig.height * 0.27f,
            face.center.x - face.width * 0.16f, face.bottom - face.height * 0.12f
        )
        close()
    }
    drawPath(neckPath, skinBrush)

    // Chin cast shadow + subtle neck tendon highlights
    drawOval(
        brush = Brush.verticalGradient(listOf(model.skinShadow.copy(alpha = 0.68f), Color.Transparent)),
        topLeft = Offset(face.center.x - face.width * 0.16f, face.bottom - face.height * 0.06f),
        size = Size(face.width * 0.32f, face.height * 0.15f)
    )

    // Arms with realistic deltoid, bicep, elbow, forearm & delicate tapered hand
    for (side in listOf(-1f, 1f)) {
        val armPath = Path().apply {
            val sx = fig.center.x + side * fig.width * 0.225f
            val sy = fig.top + fig.height * 0.312f
            moveTo(sx, sy)
            cubicTo(
                fig.center.x + side * fig.width * 0.32f, fig.top + fig.height * 0.355f,
                fig.center.x + side * fig.width * 0.345f, fig.top + fig.height * 0.445f,
                fig.center.x + side * fig.width * 0.325f, fig.top + fig.height * 0.548f
            )
            lineTo(fig.center.x + side * fig.width * 0.275f, fig.top + fig.height * 0.548f)
            cubicTo(
                fig.center.x + side * fig.width * 0.285f, fig.top + fig.height * 0.455f,
                fig.center.x + side * fig.width * 0.24f, fig.top + fig.height * 0.38f,
                fig.center.x + side * fig.width * 0.17f, fig.top + fig.height * 0.34f
            )
            close()
        }
        drawPath(armPath, skinBrush)
        drawPath(armPath, rimColor, style = rimStroke)

        // Graceful hand with thumb & elongated fingers silhouette
        val hand = Path().apply {
            val hx = fig.center.x + side * fig.width * 0.30f
            val hy = fig.top + fig.height * 0.548f
            moveTo(hx - side * fig.width * 0.024f, hy)
            lineTo(hx + side * fig.width * 0.024f, hy)
            quadraticTo(hx + side * fig.width * 0.030f, hy + fig.height * 0.032f, hx + side * fig.width * 0.010f, hy + fig.height * 0.062f)
            quadraticTo(hx - side * fig.width * 0.012f, hy + fig.height * 0.056f, hx - side * fig.width * 0.032f, hy + fig.height * 0.030f)
            close()
        }
        drawPath(hand, model.skinTone)
        drawPath(hand, rimColor, style = rimStroke)
    }

    // Shoulders, Bust, Waist & Hips silhouette
    val torsoPath = Path().apply {
        moveTo(fig.center.x - fig.width * 0.235f, fig.top + fig.height * 0.312f)
        quadraticTo(fig.center.x, fig.top + fig.height * 0.292f, fig.center.x + fig.width * 0.235f, fig.top + fig.height * 0.312f)
        cubicTo(
            fig.center.x + fig.width * 0.23f, fig.top + fig.height * 0.39f,
            fig.center.x + fig.width * 0.145f, fig.top + fig.height * 0.44f,
            fig.center.x + fig.width * 0.155f, fig.top + fig.height * 0.48f
        )
        cubicTo(
            fig.center.x + fig.width * 0.245f, fig.top + fig.height * 0.53f,
            fig.center.x + fig.width * 0.245f, fig.top + fig.height * 0.59f,
            fig.center.x + fig.width * 0.185f, fig.top + fig.height * 0.62f
        )
        lineTo(fig.center.x - fig.width * 0.185f, fig.top + fig.height * 0.62f)
        cubicTo(
            fig.center.x - fig.width * 0.245f, fig.top + fig.height * 0.59f,
            fig.center.x - fig.width * 0.245f, fig.top + fig.height * 0.53f,
            fig.center.x - fig.width * 0.155f, fig.top + fig.height * 0.48f
        )
        cubicTo(
            fig.center.x - fig.width * 0.145f, fig.top + fig.height * 0.44f,
            fig.center.x - fig.width * 0.23f, fig.top + fig.height * 0.39f,
            fig.center.x - fig.width * 0.235f, fig.top + fig.height * 0.312f
        )
        close()
    }
    drawPath(torsoPath, skinBrush)
    drawPath(torsoPath, rimColor, style = rimStroke)

    // Collarbones & soft décolletage contour
    for (side in listOf(-1f, 1f)) {
        drawLine(
            color = model.skinShadow.copy(alpha = 0.45f),
            start = fw(fig, 0.50f + side * 0.13f, 0.320f),
            end = fw(fig, 0.50f + side * 0.032f, 0.335f),
            strokeWidth = 2.6f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = model.skinHighlight.copy(alpha = 0.78f),
            start = fw(fig, 0.50f + side * 0.125f, 0.324f),
            end = fw(fig, 0.50f + side * 0.036f, 0.338f),
            strokeWidth = 2.0f,
            cap = StrokeCap.Round
        )
        // Soft bust highlight
        drawCircle(
            brush = Brush.radialGradient(
                listOf(model.skinHighlight.copy(alpha = 0.35f), Color.Transparent),
                center = fw(fig, 0.50f + side * 0.085f, 0.385f),
                radius = fig.width * 0.075f
            ),
            radius = fig.width * 0.075f,
            center = fw(fig, 0.50f + side * 0.085f, 0.385f)
        )
    }

    // Anatomical Legs with thigh, knee, calf & ankle contours
    for (side in listOf(-1f, 1f)) {
        val legPath = Path().apply {
            val topOuterX = fig.center.x + side * fig.width * 0.195f
            val topInnerX = fig.center.x + side * fig.width * 0.016f
            val kneeOuterX = fig.center.x + side * fig.width * 0.130f
            val kneeInnerX = fig.center.x + side * fig.width * 0.035f
            val calfOuterX = fig.center.x + side * fig.width * 0.142f
            val ankleOuterX = fig.center.x + side * fig.width * 0.106f
            val ankleInnerX = fig.center.x + side * fig.width * 0.058f

            moveTo(topInnerX, fig.top + fig.height * 0.60f)
            lineTo(topOuterX, fig.top + fig.height * 0.60f)
            quadraticTo(
                fig.center.x + side * fig.width * 0.18f, fig.top + fig.height * 0.71f,
                kneeOuterX, fig.top + fig.height * 0.765f
            )
            cubicTo(
                calfOuterX, fig.top + fig.height * 0.81f,
                fig.center.x + side * fig.width * 0.132f, fig.top + fig.height * 0.87f,
                ankleOuterX, fig.top + fig.height * 0.93f
            )
            lineTo(ankleInnerX, fig.top + fig.height * 0.93f)
            quadraticTo(
                fig.center.x + side * fig.width * 0.042f, fig.top + fig.height * 0.85f,
                kneeInnerX, fig.top + fig.height * 0.765f
            )
            quadraticTo(
                fig.center.x + side * fig.width * 0.016f, fig.top + fig.height * 0.69f,
                topInnerX, fig.top + fig.height * 0.60f
            )
            close()
        }
        drawPath(legPath, skinBrush)
        drawPath(legPath, rimColor, style = rimStroke)

        // Kneecap highlight & subtle shadow arc
        drawCircle(
            brush = Brush.radialGradient(
                listOf(model.skinHighlight.copy(alpha = 0.50f), Color.Transparent),
                center = fw(fig, 0.50f + side * 0.082f, 0.758f),
                radius = fig.width * 0.042f
            ),
            radius = fig.width * 0.042f,
            center = fw(fig, 0.50f + side * 0.082f, 0.758f)
        )
    }
}

private fun DrawScope.drawBrushGuideOverlay(
    faceRect: Rect,
    points: List<Offset>,
    motion: BrushMotionPattern?,
    progress: Float
) {
    if (points.isEmpty()) return
    val guideColor = Color(0xFFFFF4D2)
    val glowColor = Color(0xFFE04D7D)

    if (points.size >= 2) {
        val path = Path().apply {
            val first = uv(faceRect, points.first().x, points.first().y)
            moveTo(first.x, first.y)
            for (i in 1 until points.size) {
                val p = uv(faceRect, points[i].x, points[i].y)
                lineTo(p.x, p.y)
            }
        }
        drawPath(
            path = path,
            color = glowColor.copy(alpha = 0.55f),
            style = Stroke(
                width = faceRect.width * 0.032f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), phase = -progress * 48f)
            )
        )
        drawPath(
            path = path,
            color = guideColor,
            style = Stroke(
                width = faceRect.width * 0.012f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), phase = -progress * 48f)
            )
        )

        val totalSegments = points.size - 1
        val scaled = progress * totalSegments
        val idx = scaled.toInt().coerceIn(0, totalSegments - 1)
        val localT = scaled - idx
        val p0 = points[idx]
        val p1 = points[idx + 1]
        val interp = Offset(p0.x + (p1.x - p0.x) * localT, p0.y + (p1.y - p0.y) * localT)
        val tipPos = uv(faceRect, interp.x, interp.y)

        drawCircle(
            color = glowColor.copy(alpha = 0.45f),
            radius = faceRect.width * 0.065f,
            center = tipPos
        )
        drawCircle(
            color = guideColor,
            radius = faceRect.width * 0.032f,
            center = tipPos
        )
    } else if (motion == BrushMotionPattern.DABBING_TAPS) {
        points.forEach { pt ->
            val c = uv(faceRect, pt.x, pt.y)
            val pulseRadius = faceRect.width * (0.04f + progress * 0.06f)
            drawCircle(
                color = glowColor.copy(alpha = 1f - progress),
                radius = pulseRadius,
                center = c,
                style = Stroke(width = 3f)
            )
        }
    }
}
