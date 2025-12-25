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
package sokeriaaa.return0.ui.main.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ic_outline_left_click_24
import return0.composeapp.generated.resources.ic_outline_move_down_24
import return0.composeapp.generated.resources.ic_outline_move_up_24
import return0.composeapp.generated.resources.map_action_interact_w_display
import return0.composeapp.generated.resources.map_action_move
import return0.composeapp.generated.resources.map_action_too_far_away
import return0.composeapp.generated.resources.no_operation
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.mvi.intents.GameIntent
import sokeriaaa.return0.mvi.viewmodels.GameViewModel
import sokeriaaa.return0.shared.data.models.story.map.MapEvent
import sokeriaaa.return0.ui.common.widgets.AppDropdownMenuItem

@Composable
fun GameMap(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Spacer(
                modifier = Modifier.height(120.dp)
            )
        }
        item {
            MapRow(
                modifier = Modifier.fillMaxWidth(),
                text = "fun ${viewModel.current.name}() {",
            )
        }
        itemsIndexed(items = viewModel.mapRows) { index, item ->
            val eventDisplays = item.events.filter { it.display != null }
            MapRow(
                modifier = Modifier.fillMaxWidth(),
                lineNumber = index + 1,
                currentLine = viewModel.lineNumber,
                text = "    ".repeat(item.depth + 1) + if (eventDisplays.isEmpty()) {
                    item.text
                } else {
                    eventDisplays.joinToString(";") { it.display!! }
                },
                events = item.events,
                onMoveClicked = {
                    viewModel.onIntent(GameIntent.RequestMoveTo(index + 1))
                },
                onInteractClicked = {
                    viewModel.onIntent(GameIntent.ExecuteEvent(it))
                }
            )
        }
        item {
            MapRow(
                modifier = Modifier.fillMaxWidth(),
                text = "}",
            )
        }
        item {
            Spacer(
                modifier = Modifier.height(120.dp)
            )
        }
    }
}

/**
 * A map row.
 *
 * @param lineNumber Current line number. Set to `null` to hide.
 * @param currentLine The current line number of player.
 * @param text display text.
 * @param events [MapEvent] in this row.
 */
@Composable
private fun MapRow(
    modifier: Modifier = Modifier,
    lineNumber: Int? = null,
    currentLine: Int = -1,
    text: String,
    events: List<MapEvent> = emptyList(),
    onMoveClicked: () -> Unit = {},
    onInteractClicked: (MapEvent) -> Unit = {},
) {
    // Show DropDownMenu
    var isMenuExpanded by remember { mutableStateOf(false) }
    // Calculate map row action.
    val mapRowAction: List<MapRowAction> =
        remember(key1 = lineNumber, key2 = currentLine, key3 = events) {
            if (lineNumber == null) {
                return@remember emptyList()
            }
            val actions = ArrayList<MapRowAction>()
            // Moving
            when {
                lineNumber - currentLine in 1..AppConstants.MAP_MAX_MOVEMENT -> actions.add(
                    MapRowAction.MoveDown
                )

                currentLine - lineNumber in 1..AppConstants.MAP_MAX_MOVEMENT -> actions.add(
                    MapRowAction.MoveUp
                )

                lineNumber != currentLine -> actions.add(MapRowAction.TooFarAway)
            }
            // Interacting
            events.forEachIndexed { index, event ->
                if (
                    event.trigger == MapEvent.Trigger.INTERACTED &&
                    lineNumber - currentLine in -1..1
                ) {
                    actions.add(MapRowAction.Interact(index, event))
                }
            }
            actions
        }
    // Colors
    val rowBackgroundColor = if (isMenuExpanded) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val rowTextColor = if (events.isNotEmpty()) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Line number
        Text(
            modifier = Modifier
                .width(48.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(end = 12.dp),
            text = if (lineNumber == currentLine) {
                "⬤"
            } else {
                lineNumber?.toString() ?: ""
            },
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.bodySmall,
        )
        Box(
            modifier = Modifier.weight(1F)
        ) {
            // Line text
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(rowBackgroundColor)
                    .clickable {
                        isMenuExpanded = true
                    },
                text = text,
                color = rowTextColor,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // Operations
            DropdownMenu(
                expanded = isMenuExpanded,
                offset = DpOffset(x = 24.dp, y = 0.dp),
                onDismissRequest = {
                    isMenuExpanded = false
                },
            ) {
                if (mapRowAction.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        text = stringResource(Res.string.no_operation),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                mapRowAction.forEach {
                    when (it) {
                        is MapRowAction.Interact -> {
                            AppDropdownMenuItem(
                                iconRes = Res.drawable.ic_outline_left_click_24,
                                text = stringResource(
                                    resource = Res.string.map_action_interact_w_display,
                                    /* display = */ it.event.display ?: "#${it.index}",
                                ),
                                onClick = {
                                    isMenuExpanded = false
                                    onInteractClicked(it.event)
                                },
                            )
                        }

                        MapRowAction.MoveDown -> {
                            AppDropdownMenuItem(
                                iconRes = Res.drawable.ic_outline_move_down_24,
                                text = stringResource(Res.string.map_action_move),
                                onClick = {
                                    isMenuExpanded = false
                                    onMoveClicked()
                                },
                            )
                        }

                        MapRowAction.MoveUp -> {
                            AppDropdownMenuItem(
                                iconRes = Res.drawable.ic_outline_move_up_24,
                                text = stringResource(Res.string.map_action_move),
                                onClick = {
                                    isMenuExpanded = false
                                    onMoveClicked()
                                },
                            )
                        }

                        MapRowAction.TooFarAway -> {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                text = stringResource(Res.string.map_action_too_far_away),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Types of the map row action.
 */
private sealed class MapRowAction {
    data object MoveUp : MapRowAction()
    data object MoveDown : MapRowAction()
    data class Interact(val index: Int, val event: MapEvent) : MapRowAction()
    data object TooFarAway : MapRowAction()
}