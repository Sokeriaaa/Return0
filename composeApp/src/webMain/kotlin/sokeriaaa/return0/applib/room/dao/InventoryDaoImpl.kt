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

import sokeriaaa.return0.applib.room.table.InventoryTable

class InventoryDaoImpl(
    val queries: SQInventoryDaoQueries,
) : InventoryDao {
    override suspend fun query(
        saveID: Int,
        key: String
    ): InventoryTable? {
        return queries.query(
            save_id = saveID.toLong(),
            item_key = key,
            mapper = ::convertToTable,
        ).executeAsOneOrNull()
    }

    override suspend fun queryAll(saveID: Int): List<InventoryTable> {
        return queries.queryAll(
            save_id = saveID.toLong(),
            mapper = ::convertToTable,
        ).executeAsList()
    }

    override suspend fun insertOrUpdate(table: InventoryTable) {
        queries.insertOrUpdate(
            save_id = table.saveID.toLong(),
            item_key = table.key,
            amount = table.amount.toLong(),
        )
    }

    override suspend fun insertList(list: List<InventoryTable>) {
        list.forEach { insertOrUpdate(it) }
    }

    override suspend fun delete(saveID: Int) {
        queries.delete(save_id = saveID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertToTable(
        save_id: Long,
        item_key: String,
        amount: Long,
    ): InventoryTable = InventoryTable(
        saveID = save_id.toInt(),
        key = item_key,
        amount = amount.toInt(),
    )
}