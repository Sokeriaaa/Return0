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
import sokeriaaa.return0.applib.room.table.QuestTable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Dao
interface QuestDao {
    /**
     * Query the quest with specified save ID and quest key.
     */
    @Query(
        "SELECT * FROM `${QuestTable.TABLE_NAME}` WHERE `save_id`=:saveID AND `quest_key`=:key"
    )
    suspend fun query(saveID: Int, key: String): QuestTable?

    /**
     * Query all the quests with specified save ID.
     */
    @Query(
        "SELECT * FROM `${QuestTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun queryAll(saveID: Int): List<QuestTable>

    /**
     * Query all the activated quests with specified save ID.
     */
    @Query(
        "SELECT * FROM `${QuestTable.TABLE_NAME}` WHERE `save_id`=:saveID AND `completed`=0 " +
                "AND (`expired_at` IS NULL OR `expired_at`>:time)"
    )
    @OptIn(ExperimentalTime::class)
    suspend fun queryActivated(
        saveID: Int,
        time: Long = Clock.System.now().toEpochMilliseconds(),
    ): List<QuestTable>

    /**
     * Insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: QuestTable)

    /**
     * Insert a list of quests.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<QuestTable>)

    /**
     * Delete the quests for specified save ID.
     */
    @Query(
        "DELETE FROM `${QuestTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun delete(saveID: Int)
}