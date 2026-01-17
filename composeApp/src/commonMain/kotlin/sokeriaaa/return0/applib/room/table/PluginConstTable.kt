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
import androidx.room.PrimaryKey
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst

/**
 * A universal table for storing all the static plugin data used by all saves and emulators.
 *
 * @param isEmulator This plugin is created via emulator.
 */
@Entity(
    tableName = PluginConstTable.TABLE_NAME,
)
data class PluginConstTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "plugin_id")
    var pluginID: Long? = null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "tier")
    var tier: Int,
    @ColumnInfo(name = "is_emulator")
    var isEmulator: Boolean = false,
    @ColumnInfo(name = "const1")
    var const1: PluginConst? = null,
    @ColumnInfo(name = "const1_tier")
    var const1Tier: Int = 0,
    @ColumnInfo(name = "const2")
    var const2: PluginConst? = null,
    @ColumnInfo(name = "const2_tier")
    var const2Tier: Int = 0,
    @ColumnInfo(name = "const3")
    var const3: PluginConst? = null,
    @ColumnInfo(name = "const3_tier")
    var const3Tier: Int = 0,
    @ColumnInfo(name = "const4")
    var const4: PluginConst? = null,
    @ColumnInfo(name = "const4_tier")
    var const4Tier: Int = 0,
    @ColumnInfo(name = "const5")
    var const5: PluginConst? = null,
    @ColumnInfo(name = "const5_tier")
    var const5Tier: Int = 0,
    @ColumnInfo(name = "const6")
    var const6: PluginConst? = null,
    @ColumnInfo(name = "const6_tier")
    var const6Tier: Int = 0,
) {
    companion object {
        const val TABLE_NAME = "return0_plugin_const"
    }
}
