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
package sokeriaaa.return0.ui.main.game.entities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_menu_entities
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.viewmodels.EntitiesViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.entity.EntityProfileItem
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton
import sokeriaaa.return0.ui.nav.Scene
import sokeriaaa.return0.ui.nav.navigateSingleTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntitiesScreen(
    viewModel: EntitiesViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    LaunchedEffect(Unit) {
        viewModel.onIntent(CommonIntent.Refresh)
    }
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.game_menu_entities)
                    )
                },
                navigationIcon = {
                    AppBackIconButton(
                        onClick = {
                            mainNavHostController.navigateUp()
                        }
                    )
                },
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues = paddingValues),
            columns = GridCells.Adaptive(minSize = 200.dp),
        ) {
            items(
                items = viewModel.entities,
                key = { it.name }
            ) {
                EntityProfileItem(
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .clickable {
                            mainNavHostController.navigateSingleTop(
                                route = Scene.EntityDetails.route + "/" + it.name,
                            )
                        },
                    display = it
                )
            }
        }
    }
}