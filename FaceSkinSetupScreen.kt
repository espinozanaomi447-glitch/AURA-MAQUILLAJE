package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AtelierCatalog
import com.example.model.*
import com.example.ui.canvas.ModelCanvas
import com.example.ui.components.ColorSwatchButton

@Composable
fun FaceSkinSetupScreen(
    initialFaceShape: FaceShape,
    initialSkinPreset: SkinTonePreset,
    initialEyeColor: Color,
    initialHairStyle: HairStyle,
    initialHairColor: Color,
    allowBackDismiss: Boolean,
    onDismissBack: () -> Unit,
    onConfirmToAcademy: (WomanModel, HairStyle, Color, SkinTonePreset) -> Unit,
    onConfirmToStudio: (WomanModel, HairStyle, Color, SkinTonePreset) -> Unit,
    modifier: Modifier = Modifier
) {
    if (allowBackDismiss) {
        BackHandler { onDismissBack() }
    }

    var selectedFace by remember { mutableStateOf(initialFaceShape) }
    var selectedSkin by remember { mutableStateOf(initialSkinPreset) }
    var selectedEyeColor by remember { mutableStateOf(initialEyeColor) }
    var selectedHairStyle by remember { mutableStateOf(initialHairStyle) }
    var selectedHairColor by remember { mutableStateOf(initialHairColor) }

    val previewModel = remember(selectedFace, selectedSkin, selectedEyeColor, selectedHairStyle, selectedHairColor) {
        AtelierCatalog.buildCustomModel(
            faceShape = selectedFace,
            skin = selectedSkin,
            eyeColor = selectedEyeColor,
            hairStyle = selectedHairStyle,
            hairColor = selectedHairColor
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Live Portrait Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(242.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(26.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_runway_stage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xD823111D), Color(0x4023111D), Color(0x1023111D))
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1.05f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = Color(0x44FFFFFF),
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Face,
                                contentDescription = null,
                                tint = Color(0xFFF5E6CC),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Asesoría Inicial",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tu Rostro y Piel",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${selectedFace.displayName} • ${selectedSkin.name}",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFFF5E6CC)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = selectedSkin.undertone.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFE7D0D9)
                    )
                }

                // Live Interactive Realistic Preview
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    ModelCanvas(
                        model = previewModel,
                        hairStyle = selectedHairStyle,
                        hairColor = selectedHairColor,
                        makeup = MakeupState(
                            lipPattern = LipPattern.MIRROR_GLOSS,
                            lipColor = selectedSkin.naturalLipColor,
                            blushPattern = BlushPattern.APPLE_CIRCULAR,
                            blushColor = Color(0xFFEA8C7D),
                            blushIntensity = 0.4f
                        ),
                        wardrobe = WardrobeState(),
                        zoomMode = ViewZoomMode.FACE_CLOSEUP,
                        activeBrush = BrushTool.KABUKI,
                        activePigment = Color.Transparent,
                        isInteractivePaintingEnabled = false,
                        showGuideOverlay = false,
                        guidePoints = emptyList(),
                        guideMotion = null,
                        portraitPoseMode = previewModel.defaultPortraitPose,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Scrollable Customizer Form
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Face Shape Selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "1. Elige el estilo / forma de cara",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(FaceShape.entries) { shape ->
                        val isSelected = shape == selectedFace
                        Surface(
                            onClick = { selectedFace = shape },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.testTag("setup_face_${shape.name}")
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 24.dp, height = 30.dp)
                                        .clip(
                                            when (shape) {
                                                FaceShape.OVAL -> CircleShape
                                                FaceShape.ROUND -> RoundedCornerShape(50)
                                                FaceShape.SQUARE -> RoundedCornerShape(6.dp)
                                                FaceShape.HEART -> RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 18.dp, bottomEnd = 18.dp)
                                                FaceShape.DIAMOND -> RoundedCornerShape(10.dp)
                                            }
                                        )
                                        .background(selectedSkin.skinTone)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = shape.displayName,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // 2. Skin Tone & Undertone Selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "2. Elige el tono y subtono de piel",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(AtelierCatalog.skinTones) { preset ->
                        val isSelected = preset.id == selectedSkin.id
                        Surface(
                            onClick = { selectedSkin = preset },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.testTag("setup_skin_${preset.id}")
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                listOf(preset.skinHighlight, preset.skinTone, preset.skinShadow)
                                            )
                                        )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = preset.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = preset.undertone.displayName.removePrefix("Subtono "),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // 3. Eye Color & Hair Style quick picker
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "3. Color de ojos y peinado base",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(AtelierCatalog.eyeColors) { (name, color) ->
                        ColorSwatchButton(
                            color = color,
                            isSelected = color == selectedEyeColor,
                            contentDescription = "Ojos $name",
                            testTag = "setup_eye_$name",
                            onClick = { selectedEyeColor = color }
                        )
                    }
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(HairStyle.entries) { style ->
                        FilterChip(
                            selected = style == selectedHairStyle,
                            onClick = { selectedHairStyle = style },
                            label = { Text(style.displayName) }
                        )
                    }
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(AtelierCatalog.hairColors) { hc ->
                        ColorSwatchButton(
                            color = hc,
                            isSelected = hc == selectedHairColor,
                            contentDescription = "Color cabello",
                            testTag = "setup_hair_${hc.value}",
                            onClick = { selectedHairColor = hc }
                        )
                    }
                }
            }

            // Personalized Recommendation Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tu Asesoría de Color y Rostro",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Rostro ${selectedFace.displayName}: ${selectedFace.beautyTip}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Piel ${selectedSkin.name}: ${selectedSkin.colorTip}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }

        // Bottom CTAs
        Surface(
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onConfirmToStudio(previewModel, selectedHairStyle, selectedHairColor, selectedSkin)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("setup_go_studio_btn")
                ) {
                    Icon(Icons.Rounded.Brush, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ir al Estudio")
                }

                Button(
                    onClick = {
                        onConfirmToAcademy(previewModel, selectedHairStyle, selectedHairColor, selectedSkin)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("setup_go_academy_btn")
                ) {
                    Icon(Icons.Rounded.School, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Aprender")
                }
            }
        }
    }
}
