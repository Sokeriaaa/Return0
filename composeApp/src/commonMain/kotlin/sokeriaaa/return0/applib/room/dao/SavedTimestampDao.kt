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
import sokeriaaa.return0.applib.room.table.SavedTimestampTable

@Dao
interface SavedTimestampDao {
    /**
     * Query the saved timestamp with specified save ID and key.
     */
    @Query(
        "SELECT * FROM `${SavedTimestampTable.TABLE_NAME}` WHERE `save_id`=:saveID AND `saved_key`=:key"
    )
    suspend fun query(saveID: Int, key: String): SavedTimestampTable?

    /**
     * Query all the saved timestamps with specified save ID.
     */
    @Query(
        "SELECT * FROM `${SavedTimestampTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun queryAll(saveID: Int): List<SavedTimestampTable>

    /**
     * Insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: SavedTimestampTable)

    /**
     * Insert a list of saved timestamps.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<SavedTimestampTable>)

    /**
     * Delete the saved timestamps for specified save ID.
     */
    @Query(
        "DELETE FROM `${SavedTimestampTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun delete(saveID: Int)
}