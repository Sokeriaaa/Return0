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

import sokeriaaa.return0.applib.room.table.EntityTable

expect interface EntityDao {

    /**
     * Query all entities form specified save ID.
     */
    suspend fun queryAll(
        saveID: Int = -1,
    ): List<EntityTable>

    /**
     * Invokes when the player obtained a new entity.
     */
    suspend fun obtainedNewEntity(
        saveID: Int = -1,
        entityName: String,
        initialHP: Int,
        initialSP: Int,
        partyIndex: Int = -1,
    )

    /**
     * Get entity by name.
     */
    suspend fun getEntity(
        saveID: Int = -1,
        entityName: String,
    ): EntityTable?

    /**
     * Get entity by the index in party.
     */
    suspend fun getEntityByIndex(
        saveID: Int = -1,
        partyIndex: Int,
    ): EntityTable?

    /**
     * Update the current HP of entity.
     */
    suspend fun updateHP(
        saveID: Int = -1,
        entityName: String,
        hp: Int?,
    )

    /**
     * Update the current SP of entity.
     */
    suspend fun updateSP(
        saveID: Int = -1,
        entityName: String,
        sp: Int?,
    )

    /**
     * Change the equipped plugin of entity.
     */
    suspend fun updatePlugin(
        saveID: Int = -1,
        entityName: String,
        pluginID: Long?,
    )

    /**
     * Insert an EntityTable directly.
     */
    suspend fun insert(
        entityTable: EntityTable,
    )

    /**
     * Insert an EntityTable directly.
     */
    suspend fun insertList(
        list: List<EntityTable>,
    )

    /**
     * Delete a whole save.
     */
    suspend fun deleteSave(
        saveID: Int,
    )

}