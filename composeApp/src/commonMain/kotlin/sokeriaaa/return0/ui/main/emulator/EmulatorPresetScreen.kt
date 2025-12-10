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
package sokeriaaa.return0.ui.main.emulator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.cancel
import return0.composeapp.generated.resources.emulator_enemy
import return0.composeapp.generated.resources.emulator_party
import return0.composeapp.generated.resources.emulator_preset
import return0.composeapp.generated.resources.emulator_preset_rename
import return0.composeapp.generated.resources.emulator_preset_use
import return0.composeapp.generated.resources.general_level_w_value
import return0.composeapp.generated.resources.ic_outline_check_24
import return0.composeapp.generated.resources.ic_outline_close_24
import return0.composeapp.generated.resources.ic_outline_edit_square_24
import return0.composeapp.generated.resources.ok
import sokeriaaa.return0.applib.room.table.EmulatorEntryTable
import sokeriaaa.return0.applib.room.table.EmulatorIndexTable
import sokeriaaa.return0.mvi.intents.EmulatorIntent
import sokeriaaa.return0.mvi.intents.EmulatorPresetIntent
import sokeriaaa.return0.mvi.viewmodels.EmulatorPresetViewModel
import sokeriaaa.return0.mvi.viewmodels.EmulatorViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton
import sokeriaaa.return0.ui.common.widgets.AppIconButton
import sokeriaaa.return0.ui.common.widgets.AppTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmulatorPresetScreen(
    viewModel: EmulatorPresetViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    LaunchedEffect(Unit) {
        viewModel.refreshPresetIndices()
    }
    // Detail dialog
    val emulatorViewModel: EmulatorViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    )
    viewModel.selectedPresetIndex?.let {
        EmulatorPresetDialog(
            indexItem = it,
            isRenaming = viewModel.isRenaming,
            entries = viewModel.selectedEntries,
            onIntent = viewModel::onIntent,
            onSelected = {
                val states = viewModel.getEntityState()
                // Load presets
                emulatorViewModel.onIntent(
                    EmulatorIntent.LoadPreset(states.first, states.second)
                )
                // back
                viewModel.onIntent(EmulatorPresetIntent.DismissPresetDialog)
                mainNavHostController.navigateUp()
            }
        )
    }
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.emulator_preset)
                    )
                },
                navigationIcon = {
                    AppBackIconButton(
                        onClick = {
                            mainNavHostController.navigateUp()
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues = paddingValues),
        ) {
            items(
                items = viewModel.emulatorIndexList
            ) {
                EmulatorPresetItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onIntent(EmulatorPresetIntent.OpenPresetDialog(it))
                        },
                    indexItem = it,
                )
            }
        }
    }
}

@Composable
private fun EmulatorPresetItem(
    modifier: Modifier,
    indexItem: EmulatorIndexTable,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            text = indexItem.name,
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 4.dp),
        )
    }
}

@Composable
private fun EmulatorPresetDialog(
    indexItem: EmulatorIndexTable,
    isRenaming: Boolean,
    entries: List<EmulatorEntryTable>?,
    onIntent: (EmulatorPresetIntent) -> Unit,
    onSelected: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.padding(vertical = 64.dp),
        onDismissRequest = {
            onIntent(EmulatorPresetIntent.DismissPresetDialog)
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                var name: String by remember { mutableStateOf(indexItem.name) }
                LaunchedEffect(isRenaming) {
                    name = indexItem.name
                }
                if (isRenaming) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1F),
                        value = name,
                        onValueChange = { name = it },
                        maxLines = 1,
                        textStyle = MaterialTheme.typography.bodyLarge,
                    )
                    AppIconButton(
                        iconRes = Res.drawable.ic_outline_check_24,
                        contentDescription = stringResource(Res.string.ok),
                        onClick = {
                            indexItem.name = name
                            onIntent(EmulatorPresetIntent.ExecuteRename(name))
                        }
                    )
                    AppIconButton(
                        iconRes = Res.drawable.ic_outline_close_24,
                        contentDescription = stringResource(Res.string.cancel),
                        onClick = {
                            onIntent(EmulatorPresetIntent.DismissRename)
                        }
                    )
                } else {
                    Text(
                        modifier = Modifier.weight(1F),
                        text = indexItem.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    AppIconButton(
                        iconRes = Res.drawable.ic_outline_edit_square_24,
                        contentDescription = stringResource(Res.string.emulator_preset_rename),
                        onClick = {
                            onIntent(EmulatorPresetIntent.RequestRename)
                        }
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (entries == null) {
                    CircularProgressIndicator()
                } else {
                    // Parties
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.emulator_party),
                    )
                    entries.forEach {
                        if (it.isParty) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(
                                    resource = Res.string.general_level_w_value,
                                    /* level = */ it.level,
                                ) + " " + it.entityName,
                            )
                        }
                    }
                    // Enemies
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        text = stringResource(Res.string.emulator_enemy),
                    )
                    entries.forEach {
                        if (!it.isParty) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(
                                    resource = Res.string.general_level_w_value,
                                    /* level = */ it.level,
                                ) + " " + it.entityName,
                            )
                        }
                    }
                }
            }
        },
        dismissButton = {
            AppTextButton(
                modifier = Modifier.padding(8.dp),
                text = stringResource(Res.string.cancel),
                enabled = entries != null,
                onClick = { onIntent(EmulatorPresetIntent.DismissPresetDialog) },
            )
        },
        confirmButton = {
            AppTextButton(
                modifier = Modifier.padding(8.dp),
                text = stringResource(Res.string.emulator_preset_use),
                enabled = entries != null,
                onClick = onSelected,
            )
        },
    )
}