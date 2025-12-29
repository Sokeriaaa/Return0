/**
 * Copyright (C) 2025 Sokeriaaa
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */
package sokeriaaa.return0.ui.main.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_menu
import return0.composeapp.generated.resources.game_menu_entities
import return0.composeapp.generated.resources.game_menu_inventory
import return0.composeapp.generated.resources.game_menu_quit
import return0.composeapp.generated.resources.game_menu_save
import return0.composeapp.generated.resources.game_menu_settings
import return0.composeapp.generated.resources.game_menu_teams
import return0.composeapp.generated.resources.game_quit_warn
import return0.composeapp.generated.resources.game_save_text
import return0.composeapp.generated.resources.game_save_title
import return0.composeapp.generated.resources.ic_outline_code_24
import return0.composeapp.generated.resources.ic_outline_groups_24
import return0.composeapp.generated.resources.ic_outline_inventory_2_24
import return0.composeapp.generated.resources.ic_outline_logout_24
import return0.composeapp.generated.resources.ic_outline_menu_24
import return0.composeapp.generated.resources.ic_outline_save_24
import return0.composeapp.generated.resources.ic_outline_settings_24
import sokeriaaa.return0.models.story.event.EventEffect
import sokeriaaa.return0.mvi.intents.CombatIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.intents.GameIntent
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel
import sokeriaaa.return0.mvi.viewmodels.GameViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.ModalOverlay
import sokeriaaa.return0.ui.common.event.EventShowChoice
import sokeriaaa.return0.ui.common.event.EventShowChoiceState
import sokeriaaa.return0.ui.common.event.EventShowText
import sokeriaaa.return0.ui.common.event.EventShowTextState
import sokeriaaa.return0.ui.common.widgets.AppAlertDialog
import sokeriaaa.return0.ui.common.widgets.AppIconButton
import sokeriaaa.return0.ui.common.widgets.AppNavigateDrawerItem
import sokeriaaa.return0.ui.nav.Scene
import sokeriaaa.return0.ui.nav.navigatePopUpTo
import sokeriaaa.return0.ui.nav.navigateSingleTop

/**
 * The main gaming field.
 */
