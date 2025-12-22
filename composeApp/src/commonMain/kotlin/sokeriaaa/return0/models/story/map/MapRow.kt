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
package sokeriaaa.return0.models.story.map

import androidx.compose.runtime.mutableStateListOf
import sokeriaaa.return0.shared.data.models.story.map.MapEvent

/**
 * Map row data class.
 *
 * @param depth Depth of the braces.
 * @param text Default display text. Will be replaced by a displayable event.
 * @param events events currently at this line.
 */
class MapRow(
    val depth: Int,
    val text: String,
    val events: MutableList<MapEvent> = mutableStateListOf(),
)