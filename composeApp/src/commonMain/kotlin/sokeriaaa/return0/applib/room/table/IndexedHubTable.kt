/**
 * Copyright (C) 2026 Sokeriaaa
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
package sokeriaaa.return0.applib.room.table

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Indexed router hubs (teleport points).
 */
@Entity(
    tableName = IndexedHubTable.TABLE_NAME,
    primaryKeys = ["save_id", "file_name", "line_number"],
)
data class IndexedHubTable(
    @ColumnInfo(name = "save_id")
    var saveID: Int,
    @ColumnInfo(name = "file_name")
    var fileName: String,
    @ColumnInfo(name = "line_number")
    var lineNumber: Int,
    @ColumnInfo(name = "indexed_time")
    var indexedTime: Long
) {
    companion object {
        const val TABLE_NAME = "return0_indexed_hubs"
    }
}