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

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Team data.
 *
 * @param saveID The save ID. -1 presents the temporary save the user is current playing.
 * @param teamID The team ID.
 * @param name Team name.
 * @param isActivated This team is activated for combat.
 * @param slot1 Slot 1 entity name.
 * @param slot2 Slot 2 entity name.
 * @param slot3 Slot 3 entity name.
 * @param slot4 Slot 4 entity name.
 */
@Entity(
    tableName = TeamTable.TABLE_NAME,
    primaryKeys = ["save_id", "team_id"]
)
data class TeamTable(
    @ColumnInfo(name = "save_id")
    var saveID: Int,
    @ColumnInfo(name = "team_id")
    var teamID: Int,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "is_activated")
    var isActivated: Boolean,
    @ColumnInfo(name = "slot1")
    var slot1: String?,
    @ColumnInfo(name = "slot2")
    var slot2: String?,
    @ColumnInfo(name = "slot3")
    var slot3: String?,
    @ColumnInfo(name = "slot4")
    var slot4: String?,
) {
    companion object {
        const val TABLE_NAME = "return0_team"
    }
}