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

@Entity(
    tableName = TeamTable.TABLE_NAME,
    primaryKeys = ["save_id", "team_id"]
)
actual class TeamTable actual constructor(
    @ColumnInfo(name = "save_id")
    actual var saveID: Int,
    @ColumnInfo(name = "team_id")
    actual var teamID: Int,
    @ColumnInfo(name = "name")
    actual var name: String?,
    @ColumnInfo(name = "is_activated")
    actual var isActivated: Boolean,
    @ColumnInfo(name = "slot1")
    actual var slot1: String?,
    @ColumnInfo(name = "slot2")
    actual var slot2: String?,
    @ColumnInfo(name = "slot3")
    actual var slot3: String?,
    @ColumnInfo(name = "slot4")
    actual var slot4: String?
) {
    companion object {
        const val TABLE_NAME = "return0_team"
    }
}