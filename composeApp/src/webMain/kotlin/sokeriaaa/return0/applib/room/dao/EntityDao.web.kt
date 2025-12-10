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

actual interface EntityDao {
    actual suspend fun obtainedNewEntity(
        saveID: Int,
        entityName: String,
        initialHP: Int,
        initialSP: Int,
        partyIndex: Int
    )

    actual suspend fun getEntity(
        saveID: Int,
        entityName: String
    ): EntityTable?

    actual suspend fun getEntityByIndex(
        saveID: Int,
        partyIndex: Int
    ): EntityTable?

    actual suspend fun updateHP(saveID: Int, entityName: String, hp: Int)
    actual suspend fun updateSP(saveID: Int, entityName: String, sp: Int)
    actual suspend fun updatePlugin(saveID: Int, entityName: String, pluginID: Long?)
    actual suspend fun insert(entityTable: EntityTable)
    actual suspend fun insertList(list: List<EntityTable>)
    actual suspend fun deleteSave(saveID: Int)
}