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
package sokeriaaa.return0.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.app_name
import return0.composeapp.generated.resources.emulator
import return0.composeapp.generated.resources.ic_outline_experiment_24
import return0.composeapp.generated.resources.ic_outline_gamepad_24
import return0.composeapp.generated.resources.ic_outline_save_24
import return0.composeapp.generated.resources.ic_outline_settings_24
import return0.composeapp.generated.resources.load_game
import return0.composeapp.generated.resources.new_game
import return0.composeapp.generated.resources.settings
import sokeriaaa.return0.mvi.viewmodels.MainViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.widgets.AppFilledTonalButton
import sokeriaaa.return0.ui.nav.Scene
import sokeriaaa.return0.ui.nav.navigateSingleTop

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    AppScaffold(
        viewModel = viewModel,
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues = paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.weight(1F),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(Res.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
            MainButtons(
                modifier = Modifier.fillMaxWidth(),
                onNewGameClicked = {
                    mainNavHostController.navigateSingleTop(Scene.Game.route)
                },
                onLoadGameClicked = {},
                onEmulatorClicked = {
                    mainNavHostController.navigateSingleTop(Scene.Emulator.route)
                },
                onSettingsClicked = {},
            )
        }
    }
}

@Composable
private fun MainButtons(
    modifier: Modifier = Modifier,
    onNewGameClicked: () -> Unit,
    onLoadGameClicked: () -> Unit,
    onEmulatorClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AppFilledTonalButton(
                modifier = Modifier
                    .weight(weight = 1F)
                    .padding(horizontal = 4.dp),
                onClick = onNewGameClicked,
                iconRes = Res.drawable.ic_outline_gamepad_24,
                text = stringResource(Res.string.new_game),
            )
            AppFilledTonalButton(
                modifier = Modifier
                    .weight(weight = 1F)
                    .padding(horizontal = 4.dp),
                onClick = onLoadGameClicked,
                iconRes = Res.drawable.ic_outline_save_24,
                text = stringResource(Res.string.load_game),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AppFilledTonalButton(
                modifier = Modifier
                    .weight(weight = 1F)
                    .padding(horizontal = 4.dp),
                onClick = onEmulatorClicked,
                iconRes = Res.drawable.ic_outline_experiment_24,
                text = stringResource(Res.string.emulator),
            )
            AppFilledTonalButton(
                modifier = Modifier
                    .weight(weight = 1F)
                    .padding(horizontal = 4.dp),
                onClick = onSettingsClicked,
                iconRes = Res.drawable.ic_outline_settings_24,
                text = stringResource(Res.string.settings),
            )
        }
    }
}