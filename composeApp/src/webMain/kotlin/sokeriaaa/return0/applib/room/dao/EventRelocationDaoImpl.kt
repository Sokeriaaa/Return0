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

import sokeriaaa.return0.applib.room.sq.SQEventRelocationQueries
import sokeriaaa.return0.applib.room.table.EventRelocationTable

class EventRelocationDaoImpl(
    private val queries: SQEventRelocationQueries,
) : EventRelocationDao {

    override suspend fun queryAll(saveID: Int): List<EventRelocationTable> {
        return queries.queryAllBySave(
            save_id = saveID.toLong(),
            mapper = ::convertToTable,
        ).executeAsList()
    }

    override suspend fun insertOrUpdate(table: EventRelocationTable) {
        queries.insertOrUpdate(
            save_id = table.saveID.toLong(),
            event_key = table.eventKey,
            file_name = table.fileName,
            line_number = table.lineNumber.toLong(),
        )
    }

    override suspend fun insertOrUpdateList(list: List<EventRelocationTable>) {
        list.forEach { insertOrUpdate(it) }
    }

    override suspend fun deleteSave(saveID: Int) {
        queries.deleteSave(save_id = saveID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertToTable(
        save_id: Long,
        event_key: String,
        file_name: String,
        line_number: Long,
    ): EventRelocationTable = EventRelocationTable(
        saveID = save_id.toInt(),
        eventKey = event_key,
        fileName = file_name,
        lineNumber = line_number.toInt(),
    )
}