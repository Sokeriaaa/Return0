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

@Entity(
    tableName = SavedVariableTable.TABLE_NAME,
    primaryKeys = ["save_id", "saved_key"],
)
actual data class SavedVariableTable actual constructor(
    @ColumnInfo(name = "save_id")
    actual var saveID: Int,
    @ColumnInfo(name = "saved_key")
    actual var key: String,
    @ColumnInfo(name = "saved_value")
    actual var value: Int
) {
    companion object {
        const val TABLE_NAME = "return0_saved_variables"
    }
}