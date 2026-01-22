/**
 * Copyright (C) 2026 Sokeriaaa
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
package sokeriaaa.return0.ui.main.game.events.workbench

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_workbench
import sokeriaaa.return0.mvi.intents.GameIntent
import sokeriaaa.return0.mvi.viewmodels.GameViewModel
import sokeriaaa.return0.mvi.viewmodels.WorkbenchViewModel
import sokeriaaa.return0.ui.common.AppBackHandler
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkbenchScreen(
    viewModel: WorkbenchViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    val gameViewModel: GameViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    )
    val onBack: () -> Unit = {
        gameViewModel.onIntent(GameIntent.EventContinue)
        mainNavHostController.navigateUp()
    }
    AppBackHandler(onBack = onBack)
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.game_workbench)) },
                navigationIcon = { AppBackIconButton(onClick = onBack) },
            )
        }
    ) { paddingValues ->
    }
}