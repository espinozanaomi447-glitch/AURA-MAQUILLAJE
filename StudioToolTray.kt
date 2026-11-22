package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.AtelierCatalog
import com.example.model.*
import com.example.ui.components.BrushMotionDiagram
import com.example.ui.components.ColorSwatchButton
import com.example.ui.components.ModelSelectorCard
import com.example.ui.viewmodel.AtelierUiState
import com.example.ui.viewmodel.AtelierViewModel
import com.example.ui.viewmodel.StudioDockTab

@Composable
fun StudioToolTray(
    uiState: AtelierUiState,
    viewModel: AtelierViewModel,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(top = 10.dp, bottom = 12.dp)) {
            // Primary Dock Tabs
            LazyRow(
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(StudioDockTab.entries) { tab ->
                    val selected = tab == uiState.activeDockTab
                    FilterChip(
                        selected = selected,
                        onClick = { viewModel.setActiveDockTab(tab) },
                        label = { Text(tab.displayName) },
                        modifier = Modifier.testTag("studio_tab_${tab.name}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dock Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 166.dp, max = 212.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (uiState.activeDockTab) {
                    StudioDockTab.MAKEUP -> MakeupTrayContent(uiState, viewModel)
                    StudioDockTab.BRUSHES -> BrushesTrayContent(uiState, viewModel)
                    StudioDockTab.OUTFITS -> OutfitsTrayContent(uiState, viewModel)
                    StudioDockTab.SHOES -> ShoesTrayContent(uiState, viewModel)
                    StudioDockTab.JEWELRY -> JewelryTrayContent(uiState, viewModel)
                    StudioDockTab.MODELS -> ModelsTrayContent(uiState, viewModel)
                }
            }
        }
    }
}

