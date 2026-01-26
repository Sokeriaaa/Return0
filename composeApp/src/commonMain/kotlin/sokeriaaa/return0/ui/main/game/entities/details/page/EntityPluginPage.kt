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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_plugin_install_alt
import return0.composeapp.generated.resources.game_plugin_switch
import return0.composeapp.generated.resources.game_plugin_uninstall
import return0.composeapp.generated.resources.ic_outline_add_circle_24
import sokeriaaa.return0.models.entity.plugin.display.PluginInfo
import sokeriaaa.return0.shared.data.api.component.value.PercentValue
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.extras.CommonExtra
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginData
import sokeriaaa.return0.ui.common.modifier.dashedBorder
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
                InstallPluginCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    onClick = onRequestSwitch
                )
            }
        } else {
            item {
                EntityPluginDisplay(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
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

@Composable
private fun InstallPluginCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .dashedBorder(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(32.dp),
            )
            .clickable(onClick = onClick)
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(Res.drawable.ic_outline_add_circle_24),
            contentDescription = stringResource(Res.string.game_plugin_install_alt),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(Res.string.game_plugin_install_alt),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun InstallPluginCardPreview() {
    InstallPluginCard(
        modifier = Modifier.width(360.dp),
        onClick = {},
    )
}

@Preview
@Composable
private fun EntityPluginPageNone() {
    EntityPluginPage(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        plugin = null,
        entityPath = EntityPath.UNSPECIFIED,
        onRequestSwitch = {},
        onRequestUninstall = {},
    )
}

@Preview
@Composable
private fun EntityPluginPageIdenticalPath() {
    EntityPluginPage(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        plugin = PluginInfo(
            name = "Example Plugin",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            tier = 1,
            constMap = mapOf(
                PluginConst.ATK to 1,
                PluginConst.DEF to 2,
                PluginConst.CRIT_RATE to 3,
                PluginConst.CRIT_DMG to 4,
            ),
            data = PluginData(
                key = "example",
                nameRes = "plugin.example",
                descriptionRes = "plugin.example.desc",
                path = EntityPath.HEAP,
                onAttack = CombatExtra.HPChange(Value(-42)),
                onDefend = CommonExtra.ForUser(CombatExtra.HPChange(Value(42))),
                attackRateOffset = PercentValue(0.42F),
                defendRateOffset = PercentValue(0.42F),
            ),
            isLocked = false,
            installedBy = "Example",
        ),
        entityPath = EntityPath.HEAP,
        onRequestSwitch = {},
        onRequestUninstall = {},
    )
}


@Preview
@Composable
private fun EntityPluginPageDifferentPath() {
    EntityPluginPage(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        plugin = PluginInfo(
            name = "Example Plugin",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            tier = 1,
            constMap = mapOf(
                PluginConst.ATK to 1,
                PluginConst.DEF to 2,
                PluginConst.CRIT_RATE to 3,
                PluginConst.CRIT_DMG to 4,
            ),
            data = PluginData(
                key = "example",
                nameRes = "plugin.example",
                descriptionRes = "plugin.example.desc",
                path = EntityPath.HEAP,
                onAttack = CombatExtra.HPChange(Value(-42)),
                onDefend = CommonExtra.ForUser(CombatExtra.HPChange(Value(42))),
                attackRateOffset = PercentValue(0.42F),
                defendRateOffset = PercentValue(0.42F),
            ),
            isLocked = false,
            installedBy = "Example",
        ),
        entityPath = EntityPath.PROTOCOL,
        onRequestSwitch = {},
        onRequestUninstall = {},
    )
}
