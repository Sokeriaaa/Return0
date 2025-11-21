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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.cancel
import return0.composeapp.generated.resources.emulator_picker_add
import return0.composeapp.generated.resources.emulator_picker_remove
import return0.composeapp.generated.resources.emulator_picker_remove_warn
import return0.composeapp.generated.resources.general_level_w_value
import return0.composeapp.generated.resources.ic_baseline_add_24
import return0.composeapp.generated.resources.ic_baseline_delete_24
import return0.composeapp.generated.resources.ok
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.EmulatorIntent
import sokeriaaa.return0.shared.data.api.combat.entityData
import sokeriaaa.return0.shared.data.api.combat.level
import sokeriaaa.return0.shared.data.models.combat.EntityState
import sokeriaaa.return0.ui.common.widgets.AppFilledIconButton
import sokeriaaa.return0.ui.common.widgets.AppTextButton

/**
 * The pages of EmulatorScreen.
 */
@Composable
fun EmulatorPage(
    isParty: Boolean,
    entityStateList: List<EntityState>,
    onIntent: (BaseIntent) -> Unit,
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    val maxSize = if (isParty) AppConstants.ARENA_MAX_PARTY else AppConstants.ARENA_MAX_ENEMY
    var openPickerDialog: Boolean by remember { mutableStateOf(false) }
    var currentEntity: EntityState? by remember { mutableStateOf(null) }
    var entityToRemove: EntityState? by remember { mutableStateOf(null) }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(1),
    ) {
        items(
            items = entityStateList,
        ) { entityState ->
            EntityItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        currentEntity = entityState
                        openPickerDialog = true
                    },
                entityState = entityState,
                onDismiss = {
                    entityToRemove = entityState
                },
            )
        }
        // Shows the add button if the selected entity list is not full.
        if (entityStateList.size < maxSize) {
            item {
                AddItem(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = {
                        currentEntity = null
                        openPickerDialog = true
                    },
                )
            }
        }
    }

    // Entity picker dialog
    if (openPickerDialog) {
        EmulatorEntityPickerDialog(
            isParty = isParty,
            currentEntity = currentEntity,
            onConfirmed = { entity ->
                if (currentEntity == null) {
                    // Add
                    onIntent(EmulatorIntent.AddEntity(entity))
                } else {
                    // Alter
                    onIntent(EmulatorIntent.AlterEntity(before = currentEntity!!, after = entity))
                }
                currentEntity = null
                openPickerDialog = false
            },
            onDismiss = {
                openPickerDialog = false
            }
        )
    }
    // Remove entity dialog
    entityToRemove?.let {
        AlertDialog(
            modifier = Modifier.padding(
                vertical = 64.dp,
            ),
            onDismissRequest = {
                entityToRemove = null
            },
            text = {
                Text(
                    text = stringResource(
                        resource = Res.string.emulator_picker_remove_warn,
                        /* entityName = */ it.entityData.name,
                    ),
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 16.sp,
                )
            },
            confirmButton = {
                AppTextButton(
                    text = stringResource(Res.string.ok),
                    onClick = {
                        onIntent(EmulatorIntent.RemoveEntity(it))
                        entityToRemove = null
                    },
                )
            },
            dismissButton = {
                AppTextButton(
                    text = stringResource(Res.string.cancel),
                    onClick = {
                        entityToRemove = null
                    },
                )
            },
        )
    }
}

/**
 * Entity item. Implements swipe to dismiss.
 */
@Composable
private fun EntityItem(
    modifier: Modifier = Modifier,
    entityState: EntityState,
    onDismiss: () -> Unit,
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier,
    ) {
        SwipeToDismissBox(
            modifier = modifier,
            state = swipeToDismissBoxState,
            enableDismissFromStartToEnd = false,
            onDismiss = {
                if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                    onDismiss()
                }
                // Reset the dismiss state here,
                // as we will remove this entity via the confirm dialog.
                scope.launch {
                    swipeToDismissBoxState.reset()
                }
            },
            backgroundContent = {
                if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        painter = painterResource(Res.drawable.ic_baseline_delete_24),
                        contentDescription = stringResource(Res.string.emulator_picker_remove),
                        tint = Color.White,
                    )
                }
            },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            ) {
                Text(
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 8.dp),
                    text = entityState.entityData.name,
                )
                Text(
                    text = stringResource(
                        resource = Res.string.general_level_w_value,
                        /* level = */ entityState.level,
                    )
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun AddItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    AppFilledIconButton(
        modifier = modifier,
        iconRes = Res.drawable.ic_baseline_add_24,
        contentDescription = stringResource(Res.string.emulator_picker_add),
        onClick = onClick,
    )
}