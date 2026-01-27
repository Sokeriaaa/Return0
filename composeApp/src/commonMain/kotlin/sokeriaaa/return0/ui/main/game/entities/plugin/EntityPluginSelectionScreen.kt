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
package sokeriaaa.return0.ui.main.game.entities.plugin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_plugin_install
import return0.composeapp.generated.resources.game_plugin_installed
import return0.composeapp.generated.resources.game_plugin_locked
import return0.composeapp.generated.resources.game_plugin_select
import return0.composeapp.generated.resources.game_plugin_select_current
import return0.composeapp.generated.resources.game_plugin_select_different_path
import return0.composeapp.generated.resources.game_plugin_select_different_path_warn
import return0.composeapp.generated.resources.game_plugin_select_installed_by
import return0.composeapp.generated.resources.game_plugin_select_installed_by_warn
import return0.composeapp.generated.resources.game_plugin_select_uninstall_warn
import return0.composeapp.generated.resources.game_plugin_uninstall
import return0.composeapp.generated.resources.ic_outline_check_24
import return0.composeapp.generated.resources.ic_outline_extension_24
import return0.composeapp.generated.resources.ic_outline_extension_off_24
import return0.composeapp.generated.resources.ic_outline_lock_24
import sokeriaaa.return0.models.entity.display.ExtendedEntityProfile
import sokeriaaa.return0.models.entity.plugin.display.PluginInfo
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.intents.EntityDetailsIntent
import sokeriaaa.return0.mvi.viewmodels.EntityDetailsViewModel
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.ui.common.AppAdaptiveScaffold
import sokeriaaa.return0.ui.common.rememberAppAdaptiveScaffoldState
import sokeriaaa.return0.ui.common.widgets.AppAlertDialog
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton
import sokeriaaa.return0.ui.common.widgets.AppFilledTonalButton
import sokeriaaa.return0.ui.common.widgets.OutlinedEmojiHeader
import sokeriaaa.return0.ui.common.widgets.stickyHeaderedDropdownVisibility

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityPluginSelectionScreen(
    entityName: String,
    viewModel: EntityDetailsViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
        key = "EntityDetailsViewModel-$entityName",
        extras = MutableCreationExtras().apply {
            this[EntityDetailsViewModel.entityNameKey] = entityName
        }
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    val state = rememberAppAdaptiveScaffoldState(windowAdaptiveInfo)
    var selectedPlugin: PluginInfo? by remember { mutableStateOf(null) }
    val onBack: () -> Unit = {
        if (state.isWideScreen || !state.isShowingPane) {
            mainNavHostController.navigateUp()
        } else {
            state.hidePane()
        }
    }
    val isShowCurrentState = remember { mutableStateOf(false) }
    var isShowInstalledWarning: Boolean by remember { mutableStateOf(false) }
    var isShowDifferentWarning: Boolean by remember { mutableStateOf(false) }
    var isShowUninstallWarning: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onIntent(CommonIntent.Refresh)
    }
    AppAdaptiveScaffold(
        viewModel = viewModel,
        state = state,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.game_plugin_select)) },
                navigationIcon = { AppBackIconButton(onClick = onBack) },
            )
        },
        mainContent = {
            PluginDisplayList(
                modifier = Modifier.fillMaxSize(),
                pluginMap = viewModel.pluginMap
                    .asSequence()
                    .map { it.toPair() }
                    .sortedByDescending { it.second.tier }
                    .sortedBy {
                        if (
                            viewModel.entityProfile != null
                            && viewModel.entityProfile?.path != it.second.data.path
                        ) {
                            1
                        } else {
                            0
                        }
                    }
                    .toList()
                    .toMap(),
                entityProfile = viewModel.entityProfile,
                onPluginSelected = {
                    selectedPlugin = it
                    state.showPane()
                },
            )
        },
        paneContent = {
            val entityProfile = viewModel.entityProfile ?: return@AppAdaptiveScaffold
            val pluginInfo = selectedPlugin ?: return@AppAdaptiveScaffold
            LazyColumn(
                modifier = Modifier,
            ) {
                // Plugin display
                item {
                    EntityPluginDisplay(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 4.dp),
                        plugin = pluginInfo,
                        entityPath = entityProfile.path,
                    )
                }
                item {
                    val isInstalled = pluginInfo.id == entityProfile.plugin?.id
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        // Install
                        AppFilledTonalButton(
                            modifier = Modifier.animateContentSize(),
                            enabled = !isInstalled,
                            iconRes = if (isInstalled) {
                                Res.drawable.ic_outline_check_24
                            } else {
                                Res.drawable.ic_outline_extension_24
                            },
                            text = stringResource(
                                resource = if (isInstalled) {
                                    Res.string.game_plugin_installed
                                } else {
                                    Res.string.game_plugin_install
                                }
                            ),
                            onClick = {
                                when {
                                    pluginInfo.installedBy != null ->
                                        isShowInstalledWarning = true

                                    pluginInfo.data.path != entityProfile.path ->
                                        isShowDifferentWarning = true

                                    else -> viewModel.onIntent(
                                        EntityDetailsIntent.InstallPlugin(pluginInfo.id)
                                    )
                                }
                            }
                        )
                        // Uninstall
                        AnimatedVisibility(visible = isInstalled) {
                            AppFilledTonalButton(
                                modifier = Modifier.padding(start = 4.dp),
                                iconRes = Res.drawable.ic_outline_extension_off_24,
                                text = stringResource(Res.string.game_plugin_uninstall),
                                onClick = { isShowUninstallWarning = true },
                            )
                        }
                    }
                }
                // Current
                if (entityProfile.plugin != null) {
                    stickyHeaderedDropdownVisibility(
                        label = {
                            stringResource(
                                resource = Res.string.game_plugin_select_current,
                                /* entityName = */ entityName,
                            )
                        },
                        expandedState = isShowCurrentState,
                        content = {
                            EntityPluginDisplay(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 4.dp),
                                plugin = entityProfile.plugin,
                                entityPath = entityProfile.path,
                            )
                        }
                    )
                }
            }
        }
    )
    if (isShowInstalledWarning) {
        AppAlertDialog(
            title = stringResource(Res.string.game_plugin_install),
            text = stringResource(
                resource = Res.string.game_plugin_select_installed_by_warn,
                /* installedBy = */ selectedPlugin?.installedBy.toString(),
            ),
            onDismiss = { isShowInstalledWarning = false },
            onConfirmed = {
                isShowInstalledWarning = false
                selectedPlugin?.id?.let {
                    viewModel.onIntent(EntityDetailsIntent.InstallPlugin(it))
                }
            }
        )
    }
    if (isShowDifferentWarning) {
        AppAlertDialog(
            title = stringResource(Res.string.game_plugin_install),
            text = stringResource(Res.string.game_plugin_select_different_path_warn),
            onDismiss = { isShowDifferentWarning = false },
            onConfirmed = {
                isShowDifferentWarning = false
                selectedPlugin?.id?.let {
                    viewModel.onIntent(EntityDetailsIntent.InstallPlugin(it))
                }
            }
        )
    }
    if (isShowUninstallWarning) {
        AppAlertDialog(
            title = stringResource(Res.string.game_plugin_uninstall),
            text = stringResource(Res.string.game_plugin_select_uninstall_warn),
            onDismiss = { isShowUninstallWarning = false },
            onConfirmed = {
                isShowUninstallWarning = false
                viewModel.onIntent(EntityDetailsIntent.UninstallPlugin)
            }
        )
    }
}

