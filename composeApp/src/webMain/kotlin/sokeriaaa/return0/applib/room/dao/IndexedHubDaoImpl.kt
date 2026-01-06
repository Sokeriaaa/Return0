/**
 * Copyright (C) 2026 Sokeriaaa
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

import sokeriaaa.return0.applib.room.sq.SQIndexedHubQueries
import sokeriaaa.return0.applib.room.table.IndexedHubTable

class IndexedHubDaoImpl(
    private val queries: SQIndexedHubQueries,
) : IndexedHubDao {
    override suspend fun queryAll(saveID: Int): List<IndexedHubTable> {
        return queries
            .queryAll(saveID.toLong(), mapper = ::convertToTable)
            .executeAsList()
    }

    override suspend fun insertOrUpdate(table: IndexedHubTable) {
        queries.insertOrUpdate(
            save_id = table.saveID.toLong(),
            file_name = table.fileName,
            line_number = table.lineNumber.toLong(),
            indexed_time = table.indexedTime,
        )
    }

    override suspend fun insertList(list: List<IndexedHubTable>) {
        list.forEach { insertOrUpdate(it) }
    }

    override suspend fun delete(saveID: Int) {
        queries.deleteBySave(saveID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertToTable(
        save_id: Long,
        file_name: String,
        line_number: Long,
        indexed_time: Long,
    ): IndexedHubTable = IndexedHubTable(
        saveID = save_id.toInt(),
        fileName = file_name,
        lineNumber = line_number.toInt(),
        indexedTime = indexed_time,
    )
}