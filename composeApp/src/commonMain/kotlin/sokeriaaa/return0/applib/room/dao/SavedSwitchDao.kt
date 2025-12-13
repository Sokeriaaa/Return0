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

import sokeriaaa.return0.applib.room.table.SavedSwitchTable

expect interface SavedSwitchDao {
    /**
     * Query the saved switch with specified save ID and key.
     */
    fun query(saveID: Int, key: String): SavedSwitchTable

    /**
     * Query all the saved switches with specified save ID.
     */
    fun queryAll(saveID: Int): List<SavedSwitchTable>

    /**
     * Insert or update.
     */
    fun insertOrUpdate(table: SavedSwitchTable)

    /**
     * Insert a list of saved switches.
     */
    fun insertList(list: List<SavedSwitchTable>)

    /**
     * Delete the saved switches for specified save ID.
     */
    fun delete(saveID: Int)
}