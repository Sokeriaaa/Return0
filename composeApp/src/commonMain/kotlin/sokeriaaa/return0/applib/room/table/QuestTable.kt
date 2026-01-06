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
 * Quest table.
 */
@Entity(
    tableName = QuestTable.TABLE_NAME,
    primaryKeys = ["save_id", "quest_key"],
)
data class QuestTable(
    @ColumnInfo(name = "save_id")
    var saveID: Int,
    @ColumnInfo(name = "quest_key")
    var key: String,
    @ColumnInfo(name = "expired_at")
    var expiredAt: Long?,
    @ColumnInfo(name = "completed")
    var completed: Boolean,
    @ColumnInfo(name = "completed_time")
    var completedTime: Long?,
) {
    companion object {
        const val TABLE_NAME = "return0_quests"
    }
}