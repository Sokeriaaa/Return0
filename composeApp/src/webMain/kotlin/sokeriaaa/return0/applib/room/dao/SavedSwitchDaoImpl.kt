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

import sokeriaaa.return0.applib.room.sq.SQSavedSwitchesQueries
import sokeriaaa.return0.applib.room.table.SavedSwitchTable

class SavedSwitchDaoImpl(
    val queries: SQSavedSwitchesQueries,
) : SavedSwitchDao {
    override suspend fun query(
        saveID: Int,
        key: String
    ): SavedSwitchTable? {
        return queries.query(
            save_id = saveID.toLong(),
            saved_key = key,
            mapper = ::convertToTable,
        ).executeAsOneOrNull()
    }

    override suspend fun queryAll(saveID: Int): List<SavedSwitchTable> {
        return queries.queryAll(
            save_id = saveID.toLong(),
            mapper = ::convertToTable,
        ).executeAsList()
    }

    override suspend fun insertOrUpdate(table: SavedSwitchTable) {
        queries.insertOrUpdate(
            save_id = table.saveID.toLong(),
            saved_key = table.key,
            saved_value = if (table.value) 1L else 0L,
        )
    }

    override suspend fun insertList(list: List<SavedSwitchTable>) {
        list.forEach { insertOrUpdate(it) }
    }

    override suspend fun delete(saveID: Int) {
        queries.deleteBySave(save_id = saveID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertToTable(
        save_id: Long,
        saved_key: String,
        saved_value: Long,
    ): SavedSwitchTable = SavedSwitchTable(
        saveID = save_id.toInt(),
        key = saved_key,
        value = saved_value == 1L,
    )
}