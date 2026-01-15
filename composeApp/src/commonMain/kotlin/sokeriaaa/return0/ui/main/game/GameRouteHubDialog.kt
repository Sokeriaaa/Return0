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
package sokeriaaa.return0.ui.main.game

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_hub
import return0.composeapp.generated.resources.game_hub_indexed
import return0.composeapp.generated.resources.game_hub_teleport
import return0.composeapp.generated.resources.game_hub_teleport_warn
import return0.composeapp.generated.resources.general_location
import return0.composeapp.generated.resources.ic_outline_location_on_24
import sokeriaaa.return0.ui.common.widgets.AppAlertDialog
import sokeriaaa.return0.ui.common.widgets.AppRadioGroup

@Composable
fun GameRouteHubDialog(
    modifier: Modifier = Modifier,
    currentLocation: Pair<String, Int>?,
    availableHubs: List<Pair<String, Int>>,
    onConfirm: (Pair<String, Int>) -> Unit,
    onDismiss: () -> Unit,
) {
    // Selected hub
    var selectedHub: Pair<String, Int>? by remember { mutableStateOf(null) }
    // Warning
    var warning: StringResource? by remember { mutableStateOf(null) }
    AppAlertDialog(
        modifier = modifier,
        iconRes = Res.drawable.ic_outline_location_on_24,
        title = currentLocation?.let {
            stringResource(
                resource = Res.string.general_location,
                /* file = */ it.first,
                /* row = */ it.second,
            )
        } ?: stringResource(Res.string.game_hub),
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                // List
                item {
                    Text(
                        text = stringResource(Res.string.game_hub_indexed),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                item {
                    AppRadioGroup(
                        modifier = Modifier.fillMaxWidth(),
                        items = availableHubs,
                        selectedIndex = availableHubs.indexOf(selectedHub),
                        itemLabel = {
                            stringResource(
                                resource = Res.string.general_location,
                                /* file = */ it.first,
                                /* row = */ it.second,
                            )
                        },
                        onSelected = {
                            selectedHub = availableHubs[it]
                        },
                    )
                }
                // Warning text
                warning?.let {
                    item {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = stringResource(it),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
        },
        confirmText = stringResource(Res.string.game_hub_teleport),
        onDismiss = onDismiss,
        onConfirmed = {
            if (selectedHub == null) {
                warning = Res.string.game_hub_teleport_warn
            } else {
                onConfirm(selectedHub!!)
            }
        },
    )
}