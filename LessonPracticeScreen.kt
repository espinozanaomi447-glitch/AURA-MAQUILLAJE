package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoFixHigh
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AtelierCatalog
import com.example.model.BrushTool
import com.example.model.ViewZoomMode
import com.example.ui.canvas.ModelCanvas
import com.example.ui.components.BrushMotionDiagram
import com.example.ui.viewmodel.AtelierUiState

@Composable
fun LessonPracticeScreen(
    uiState: AtelierUiState,
    onSelectBrush: (BrushTool) -> Unit,
    onCycleModel: () -> Unit,
    onStrokeCompleted: (List<androidx.compose.ui.geometry.Offset>, BrushTool) -> Unit,
    onTapPoint: (androidx.compose.ui.geometry.Offset, BrushTool) -> Unit,
    onAutoDemo: () -> Unit,
    onNextStep: () -> Unit,
    onExitLesson: () -> Unit,
    onGoToFullBodyStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onExitLesson() }

    val lesson = uiState.activeLesson ?: return
    val step = lesson.steps.getOrNull(uiState.activeStepIndex) ?: return
    val isStepDone = uiState.stepProgress >= 0.99f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onExitLesson,
                modifier = Modifier.testTag("lesson_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Volver a la Academia"
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    lesson.steps.forEachIndexed { idx, _ ->
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (idx == uiState.activeStepIndex) 22.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        idx < uiState.activeStepIndex -> MaterialTheme.colorScheme.secondary
                                        idx == uiState.activeStepIndex -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.outlineVariant
                                    }
                                )
                        )
                    }
                }
            }

            FilledTonalButton(
                onClick = onCycleModel,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("lesson_switch_model_btn")
            ) {
                Icon(
                    imageVector = Icons.Rounded.Face,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(uiState.selectedModel.name, style = MaterialTheme.typography.labelMedium)
            }
        }

        // Interactive Face Canvas Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .clip(RoundedCornerShape(26.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_runway_stage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            ModelCanvas(
                model = uiState.selectedModel,
                hairStyle = uiState.hairStyle,
                hairColor = uiState.hairColor,
                makeup = uiState.makeup,
                wardrobe = uiState.wardrobe,
                zoomMode = ViewZoomMode.FACE_CLOSEUP,
                activeBrush = uiState.activeBrush,
                activePigment = step.previewColor,
                isInteractivePaintingEnabled = true,
                showGuideOverlay = !isStepDone,
                guidePoints = step.guideNormalizedPoints,
                guideMotion = step.requiredBrush.motionPattern,
                portraitPoseMode = uiState.portraitPoseMode,
                onStrokeCompleted = onStrokeCompleted,
                onTapPoint = onTapPoint,
                modifier = Modifier.fillMaxSize()
            )

            // Floating Coaching Feedback Pill
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(12.dp)
            ) {
                uiState.coachFeedback?.let { fb ->
                    Surface(
                        color = if (fb.isPositive) Color(0xEE23111D) else Color(0xEE8B2635),
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isStepDone) Icons.Rounded.CheckCircle else Icons.Rounded.Lightbulb,
                                contentDescription = null,
                                tint = Color(0xFFF5E6CC),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = fb.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Progress Badge bottom-left of canvas
            Surface(
                color = Color(0xDD23111D),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isStepDone) "¡Técnica lograda!" else "Progreso del trazo: ${(uiState.stepProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    LinearProgressIndicator(
                        progress = { uiState.stepProgress },
                        modifier = Modifier
                            .width(72.dp)
                            .height(6.dp)
                            .clip(CircleShape),
                        color = Color(0xFFF5E6CC),
                        trackColor = Color(0x44FFFFFF)
                    )
                }
            }
        }

        // Bottom Coaching & Brush Tray
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = step.instruction,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tip pro: ${step.proTip}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Brush Picker showing required brush highlight
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(BrushTool.entries) { brush ->
                        val isSelected = brush == uiState.activeBrush
                        val isRequired = brush == step.requiredBrush
                        Surface(
                            onClick = { onSelectBrush(brush) },
                            shape = RoundedCornerShape(14.dp),
                            color = when {
                                isSelected -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    isRequired -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                }
                            ),
                            modifier = Modifier.testTag("lesson_brush_${brush.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BrushMotionDiagram(
                                    motion = brush.motionPattern,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = brush.displayName,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (isRequired) "Recomendada" else brush.motionPattern.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isRequired) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onAutoDemo,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("lesson_auto_demo_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoFixHigh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Demostración")
                    }

                    Button(
                        onClick = onNextStep,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("lesson_next_step_btn")
                    ) {
                        Text(
                            if (uiState.activeStepIndex + 1 < lesson.steps.size) "Siguiente paso"
                            else "Finalizar"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (uiState.isLessonCompletedDialogVisible) {
        AlertDialog(
            onDismissRequest = onExitLesson,
            title = {
                Text("¡Lección completada!", style = MaterialTheme.typography.headlineMedium)
            },
            text = {
                Text(
                    "Has dominado las técnicas de ${lesson.title} sobre ${uiState.selectedModel.name}. ¿Quieres pasar al Estudio para combinarlo con ropa, zapatos y joyería?"
                )
            },
            confirmButton = {
                Button(
                    onClick = onGoToFullBodyStudio,
                    modifier = Modifier.testTag("lesson_done_go_studio_btn")
                ) {
                    Text("Vestir en Estudio")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onExitLesson,
                    modifier = Modifier.testTag("lesson_done_back_academy_btn")
                ) {
                    Text("Volver a Academia")
                }
            }
        )
    }
}
