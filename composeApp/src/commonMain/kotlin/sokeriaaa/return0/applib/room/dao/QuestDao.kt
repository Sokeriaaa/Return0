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

import sokeriaaa.return0.applib.room.table.QuestTable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

expect interface QuestDao {
    /**
     * Query the quest with specified save ID and quest key.
     */
    suspend fun query(saveID: Int, key: String): QuestTable?

    /**
     * Query all the quests with specified save ID.
     */
    suspend fun queryAll(saveID: Int): List<QuestTable>

    /**
     * Query all the activated quests with specified save ID.
     */
    @OptIn(ExperimentalTime::class)
    suspend fun queryActivated(
        saveID: Int,
        time: Long = Clock.System.now().toEpochMilliseconds(),
    ): List<QuestTable>

    /**
     * Insert or update.
     */
    suspend fun insertOrUpdate(table: QuestTable)

    /**
     * Insert a list of quests.
     */
    suspend fun insertList(list: List<QuestTable>)

    /**
     * Delete the quests for specified save ID.
     */
    suspend fun delete(saveID: Int)
}