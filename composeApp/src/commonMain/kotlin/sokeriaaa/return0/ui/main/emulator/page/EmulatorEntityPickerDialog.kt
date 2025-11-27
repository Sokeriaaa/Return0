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
package sokeriaaa.return0.ui.main.emulator.page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.cancel
import return0.composeapp.generated.resources.emulator_picker_add
import return0.composeapp.generated.resources.emulator_picker_edit
import return0.composeapp.generated.resources.emulator_picker_entity
import return0.composeapp.generated.resources.emulator_picker_warning_select_entity
import return0.composeapp.generated.resources.general_level_w_value
import return0.composeapp.generated.resources.general_please_select
import return0.composeapp.generated.resources.ok
import sokeriaaa.return0.shared.data.api.combat.entityData
import sokeriaaa.return0.shared.data.api.combat.level
import sokeriaaa.return0.shared.data.models.combat.EnemyState
import sokeriaaa.return0.shared.data.models.combat.EntityState
import sokeriaaa.return0.shared.data.models.combat.PartyState
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.ui.common.widgets.AppDropdownSelector
import sokeriaaa.return0.ui.common.widgets.AppTextButton
import kotlin.math.roundToInt

/**
 * Entity picker dialog. Add or alter an EntityState.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmulatorEntityPickerDialog(
    isParty: Boolean,
    availableEntities: List<EntityData>,
    currentEntity: EntityState?,
    onConfirmed: (entityState: EntityState) -> Unit,
    onDismiss: () -> Unit,
) {
    // Is adding a brand new entity.
    val isAdd = currentEntity == null
    // Selected data
    var selectedEntityData: EntityData? by remember { mutableStateOf(currentEntity?.entityData) }
    // Entity level
    var level: Int by remember { mutableStateOf(currentEntity?.level ?: 100) }
    // Warning
    var warning: StringResource? by remember { mutableStateOf(null) }
    // TODO Temp list
    val options = arrayOf(
        null,
        *availableEntities.toTypedArray(),
    )
    val availableEntityNameList = options
        .map { it?.name ?: stringResource(Res.string.general_please_select) }
        .toTypedArray()
    AlertDialog(
        modifier = Modifier.padding(vertical = 64.dp),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    resource = if (isAdd) {
                        Res.string.emulator_picker_add
                    } else {
                        Res.string.emulator_picker_edit
                    },
                )
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                // Entity
                item {
                    AppDropdownSelector(
                        title = stringResource(Res.string.emulator_picker_entity),
                        menuOptions = availableEntityNameList,
                        initSelectedOption = options.indexOf(selectedEntityData),
                        onItemSelected = { index ->
                            selectedEntityData = options[index]
                        },
                    )
                }
                // Level
                item {
                    Text(
                        text = stringResource(
                            resource = Res.string.general_level_w_value,
                            /* level = */ level,
                        ),
                        modifier = Modifier.padding(top = 8.dp),
                        fontSize = 16.sp,
                    )
                    Slider(
                        value = level.toFloat(),
                        onValueChange = {
                            level = it.roundToInt()
                        },
                        valueRange = 1F..100F,
                        steps = 100,
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
        confirmButton = {
            AppTextButton(
                text = stringResource(Res.string.ok),
                onClick = {
                    val entityData = selectedEntityData
                    if (entityData == null) {
                        // Please select the entity.
                        warning = Res.string.emulator_picker_warning_select_entity
                    } else {
                        // Add/Alter the entity.
                        onConfirmed(
                            if (isParty) {
                                PartyState(
                                    entityData = entityData,
                                    level = level,
                                )
                            } else {
                                EnemyState(
                                    entityData = entityData,
                                    level = level,
                                )
                            }
                        )
                    }
                },
            )
        },
        dismissButton = {
            AppTextButton(
                text = stringResource(Res.string.cancel),
                onClick = onDismiss,
            )
        },
    )
}