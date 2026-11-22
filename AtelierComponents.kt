package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.model.*
import com.example.ui.canvas.ModelCanvas

@Composable
fun BrushMotionDiagram(
    motion: BrushMotionPattern,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(34.dp)) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = 2.6f, cap = StrokeCap.Round)

        when (motion) {
            BrushMotionPattern.CIRCULAR -> {
                drawArc(
                    color = tint,
                    startAngle = 20f,
                    sweepAngle = 290f,
                    useCenter = false,
                    style = stroke
                )
                drawCircle(color = tint, radius = 3.5f, center = Offset(w * 0.78f, h * 0.30f))
            }
            BrushMotionPattern.UPWARD_SWEEP -> {
                drawLine(tint, Offset(w * 0.20f, h * 0.80f), Offset(w * 0.80f, h * 0.20f), strokeWidth = 3f, cap = StrokeCap.Round)
                drawLine(tint, Offset(w * 0.50f, h * 0.20f), Offset(w * 0.80f, h * 0.20f), strokeWidth = 3f, cap = StrokeCap.Round)
                drawLine(tint, Offset(w * 0.80f, h * 0.20f), Offset(w * 0.80f, h * 0.50f), strokeWidth = 3f, cap = StrokeCap.Round)
            }
            BrushMotionPattern.WINDSHIELD_ARCH -> {
                val path = Path().apply {
                    moveTo(w * 0.14f, h * 0.70f)
                    quadraticTo(w * 0.50f, h * 0.18f, w * 0.86f, h * 0.70f)
                }
                drawPath(path, tint, style = stroke)
                drawCircle(tint, radius = 3f, center = Offset(w * 0.14f, h * 0.70f))
                drawCircle(tint, radius = 3f, center = Offset(w * 0.86f, h * 0.70f))
            }
            BrushMotionPattern.CAT_EYE_FLICK -> {
                val path = Path().apply {
                    moveTo(w * 0.14f, h * 0.68f)
                    quadraticTo(w * 0.52f, h * 0.65f, w * 0.86f, h * 0.24f)
                }
                drawPath(path, tint, style = stroke)
            }
            BrushMotionPattern.DABBING_TAPS -> {
                drawCircle(tint, radius = 3.2f, center = Offset(w * 0.28f, h * 0.58f))
                drawCircle(tint, radius = 4.2f, center = Offset(w * 0.52f, h * 0.36f))
                drawCircle(tint, radius = 3.2f, center = Offset(w * 0.74f, h * 0.60f))
                drawCircle(
                    color = tint.copy(alpha = 0.45f),
                    radius = 9f,
                    center = Offset(w * 0.52f, h * 0.36f),
                    style = Stroke(width = 1.6f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f)))
                )
            }
            BrushMotionPattern.LIP_CONTOUR -> {
                val path = Path().apply {
                    moveTo(w * 0.14f, h * 0.62f)
                    lineTo(w * 0.38f, h * 0.36f)
                    lineTo(w * 0.50f, h * 0.48f)
                    lineTo(w * 0.62f, h * 0.36f)
                    lineTo(w * 0.86f, h * 0.62f)
                }
                drawPath(path, tint, style = stroke)
            }
        }
    }
}

@Composable
fun ColorSwatchButton(
    color: Color,
    isSelected: Boolean,
    contentDescription: String,
    testTag: String,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
        label = "swatchBorder"
    )
    Box(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag(testTag)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(4.dp)
            .size(38.dp)
            .border(width = if (isSelected) 2.5.dp else 1.dp, color = borderColor, shape = CircleShape)
            .padding(3.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = contentDescription,
                tint = if (color.red * 0.3f + color.green * 0.6f + color.blue * 0.1f > 0.65f) Color(0xFF23111D) else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ModelSelectorCard(
    model: WomanModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .width(126.dp)
            .testTag("model_card_${model.id}")
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val portraitRes = model.visualAssets?.faceRes
            if (portraitRes != null) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = CircleShape
                        )
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = portraitRes),
                        contentDescription = model.name,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                // Fallback stylized miniature skin + hair emblem
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(model.defaultHairColor)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(model.skinTone),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .size(width = 14.dp, height = 5.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(model.naturalLipColor)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = model.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = model.subtitle.substringBefore(" •"),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RunwayShowcaseDialog(
    model: WomanModel,
    hairStyle: HairStyle,
    hairColor: Color,
    makeup: MakeupState,
    wardrobe: WardrobeState,
    appliedLookTitle: String?,
    onDismiss: () -> Unit,
    onSwitchModel: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_runway_stage),
                    contentDescription = "Escenario de pasarela",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0x5523111D),
                                    Color(0x1023111D),
                                    Color(0xCC23111D)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xCC23111D),
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFFF5E6CC),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = appliedLookTitle ?: "Look Personalizado",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .testTag("close_runway_button")
                                .background(Color(0xAA23111D), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Cerrar pasarela",
                                tint = Color.White
                            )
                        }
                    }

                    ModelCanvas(
                        model = model,
                        hairStyle = hairStyle,
                        hairColor = hairColor,
                        makeup = makeup,
                        wardrobe = wardrobe,
                        zoomMode = ViewZoomMode.FULL_BODY,
                        activeBrush = BrushTool.KABUKI,
                        activePigment = Color.Transparent,
                        isInteractivePaintingEnabled = false,
                        showGuideOverlay = false,
                        guidePoints = emptyList(),
                        guideMotion = null,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xEEFDF8F5),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${model.name} • ${wardrobe.outfitStyle.displayName}",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF23111D),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${makeup.eyeshadowPattern.displayName} • ${makeup.blushPattern.displayName} • ${wardrobe.shoeStyle.displayName}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF745D68)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onSwitchModel,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("runway_switch_model_button")
                                ) {
                                    Text("Otra modelo")
                                }
                                Button(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("runway_keep_styling_button")
                                ) {
                                    Text("Seguir editando")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
