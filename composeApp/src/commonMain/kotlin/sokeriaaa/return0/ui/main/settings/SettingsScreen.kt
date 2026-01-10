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
package sokeriaaa.return0.ui.main.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.settings
import return0.composeapp.generated.resources.settings_combat_auto
import return0.composeapp.generated.resources.settings_combat_auto_desc
import return0.composeapp.generated.resources.settings_gameplay_display_item_desc
import return0.composeapp.generated.resources.settings_gameplay_display_item_desc_desc
import sokeriaaa.return0.applib.repository.settings.SettingsRepo
import sokeriaaa.return0.mvi.viewmodels.SettingsViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isInGame: Boolean,
    viewModel: SettingsViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
        extras = MutableCreationExtras().apply {
            this[SettingsViewModel.isInGameKey] = isInGame
        }
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.settings)
                    )
                },
                navigationIcon = {
                    AppBackIconButton(
                        onClick = {
                            mainNavHostController.navigateUp()
                        },
                    )
                },
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues = paddingValues),
            columns = GridCells.Fixed(1),
        ) {
            item {
                BooleanItem(
                    modifier = Modifier.fillMaxWidth(),
                    entry = viewModel.gameplayDisplayItemDesc,
                    labelRes = Res.string.settings_gameplay_display_item_desc,
                    descRes = Res.string.settings_gameplay_display_item_desc_desc,
                )
            }
            item {
                BooleanItem(
                    modifier = Modifier.fillMaxWidth(),
                    entry = viewModel.combatAuto,
                    labelRes = Res.string.settings_combat_auto,
                    descRes = Res.string.settings_combat_auto_desc,
                )
            }
        }
    }
}

@Composable
private fun BooleanItem(
    modifier: Modifier = Modifier,
    entry: SettingsRepo.Entry<Boolean>,
    labelRes: StringResource,
    descRes: StringResource,
) {
    val checked by entry.flow.collectAsState(entry.defaultValue)
    val scope = rememberCoroutineScope()
    ListItem(
        modifier = modifier,
        headlineContent = { Text(stringResource(labelRes)) },
        supportingContent = { Text(stringResource(descRes)) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = { scope.launch { entry.set(it) } },
            )
        }
    )
}