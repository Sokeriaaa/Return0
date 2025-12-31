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
package sokeriaaa.return0.ui.main.save

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.general_location
import return0.composeapp.generated.resources.saves
import return0.composeapp.generated.resources.saves_idle
import return0.composeapp.generated.resources.saves_load_warn
import return0.composeapp.generated.resources.saves_num
import return0.composeapp.generated.resources.saves_overwrite_warn_text
import return0.composeapp.generated.resources.saves_overwrite_warn_title
import return0.composeapp.generated.resources.saves_select
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.room.table.SaveMetaTable
import sokeriaaa.return0.mvi.intents.GameIntent
import sokeriaaa.return0.mvi.viewmodels.GameViewModel
import sokeriaaa.return0.mvi.viewmodels.SaveViewModel
import sokeriaaa.return0.ui.common.AppBackHandler
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.widgets.AppAlertDialog
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton
import sokeriaaa.return0.ui.nav.Scene
import sokeriaaa.return0.ui.nav.navigateSingleTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveScreen(
    isSaving: Boolean,
    viewModel: SaveViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
        extras = MutableCreationExtras().apply {
            this[SaveViewModel.isSavingKey] = isSaving
        }
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    var loadWarn: SaveMetaTable? by remember { mutableStateOf(null) }
    var saveWarn: SaveMetaTable? by remember { mutableStateOf(null) }

    val gameViewModel: GameViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    )

    val onBack: () -> Unit = {
        gameViewModel.onIntent(GameIntent.EventContinue)
        mainNavHostController.navigateUp()
    }

    fun readFrom(saveID: Int) {
        viewModel.readFrom(
            saveID = saveID,
            onFinished = {
                // Refresh map
                gameViewModel.onIntent(GameIntent.RefreshMap)
                mainNavHostController.navigateUp()
                mainNavHostController.navigateSingleTop(Scene.Game.route + "/false")
                loadWarn = null
            }
        )
    }

    fun saveTo(saveID: Int) {
        viewModel.saveTo(
            saveID = saveID,
            onFinished = {
                gameViewModel.onIntent(GameIntent.EventContinue)
                mainNavHostController.navigateUp()
                mainNavHostController.navigateSingleTop(Scene.Game.route + "/false")
                saveWarn = null
            }
        )
    }

    loadWarn?.let {
        AppAlertDialog(
            modifier = Modifier.padding(vertical = 64.dp),
            title = stringResource(Res.string.saves_load_warn),
            onDismiss = { loadWarn = null },
            onConfirmed = { readFrom(it.saveID) }
        )
    }
    saveWarn?.let {
        AppAlertDialog(
            modifier = Modifier.padding(vertical = 64.dp),
            title = stringResource(Res.string.saves_overwrite_warn_title),
            text = stringResource(Res.string.saves_overwrite_warn_text),
            onDismiss = { saveWarn = null },
            onConfirmed = { saveTo(it.saveID) }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    AppBackHandler(onBack = onBack)
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            resource = if (isSaving) {
                                Res.string.saves_select
                            } else {
                                Res.string.saves
                            },
                        )
                    )
                },
                navigationIcon = {
                    AppBackIconButton(
                        onClick = onBack,
                    )
                },
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues = paddingValues),
            columns = GridCells.Fixed(1),
        ) {
            repeat(AppConstants.MAXIMUM_SAVES) { index ->
                item {
                    val item = viewModel.saveMap[index]
                    SaveCard(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        index = index,
                        saveMetaTable = item,
                        onClick = {
                            if (isSaving) {
                                // Save
                                if (item == null) {
                                    // Save to an empty slot
                                    saveTo(index)
                                } else {
                                    // Overwrite warning
                                    saveWarn = item
                                }
                            } else {
                                // Load
                                if (item != null) {
                                    loadWarn = item
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SaveCard(
    modifier: Modifier = Modifier,
    index: Int,
    saveMetaTable: SaveMetaTable?,
    onClick: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
    ) {
        // Saved time.
        Text(
            text = saveMetaTable?.savedTime?.toString() ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 3.dp,
                    start = 8.dp,
                    end = 8.dp,
                ),
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall,
        )
        // Title
        Text(
            text = stringResource(
                resource = Res.string.saves_num,
                /* num = */ index,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 8.dp,
                    end = 8.dp,
                ),
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.titleLarge,
        )
        HorizontalDivider(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
        )
        Column(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 8.dp,
            ),
        ) {
            // Line 1: title
            Text(
                text = saveMetaTable?.title?.toString() ?: stringResource(
                    Res.string.saves_idle,
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            // Line 2: location
            Text(
                text = saveMetaTable?.let {
                    stringResource(
                        resource = Res.string.general_location,
                        /* file = */ it.fileName,
                        /* row = */ it.lineNumber,
                    )
                } ?: "",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

