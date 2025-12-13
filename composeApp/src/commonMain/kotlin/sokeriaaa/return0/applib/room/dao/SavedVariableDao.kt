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

import sokeriaaa.return0.applib.room.table.SavedVariableTable

expect interface SavedVariableDao {
    /**
     * Query the saved variable with specified save ID and key.
     */
    suspend fun query(saveID: Int, key: String): SavedVariableTable?

    /**
     * Query all the saved variables with specified save ID.
     */
    suspend fun queryAll(saveID: Int): List<SavedVariableTable>

    /**
     * Insert or update.
     */
    suspend fun insertOrUpdate(table: SavedVariableTable)

    /**
     * Insert a list of saved variables.
     */
    suspend fun insertList(list: List<SavedVariableTable>)

    /**
     * Delete the saved variables for specified save ID.
     */
    suspend fun delete(saveID: Int)
}