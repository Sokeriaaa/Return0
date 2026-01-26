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
 * The entity table for the player.
 *
 * @param saveID The save ID. -1 presents the temporary save the user is current playing.
 * @param entityName Entity name.
 * @param level Entity level.
 * @param exp EXP of current entity.
 * @param currentHP Current HP. `null` means full HP.
 * @param indexedTime The index in the party, between 0 and 3. -1 means not in party.
 * @param pluginID The plugin (weapon) this entity equipped.
 */
@Entity(
    tableName = EntityTable.TABLE_NAME,
    primaryKeys = ["save_id", "entity_name"],
    indices = [Index("plugin_id")]
)
data class EntityTable(
    @ColumnInfo(name = "save_id")
    var saveID: Int = -1,
    @ColumnInfo(name = "entity_name")
    var entityName: String = "",
    @ColumnInfo(name = "level")
    var level: Int = 1,
    @ColumnInfo(name = "exp")
    var exp: Int = 0,
    @ColumnInfo(name = "current_hp")
    var currentHP: Int? = null,
    @ColumnInfo(name = "indexed_time")
    var indexedTime: Long,
    @ColumnInfo(name = "plugin_id")
    var pluginID: Long? = null,
) {
    companion object {
        const val TABLE_NAME = "return0_entities"
    }
}