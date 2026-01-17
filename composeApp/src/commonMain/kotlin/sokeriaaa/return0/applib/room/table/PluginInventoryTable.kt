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
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Relation

/**
 * The plugin inventory.
 */
@Entity(
    tableName = PluginInventoryTable.TABLE_NAME,
    primaryKeys = ["save_id", "plugin_id"],
    indices = [Index("installed_by")]
)
data class PluginInventoryTable(
    @ColumnInfo(name = "save_id")
    var saveID: Int,
    @ColumnInfo(name = "plugin_id")
    var pluginID: Long,
    @ColumnInfo(name = "installed_by")
    var installedBy: String? = null,
    // Params: Reserved for future use.
    @ColumnInfo(name = "param1")
    var param1: String? = null,
    @ColumnInfo(name = "param2")
    var param2: String? = null,
    @ColumnInfo(name = "param3")
    var param3: String? = null,
    @ColumnInfo(name = "param4")
    var param4: String? = null,
    @ColumnInfo(name = "param5")
    var param5: String? = null,
) {
    companion object {
        const val TABLE_NAME = "return0_plugin_inventory"
    }
}

/**
 * The full plugin data.
 */
data class PluginItem(
    @Embedded
    val inventory: PluginInventoryTable,
    @Relation(
        parentColumn = "plugin_id",
        entityColumn = "plugin_id",
    )
    val constData: PluginConstTable,
)