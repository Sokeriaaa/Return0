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

/**
 * The emulator table, mainly for storing presets.
 */
expect class EmulatorEntryTable {

    /**
     * Auto-generated ID.
     */
    var id: Int?

    /**
     * Preset ID.
     */
    var presetID: Int

    /**
     * Is party.
     */
    var isParty: Boolean

    /**
     * Entity name.
     */
    var entityName: String

    /**
     * Entity level.
     */
    var level: Int

    /**
     * The ID of plugin which will be installed for this entity in the simulate combat.
     *
     * Preserved for future use.
     */
    var pluginID: Long?

    /**
     * Multiplier for the boss. Default is 1. Set to higher value will increase the status
     *  of this entity, especially for HP.
     *
     * Preserved for future use.
     */
    var bossMultiplier: Int
}