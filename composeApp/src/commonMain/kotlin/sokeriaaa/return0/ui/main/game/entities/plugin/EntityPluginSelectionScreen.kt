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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
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
import return0.composeapp.generated.resources.component_extra_empty
import return0.composeapp.generated.resources.game_plugin_install
import return0.composeapp.generated.resources.game_plugin_installed
import return0.composeapp.generated.resources.game_plugin_locked
import return0.composeapp.generated.resources.game_plugin_select
import return0.composeapp.generated.resources.game_plugin_select_current
import return0.composeapp.generated.resources.game_plugin_select_different_path
import return0.composeapp.generated.resources.game_plugin_select_different_path_warn
import return0.composeapp.generated.resources.game_plugin_select_installed_by
import return0.composeapp.generated.resources.game_plugin_select_installed_by_warn
import return0.composeapp.generated.resources.game_plugin_select_selected
import return0.composeapp.generated.resources.game_plugin_select_uninstall_warn
import return0.composeapp.generated.resources.game_plugin_uninstall
import return0.composeapp.generated.resources.ic_outline_check_24
import return0.composeapp.generated.resources.ic_outline_lock_24
import sokeriaaa.return0.models.entity.display.ExtendedEntityProfile
import sokeriaaa.return0.models.entity.plugin.display.PluginInfo
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.intents.EntityDetailsIntent
import sokeriaaa.return0.mvi.viewmodels.EntityDetailsViewModel
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.widgets.AppAlertDialog
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton
import sokeriaaa.return0.ui.common.widgets.OutlinedEmojiHeader

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
    LaunchedEffect(Unit) {
        viewModel.onIntent(CommonIntent.Refresh)
    }
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.game_plugin_select)) },
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
        var selectedPlugin: PluginInfo? by remember { mutableStateOf(null) }
        val entityProfile = viewModel.entityProfile ?: return@AppScaffold


        if (windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(600)) {
            // Horizontal
            Row(modifier = Modifier.padding(paddingValues = paddingValues)) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(300.dp)
                        .padding(horizontal = 8.dp),
                ) {
                    CurrentPluginPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                        entityName = entityName,
                        pluginInfo = entityProfile.plugin,
                        entityPath = entityProfile.path,
                        onIntent = viewModel::onIntent,
                    )
                    HorizontalDivider()
                    SelectedPluginPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                        pluginInfo = selectedPlugin,
                        entityPath = entityProfile.path,
                        onIntent = viewModel::onIntent,
                    )
                }
                VerticalDivider()
                PluginSelectPanel(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1F),
                    viewModel = viewModel,
                    onPluginSelected = { selectedPlugin = it },
                )
            }
        } else {
            // Vertical
            Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                ) {
                    CurrentPluginPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                            .padding(horizontal = 8.dp),
                        entityName = entityName,
                        pluginInfo = entityProfile.plugin,
                        entityPath = entityProfile.path,
                        onIntent = viewModel::onIntent,
                    )
                    VerticalDivider()
                    SelectedPluginPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                            .padding(horizontal = 8.dp),
                        pluginInfo = selectedPlugin,
                        entityPath = entityProfile.path,
                        onIntent = viewModel::onIntent,
                    )
                }
                HorizontalDivider()
                PluginSelectPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F),
                    viewModel = viewModel,
                    onPluginSelected = { selectedPlugin = it },
                )
            }
        }
    }
}

@Composable
private fun CurrentPluginPanel(
    modifier: Modifier = Modifier,
    entityName: String,
    pluginInfo: PluginInfo?,
    entityPath: EntityPath,
    onIntent: (BaseIntent) -> Unit,
) {
    var isShowUninstallWarning: Boolean by remember { mutableStateOf(false) }
    if (isShowUninstallWarning) {
        AppAlertDialog(
            title = stringResource(Res.string.game_plugin_uninstall),
            text = stringResource(Res.string.game_plugin_select_uninstall_warn),
            onDismiss = { isShowUninstallWarning = false },
            onConfirmed = {
                isShowUninstallWarning = false
                onIntent(EntityDetailsIntent.UninstallPlugin)
            }
        )
    }
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(
                resource = Res.string.game_plugin_select_current,
                /* entityName = */ entityName,
            ),
            style = MaterialTheme.typography.bodySmall,
        )
        if (pluginInfo == null) {
            Text(stringResource(Res.string.component_extra_empty))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                item {
                    EntityPluginDisplay(
                        modifier = Modifier.fillMaxWidth(),
                        plugin = pluginInfo,
                        entityPath = entityPath,
                    )
                }
            }
            Row {
                TextButton(onClick = { isShowUninstallWarning = true }) {
                    Text(stringResource(Res.string.game_plugin_uninstall))
                }
            }
        }
    }
}

@Composable
private fun SelectedPluginPanel(
    modifier: Modifier = Modifier,
    pluginInfo: PluginInfo?,
    entityPath: EntityPath,
    onIntent: (BaseIntent) -> Unit,
) {
    var isShowInstalledWarning: Boolean by remember { mutableStateOf(false) }
    var isShowDifferentWarning: Boolean by remember { mutableStateOf(false) }
    if (isShowInstalledWarning) {
        AppAlertDialog(
            title = stringResource(Res.string.game_plugin_install),
            text = stringResource(
                resource = Res.string.game_plugin_select_installed_by_warn,
                /* installedBy = */ pluginInfo?.installedBy.toString(),
            ),
            onDismiss = { isShowInstalledWarning = false },
            onConfirmed = {
                isShowInstalledWarning = false
                pluginInfo?.id?.let { onIntent(EntityDetailsIntent.InstallPlugin(it)) }
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
                pluginInfo?.id?.let { onIntent(EntityDetailsIntent.InstallPlugin(it)) }
            }
        )
    }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.game_plugin_select_selected),
            style = MaterialTheme.typography.bodySmall,
        )
        if (pluginInfo == null) {
            Text(stringResource(Res.string.component_extra_empty))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                item {
                    EntityPluginDisplay(
                        modifier = Modifier.fillMaxWidth(),
                        plugin = pluginInfo,
                        entityPath = entityPath,
                    )
                }
            }
            Row {
                TextButton(
                    onClick = {
                        when {
                            pluginInfo.installedBy != null -> isShowInstalledWarning = true
                            pluginInfo.data.path != entityPath -> isShowDifferentWarning = true
                            else -> onIntent(EntityDetailsIntent.InstallPlugin(pluginInfo.id))
                        }
                    }
                ) {
                    Text(stringResource(Res.string.game_plugin_install))
                }
            }
        }
    }
}

@Composable
private fun PluginSelectPanel(
    modifier: Modifier = Modifier,
    viewModel: EntityDetailsViewModel,
    onPluginSelected: (PluginInfo) -> Unit,
) {
    Column(modifier = modifier) {
        PluginDisplayList(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
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
            onPluginSelected = onPluginSelected,
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
                    .animateItem()
                    .clickable { onPluginSelected(it.second) },
                pluginName = it.second.name,
                pluginPath = it.second.data.path,
                pluginInstalledBy = it.second.installedBy,
                pluginIsLocked = it.second.isLocked,
                entityPath = entityProfile?.path,
                isInstalled = entityProfile?.plugin?.id == it.second.id,
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
        )
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
    )
}