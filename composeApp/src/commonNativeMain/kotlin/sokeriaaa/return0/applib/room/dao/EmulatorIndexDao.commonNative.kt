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
package sokeriaaa.return0.applib.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import sokeriaaa.return0.applib.room.table.EmulatorIndexTable

@Dao
actual interface EmulatorIndexDao {

    @Query(
        "SELECT * FROM `${EmulatorIndexTable.TABLE_NAME}`"
    )
    actual suspend fun queryAll(): List<EmulatorIndexTable>

    @Insert
    actual suspend fun insert(emulatorIndexTable: EmulatorIndexTable): Long

    @Query(
        "UPDATE `${EmulatorIndexTable.TABLE_NAME}` SET " +
                "`name`=:name WHERE " +
                "`preset_id`=:presetID"
    )
    actual suspend fun rename(presetID: Int, name: String)

    @Query(
        "DELETE FROM `${EmulatorIndexTable.TABLE_NAME}` WHERE `preset_id`=:presetID"
    )
    actual suspend fun delete(presetID: Int)
}