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

/**
 * Saved timestamps by events.
 *
 * @param saveID The save ID. -1 presents the temporary save the user is current playing.
 * @param key Timestamp key.
 * @param timestamp Timestamp value.
 */
@Entity(
    tableName = SavedTimestampTable.TABLE_NAME,
    primaryKeys = ["save_id", "saved_key"],
)
data class SavedTimestampTable(
    @ColumnInfo(name = "save_id")
    var saveID: Int,
    @ColumnInfo(name = "saved_key")
    var key: String,
    @ColumnInfo(name = "saved_timestamp")
    var timestamp: Long = 0L,
) {
    companion object {
        const val TABLE_NAME = "return0_saved_timestamps"
    }
}