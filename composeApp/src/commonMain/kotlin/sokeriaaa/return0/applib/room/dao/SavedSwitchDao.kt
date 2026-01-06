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
import sokeriaaa.return0.applib.room.table.SavedSwitchTable

@Dao
interface SavedSwitchDao {
    /**
     * Query the saved switch with specified save ID and key.
     */
    @Query(
        "SELECT * FROM `${SavedSwitchTable.TABLE_NAME}` WHERE `save_id`=:saveID AND `saved_key`=:key"
    )
    suspend fun query(saveID: Int, key: String): SavedSwitchTable?

    /**
     * Query all the saved switches with specified save ID.
     */
    @Query(
        "SELECT * FROM `${SavedSwitchTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun queryAll(saveID: Int): List<SavedSwitchTable>

    /**
     * Insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: SavedSwitchTable)

    /**
     * Insert a list of saved switches.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<SavedSwitchTable>)

    /**
     * Delete the saved switches for specified save ID.
     */
    @Query(
        "DELETE FROM `${SavedSwitchTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun delete(saveID: Int)
}