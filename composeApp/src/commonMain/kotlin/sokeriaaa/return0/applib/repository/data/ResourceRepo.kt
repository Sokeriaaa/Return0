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
package sokeriaaa.return0.applib.repository.data

import org.jetbrains.compose.resources.MissingResourceException
import return0.composeapp.generated.resources.Res
import sokeriaaa.return0.shared.common.helpers.JsonHelper

/**
 * Managing resources such as i18n string.
 *
 * Mainly for the story texts and archive descriptions.
 */
class ResourceRepo {

    private val _stringResMap: MutableMap<String, String> = HashMap()

    /**
     * Get string value.
     */
    fun getString(key: String, default: String = ""): String = _stringResMap[key] ?: default

    /**
     * Load the string resources of specified language.
     *
     * @param language Format like "en-us"
     */
    suspend fun load(language: String) {
        val strings = try {
            // Match the full language & region.
            Res.readBytes("files/res/strings_$language.json").decodeToString()
        } catch (_: MissingResourceException) {
            // Use default source set.
            Res.readBytes("files/res/strings.json").decodeToString()
        }
        // Load
        _stringResMap.putAll(JsonHelper.decodeFromString<Map<String, String>>(strings))
    }

    /**
     * Reset this repo.
     */
    fun reset() {
        _stringResMap.clear()
    }

}