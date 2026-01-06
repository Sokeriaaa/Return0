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
import sokeriaaa.return0.applib.room.table.StatisticsTable

@Dao
interface StatisticsDao {

    /**
     * Query all the saves in current device.
     */
    @Query(
        "SELECT * FROM `${StatisticsTable.TABLE_NAME}`"
    )
    suspend fun queryAll(): List<StatisticsTable>

    /**
     * Query the save with the specified ID.
     */
    @Query(
        "SELECT * FROM `${StatisticsTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun query(saveID: Int): StatisticsTable?

    /**
     * Insert or update the save.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: StatisticsTable)

    /**
     * Delete the save with specified ID.
     */

    @Query(
        "DELETE FROM `${StatisticsTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun delete(saveID: Int)
}