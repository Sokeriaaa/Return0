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

import sokeriaaa.return0.applib.room.table.EventRelocationTable

expect interface EventRelocationDao {

    /**
     * Query all events form specified save ID.
     */
    suspend fun queryAll(
        saveID: Int = -1,
    ): List<EventRelocationTable>

    /**
     * Insert an EventRelocationTable directly.
     */
    suspend fun insertOrUpdate(
        table: EventRelocationTable,
    )

    /**
     * Insert a list of EventRelocationTable directly.
     */
    suspend fun insertOrUpdateList(
        list: List<EventRelocationTable>,
    )

    /**
     * Delete a whole save.
     */
    suspend fun deleteSave(
        saveID: Int,
    )
}