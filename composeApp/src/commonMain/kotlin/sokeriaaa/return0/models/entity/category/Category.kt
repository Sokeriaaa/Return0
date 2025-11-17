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
package sokeriaaa.return0.models.entity.category

import kotlinx.serialization.SerialName

/**
 * Category of an Entity or Action, like "Types" in Pokémon.
 * Damage will be affected by categories of both Action and Entity.
 */
enum class Category(val id: Int, val icon: String) {

    /**
     * A special category for Items. (Hide)
     */
    @SerialName("itm")
    ITEM(id = -1, icon = ""),

    /**
     * A special category for Common actions like Attack, Defense and Escape. (Hide)
     */
    @SerialName("nml")
    NORMAL(id = 0, icon = ""),

    @SerialName("cls")
    CLASS(id = 1, icon = "\uD83E\uDDF1"),

    @SerialName("int")
    INTERFACE(id = 2, icon = "\uD83E\uDDE9\uFE0F"),

    @SerialName("gen")
    GENERIC(id = 3, icon = "\uD83C\uDF00"),

    @SerialName("ioo")
    IO(id = 4, icon = "\uD83D\uDDC2\uFE0F"),

    @SerialName("con")
    CONCURRENT(id = 5, icon = "\uD83D\uDD17"),

    @SerialName("exc")
    EXCEPTION(id = 6, icon = "\uD83D\uDCA5"),

    @SerialName("mem")
    MEMORY(id = 7, icon = "\uD83D\uDCBE"),

    @SerialName("str")
    STREAM(id = 8, icon = "\uD83C\uDF0A"),

    @SerialName("dbg")
    DEBUG(id = 9, icon = "\uD83E\uDEB2"),

    @SerialName("vid")
    VOID(id = 10, icon = "\uD83C\uDF0C"),
}