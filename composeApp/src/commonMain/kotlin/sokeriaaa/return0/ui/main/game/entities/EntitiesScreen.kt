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

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_menu_entities
import sokeriaaa.return0.mvi.intents.EntitiesIntent
import sokeriaaa.return0.mvi.viewmodels.EntitiesViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.entity.EntityHPBar
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton

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
        viewModel.onIntent(EntitiesIntent.Refresh)
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
            columns = GridCells.Adaptive(minSize = 160.dp),
        ) {
            items(
                items = viewModel.entities,
                key = { it.name }
            ) {
                EntityItem(
                    modifier = Modifier.padding(all = 4.dp),
                    display = it
                )
            }
        }
    }
}

@Composable
private fun EntityItem(
    modifier: Modifier = Modifier,
    display: EntitiesViewModel.Display,
) {
    Column(
        modifier = modifier,
    ) {
        // Name
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(Int.MAX_VALUE)
                .padding(bottom = 4.dp),
            text = display.name,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                // EXP progress
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center),
                    progress = { display.expProgress }
                )
                // Level
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = display.level.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(start = 6.dp),
            ) {
                // HP
                EntityHPBar(
                    modifier = Modifier.fillMaxWidth(),
                    label = "HP",
                    current = display.hp,
                    max = display.maxHP,
                    valueStyle = EntityHPBar.ValueStyle.FULL,
                    style = MaterialTheme.typography.labelMedium,
                )
                // SP
                EntityHPBar(
                    modifier = Modifier.fillMaxWidth(),
                    label = "SP",
                    current = display.sp,
                    max = display.maxSP,
                    valueStyle = EntityHPBar.ValueStyle.FULL,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}