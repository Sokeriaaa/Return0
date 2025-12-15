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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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
import return0.composeapp.generated.resources.ic_outline_code_24
import return0.composeapp.generated.resources.ic_outline_groups_24
import return0.composeapp.generated.resources.ic_outline_inventory_2_24
import return0.composeapp.generated.resources.ic_outline_logout_24
import return0.composeapp.generated.resources.ic_outline_menu_24
import return0.composeapp.generated.resources.ic_outline_save_24
import return0.composeapp.generated.resources.ic_outline_settings_24
import sokeriaaa.return0.mvi.intents.CombatIntent
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel
import sokeriaaa.return0.mvi.viewmodels.GameViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.widgets.AppIconButton
import sokeriaaa.return0.ui.common.widgets.AppNavigateDrawerItem
import sokeriaaa.return0.ui.nav.Scene
import sokeriaaa.return0.ui.nav.navigateSingleTop

/**
 * The main gaming field.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Combat event
    val combatViewModel: CombatViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.combatEvents.collect {
            combatViewModel.onIntent(CombatIntent.Prepare(it.config))
            mainNavHostController.navigateSingleTop(Scene.Combat.route)
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            GameDrawerContent(
                onSaveClicked = {
                    mainNavHostController.navigateSingleTop(Scene.Save.route + "/true")
                },
                onEntitiesClicked = {},
                onTeamsClicked = {},
                onInventoryClicked = {},
                onSettingsClicked = {},
                onQuitClicked = {},
            )
        },
    ) {
        AppScaffold(
            viewModel = viewModel,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        // TODO
                        Text(text = "TODO: file name here")
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
        ) {

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