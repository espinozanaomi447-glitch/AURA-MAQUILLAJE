package com.example

import android.app.Application
import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.test.core.app.ApplicationProvider
import com.example.data.AtelierCatalog
import com.example.model.*
import com.example.ui.viewmodel.AtelierViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Aura Atelier", appName)
    }

    @Test
    fun `custom face shape and skin tone setup updates active model and dismisses setup`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = AtelierViewModel(app)
        assertTrue(vm.uiState.value.showFaceSkinSetup)

        val skin = AtelierCatalog.skinTones.last()
        val custom = AtelierCatalog.buildCustomModel(
            faceShape = FaceShape.SQUARE,
            skin = skin,
            eyeColor = AtelierCatalog.eyeColors[2].second,
            hairStyle = HairStyle.HALF_UP_GLAM,
            hairColor = Color(0xFF732544)
        )
        vm.applyCustomFaceAndSkin(custom, HairStyle.HALF_UP_GLAM, Color(0xFF732544), skin)

        assertFalse(vm.uiState.value.showFaceSkinSetup)
        assertEquals(FaceShape.SQUARE, vm.uiState.value.selectedModel.faceShape)
        assertEquals(skin.skinTone, vm.uiState.value.selectedModel.skinTone)
        assertTrue(OutfitStyle.entries.size >= 12)
        assertTrue(ShoeStyle.entries.size >= 10)
    }

    @Test
    fun `academy lesson coaching advances with correct brush stroke`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = AtelierViewModel(app)
        val lesson = AtelierCatalog.lessons.first()
        vm.startLesson(lesson)

        val firstStep = lesson.steps.first()
        assertEquals(firstStep.requiredBrush, vm.uiState.value.activeBrush)

        vm.onLessonTapPoint(firstStep.guideNormalizedPoints.first(), BrushTool.SPONGE)
        assertTrue(vm.uiState.value.stepProgress > 0f)

        vm.autoDemonstrateStep()
        vm.advanceLessonStep()
        assertEquals(1, vm.uiState.value.activeStepIndex)

        val step2 = lesson.steps[1]
        vm.onLessonStrokeCompleted(
            listOf(
                Offset(0.28f, 0.40f),
                Offset(0.36f, 0.37f),
                Offset(0.43f, 0.40f)
            ),
            step2.requiredBrush
        )
        assertTrue(vm.uiState.value.stepProgress > 0.4f)
    }

    @Test
    fun `switching models and applying lookbook updates wardrobe and makeup`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = AtelierViewModel(app)

        val zara = AtelierCatalog.models.first { it.id == "zara" }
        vm.selectModel(zara)
        assertEquals("Zara", vm.uiState.value.selectedModel.name)

        val look = AtelierCatalog.lookbooks.last()
        vm.applyLookbook(look)
        assertEquals(look.wardrobe.outfitStyle, vm.uiState.value.wardrobe.outfitStyle)
        assertEquals(look.wardrobe.shoeStyle, vm.uiState.value.wardrobe.shoeStyle)
        assertEquals(look.makeup.lipPattern, vm.uiState.value.makeup.lipPattern)
    }

    @Test
    fun `illustrated model assets and portrait pose switching work across all models`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = AtelierViewModel(app)

        assertEquals(7, AtelierCatalog.models.size)
        AtelierCatalog.models.forEach { model ->
            val assets = model.visualAssets!!
            assertTrue(assets.bodyRes != 0)
            assertTrue(assets.faceRes != 0)
            assertTrue(assets.bunRes != 0)
            assertTrue(assets.faceLandmarks.eyeRight.x > assets.faceLandmarks.eyeLeft.x)
            assertTrue(assets.faceLandmarks.mouthCenter.y > assets.faceLandmarks.eyeLeft.y)
        }

        vm.setPortraitPoseMode(PortraitPoseMode.HIGH_BUN)
        assertEquals(PortraitPoseMode.HIGH_BUN, vm.uiState.value.portraitPoseMode)

        vm.setPortraitPoseMode(PortraitPoseMode.SPA_HEADBAND)
        assertEquals(PortraitPoseMode.SPA_HEADBAND, vm.uiState.value.portraitPoseMode)
        assertEquals(HeadAccessoryStyle.SATIN_HEADBAND, vm.uiState.value.wardrobe.headAccessoryStyle)

        vm.setHairStyle(HairStyle.LONG_WAVES)
        assertEquals(PortraitPoseMode.LOOSE_HAIR, vm.uiState.value.portraitPoseMode)
    }
}
