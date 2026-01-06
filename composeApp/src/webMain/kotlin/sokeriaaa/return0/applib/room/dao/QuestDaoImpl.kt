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

import sokeriaaa.return0.applib.room.sq.SQQuestQueries
import sokeriaaa.return0.applib.room.table.QuestTable

class QuestDaoImpl(
    val queries: SQQuestQueries,
) : QuestDao {
    override suspend fun query(
        saveID: Int,
        key: String
    ): QuestTable? {
        return queries.query(
            save_id = saveID.toLong(),
            quest_key = key,
            mapper = ::convertToTable,
        ).executeAsOneOrNull()
    }

    override suspend fun queryAll(saveID: Int): List<QuestTable> {
        return queries.queryAll(
            save_id = saveID.toLong(),
            mapper = ::convertToTable,
        ).executeAsList()
    }

    override suspend fun queryActivated(
        saveID: Int,
        time: Long
    ): List<QuestTable> {
        return queries.queryActivated(
            save_id = saveID.toLong(),
            expired_at = time,
            mapper = ::convertToTable,
        ).executeAsList()
    }

    override suspend fun insertOrUpdate(table: QuestTable) {
        queries.insertOrUpdate(
            save_id = table.saveID.toLong(),
            quest_key = table.key,
            expired_at = table.expiredAt,
            completed = if (table.completed) 1L else 0L,
            completed_time = table.completedTime,
        )
    }

    override suspend fun insertList(list: List<QuestTable>) {
        list.forEach { insertOrUpdate(it) }
    }

    override suspend fun delete(saveID: Int) {
        queries.deleteBySave(save_id = saveID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertToTable(
        save_id: Long,
        quest_key: String,
        expired_at: Long?,
        completed: Long,
        completedTime: Long?,
    ): QuestTable = QuestTable(
        saveID = save_id.toInt(),
        key = quest_key,
        expiredAt = expired_at,
        completed = completed == 1L,
        completedTime = completedTime,
    )
}