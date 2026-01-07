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
import sokeriaaa.return0.shared.data.models.title.Title

class SaveMetaDaoImpl(
    private val queries: SQSaveMetaDaoQueries,
) : SaveMetaDao {
    override suspend fun queryAll(): List<SaveMetaTable> {
        return queries.queryAll(mapper = ::convertTotable).executeAsList()
    }

    override suspend fun query(saveID: Int): SaveMetaTable? {
        return queries.query(saveID.toLong(), mapper = ::convertTotable).executeAsOneOrNull()
    }

    override suspend fun insertOrUpdate(table: SaveMetaTable) {
        queries.insertOrUpdate(
            save_id = table.saveID.toLong(),
            created_timed = table.createdTime,
            saved_timed = table.savedTime,
            title = table.title.name,
            file_name = table.fileName,
            line_number = table.lineNumber.toLong(),
        )
    }

    override suspend fun updatePosition(
        saveID: Int,
        fileName: String,
        lineNumber: Int
    ) {
        queries.updatePosition(
            save_id = saveID.toLong(),
            file_name = fileName,
            line_number = lineNumber.toLong(),
        )
    }

    override suspend fun updateTitle(
        saveID: Int,
        title: Title
    ) {
        queries.updateTitle(
            save_id = saveID.toLong(),
            title = title.name
        )
    }

    override suspend fun delete(saveID: Int) {
        queries.delete(saveID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertTotable(
        save_id: Long,
        created_timed: Long,
        saved_timed: Long,
        title: String,
        file_name: String,
        line_number: Long,
    ): SaveMetaTable = SaveMetaTable(
        saveID = save_id.toInt(),
        createdTime = created_timed,
        savedTime = saved_timed,
        title = Title.valueOf(title),
        fileName = file_name,
        lineNumber = line_number.toInt(),
    )
}