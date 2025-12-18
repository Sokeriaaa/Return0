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
import sokeriaaa.return0.applib.room.table.EventRelocationTable

@Dao
actual interface EventRelocationDao {
    @Query(
        "SELECT * FROM `${EventRelocationTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun queryAll(saveID: Int): List<EventRelocationTable>

    @Query(
        "SELECT * FROM `${EventRelocationTable.TABLE_NAME}` WHERE `save_id`=:saveID " +
                "AND `event_key`=:eventKey AND `file_name`=:fileName LIMIT 1"
    )
    actual suspend fun query(saveID: Int, eventKey: String, fileName: String): EventRelocationTable?

    @Query(
        "SELECT * FROM `${EventRelocationTable.TABLE_NAME}` WHERE `save_id`=:saveID " +
                "AND `file_name`=:fileName"
    )
    actual suspend fun queryAllByFileName(saveID: Int, fileName: String): List<EventRelocationTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insertOrUpdate(table: EventRelocationTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insertOrUpdateList(list: List<EventRelocationTable>)

    @Query(
        "DELETE FROM `${EventRelocationTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun deleteSave(saveID: Int)
}