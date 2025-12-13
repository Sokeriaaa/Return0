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

import sokeriaaa.return0.applib.room.table.SaveMetaTable

expect interface SaveMetaDao {

    /**
     * Query all the saves in current device.
     */
    suspend fun queryAll(): List<SaveMetaTable>

    /**
     * Query the save with the specified ID.
     */
    suspend fun query(saveID: Int): SaveMetaTable?

    /**
     * Insert or update the save.
     */
    suspend fun insertOrUpdate(table: SaveMetaTable)

    /**
     * Delete the save with specified ID.
     */
    suspend fun delete(saveID: Int)
}