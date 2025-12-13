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

actual interface QuestDao {
    actual fun query(saveID: Int, key: String): QuestTable
    actual fun queryAll(saveID: Int): List<QuestTable>
    actual fun insertOrUpdate(table: QuestTable)
    actual fun insertList(list: List<QuestTable>)
    actual fun delete(saveID: Int)
}