@Composable
private fun MakeupTrayContent(uiState: AtelierUiState, viewModel: AtelierViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Makeup Categories
        LazyRow(
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(MakeupCategory.entries) { cat ->
                val selected = cat == uiState.activeMakeupCategory
                SuggestionChip(
                    onClick = { viewModel.setActiveMakeupCategory(cat) },
                    label = { Text(cat.displayName) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("makeup_cat_${cat.name}")
                )
            }
        }

        when (uiState.activeMakeupCategory) {
            MakeupCategory.BASE_CONTOUR -> {
                PatternChipRow(
                    items = BaseContourStyle.entries,
                    selected = uiState.makeup.baseStyle,
                    labelOf = { it.displayName },
                    onSelect = { style -> viewModel.updateMakeup { it.copy(baseStyle = style) } }
                )
                ColorSwatchRow(
                    colors = AtelierCatalog.highlighterColors,
                    selectedColor = uiState.makeup.highlighterColor,
                    onSelect = { c ->
                        viewModel.setActivePigmentColor(c)
                        viewModel.updateMakeup { it.copy(highlighterColor = c) }
                    }
                )
                IntensitySliderRow(
                    label = "Intensidad",
                    value = uiState.makeup.baseIntensity,
                    onValueChange = { v -> viewModel.updateMakeup { it.copy(baseIntensity = v) } }
                )
            }

            MakeupCategory.EYESHADOW -> {
                PatternChipRow(
                    items = EyeshadowPattern.entries,
                    selected = uiState.makeup.eyeshadowPattern,
                    labelOf = { it.displayName },
                    onSelect = { pattern -> viewModel.updateMakeup { it.copy(eyeshadowPattern = pattern) } }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(AtelierCatalog.eyeshadowPalettes) { pair ->
                        ColorSwatchButton(
                            color = pair.first,
                            isSelected = uiState.makeup.eyeshadowPrimary == pair.first,
                            contentDescription = "Paleta sombra",
                            testTag = "shadow_palette_${pair.first.value}",
                            onClick = {
                                viewModel.setActivePigmentColor(pair.first)
                                viewModel.updateMakeup {
                                    it.copy(
                                        eyeshadowPrimary = pair.first,
                                        eyeshadowSecondary = pair.second,
                                        eyeshadowPattern = if (it.eyeshadowPattern == EyeshadowPattern.NONE) EyeshadowPattern.GRADIENT_SUNSET else it.eyeshadowPattern
                                    )
                                }
                            }
                        )
                    }
                }
                IntensitySliderRow(
                    label = "Intensidad",
                    value = uiState.makeup.eyeshadowIntensity,
                    onValueChange = { v -> viewModel.updateMakeup { it.copy(eyeshadowIntensity = v) } }
                )
            }

            MakeupCategory.EYELINER -> {
                PatternChipRow(
                    items = EyelinerPattern.entries,
                    selected = uiState.makeup.eyelinerPattern,
                    labelOf = { it.displayName },
                    onSelect = { pattern -> viewModel.updateMakeup { it.copy(eyelinerPattern = pattern) } }
                )
                ColorSwatchRow(
                    colors = AtelierCatalog.eyelinerColors,
                    selectedColor = uiState.makeup.eyelinerColor,
                    onSelect = { c ->
                        viewModel.setActivePigmentColor(c)
                        viewModel.updateMakeup {
                            it.copy(
                                eyelinerColor = c,
                                eyelinerPattern = if (it.eyelinerPattern == EyelinerPattern.NONE) EyelinerPattern.CAT_EYE_WING else it.eyelinerPattern
                            )
                        }
                    }
                )
            }

            MakeupCategory.BROWS_LASHES -> {
                PatternChipRow(
                    items = BrowLashStyle.entries,
                    selected = uiState.makeup.browLashStyle,
                    labelOf = { it.displayName },
                    onSelect = { style -> viewModel.updateMakeup { it.copy(browLashStyle = style) } }
                )
            }

            MakeupCategory.BLUSH -> {
                PatternChipRow(
                    items = BlushPattern.entries,
                    selected = uiState.makeup.blushPattern,
                    labelOf = { it.displayName },
                    onSelect = { pattern -> viewModel.updateMakeup { it.copy(blushPattern = pattern) } }
                )
                ColorSwatchRow(
                    colors = AtelierCatalog.blushColors,
                    selectedColor = uiState.makeup.blushColor,
                    onSelect = { c ->
                        viewModel.setActivePigmentColor(c)
                        viewModel.updateMakeup {
                            it.copy(
                                blushColor = c,
                                blushPattern = if (it.blushPattern == BlushPattern.NONE) BlushPattern.APPLE_CIRCULAR else it.blushPattern
                            )
                        }
                    }
                )
                IntensitySliderRow(
                    label = "Intensidad",
                    value = uiState.makeup.blushIntensity,
                    onValueChange = { v -> viewModel.updateMakeup { it.copy(blushIntensity = v) } }
                )
            }

            MakeupCategory.LIPS -> {
                PatternChipRow(
                    items = LipPattern.entries,
                    selected = uiState.makeup.lipPattern,
                    labelOf = { it.displayName },
                    onSelect = { pattern -> viewModel.updateMakeup { it.copy(lipPattern = pattern) } }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(AtelierCatalog.lipColors) { pair ->
                        ColorSwatchButton(
                            color = pair.first,
                            isSelected = uiState.makeup.lipColor == pair.first,
                            contentDescription = "Tono labial",
                            testTag = "lip_color_${pair.first.value}",
                            onClick = {
                                viewModel.setActivePigmentColor(pair.first)
                                viewModel.updateMakeup {
                                    it.copy(
                                        lipColor = pair.first,
                                        lipSecondaryColor = pair.second,
                                        lipPattern = if (it.lipPattern == LipPattern.NONE) LipPattern.VELVET_MATTE else it.lipPattern
                                    )
                                }
                            }
                        )
                    }
                }
                IntensitySliderRow(
                    label = "Intensidad",
                    value = uiState.makeup.lipIntensity,
                    onValueChange = { v -> viewModel.updateMakeup { it.copy(lipIntensity = v) } }
                )
            }

            MakeupCategory.GEMS -> {
                PatternChipRow(
                    items = FaceGemStyle.entries,
                    selected = uiState.makeup.gemStyle,
                    labelOf = { it.displayName },
                    onSelect = { style -> viewModel.updateMakeup { it.copy(gemStyle = style) } }
                )
            }
        }
    }
}

