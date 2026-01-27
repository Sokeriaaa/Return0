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
package sokeriaaa.return0.ui.main.game.events.shop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_shop
import sokeriaaa.return0.models.story.event.interactive.ShopItem
import sokeriaaa.return0.mvi.intents.GameIntent
import sokeriaaa.return0.mvi.viewmodels.GameViewModel
import sokeriaaa.return0.mvi.viewmodels.ShopViewModel
import sokeriaaa.return0.ui.common.AppAdaptiveScaffold
import sokeriaaa.return0.ui.common.AppBackHandler
import sokeriaaa.return0.ui.common.event.interactive.ShopDisplayItem
import sokeriaaa.return0.ui.common.rememberAppAdaptiveScaffoldState
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    viewModel: ShopViewModel = viewModel(
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

    val state = rememberAppAdaptiveScaffoldState(windowAdaptiveInfo)
    AppBackHandler(onBack = onBack)
    AppAdaptiveScaffold(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.game_shop)) },
                navigationIcon = { AppBackIconButton(onClick = onBack) },
            )
        },
        state = state,
        mainContent = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(items = viewModel.items) {
                    ShopDisplayItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 10.dp,
                                vertical = 4.dp,
                            ),
                        item = it,
                    )
                }
            }
        },
        paneContent = {

        },
    )
}