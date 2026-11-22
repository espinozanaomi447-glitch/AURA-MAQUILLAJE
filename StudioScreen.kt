package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.*
import com.example.ui.canvas.ModelCanvas
import com.example.ui.viewmodel.AtelierUiState
import com.example.ui.viewmodel.AtelierViewModel
import com.example.ui.viewmodel.StudioDockTab

@Composable
fun StudioScreen(
    uiState: AtelierUiState,
    viewModel: AtelierViewModel,
    modifier: Modifier = Modifier
) {
    val activeGuidePoints = rememberGuidePointsForCategory(uiState.activeMakeupCategory)
    val effectiveMakeup = if (uiState.isComparingOriginal) MakeupState() else uiState.makeup

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Studio Stage + Top Overlay Controls
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(26.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_runway_stage),
                contentDescription = "Estudio Aura Atelier",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            ModelCanvas(
                model = uiState.selectedModel,
                hairStyle = uiState.hairStyle,
                hairColor = uiState.hairColor,
                makeup = effectiveMakeup,
                wardrobe = uiState.wardrobe,
                zoomMode = uiState.zoomMode,
                activeBrush = uiState.activeBrush,
                activePigment = uiState.activePigmentColor,
                isInteractivePaintingEnabled = uiState.zoomMode == ViewZoomMode.FACE_CLOSEUP && !uiState.isComparingOriginal,
                showGuideOverlay = uiState.showBrushGuides &&
                    uiState.zoomMode == ViewZoomMode.FACE_CLOSEUP &&
                    (uiState.activeDockTab == StudioDockTab.BRUSHES || uiState.activeDockTab == StudioDockTab.MAKEUP),
                guidePoints = activeGuidePoints,
                guideMotion = uiState.activeBrush.motionPattern,
                portraitPoseMode = uiState.portraitPoseMode,
                onStrokeCompleted = { pts, brush -> viewModel.onStudioStrokeCompleted(pts, brush) },
                onTapPoint = { pt, brush -> viewModel.onStudioTapPoint(pt, brush) },
                modifier = Modifier.fillMaxSize()
            )

            // Top Floating Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Zoom Toggle (Rostro vs Cuerpo)
                Surface(
                    color = Color(0xCC23111D),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        val isFace = uiState.zoomMode == ViewZoomMode.FACE_CLOSEUP
                        FilledTonalButton(
                            onClick = { viewModel.setZoomMode(ViewZoomMode.FACE_CLOSEUP) },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (isFace) MaterialTheme.colorScheme.primary else Color.Transparent,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("zoom_face_btn")
                        ) {
                            Text("Rostro", style = MaterialTheme.typography.labelMedium)
                        }
                        FilledTonalButton(
                            onClick = { viewModel.setZoomMode(ViewZoomMode.FULL_BODY) },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (!isFace) MaterialTheme.colorScheme.primary else Color.Transparent,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("zoom_body_btn")
                        ) {
                            Text("Cuerpo", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledIconButton(
                        onClick = { viewModel.undoLastPaintedStroke() },
                        enabled = uiState.makeup.paintedStrokes.isNotEmpty(),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xCC23111D),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("studio_undo_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.Undo, contentDescription = "Deshacer trazo")
                    }

                    FilledIconButton(
                        onClick = { viewModel.setComparingOriginal(!uiState.isComparingOriginal) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (uiState.isComparingOriginal) MaterialTheme.colorScheme.primary else Color(0xCC23111D),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("studio_compare_btn")
                    ) {
                        Icon(Icons.Rounded.Compare, contentDescription = "Comparar antes y después")
                    }

                    FilledIconButton(
                        onClick = { viewModel.clearAllMakeup() },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xCC23111D),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("studio_clear_btn")
                    ) {
                        Icon(Icons.Rounded.CleaningServices, contentDescription = "Limpiar maquillaje")
                    }
                }
            }

            // Bottom Canvas Overlay: Model Pill + Runway Showcase CTA
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = { viewModel.setActiveDockTab(StudioDockTab.MODELS) },
                    color = Color(0xCC23111D),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.testTag("studio_model_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Face,
                            contentDescription = null,
                            tint = Color(0xFFF5E6CC),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = uiState.selectedModel.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }
                }

                if (uiState.coachFeedback != null && uiState.zoomMode == ViewZoomMode.FACE_CLOSEUP) {
                    uiState.coachFeedback.let { fb ->
                        Surface(
                            color = Color(0xCC23111D),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.widthIn(max = 180.dp)
                        ) {
                            Text(
                                text = fb.message,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFF5E6CC),
                                maxLines = 2,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = { viewModel.setRunwayShowcaseVisible(true) },
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("studio_runway_btn")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pasarela", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        // Bottom Interactive Styling Tool Tray
        StudioToolTray(
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

private fun rememberGuidePointsForCategory(category: MakeupCategory): List<Offset> {
    return when (category) {
        MakeupCategory.BASE_CONTOUR -> listOf(
            Offset(0.35f, 0.56f), Offset(0.26f, 0.49f),
            Offset(0.65f, 0.56f), Offset(0.74f, 0.49f)
        )
        MakeupCategory.EYESHADOW -> listOf(
            Offset(0.27f, 0.39f), Offset(0.35f, 0.355f), Offset(0.43f, 0.39f),
            Offset(0.57f, 0.39f), Offset(0.65f, 0.355f), Offset(0.73f, 0.39f)
        )
        MakeupCategory.EYELINER -> listOf(
            Offset(0.41f, 0.415f), Offset(0.25f, 0.37f),
            Offset(0.59f, 0.415f), Offset(0.75f, 0.37f)
        )
        MakeupCategory.BROWS_LASHES -> listOf(
            Offset(0.42f, 0.335f), Offset(0.32f, 0.305f), Offset(0.23f, 0.34f),
            Offset(0.58f, 0.335f), Offset(0.68f, 0.305f), Offset(0.77f, 0.34f)
        )
        MakeupCategory.BLUSH -> listOf(
            Offset(0.29f, 0.52f), Offset(0.35f, 0.51f), Offset(0.36f, 0.56f), Offset(0.29f, 0.55f),
            Offset(0.71f, 0.52f), Offset(0.65f, 0.51f), Offset(0.64f, 0.56f), Offset(0.71f, 0.55f)
        )
        MakeupCategory.LIPS -> listOf(
            Offset(0.41f, 0.68f), Offset(0.47f, 0.66f), Offset(0.50f, 0.675f),
            Offset(0.53f, 0.66f), Offset(0.59f, 0.68f)
        )
        MakeupCategory.GEMS -> listOf(
            Offset(0.43f, 0.41f), Offset(0.57f, 0.41f)
        )
    }
}
