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
actual interface StatisticsDao {
    @Query(
        "SELECT * FROM `${StatisticsTable.TABLE_NAME}`"
    )
    actual suspend fun queryAll(): List<StatisticsTable>

    @Query(
        "SELECT * FROM `${StatisticsTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun query(saveID: Int): StatisticsTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insertOrUpdate(table: StatisticsTable)

    @Query(
        "DELETE FROM `${StatisticsTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun delete(saveID: Int)
}