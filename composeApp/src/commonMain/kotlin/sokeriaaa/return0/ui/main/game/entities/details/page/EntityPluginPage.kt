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
package sokeriaaa.return0.ui.main.game.entities.details.page

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_plugin_install
import return0.composeapp.generated.resources.game_plugin_not_installed
import return0.composeapp.generated.resources.game_plugin_switch
import return0.composeapp.generated.resources.game_plugin_uninstall
import sokeriaaa.return0.models.entity.plugin.display.PluginInfo
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.ui.common.widgets.AppButton
import sokeriaaa.return0.ui.main.game.entities.plugin.EntityPluginDisplay

@Composable
fun EntityPluginPage(
    modifier: Modifier = Modifier,
    plugin: PluginInfo?,
    entityPath: EntityPath,
    onRequestSwitch: () -> Unit,
    onRequestUninstall: () -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (plugin == null) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.game_plugin_not_installed),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            item {
                AppButton(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(Res.string.game_plugin_install),
                    onClick = onRequestSwitch,
                )
            }
        } else {
            item {
                EntityPluginDisplay(
                    modifier = Modifier.fillMaxWidth(),
                    plugin = plugin,
                    entityPath = entityPath,
                )
            }
            item {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    AppButton(
                        text = stringResource(Res.string.game_plugin_switch),
                        onClick = onRequestSwitch,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    AppButton(
                        text = stringResource(Res.string.game_plugin_uninstall),
                        onClick = onRequestUninstall,
                    )
                }
            }
        }
    }
}
