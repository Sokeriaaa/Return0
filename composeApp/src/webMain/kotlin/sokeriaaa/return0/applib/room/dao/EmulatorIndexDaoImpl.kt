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

import sokeriaaa.return0.applib.room.table.EmulatorIndexTable

class EmulatorIndexDaoImpl(
    private val queries: SQEmulatorIndexDaoQueries,
) : EmulatorIndexDao {
    override suspend fun queryAll(): List<EmulatorIndexTable> {
        return queries.queryAll(mapper = ::convertToTable).executeAsList()
    }

    override suspend fun insert(emulatorIndexTable: EmulatorIndexTable): Long {
        queries.insert(
            preset_id = null,
            name = emulatorIndexTable.name,
            created_time = emulatorIndexTable.createdTime,
        )
        return selectLastInsertRowId()
    }

    override suspend fun selectLastInsertRowId(): Long {
        return queries.selectLastInsertRowId().executeAsOne()
    }

    override suspend fun rename(presetID: Int, name: String) {
        queries.rename(
            preset_id = presetID.toLong(),
            name = name,
        )
    }

    override suspend fun delete(presetID: Int) {
        queries.delete(
            preset_id = presetID.toLong(),
        )
    }

    @Suppress("LocalVariableName")
    private fun convertToTable(
        preset_id: Long,
        name: String,
        created_time: Long,
    ): EmulatorIndexTable = EmulatorIndexTable(
        presetID = preset_id.toInt(),
        name = name,
        createdTime = created_time,
    )
}