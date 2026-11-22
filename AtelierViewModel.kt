package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AtelierCatalog
import com.example.data.AtelierPreferences
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.hypot

enum class StudioDockTab(val displayName: String) {
    MAKEUP("Maquillaje"),
    BRUSHES("Pinceles"),
    OUTFITS("Ropa"),
    SHOES("Zapatos"),
    JEWELRY("Joyería"),
    MODELS("Modelos")
}

data class AtelierUiState(
    val selectedModel: WomanModel = AtelierCatalog.models.first(),
    val selectedSkinPreset: SkinTonePreset = AtelierCatalog.skinTones[3],
    val hairStyle: HairStyle = AtelierCatalog.models.first().defaultHairStyle,
    val hairColor: Color = AtelierCatalog.models.first().defaultHairColor,
    val portraitPoseMode: PortraitPoseMode = AtelierCatalog.models.first().defaultPortraitPose,
    val makeup: MakeupState = MakeupState(),
    val wardrobe: WardrobeState = WardrobeState(),
    val zoomMode: ViewZoomMode = ViewZoomMode.FACE_CLOSEUP,
    val isComparingOriginal: Boolean = false,
    val showBrushGuides: Boolean = true,

    // Face & Skin Setup Gate
    val showFaceSkinSetup: Boolean = true,
    val hasCompletedInitialSetup: Boolean = false,

    // Studio active tools
    val activeDockTab: StudioDockTab = StudioDockTab.MAKEUP,
    val activeMakeupCategory: MakeupCategory = MakeupCategory.EYESHADOW,
    val activeBrush: BrushTool = BrushTool.BLENDER,
    val activePigmentColor: Color = AtelierCatalog.eyeshadowPalettes.first().first,

    // Academy / Guided Lesson state
    val activeLesson: MakeupLesson? = null,
    val activeStepIndex: Int = 0,
    val stepProgress: Float = 0f,
    val coachFeedback: StrokeFeedback? = null,
    val completedLessonIds: Set<String> = emptySet(),
    val isLessonCompletedDialogVisible: Boolean = false,

    // Runway showcase modal
    val isRunwayShowcaseVisible: Boolean = false,
    val appliedLookbookTitle: String? = null
)

class AtelierViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = AtelierPreferences(application.applicationContext)

    private val _uiState = MutableStateFlow(AtelierUiState())
    val uiState: StateFlow<AtelierUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                prefs.completedLessons,
                prefs.showBrushGuides,
                prefs.lastModelId,
                prefs.hasConfiguredFaceSkin
            ) { completed, showGuides, modelId, hasConfigured ->
                listOf(completed, showGuides, modelId, hasConfigured)
            }.collect { values ->
                @Suppress("UNCHECKED_CAST")
                val completed = values[0] as Set<String>
                val showGuides = values[1] as Boolean
                val modelId = values[2] as String
                val hasConfigured = values[3] as Boolean

                _uiState.update { state ->
                    val foundModel = AtelierCatalog.models.find { it.id == modelId }
                    state.copy(
                        completedLessonIds = completed,
                        showBrushGuides = showGuides,
                        hasCompletedInitialSetup = hasConfigured,
                        showFaceSkinSetup = if (hasConfigured && !state.hasCompletedInitialSetup) false else state.showFaceSkinSetup,
                        selectedModel = if (state.activeLesson == null && foundModel != null && state.selectedModel.id == "sofia" && modelId != "sofia") {
                            foundModel
                        } else state.selectedModel
                    )
                }
            }
        }
    }

    fun openFaceSkinSetup() {
        _uiState.update { it.copy(showFaceSkinSetup = true) }
    }

    fun dismissFaceSkinSetup() {
        _uiState.update { it.copy(showFaceSkinSetup = false) }
    }

    fun applyCustomFaceAndSkin(
        customModel: WomanModel,
        hairStyle: HairStyle,
        hairColor: Color,
        skinPreset: SkinTonePreset
    ) {
        _uiState.update {
            it.copy(
                selectedModel = customModel,
                selectedSkinPreset = skinPreset,
                hairStyle = hairStyle,
                hairColor = hairColor,
                portraitPoseMode = customModel.defaultPortraitPose,
                showFaceSkinSetup = false,
                hasCompletedInitialSetup = true
            )
        }
        viewModelScope.launch { prefs.setHasConfiguredFaceSkin(true) }
    }

    fun selectModel(model: WomanModel) {
        val matchingSkin = AtelierCatalog.skinTones.minByOrNull {
            abs(it.skinTone.red - model.skinTone.red) + abs(it.skinTone.green - model.skinTone.green)
        } ?: AtelierCatalog.skinTones.first()
        _uiState.update {
            it.copy(
                selectedModel = model,
                selectedSkinPreset = matchingSkin,
                hairStyle = model.defaultHairStyle,
                hairColor = model.defaultHairColor,
                portraitPoseMode = model.defaultPortraitPose
            )
        }
        viewModelScope.launch { prefs.setLastModelId(model.id) }
    }

    fun setHairStyle(style: HairStyle) {
        val mappedPose = when (style) {
            HairStyle.SLEEK_PONY, HairStyle.BOHO_BRAIDS, HairStyle.PIXIE_CHIC, HairStyle.HALF_UP_GLAM ->
                PortraitPoseMode.HIGH_BUN
            else -> PortraitPoseMode.LOOSE_HAIR
        }
        _uiState.update { it.copy(hairStyle = style, portraitPoseMode = mappedPose) }
    }

    fun setPortraitPoseMode(mode: PortraitPoseMode) {
        _uiState.update { state ->
            val updatedWardrobe = if (mode == PortraitPoseMode.SPA_HEADBAND) {
                state.wardrobe.copy(headAccessoryStyle = HeadAccessoryStyle.SATIN_HEADBAND)
            } else if (state.wardrobe.headAccessoryStyle == HeadAccessoryStyle.SATIN_HEADBAND) {
                state.wardrobe.copy(headAccessoryStyle = HeadAccessoryStyle.NONE)
            } else {
                state.wardrobe
            }
            state.copy(portraitPoseMode = mode, wardrobe = updatedWardrobe)
        }
    }

    fun setHairColor(color: Color) {
        _uiState.update { it.copy(hairColor = color) }
    }

    fun setZoomMode(mode: ViewZoomMode) {
        _uiState.update { it.copy(zoomMode = mode) }
    }

    fun setComparingOriginal(comparing: Boolean) {
        _uiState.update { it.copy(isComparingOriginal = comparing) }
    }

    fun toggleBrushGuides() {
        val next = !_uiState.value.showBrushGuides
        _uiState.update { it.copy(showBrushGuides = next) }
        viewModelScope.launch { prefs.setShowBrushGuides(next) }
    }

    fun setActiveDockTab(tab: StudioDockTab) {
        val suggestedZoom = when (tab) {
            StudioDockTab.MAKEUP, StudioDockTab.BRUSHES -> ViewZoomMode.FACE_CLOSEUP
            StudioDockTab.OUTFITS, StudioDockTab.SHOES -> ViewZoomMode.FULL_BODY
            StudioDockTab.JEWELRY, StudioDockTab.MODELS -> _uiState.value.zoomMode
        }
        _uiState.update { it.copy(activeDockTab = tab, zoomMode = suggestedZoom) }
    }

    fun setActiveMakeupCategory(category: MakeupCategory) {
        val recommendedBrush = when (category) {
            MakeupCategory.BASE_CONTOUR -> BrushTool.SPONGE
            MakeupCategory.EYESHADOW -> BrushTool.BLENDER
            MakeupCategory.EYELINER -> BrushTool.PRECISION_LINER
            MakeupCategory.BROWS_LASHES -> BrushTool.PRECISION_LINER
            MakeupCategory.BLUSH -> BrushTool.KABUKI
            MakeupCategory.LIPS -> BrushTool.LIP_BRUSH
            MakeupCategory.GEMS -> BrushTool.SPONGE
        }
        val recommendedColor = when (category) {
            MakeupCategory.BASE_CONTOUR -> AtelierCatalog.highlighterColors.first()
            MakeupCategory.EYESHADOW -> AtelierCatalog.eyeshadowPalettes.first().first
            MakeupCategory.EYELINER -> AtelierCatalog.eyelinerColors.first()
            MakeupCategory.BROWS_LASHES -> _uiState.value.hairColor
            MakeupCategory.BLUSH -> AtelierCatalog.blushColors.first()
            MakeupCategory.LIPS -> AtelierCatalog.lipColors.first().first
            MakeupCategory.GEMS -> Color(0xFFFFF4E3)
        }
        _uiState.update {
            it.copy(
                activeMakeupCategory = category,
                activeBrush = recommendedBrush,
                activePigmentColor = recommendedColor,
                zoomMode = ViewZoomMode.FACE_CLOSEUP
            )
        }
    }

    fun setActiveBrush(brush: BrushTool) {
        _uiState.update { it.copy(activeBrush = brush) }
    }

    fun setActivePigmentColor(color: Color) {
        _uiState.update { it.copy(activePigmentColor = color) }
    }

    fun updateMakeup(transform: (MakeupState) -> MakeupState) {
        _uiState.update { it.copy(makeup = transform(it.makeup), appliedLookbookTitle = null) }
    }

    fun updateWardrobe(transform: (WardrobeState) -> WardrobeState) {
        _uiState.update { state ->
            val nextWardrobe = transform(state.wardrobe)
            val nextPose = when {
                nextWardrobe.headAccessoryStyle == HeadAccessoryStyle.SATIN_HEADBAND -> PortraitPoseMode.SPA_HEADBAND
                state.portraitPoseMode == PortraitPoseMode.SPA_HEADBAND &&
                    nextWardrobe.headAccessoryStyle != HeadAccessoryStyle.SATIN_HEADBAND -> PortraitPoseMode.LOOSE_HAIR
                else -> state.portraitPoseMode
            }
            state.copy(wardrobe = nextWardrobe, portraitPoseMode = nextPose, appliedLookbookTitle = null)
        }
    }

    fun undoLastPaintedStroke() {
        _uiState.update { state ->
            if (state.makeup.paintedStrokes.isEmpty()) state
            else state.copy(
                makeup = state.makeup.copy(
                    paintedStrokes = state.makeup.paintedStrokes.dropLast(1)
                )
            )
        }
    }

    fun clearAllMakeup() {
        _uiState.update {
            it.copy(
                makeup = MakeupState(),
                coachFeedback = null,
                appliedLookbookTitle = null
            )
        }
    }

    fun applyLookbook(lookbook: StyleLookbook) {
        val mappedPose = when (lookbook.suggestedHair) {
            HairStyle.SLEEK_PONY, HairStyle.BOHO_BRAIDS, HairStyle.PIXIE_CHIC, HairStyle.HALF_UP_GLAM ->
                PortraitPoseMode.HIGH_BUN
            else -> PortraitPoseMode.LOOSE_HAIR
        }
        _uiState.update {
            it.copy(
                makeup = lookbook.makeup,
                wardrobe = lookbook.wardrobe,
                hairStyle = lookbook.suggestedHair,
                portraitPoseMode = mappedPose,
                zoomMode = ViewZoomMode.FULL_BODY,
                appliedLookbookTitle = lookbook.title
            )
        }
    }

    fun setRunwayShowcaseVisible(visible: Boolean) {
        _uiState.update { it.copy(isRunwayShowcaseVisible = visible) }
    }

    // --- Interactive Studio Painting ---

    fun onStudioStrokeCompleted(points: List<Offset>, brush: BrushTool) {
        val state = _uiState.value
        val newStroke = PaintedBrushStroke(
            points = points,
            color = state.activePigmentColor,
            brush = brush,
            alpha = 0.72f,
            category = state.activeMakeupCategory
        )
        val feedback = evaluateBrushGesture(points, brush, null)
        _uiState.update {
            it.copy(
                makeup = it.makeup.copy(
                    paintedStrokes = (it.makeup.paintedStrokes + newStroke).takeLast(60)
                ),
                coachFeedback = feedback
            )
        }
    }

    fun onStudioTapPoint(point: Offset, brush: BrushTool) {
        val state = _uiState.value
        val newStroke = PaintedBrushStroke(
            points = listOf(point),
            color = state.activePigmentColor,
            brush = brush,
            alpha = 0.75f,
            category = state.activeMakeupCategory
        )
        val msg = if (brush == BrushTool.SPONGE) {
            "¡Toquecito perfecto con esponja! Fija el producto sin arrastrar."
        } else {
            "Toque aplicado con ${brush.displayName}. Desliza para difuminar."
        }
        _uiState.update {
            it.copy(
                makeup = it.makeup.copy(
                    paintedStrokes = (it.makeup.paintedStrokes + newStroke).takeLast(60)
                ),
                coachFeedback = StrokeFeedback(msg, isPositive = true, progressGain = 0.25f)
            )
        }
    }

    // --- Academy Guided Lessons ---

    fun startLesson(lesson: MakeupLesson) {
        val firstStep = lesson.steps.first()
        _uiState.update {
            it.copy(
                activeLesson = lesson,
                activeStepIndex = 0,
                stepProgress = 0f,
                makeup = MakeupState(),
                zoomMode = ViewZoomMode.FACE_CLOSEUP,
                activeBrush = firstStep.requiredBrush,
                activeMakeupCategory = firstStep.targetCategory,
                activePigmentColor = firstStep.previewColor,
                coachFeedback = StrokeFeedback(
                    message = "Sigue la guía animada usando ${firstStep.requiredBrush.displayName}.",
                    isPositive = true,
                    progressGain = 0f
                ),
                isLessonCompletedDialogVisible = false
            )
        }
    }

    fun exitLesson() {
        _uiState.update {
            it.copy(
                activeLesson = null,
                activeStepIndex = 0,
                stepProgress = 0f,
                coachFeedback = null,
                isLessonCompletedDialogVisible = false
            )
        }
    }

    fun selectLessonBrush(brush: BrushTool) {
        val step = _uiState.value.let { it.activeLesson?.steps?.getOrNull(it.activeStepIndex) }
        val feedback = if (step != null && brush == step.requiredBrush) {
            StrokeFeedback(
                "¡Herramienta correcta! Ahora aplica con ${brush.motionPattern.title.lowercase()}.",
                isPositive = true,
                progressGain = 0f
            )
        } else if (step != null) {
            StrokeFeedback(
                "Para este paso te recomendamos ${step.requiredBrush.displayName} (${step.requiredBrush.motionPattern.title}).",
                isPositive = false,
                progressGain = 0f
            )
        } else null

        _uiState.update {
            it.copy(activeBrush = brush, coachFeedback = feedback)
        }
    }

    fun onLessonStrokeCompleted(points: List<Offset>, brush: BrushTool) {
        val state = _uiState.value
        val lesson = state.activeLesson ?: return
        val step = lesson.steps.getOrNull(state.activeStepIndex) ?: return

        if (brush != step.requiredBrush) {
            _uiState.update {
                it.copy(
                    coachFeedback = StrokeFeedback(
                        message = "Cambia a ${step.requiredBrush.displayName} para practicar el movimiento correcto.",
                        isPositive = false,
                        progressGain = 0f
                    )
                )
            }
            return
        }

        val feedback = evaluateBrushGesture(points, brush, step)
        val newProgress = (state.stepProgress + feedback.progressGain).coerceIn(0f, 1f)
        val newStroke = PaintedBrushStroke(
            points = points,
            color = step.previewColor,
            brush = brush,
            alpha = 0.55f,
            category = step.targetCategory
        )

        var updatedMakeup = state.makeup.copy(
            paintedStrokes = (state.makeup.paintedStrokes + newStroke).takeLast(40)
        )
        if (newProgress >= 0.99f) {
            updatedMakeup = step.applyStep(updatedMakeup).copy(paintedStrokes = emptyList())
        }

        _uiState.update {
            it.copy(
                makeup = updatedMakeup,
                stepProgress = newProgress,
                coachFeedback = feedback
            )
        }
    }

    fun onLessonTapPoint(point: Offset, brush: BrushTool) {
        val state = _uiState.value
        val lesson = state.activeLesson ?: return
        val step = lesson.steps.getOrNull(state.activeStepIndex) ?: return

        if (brush != step.requiredBrush) {
            _uiState.update {
                it.copy(
                    coachFeedback = StrokeFeedback(
                        message = "Elige ${step.requiredBrush.displayName} para este paso.",
                        isPositive = false,
                        progressGain = 0f
                    )
                )
            }
            return
        }

        val isDabbingStep = step.requiredBrush == BrushTool.SPONGE
        val gain = if (isDabbingStep && isPointNearGuide(point, step.guideNormalizedPoints, 0.16f)) 0.35f else 0.12f
        val newProgress = (state.stepProgress + gain).coerceIn(0f, 1f)
        val newStroke = PaintedBrushStroke(
            points = listOf(point),
            color = step.previewColor,
            brush = brush,
            alpha = 0.55f,
            category = step.targetCategory
        )
        var updatedMakeup = state.makeup.copy(
            paintedStrokes = (state.makeup.paintedStrokes + newStroke).takeLast(40)
        )
        if (newProgress >= 0.99f) {
            updatedMakeup = step.applyStep(updatedMakeup).copy(paintedStrokes = emptyList())
        }

        val msg = if (isDabbingStep) {
            "¡Excelente toque suave con la esponja! Continúa dando toquecitos."
        } else {
            "Desliza el pincel siguiendo las líneas animadas."
        }
        _uiState.update {
            it.copy(
                makeup = updatedMakeup,
                stepProgress = newProgress,
                coachFeedback = StrokeFeedback(msg, isPositive = isDabbingStep, progressGain = gain)
            )
        }
    }

    fun autoDemonstrateStep() {
        val state = _uiState.value
        val lesson = state.activeLesson ?: return
        val step = lesson.steps.getOrNull(state.activeStepIndex) ?: return

        _uiState.update {
            it.copy(
                makeup = step.applyStep(it.makeup).copy(paintedStrokes = emptyList()),
                stepProgress = 1f,
                coachFeedback = StrokeFeedback(
                    message = "¡Demostración aplicada! Observa cómo el ${step.requiredBrush.motionPattern.title.lowercase()} transforma la zona.",
                    isPositive = true,
                    progressGain = 1f
                )
            )
        }
    }

    fun advanceLessonStep() {
        val state = _uiState.value
        val lesson = state.activeLesson ?: return
        val currentStep = lesson.steps.getOrNull(state.activeStepIndex)
        val ensuredMakeup = currentStep?.applyStep(state.makeup)?.copy(paintedStrokes = emptyList()) ?: state.makeup

        if (state.activeStepIndex + 1 < lesson.steps.size) {
            val nextIdx = state.activeStepIndex + 1
            val nextStep = lesson.steps[nextIdx]
            _uiState.update {
                it.copy(
                    makeup = ensuredMakeup,
                    activeStepIndex = nextIdx,
                    stepProgress = 0f,
                    activeBrush = nextStep.requiredBrush,
                    activeMakeupCategory = nextStep.targetCategory,
                    activePigmentColor = nextStep.previewColor,
                    coachFeedback = StrokeFeedback(
                        message = "Siguiente técnica: ${nextStep.requiredBrush.motionPattern.title}",
                        isPositive = true,
                        progressGain = 0f
                    )
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    makeup = ensuredMakeup,
                    isLessonCompletedDialogVisible = true
                )
            }
            viewModelScope.launch {
                prefs.markLessonCompleted(lesson.id)
            }
        }
    }

    private fun evaluateBrushGesture(
        points: List<Offset>,
        brush: BrushTool,
        step: LessonStep?
    ): StrokeFeedback {
        if (points.size < 2) {
            return StrokeFeedback("Sigue trazando con el pincel.", true, 0.1f)
        }

        val start = points.first()
        val end = points.last()
        var pathLen = 0f
        var minX = 1f
        var maxX = 0f
        var minY = 1f
        var maxY = 0f
        for (i in points.indices) {
            val p = points[i]
            if (p.x < minX) minX = p.x
            if (p.x > maxX) maxX = p.x
            if (p.y < minY) minY = p.y
            if (p.y > maxY) maxY = p.y
            if (i > 0) {
                pathLen += hypot(p.x - points[i - 1].x, p.y - points[i - 1].y)
            }
        }
        val boxDiag = hypot(maxX - minX, maxY - minY).coerceAtLeast(0.01f)
        val endToEndDist = hypot(end.x - start.x, end.y - start.y)
        val nearGuide = step == null || points.any { isPointNearGuide(it, step.guideNormalizedPoints, 0.17f) }

        if (!nearGuide) {
            return StrokeFeedback(
                message = "Acércate a la zona iluminada por la guía animada.",
                isPositive = false,
                progressGain = 0.08f
            )
        }

        return when (brush.motionPattern) {
            BrushMotionPattern.CIRCULAR -> {
                val ratio = pathLen / (endToEndDist + 0.02f)
                if (ratio > 2.0f && pathLen > 0.12f) {
                    StrokeFeedback("¡Excelente movimiento circular! El rubor se funde sin bordes.", true, 0.52f)
                } else {
                    StrokeFeedback("Intenta girar en pequeños círculos continuos.", true, 0.28f)
                }
            }
            BrushMotionPattern.UPWARD_SWEEP -> {
                val movedUp = end.y < start.y - 0.02f
                if (movedUp && boxDiag > 0.06f) {
                    StrokeFeedback("¡Barrido ascendente perfecto! Estiliza y eleva el pómulo.", true, 0.55f)
                } else {
                    StrokeFeedback("Desliza hacia arriba rumbo a la sien para lograr efecto lifting.", true, 0.28f)
                }
            }
            BrushMotionPattern.WINDSHIELD_ARCH -> {
                val horizSpan = abs(maxX - minX)
                if (horizSpan > 0.07f && pathLen > 0.10f) {
                    StrokeFeedback("¡Vaivén tipo parabrisas impecable! Degradado suave en la cuenca.", true, 0.52f)
                } else {
                    StrokeFeedback("Mueve el pincel de lado a lado sobre el pliegue del ojo.", true, 0.28f)
                }
            }
            BrushMotionPattern.CAT_EYE_FLICK -> {
                if (boxDiag > 0.06f) {
                    StrokeFeedback("¡Trazo firme y diagonal! El ala enmarca la mirada.", true, 0.55f)
                } else {
                    StrokeFeedback("Alarga el trazo hacia el extremo exterior del ojo.", true, 0.30f)
                }
            }
            BrushMotionPattern.DABBING_TAPS -> {
                StrokeFeedback("Buen toque suave. Presiona ligeramente sin arrastrar.", true, 0.36f)
            }
            BrushMotionPattern.LIP_CONTOUR -> {
                if (abs(maxX - minX) > 0.06f) {
                    StrokeFeedback("¡Contorno de Cupido definido con precisión editorial!", true, 0.55f)
                } else {
                    StrokeFeedback("Recorre de lado a lado siguiendo la forma natural del labio.", true, 0.30f)
                }
            }
        }
    }

    private fun isPointNearGuide(point: Offset, guide: List<Offset>, threshold: Float): Boolean {
        return guide.any { hypot(point.x - it.x, point.y - it.y) <= threshold }
    }
}
