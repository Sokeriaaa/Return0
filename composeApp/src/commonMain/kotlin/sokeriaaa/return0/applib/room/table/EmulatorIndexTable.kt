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
import androidx.room.PrimaryKey

/**
 * The index of emulator table, contains some meta.
 *
 * @param presetID Preset ID.
 * @param name Name.
 * @param createdTime Created time in millis.
 */
@Entity(
    tableName = EmulatorIndexTable.TABLE_NAME,
)
data class EmulatorIndexTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "preset_id")
    var presetID: Int? = null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "created_time")
    var createdTime: Long,
) {
    companion object {
        const val TABLE_NAME = "return0_emulator_indices"
    }
}