@Composable
private fun PluginDisplayList(
    modifier: Modifier = Modifier,
    pluginMap: Map<Long, PluginInfo>,
    entityProfile: ExtendedEntityProfile?,
    onPluginSelected: (PluginInfo) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            items = pluginMap.toList(),
            key = { it.first }
        ) {
            PluginDisplayItem(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 10.dp,
                        vertical = 4.dp,
                    )
                    .animateItem(),
                pluginName = it.second.name,
                pluginPath = it.second.data.path,
                pluginInstalledBy = it.second.installedBy,
                pluginIsLocked = it.second.isLocked,
                entityPath = entityProfile?.path,
                isInstalled = entityProfile?.plugin?.id == it.second.id,
                onClick = { onPluginSelected(it.second) },
            )
        }
    }
}

@Composable
private fun PluginDisplayItem(
    modifier: Modifier = Modifier,
    pluginName: String,
    pluginPath: EntityPath,
    pluginInstalledBy: String?,
    pluginIsLocked: Boolean,
    entityPath: EntityPath?,
    isInstalled: Boolean,
    onClick: () -> Unit,
) {
    val isIdenticalPath = entityPath == null || entityPath == pluginPath
    val supportingText = buildString {
        if (!isIdenticalPath) {
            append(stringResource(Res.string.game_plugin_select_different_path))
        }
        if (pluginInstalledBy != null) {
            append(
                stringResource(
                    resource = Res.string.game_plugin_select_installed_by,
                    /* installedBy = */ pluginInstalledBy,
                )
            )
        }
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isIdenticalPath) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceDim
            },
        ),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedEmojiHeader(
                modifier = Modifier.weight(1F),
                emoji = pluginPath.icon,
                label = pluginName,
                supportingText = supportingText.takeIf { it.isNotEmpty() },
                supportingTextColor = if (isIdenticalPath) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.error
                },
            )
            if (isInstalled) {
                Icon(
                    painter = painterResource(Res.drawable.ic_outline_check_24),
                    contentDescription = stringResource(Res.string.game_plugin_installed),
                )
            }
            if (pluginIsLocked) {
                Icon(
                    painter = painterResource(Res.drawable.ic_outline_lock_24),
                    contentDescription = stringResource(Res.string.game_plugin_locked),
                )
            }
        }
    }
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun GeneralPluginItem() {
    PluginDisplayItem(
        pluginName = "foo",
        pluginPath = EntityPath.HEAP,
        pluginInstalledBy = null,
        pluginIsLocked = false,
        entityPath = EntityPath.HEAP,
        isInstalled = false,
        onClick = {},
    )
}

