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
import kotlin.time.ExperimentalTime

@Entity(
    tableName = EmulatorIndexTable.TABLE_NAME,
)
@OptIn(ExperimentalTime::class)
actual data class EmulatorIndexTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "preset_id")
    actual var presetID: Int? = null,
    @ColumnInfo(name = "name")
    actual var name: String,
    @ColumnInfo(name = "created_time")
    actual var createdTime: Long,
) {
    companion object {
        const val TABLE_NAME = "return0_emulator_indices"
    }
}