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
interface EventRelocationDao {

    /**
     * Query all events form specified save ID.
     */
    @Query(
        "SELECT * FROM `${EventRelocationTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun queryAll(saveID: Int = -1): List<EventRelocationTable>

    /**
     * Query the event with specified key and from specified map in the specified save.
     */
    @Query(
        "SELECT * FROM `${EventRelocationTable.TABLE_NAME}` WHERE `save_id`=:saveID " +
                "AND `event_key`=:eventKey AND `file_name`=:fileName LIMIT 1"
    )
    suspend fun query(
        saveID: Int = -1,
        eventKey: String,
        fileName: String,
    ): EventRelocationTable?

    /**
     * Query all events from specified map in the specified save.
     */
    @Query(
        "SELECT * FROM `${EventRelocationTable.TABLE_NAME}` WHERE `save_id`=:saveID " +
                "AND `file_name`=:fileName"
    )
    suspend fun queryAllByFileName(
        saveID: Int = -1,
        fileName: String,
    ): List<EventRelocationTable>

    /**
     * Insert an EventRelocationTable directly.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: EventRelocationTable)

    /**
     * Insert a list of EventRelocationTable directly.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateList(list: List<EventRelocationTable>)

    /**
     * Delete a whole save.
     */
    @Query(
        "DELETE FROM `${EventRelocationTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun deleteSave(saveID: Int)
}