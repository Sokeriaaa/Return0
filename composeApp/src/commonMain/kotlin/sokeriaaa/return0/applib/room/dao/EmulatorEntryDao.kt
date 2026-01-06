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
import sokeriaaa.return0.applib.room.table.EmulatorEntryTable

@Dao
interface EmulatorEntryDao {

    /**
     * Insert a list of emulator preset.
     */
    @Insert
    suspend fun insertList(list: List<EmulatorEntryTable>)

    /**
     * Query the preset list with the specified ID.
     */
    @Query(
        "SELECT * FROM `${EmulatorEntryTable.TABLE_NAME}` WHERE " +
                "`preset_id`=:presetID"
    )
    suspend fun queryList(presetID: Int): List<EmulatorEntryTable>

    /**
     * Delete the whole preset list with the specified ID.
     */
    @Query(
        "DELETE FROM `${EmulatorEntryTable.TABLE_NAME}` WHERE `preset_id`=:presetID"
    )
    suspend fun deletePreset(presetID: Int)
}