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
package sokeriaaa.return0.ui.main.game.inventory

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_menu_inventory
import return0.composeapp.generated.resources.ic_outline_code_blocks_24
import return0.composeapp.generated.resources.ic_outline_deployed_code_24
import return0.composeapp.generated.resources.ic_outline_more_horiz_24
import return0.composeapp.generated.resources.ic_outline_terminal_24
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.viewmodels.InventoryViewModel
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel = viewModel(
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
                        text = stringResource(Res.string.game_menu_inventory)
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
        LazyColumn(
            modifier = Modifier.padding(paddingValues = paddingValues),
        ) {
            itemsIndexed(
                items = viewModel.items
            ) { index, item ->
                InventoryItem(
                    modifier = Modifier.fillMaxWidth(),
                    display = item,
                    onSelected = {
                        // TODO
                    }
                )
            }
        }
    }
}

@Composable
private fun InventoryItem(
    modifier: Modifier = Modifier,
    display: InventoryViewModel.ItemDisplay,
    onSelected: () -> Unit,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                painter = painterResource(
                    when (display.itemData.types.firstOrNull()) {
                        ItemData.Type.CONSUMABLE -> Res.drawable.ic_outline_terminal_24
                        ItemData.Type.MATERIAL -> Res.drawable.ic_outline_code_blocks_24
                        ItemData.Type.QUEST -> Res.drawable.ic_outline_deployed_code_24
                        ItemData.Type.OTHER, null -> Res.drawable.ic_outline_more_horiz_24
                    }
                ),
                contentDescription = null,
            )
        },
        headlineContent = { Text(display.name) },
        supportingContent = { Text(display.description) },
        trailingContent = { Text(display.amount.toString()) }
    )
}