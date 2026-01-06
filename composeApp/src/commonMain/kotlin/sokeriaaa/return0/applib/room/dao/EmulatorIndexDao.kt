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
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sokeriaaa.return0.applib.room.table.EmulatorIndexTable

@Dao
interface EmulatorIndexDao {

    @Query(
        "SELECT * FROM `${EmulatorIndexTable.TABLE_NAME}`"
    )
    suspend fun queryAll(): List<EmulatorIndexTable>

    /**
     * Insert a new item, then return the auto-generated ID.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(emulatorIndexTable: EmulatorIndexTable): Long

    /**
     * Rename a preset.
     */
    @Query(
        "UPDATE `${EmulatorIndexTable.TABLE_NAME}` SET " +
                "`name`=:name WHERE " +
                "`preset_id`=:presetID"
    )
    suspend fun rename(presetID: Int, name: String)

    /**
     * Delete a preset.
     */
    @Query(
        "DELETE FROM `${EmulatorIndexTable.TABLE_NAME}` WHERE `preset_id`=:presetID"
    )
    suspend fun delete(presetID: Int)
}