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

import sokeriaaa.return0.applib.room.table.SavedTimestampTable

expect interface SavedTimestampDao {
    /**
     * Query the saved timestamp with specified save ID and key.
     */
    suspend fun query(saveID: Int, key: String): SavedTimestampTable?

    /**
     * Query all the saved timestamps with specified save ID.
     */
    suspend fun queryAll(saveID: Int): List<SavedTimestampTable>

    /**
     * Insert or update.
     */
    suspend fun insertOrUpdate(table: SavedTimestampTable)

    /**
     * Insert a list of saved timestamps.
     */
    suspend fun insertList(list: List<SavedTimestampTable>)

    /**
     * Delete the saved timestamps for specified save ID.
     */
    suspend fun delete(saveID: Int)
}