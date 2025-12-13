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

import sokeriaaa.return0.applib.room.table.StatisticsTable

expect interface StatisticsDao {

    /**
     * Query all the saves in current device.
     */
    suspend fun queryAll(): List<StatisticsTable>

    /**
     * Query the save with the specified ID.
     */
    suspend fun query(saveID: Int): StatisticsTable?

    /**
     * Insert or update the save.
     */
    suspend fun insertOrUpdate(table: StatisticsTable)

    /**
     * Delete the save with specified ID.
     */
    suspend fun delete(saveID: Int)
}