@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    val scope = rememberCoroutineScope()
    // Dialogue text
    var eventShowTextState by remember { mutableStateOf(EventShowTextState()) }
    // Choices
    var eventShowChoiceState by remember { mutableStateOf(EventShowChoiceState()) }
    // Save dialog
    var isShowingSaveDialog by remember { mutableStateOf(false) }

    // Showing map
    var isShowingMap by remember { mutableStateOf(true) }
    // Shaking map
    var shakeJob by remember { mutableStateOf<Job?>(null) }
    val shakeOffset = remember { Animatable(0f) }

    fun showDialogueText(effect: EventEffect.ShowText) {
        eventShowTextState = EventShowTextState(
            visible = true,
            effect = effect,
        )
    }

    fun hideDialogueText() {
        eventShowTextState = eventShowTextState.copy(visible = false)
    }

    fun showChoices(effect: EventEffect.ShowChoice) {
        eventShowChoiceState = EventShowChoiceState(
            visible = true,
            effect = effect,
        )
    }

    fun hideChoices() {
        eventShowChoiceState = eventShowChoiceState.copy(visible = false)
    }

    // For Combat event
    val combatViewModel: CombatViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    )
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is EventEffect.ShowText -> showDialogueText(effect)
                is EventEffect.ShowChoice -> showChoices(effect)

                is EventEffect.ShowSnackBar -> {
                    viewModel.onIntent(CommonIntent.ShowSnackBar(effect.text))
                }

                is EventEffect.StartCombat -> {
                    hideDialogueText()
                    hideChoices()
                    combatViewModel.onIntent(CombatIntent.Prepare(effect.config))
                    mainNavHostController.navigateSingleTop(Scene.Combat.route)
                }

                is EventEffect.MovePlayer -> {
                    hideDialogueText()
                    hideChoices()
                    viewModel.onIntent(
                        GameIntent.RequestMoveTo(
                            line = effect.line,
                            isByEvent = true,
                            // Never encounter enemies when moving by event effects.
                            isEncounterDisabled = true,
                        ),
                    )
                }

                is EventEffect.TeleportPlayer -> {
                    hideDialogueText()
                    hideChoices()
                    viewModel.onIntent(
                        GameIntent.TeleportTo(
                            fileName = effect.fileName,
                            line = effect.line,
                        ),
                    )
                }

                EventEffect.ShowMap -> isShowingMap = true
                EventEffect.HideMap -> isShowingMap = false
                EventEffect.ShakeMap -> {
                    shakeJob?.cancel()
                    shakeJob = scope.launch {
                        // Animate shake.
                        repeat(5) {
                            shakeOffset.animateTo(10f, tween(40))
                            shakeOffset.animateTo(-10f, tween(40))
                        }
                        shakeOffset.animateTo(0f)
                    }
                }

                EventEffect.ChooseEntity -> TODO()

                EventEffect.RequestSave -> isShowingSaveDialog = true
                EventEffect.RefreshEvents -> viewModel.onIntent(GameIntent.RefreshMap)

                EventEffect.EventFinished -> {
                    hideDialogueText()
                    hideChoices()
                    isShowingMap = true
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Game content.
        GameContent(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            mainNavHostController = mainNavHostController,
            windowAdaptiveInfo = windowAdaptiveInfo,
            isShowingMap = isShowingMap,
            mapOffset = shakeOffset.value,
        )
        // Event dialogs.
        // Click blocker.
        ModalOverlay(
            modifier = Modifier.fillMaxSize(),
            enabled = eventShowTextState.visible ||
                    eventShowChoiceState.visible ||
                    viewModel.isMovingByEvent ||
                    viewModel.isSwitchingFile,
            dim = eventShowTextState.visible ||
                    eventShowChoiceState.visible,
        ) {
            // ShowText
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
                    .align(Alignment.BottomCenter),
                visible = eventShowTextState.visible && eventShowTextState.effect != null
            ) {
                eventShowTextState.effect?.let {
                    EventShowText(
                        modifier = Modifier.fillMaxWidth(),
                        effect = it,
                        onContinue = { viewModel.onIntent(GameIntent.EventContinue) }
                    )
                }
            }
            // ShowChoice
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                visible = eventShowChoiceState.visible && eventShowChoiceState.effect != null
            ) {
                eventShowChoiceState.effect?.let {
                    EventShowChoice(
                        effect = it,
                        onSelected = { index ->
                            hideChoices()
                            viewModel.onIntent(GameIntent.EventChoice(index))
                        }
                    )
                }
            }
        }
        // Save progress dialog
        if (isShowingSaveDialog) {
            AppAlertDialog(
                modifier = Modifier.padding(vertical = 64.dp),
                title = stringResource(Res.string.game_save_title),
                text = stringResource(Res.string.game_save_text),
                onDismiss = { },
                onConfirmed = {
                    mainNavHostController.navigateSingleTop(Scene.Save.route + "/true")
                    isShowingSaveDialog = false
                },
                onCanceled = {
                    isShowingSaveDialog = false
                    viewModel.onIntent(GameIntent.EventContinue)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameContent(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel,
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
    isShowingMap: Boolean,
    mapOffset: Float,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Map alpha
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isShowingMap) 1F else 0F,
        label = "animatedMapAlpha",
    )

    // Quit dialog
    var isShowingQuitDialog by remember { mutableStateOf(false) }
    if (isShowingQuitDialog) {
        AppAlertDialog(
            modifier = Modifier.padding(vertical = 64.dp),
            title = stringResource(Res.string.game_menu_quit),
            text = stringResource(Res.string.game_quit_warn),
            onDismiss = { isShowingQuitDialog = false },
            onConfirmed = {
                isShowingQuitDialog = false
                mainNavHostController.navigatePopUpTo(Scene.Main.route)
            }
        )
    }
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            GameDrawerContent(
                onSaveClicked = {
                    scope.launch {
                        mainNavHostController.navigateSingleTop(Scene.Save.route + "/true")
                        drawerState.close()
                    }
                },
                onEntitiesClicked = {},
                onTeamsClicked = {},
                onInventoryClicked = {},
                onSettingsClicked = {},
                onQuitClicked = {
                    scope.launch {
                        isShowingQuitDialog = true
                        drawerState.close()
                    }
                },
            )
        },
    ) {
        AppScaffold(
            viewModel = viewModel,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = viewModel.current.name)
                    },
                    navigationIcon = {
                        AppIconButton(
                            iconRes = Res.drawable.ic_outline_menu_24,
                            contentDescription = stringResource(Res.string.game_menu),
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }
                )
            }
        ) { paddingValues ->
            GameMap(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .alpha(animatedAlpha)
                    .graphicsLayer { translationX = mapOffset },
                viewModel = viewModel,
            )
        }
    }
}

@Composable
private fun GameDrawerContent(
    onSaveClicked: () -> Unit,
    onEntitiesClicked: () -> Unit,
    onTeamsClicked: () -> Unit,
    onInventoryClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onQuitClicked: () -> Unit,
) {
    ModalDrawerSheet {
        // Save game
        AppNavigateDrawerItem(
            iconRes = Res.drawable.ic_outline_save_24,
            label = stringResource(Res.string.game_menu_save),
            onClick = onSaveClicked,
        )
        // Entities
        AppNavigateDrawerItem(
            iconRes = Res.drawable.ic_outline_code_24,
            label = stringResource(Res.string.game_menu_entities),
            onClick = onEntitiesClicked,
        )
        // Teams
        AppNavigateDrawerItem(
            iconRes = Res.drawable.ic_outline_groups_24,
            label = stringResource(Res.string.game_menu_teams),
            onClick = onTeamsClicked,
        )
        // Inventory
        AppNavigateDrawerItem(
            iconRes = Res.drawable.ic_outline_inventory_2_24,
            label = stringResource(Res.string.game_menu_inventory),
            onClick = onInventoryClicked,
        )
        // Settings
        AppNavigateDrawerItem(
            iconRes = Res.drawable.ic_outline_settings_24,
            label = stringResource(Res.string.game_menu_settings),
            onClick = onSettingsClicked,
        )
        // Quit game
        AppNavigateDrawerItem(
            iconRes = Res.drawable.ic_outline_logout_24,
            label = stringResource(Res.string.game_menu_quit),
            onClick = onQuitClicked,
        )
    }
}