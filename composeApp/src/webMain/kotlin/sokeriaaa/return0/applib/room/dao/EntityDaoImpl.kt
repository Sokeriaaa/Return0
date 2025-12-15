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

import sokeriaaa.return0.applib.room.sq.SQEntityQueries
import sokeriaaa.return0.applib.room.table.EntityTable

class EntityDaoImpl(
    private val queries: SQEntityQueries,
) : EntityDao {

    override suspend fun queryAll(saveID: Int): List<EntityTable> {
        return queries.queryAll(
            save_id = saveID.toLong(),
            mapper = ::convertToTable,
        ).executeAsList()
    }

    override suspend fun obtainedNewEntity(
        saveID: Int,
        entityName: String,
        initialHP: Int,
        initialSP: Int,
        partyIndex: Int
    ) {
        queries.obtainNewEntity(
            save_id = saveID.toLong(),
            entity_name = entityName,
            current_hp = initialHP.toLong(),
            current_sp = initialSP.toLong(),
            party_index = partyIndex.toLong(),
        )
    }

    override suspend fun getEntity(
        saveID: Int,
        entityName: String
    ): EntityTable? {
        return queries.getEntity(
            save_id = saveID.toLong(),
            entity_name = entityName,
            mapper = ::convertToTable,
        ).executeAsOneOrNull()
    }

    override suspend fun getEntityByIndex(
        saveID: Int,
        partyIndex: Int
    ): EntityTable? {
        return queries.getEntityByIndex(
            save_id = saveID.toLong(),
            party_index = partyIndex.toLong(),
            mapper = ::convertToTable,
        ).executeAsOneOrNull()
    }

    override suspend fun updateHP(saveID: Int, entityName: String, hp: Int) {
        queries.updateHP(
            save_id = saveID.toLong(),
            entity_name = entityName,
            current_hp = hp.toLong(),
        )
    }

    override suspend fun updateSP(saveID: Int, entityName: String, sp: Int) {
        queries.updateSP(
            save_id = saveID.toLong(),
            entity_name = entityName,
            current_sp = sp.toLong(),
        )
    }

    override suspend fun updatePlugin(
        saveID: Int,
        entityName: String,
        pluginID: Long?
    ) {
        queries.updatePlugin(
            save_id = saveID.toLong(),
            entity_name = entityName,
            plugin_id = pluginID,
        )
    }

    override suspend fun insert(entityTable: EntityTable) {
        queries.insertEntity(
            save_id = entityTable.saveID.toLong(),
            entity_name = entityTable.entityName,
            level = entityTable.level.toLong(),
            exp = entityTable.exp.toLong(),
            current_hp = entityTable.currentHP.toLong(),
            current_sp = entityTable.currentHP.toLong(),
            party_index = entityTable.partyIndex.toLong(),
            plugin_id = entityTable.pluginID,
        )
    }

    override suspend fun insertList(list: List<EntityTable>) {
        list.forEach { insert(it) }
    }

    override suspend fun deleteSave(saveID: Int) {
        queries.deleteSave(save_id = saveID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertToTable(
        save_id: Long,
        entity_name: String,
        level: Long,
        exp: Long,
        current_hp: Long,
        current_sp: Long,
        party_index: Long,
        plugin_id: Long?,
    ): EntityTable = EntityTable(
        saveID = save_id.toInt(),
        entityName = entity_name,
        level = level.toInt(),
        exp = exp.toInt(),
        currentHP = current_hp.toInt(),
        currentSP = current_sp.toInt(),
        partyIndex = party_index.toInt(),
        pluginID = plugin_id,
    )

}