@Composable
private fun IntensitySliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label ${(value * 100).toInt()}%",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(102.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0.2f..1f,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BrushesTrayContent(uiState: AtelierUiState, viewModel: AtelierViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pinta sobre el rostro con el movimiento:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            FilterChip(
                selected = uiState.showBrushGuides,
                onClick = { viewModel.toggleBrushGuides() },
                label = { Text(if (uiState.showBrushGuides) "Guía visible" else "Sin guía") },
                modifier = Modifier.testTag("toggle_brush_guides_chip")
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(BrushTool.entries) { brush ->
                val isSelected = brush == uiState.activeBrush
                Surface(
                    onClick = { viewModel.setActiveBrush(brush) },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .width(184.dp)
                        .testTag("studio_brush_${brush.name}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BrushMotionDiagram(
                                motion = brush.motionPattern,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(brush.displayName, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    brush.motionPattern.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = brush.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OutfitsTrayContent(uiState: AtelierUiState, viewModel: AtelierViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        PatternChipRow(
            items = OutfitStyle.entries,
            selected = uiState.wardrobe.outfitStyle,
            labelOf = { it.displayName },
            onSelect = { style -> viewModel.updateWardrobe { it.copy(outfitStyle = style) } }
        )
        ColorSwatchRow(
            colors = AtelierCatalog.outfitColors,
            selectedColor = uiState.wardrobe.outfitColor,
            onSelect = { c -> viewModel.updateWardrobe { it.copy(outfitColor = c) } }
        )
    }
}

@Composable
private fun ShoesTrayContent(uiState: AtelierUiState, viewModel: AtelierViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        PatternChipRow(
            items = ShoeStyle.entries,
            selected = uiState.wardrobe.shoeStyle,
            labelOf = { it.displayName },
            onSelect = { style -> viewModel.updateWardrobe { it.copy(shoeStyle = style) } }
        )
        ColorSwatchRow(
            colors = AtelierCatalog.shoeColors,
            selectedColor = uiState.wardrobe.shoeColor,
            onSelect = { c -> viewModel.updateWardrobe { it.copy(shoeColor = c) } }
        )
    }
}

@Composable
private fun JewelryTrayContent(uiState: AtelierUiState, viewModel: AtelierViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Metal / Finish selector
        PatternChipRow(
            items = JewelryMetal.entries,
            selected = uiState.wardrobe.jewelryMetal,
            labelOf = { it.displayName },
            onSelect = { metal -> viewModel.updateWardrobe { it.copy(jewelryMetal = metal) } }
        )
        PatternChipRow(
            items = EarringStyle.entries,
            selected = uiState.wardrobe.earringStyle,
            labelOf = { "Aretes: ${it.displayName}" },
            onSelect = { e -> viewModel.updateWardrobe { it.copy(earringStyle = e) } }
        )
        PatternChipRow(
            items = NecklaceStyle.entries,
            selected = uiState.wardrobe.necklaceStyle,
            labelOf = { "Collar: ${it.displayName}" },
            onSelect = { n -> viewModel.updateWardrobe { it.copy(necklaceStyle = n) } }
        )
        PatternChipRow(
            items = HeadAccessoryStyle.entries,
            selected = uiState.wardrobe.headAccessoryStyle,
            labelOf = { "Tocado: ${it.displayName}" },
            onSelect = { h -> viewModel.updateWardrobe { it.copy(headAccessoryStyle = h) } }
        )
    }
}

@Composable
private fun ModelsTrayContent(uiState: AtelierUiState, viewModel: AtelierViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Modelos o diseño a medida",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            FilledTonalButton(
                onClick = { viewModel.openFaceSkinSetup() },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier
                    .height(34.dp)
                    .testTag("studio_customize_face_skin_btn")
            ) {
                Text("Rostro y Piel", style = MaterialTheme.typography.labelMedium)
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(AtelierCatalog.models) { model ->
                ModelSelectorCard(
                    model = model,
                    isSelected = model.id == uiState.selectedModel.id,
                    onClick = { viewModel.selectModel(model) }
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(PortraitPoseMode.entries) { mode ->
                FilterChip(
                    selected = mode == uiState.portraitPoseMode,
                    onClick = { viewModel.setPortraitPoseMode(mode) },
                    label = { Text("Retrato: ${mode.displayName}") },
                    modifier = Modifier.testTag("pose_mode_${mode.name}")
                )
            }
        }

        PatternChipRow(
            items = HairStyle.entries,
            selected = uiState.hairStyle,
            labelOf = { it.displayName },
            onSelect = { viewModel.setHairStyle(it) }
        )

        ColorSwatchRow(
            colors = AtelierCatalog.hairColors,
            selectedColor = uiState.hairColor,
            onSelect = { viewModel.setHairColor(it) }
        )
    }
}

@Composable
private fun <T> PatternChipRow(
    items: List<T>,
    selected: T,
    labelOf: (T) -> String,
    onSelect: (T) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            val isSel = item == selected
            FilterChip(
                selected = isSel,
                onClick = { onSelect(item) },
                label = { Text(labelOf(item)) }
            )
        }
    }
}

@Composable
private fun ColorSwatchRow(
    colors: List<Color>,
    selectedColor: Color,
    onSelect: (Color) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(colors) { color ->
            ColorSwatchButton(
                color = color,
                isSelected = color == selectedColor,
                contentDescription = "Elegir tono",
                testTag = "swatch_${color.value}",
                onClick = { onSelect(color) }
            )
        }
    }
}
