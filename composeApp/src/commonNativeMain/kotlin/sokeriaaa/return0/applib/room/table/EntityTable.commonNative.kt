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

@Entity(
    tableName = EntityTable.TABLE_NAME,
    primaryKeys = ["save_id", "entity_name"],
    indices = [Index("party_index"), Index("plugin_id")]
)
actual data class EntityTable(
    @ColumnInfo(name = "save_id")
    actual var saveID: Int = -1,
    @ColumnInfo(name = "entity_name")
    actual var entityName: String = "",
    @ColumnInfo(name = "level")
    actual var level: Int = 1,
    @ColumnInfo(name = "exp")
    actual var exp: Int = 0,
    @ColumnInfo(name = "current_hp")
    actual var currentHP: Int = 0,
    @ColumnInfo(name = "current_sp")
    actual var currentSP: Int = 0,
    @ColumnInfo(name = "party_index")
    actual var partyIndex: Int = -1,
    @ColumnInfo(name = "plugin_id")
    actual var pluginID: Long? = null,
) {
    companion object {
        const val TABLE_NAME = "return0_entities"
    }
}