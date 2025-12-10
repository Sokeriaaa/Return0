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
package sokeriaaa.return0.applib.room.table

/**
 * The entity table for the player.
 */
expect class EntityTable(
    saveID: Int = -1,
    entityName: String = "",
    level: Int = 1,
    exp: Int = 0,
    currentHP: Int = 0,
    currentSP: Int = 0,
    partyIndex: Int = -1,
    pluginID: Long? = null,
) {

    /**
     * The save ID. -1 presents the temporary save the user is current playing.
     */
    var saveID: Int

    /**
     * Entity name.
     */
    var entityName: String

    /**
     * Entity level.
     */
    var level: Int

    /**
     * EXP of current entity.
     */
    var exp: Int

    /**
     * Current HP.
     */
    var currentHP: Int

    /**
     * Current SP.
     */
    var currentSP: Int

    /**
     * The index in the party, between 0 and 3. -1 means not in party.
     */
    var partyIndex: Int

    /**
     * The plugin (weapon) this entity equipped.
     */
    var pluginID: Long?

}