package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.AtelierCatalog
import com.example.model.ViewZoomMode
import com.example.ui.components.RunwayShowcaseDialog
import com.example.ui.screens.AcademyScreen
import com.example.ui.screens.FaceSkinSetupScreen
import com.example.ui.screens.LessonPracticeScreen
import com.example.ui.screens.LookbookScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.theme.AuraAtelierTheme
import com.example.ui.viewmodel.AtelierViewModel
import com.example.ui.viewmodel.StudioDockTab

private enum class AtelierRoute(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    ACADEMY("academy", "Academia", Icons.Rounded.AutoAwesome, Icons.Outlined.AutoAwesome),
    STUDIO("studio", "Estudio", Icons.Rounded.Brush, Icons.Outlined.Brush),
    LOOKBOOK("lookbook", "Estilos", Icons.Rounded.Checkroom, Icons.Outlined.Checkroom)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuraAtelierTheme {
                AuraAtelierApp()
            }
        }
    }
}

@Composable
fun AuraAtelierApp(
    viewModel: AtelierViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val navigateTo: (AtelierRoute) -> Unit = { dest ->
        navController.navigate(dest.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    when {
        uiState.showFaceSkinSetup -> {
            FaceSkinSetupScreen(
                initialFaceShape = uiState.selectedModel.faceShape,
                initialSkinPreset = uiState.selectedSkinPreset,
                initialEyeColor = uiState.selectedModel.eyeColor,
                initialHairStyle = uiState.hairStyle,
                initialHairColor = uiState.hairColor,
                allowBackDismiss = uiState.hasCompletedInitialSetup,
                onDismissBack = { viewModel.dismissFaceSkinSetup() },
                onConfirmToAcademy = { model, hair, hairColor, skin ->
                    viewModel.applyCustomFaceAndSkin(model, hair, hairColor, skin)
                },
                onConfirmToStudio = { model, hair, hairColor, skin ->
                    viewModel.applyCustomFaceAndSkin(model, hair, hairColor, skin)
                    navigateTo(AtelierRoute.STUDIO)
                }
            )
        }

        uiState.activeLesson != null -> {
            LessonPracticeScreen(
                uiState = uiState,
                onSelectBrush = { viewModel.selectLessonBrush(it) },
                onCycleModel = {
                    val models = AtelierCatalog.models
                    val nextIdx = (models.indexOfFirst { it.id == uiState.selectedModel.id } + 1) % models.size
                    viewModel.selectModel(models[nextIdx])
                },
                onStrokeCompleted = { pts, brush -> viewModel.onLessonStrokeCompleted(pts, brush) },
                onTapPoint = { pt, brush -> viewModel.onLessonTapPoint(pt, brush) },
                onAutoDemo = { viewModel.autoDemonstrateStep() },
                onNextStep = { viewModel.advanceLessonStep() },
                onExitLesson = { viewModel.exitLesson() },
                onGoToFullBodyStudio = {
                    viewModel.exitLesson()
                    viewModel.setZoomMode(ViewZoomMode.FULL_BODY)
                    viewModel.setActiveDockTab(StudioDockTab.OUTFITS)
                    navigateTo(AtelierRoute.STUDIO)
                }
            )
        }

        else -> {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val useNavigationRail = maxWidth >= 600.dp

                if (useNavigationRail) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        NavigationRail {
                            AtelierRoute.entries.forEach { dest ->
                                val selected = currentRoute == dest.route
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { navigateTo(dest) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                            contentDescription = dest.label
                                        )
                                    },
                                    label = { Text(dest.label) },
                                    modifier = Modifier.testTag("nav_${dest.route}")
                                )
                            }
                        }

                        Scaffold(
                            contentWindowInsets = WindowInsets.safeDrawing,
                            modifier = Modifier.weight(1f)
                        ) { innerPadding ->
                            AtelierNavHost(
                                navController = navController,
                                uiState = uiState,
                                viewModel = viewModel,
                                navigateTo = navigateTo,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                } else {
                    Scaffold(
                        contentWindowInsets = WindowInsets.safeDrawing,
                        bottomBar = {
                            NavigationBar {
                                AtelierRoute.entries.forEach { dest ->
                                    val selected = currentRoute == dest.route
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { navigateTo(dest) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                                contentDescription = dest.label
                                            )
                                        },
                                        label = { Text(dest.label) },
                                        modifier = Modifier.testTag("nav_${dest.route}")
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        AtelierNavHost(
                            navController = navController,
                            uiState = uiState,
                            viewModel = viewModel,
                            navigateTo = navigateTo,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    if (uiState.isRunwayShowcaseVisible) {
        RunwayShowcaseDialog(
            model = uiState.selectedModel,
            hairStyle = uiState.hairStyle,
            hairColor = uiState.hairColor,
            makeup = uiState.makeup,
            wardrobe = uiState.wardrobe,
            appliedLookTitle = uiState.appliedLookbookTitle,
            onDismiss = { viewModel.setRunwayShowcaseVisible(false) },
            onSwitchModel = {
                val models = AtelierCatalog.models
                val nextIdx = (models.indexOfFirst { it.id == uiState.selectedModel.id } + 1) % models.size
                viewModel.selectModel(models[nextIdx])
            }
        )
    }
}

@Composable
private fun AtelierNavHost(
    navController: androidx.navigation.NavHostController,
    uiState: com.example.ui.viewmodel.AtelierUiState,
    viewModel: AtelierViewModel,
    navigateTo: (AtelierRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AtelierRoute.ACADEMY.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(AtelierRoute.ACADEMY.route) {
            AcademyScreen(
                completedLessonIds = uiState.completedLessonIds,
                selectedModelName = uiState.selectedModel.name,
                onStartLesson = { lesson -> viewModel.startLesson(lesson) },
                onOpenStudio = { navigateTo(AtelierRoute.STUDIO) },
                onOpenFaceSkinSetup = { viewModel.openFaceSkinSetup() }
            )
        }
        composable(AtelierRoute.STUDIO.route) {
            StudioScreen(
                uiState = uiState,
                viewModel = viewModel
            )
        }
        composable(AtelierRoute.LOOKBOOK.route) {
            LookbookScreen(
                selectedModel = uiState.selectedModel,
                onSelectModel = { viewModel.selectModel(it) },
                onApplyLookbook = { look ->
                    viewModel.applyLookbook(look)
                    navigateTo(AtelierRoute.STUDIO)
                }
            )
        }
    }
}
