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
package sokeriaaa.return0.ui.common.entity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.cancel
import return0.composeapp.generated.resources.empty_slot
import return0.composeapp.generated.resources.game_select_entity
import return0.composeapp.generated.resources.game_select_entity_warn
import return0.composeapp.generated.resources.ok
import sokeriaaa.return0.models.entity.display.EntityProfile
import sokeriaaa.return0.ui.common.widgets.AppTextButton

/**
 * Select an entity in current team. Mainly for item usages.
 */
@Composable
fun EntitySelectionDialog(
    modifier: Modifier = Modifier,
    entities: Array<EntityProfile?>,
    onSelectedIndex: (index: Int) -> Unit,
) {
    // Selected index
    var selectedIndex by remember { mutableStateOf(-1) }
    // Warning
    var warningRes: StringResource? by remember { mutableStateOf(null) }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(Res.string.game_select_entity),
            )
        },
        text = {
            Column {
                Column(
                    modifier = Modifier.selectableGroup(),
                ) {
                    entities.forEachIndexed { index, entity ->
                        val enabled = entity != null
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = index == selectedIndex,
                                    enabled = enabled,
                                    onClick = { selectedIndex = index },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = index == selectedIndex,
                                enabled = enabled,
                                // null recommended for accessibility with screen readers
                                onClick = null,
                            )
                            Text(
                                text = entity?.name ?: stringResource(Res.string.empty_slot),
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (enabled) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.outline
                                },
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
                // Warning text
                warningRes?.let {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        confirmButton = {
            AppTextButton(
                text = stringResource(Res.string.ok),
                onClick = {
                    if (selectedIndex == -1) {
                        warningRes = Res.string.game_select_entity_warn
                    } else {
                        onSelectedIndex(selectedIndex)
                    }
                },
            )
        },
        dismissButton = {
            AppTextButton(
                text = stringResource(Res.string.cancel),
                onClick = { onSelectedIndex(-1) },
            )
        },
    )
}