@Preview
@Composable
private fun InstalledPluginItem() {
    PluginDisplayItem(
        pluginName = "foo",
        pluginPath = EntityPath.HEAP,
        pluginInstalledBy = "Object",
        pluginIsLocked = false,
        entityPath = EntityPath.HEAP,
        isInstalled = true,
        onClick = {},
    )
}

@Preview
@Composable
private fun LockedPluginItem() {
    PluginDisplayItem(
        pluginName = "foo",
        pluginPath = EntityPath.HEAP,
        pluginInstalledBy = "Object",
        pluginIsLocked = true,
        entityPath = EntityPath.HEAP,
        isInstalled = false,
        onClick = {},
    )
}

@Preview
@Composable
private fun DifferentPluginItem() {
    PluginDisplayItem(
        pluginName = "foo",
        pluginPath = EntityPath.HEAP,
        pluginInstalledBy = null,
        pluginIsLocked = false,
        entityPath = EntityPath.PROTOCOL,
        isInstalled = false,
        onClick = {},
    )
}

@Preview
@Composable
private fun DifferentAndInstalledPluginItem() {
    PluginDisplayItem(
        pluginName = "foo",
        pluginPath = EntityPath.HEAP,
        pluginInstalledBy = "AnotherEntity",
        pluginIsLocked = false,
        entityPath = EntityPath.PROTOCOL,
        isInstalled = false,
        onClick = {},
    )
}