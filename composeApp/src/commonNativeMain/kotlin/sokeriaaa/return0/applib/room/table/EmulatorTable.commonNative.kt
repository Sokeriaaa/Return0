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

@Entity(
    tableName = EmulatorEntryTable.TABLE_NAME,
    indices = [Index("preset_id")]
)
actual data class EmulatorEntryTable actual constructor(
    @PrimaryKey(autoGenerate = true)
    actual var id: Int?,
    @ColumnInfo(name = "preset_id")
    actual var presetID: Int,
    @ColumnInfo(name = "is_party")
    actual var isParty: Boolean,
    @ColumnInfo(name = "entity_name")
    actual var entityName: String,
    @ColumnInfo(name = "level")
    actual var level: Int,
    @ColumnInfo(name = "plugin_id")
    actual var pluginID: Long?,
    @ColumnInfo(name = "boss_multiplier")
    actual var bossMultiplier: Int,
) {
    companion object {
        const val TABLE_NAME = "return0_emulator_entries"
    }
}