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

expect class TeamTable(
    saveID: Int,
    teamID: Int,
    name: String?,
    isActivated: Boolean,
    slot1: String?,
    slot2: String?,
    slot3: String?,
    slot4: String?,
) {

    /**
     * The save ID. -1 presents the temporary save the user is current playing.
     */
    var saveID: Int

    /**
     * The team ID.
     */
    var teamID: Int

    /**
     * Team name.
     */
    var name: String?

    /**
     * This team is activated for combat.
     */
    var isActivated: Boolean

    /**
     * Slot 1 entity name.
     */
    var slot1: String?

    /**
     * Slot 2 entity name.
     */
    var slot2: String?

    /**
     * Slot 3 entity name.
     */
    var slot3: String?

    /**
     * Slot 4 entity name.
     */
    var slot4: String?
}