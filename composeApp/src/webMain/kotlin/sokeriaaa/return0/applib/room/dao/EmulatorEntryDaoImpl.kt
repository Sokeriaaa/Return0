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

import sokeriaaa.return0.applib.room.sq.SQEmulatorEntryQueries
import sokeriaaa.return0.applib.room.table.EmulatorEntryTable

class EmulatorEntryDaoImpl(
    private val queries: SQEmulatorEntryQueries,
) : EmulatorEntryDao {

    override suspend fun insertList(list: List<EmulatorEntryTable>) {
        list.forEach {
            queries.insert(
                preset_id = it.presetID.toLong(),
                is_party = if (it.isParty) 1L else 0L,
                entity_name = it.entityName,
                level = it.level.toLong(),
                plugin_id = it.pluginID,
                boss_multiplier = it.bossMultiplier.toLong()
            )
        }
    }

    override suspend fun queryList(presetID: Int): List<EmulatorEntryTable> {
        return queries.queryList(
            preset_id = presetID.toLong(),
            mapper = ::convertToTable
        ).executeAsList()
    }

    override suspend fun deletePreset(presetID: Int) {
        queries.deletePreset(preset_id = presetID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertToTable(
        id: Long?,
        preset_id: Long,
        is_party: Long,
        entity_name: String,
        level: Long,
        plugin_id: Long?,
        boss_multiplier: Long,
    ): EmulatorEntryTable = EmulatorEntryTable(
        id = id?.toInt(),
        presetID = preset_id.toInt(),
        isParty = is_party == 1L,
        entityName = entity_name,
        level = level.toInt(),
        pluginID = plugin_id,
        bossMultiplier = boss_multiplier.toInt()
    )
}