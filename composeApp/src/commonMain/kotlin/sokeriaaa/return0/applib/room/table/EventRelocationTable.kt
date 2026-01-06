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
package sokeriaaa.return0.applib.room.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

/**
 * Relocating events.
 *
 * @param saveID The save ID. -1 presents the temporary save the user is current playing.
 * @param eventKey Event key.
 * @param fileName Current file name. (For game map)
 * @param lineNumber Current line number. (For game map)
 */
@Entity(
    tableName = EventRelocationTable.TABLE_NAME,
    primaryKeys = ["save_id", "event_key"],
    indices = [Index("file_name")]
)
data class EventRelocationTable(
    @ColumnInfo(name = "save_id")
    var saveID: Int,
    @ColumnInfo(name = "event_key")
    var eventKey: String,
    @ColumnInfo(name = "file_name")
    var fileName: String,
    @ColumnInfo(name = "line_number")
    var lineNumber: Int,
) {
    companion object {
        const val TABLE_NAME = "return0_event_relocations"
    }
}