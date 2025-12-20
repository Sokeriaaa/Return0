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
package sokeriaaa.return0.applib.repository.game

import org.jetbrains.compose.resources.MissingResourceException
import return0.composeapp.generated.resources.Res
import sokeriaaa.return0.shared.common.helpers.JsonHelper
import sokeriaaa.return0.shared.data.models.story.map.MapData

/**
 * Manage the game maps.
 */
class GameMapRepo {

    /**
     * Load a map with specified path.
     *
     * @throws MissingResourceException If the map file is not exist.
     */
    suspend fun loadMap(path: String): MapData {
        return JsonHelper.decodeFromString(
            string = Res.readBytes("files/data/map/$path.json").decodeToString(),
        )
    }

    /**
     * Load a map with specified path. Returns `null` if the map is not exist.
     */
    suspend fun loadMapOrNull(path: String): MapData? {
        return try {
            loadMap(path)
        } catch (_: MissingResourceException) {
            null
        }
    }

}