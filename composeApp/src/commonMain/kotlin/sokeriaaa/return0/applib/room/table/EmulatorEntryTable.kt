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
import androidx.room.PrimaryKey

/**
 * The emulator table, mainly for storing presets.
 *
 * @param id Auto-generated ID.
 * @param presetID Preset ID.
 * @param isParty Is party.
 * @param entityName Entity name.
 * @param level Entity level.
 * @param pluginID The ID of plugin which will be installed for this entity in the simulate combat.
 *                 Preserved for future use.
 * @param bossMultiplier Multiplier for the boss. Default is 1. Set to higher value will increase
 *                       the status of this entity, especially for HP. Preserved for future use.
 */
@Entity(
    tableName = EmulatorEntryTable.TABLE_NAME,
    indices = [Index("preset_id")]
)
data class EmulatorEntryTable(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "preset_id")
    var presetID: Int,
    @ColumnInfo(name = "is_party")
    var isParty: Boolean,
    @ColumnInfo(name = "entity_name")
    var entityName: String,
    @ColumnInfo(name = "level")
    var level: Int = 1,
    @ColumnInfo(name = "plugin_id")
    var pluginID: Long? = null,
    @ColumnInfo(name = "boss_multiplier")
    var bossMultiplier: Int = 1,
) {
    companion object {
        const val TABLE_NAME = "return0_emulator_entries"
    }
}