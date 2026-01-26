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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import sokeriaaa.return0.ui.common.AppAdaptiveScaffold
import sokeriaaa.return0.ui.common.AppBackHandler
import sokeriaaa.return0.ui.common.rememberAppAdaptiveScaffoldState
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
    val state = rememberAppAdaptiveScaffoldState(windowAdaptiveInfo)
    var selectedItem: InventoryViewModel.ItemDisplay? by remember { mutableStateOf(null) }
    val onBack: () -> Unit = {
        if (state.isWideScreen || !state.isShowingPane) {
            mainNavHostController.navigateUp()
        } else {
            state.hidePane()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.onIntent(CommonIntent.Refresh)
    }
    AppBackHandler(onBack = onBack)
    AppAdaptiveScaffold(
        viewModel = viewModel,
        state = state,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.game_menu_inventory)) },
                navigationIcon = { AppBackIconButton(onClick = onBack) },
            )
        },
        mainContent = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(
                    items = viewModel.items
                ) { index, item ->
                    InventoryItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItem = item
                                state.showPane()
                            },
                        display = item,
                        isShowDescription = viewModel.isShowDescription,
                    )
                }
            }
        },
        paneContent = {
            selectedItem?.let {
                InventoryDetail(
                    modifier = Modifier.fillMaxSize(),
                    display = it,
                )
            }
        },
    )
}

@Composable
private fun InventoryItem(
    modifier: Modifier = Modifier,
    display: InventoryViewModel.ItemDisplay,
    isShowDescription: Boolean,
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
        supportingContent = if (isShowDescription) {
            { Text(display.description) }
        } else {
            null
        },
        trailingContent = { Text(display.amount.toString()) }
    )
}

@Composable
private fun InventoryDetail(
    modifier: Modifier = Modifier,
    display: InventoryViewModel.ItemDisplay,
) {
    // TODO Placeholder for testing the AppAdaptiveScaffold.
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