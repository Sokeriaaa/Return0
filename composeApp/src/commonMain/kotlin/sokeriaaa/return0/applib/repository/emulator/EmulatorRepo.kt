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
package sokeriaaa.return0.applib.repository.emulator

import sokeriaaa.return0.applib.room.dao.EmulatorEntryDao
import sokeriaaa.return0.applib.room.dao.EmulatorIndexDao
import sokeriaaa.return0.applib.room.helper.TransactionManager
import sokeriaaa.return0.applib.room.table.EmulatorEntryTable
import sokeriaaa.return0.applib.room.table.EmulatorIndexTable
import sokeriaaa.return0.shared.common.helpers.TimeHelper
import sokeriaaa.return0.shared.data.models.combat.EnemyState
import sokeriaaa.return0.shared.data.models.combat.PartyState

/**
 * Repository manages the emulator.
 */
class EmulatorRepo(
    private val emulatorIndexDao: EmulatorIndexDao,
    private val emulatorEntryDao: EmulatorEntryDao,
    private val transactionManager: TransactionManager,
) {

    suspend fun queryAllIndices(): List<EmulatorIndexTable> {
        return emulatorIndexDao.queryAll()
    }

    suspend fun queryEntries(presetID: Int): List<EmulatorEntryTable> {
        return emulatorEntryDao.queryList(presetID)
    }

    suspend fun rename(presetID: Int, name: String) {
        emulatorIndexDao.rename(presetID, name)
    }

    suspend fun delete(presetID: Int) {
        transactionManager.withTransaction {
            emulatorEntryDao.deletePreset(presetID)
            emulatorIndexDao.delete(presetID)
        }
    }

    /**
     * Save preset.
     *
     * @return the saved preset ID.
     */
    suspend fun savePreset(
        parties: List<PartyState>,
        enemies: List<EnemyState>,
        createdTime: Long = TimeHelper.currentTimeMillis(),
    ): Int {
        val presetID = emulatorIndexDao.insert(
            EmulatorIndexTable(
                // Use created time as default name.
                name = createdTime.toString(),
                createdTime = createdTime,
            )
        ).toInt()
        // Save entries
        val list = ArrayList<EmulatorEntryTable>()
        parties.forEach {
            list.add(
                EmulatorEntryTable(
                    presetID = presetID,
                    isParty = true,
                    entityName = it.entityData.name,
                    level = it.level,
                    // Preserved future use: Plugin
                    pluginID = null,
                    // Preserved future use: Boss multiplier
                    bossMultiplier = 1,
                )
            )
        }
        enemies.forEach {
            list.add(
                EmulatorEntryTable(
                    presetID = presetID,
                    isParty = false,
                    entityName = it.entityData.name,
                    level = it.level,
                    // Preserved future use: Plugin
                    pluginID = null,
                    // Preserved future use: Boss multiplier
                    bossMultiplier = 1,
                )
            )
        }
        // Insert to database
        emulatorEntryDao.insertList(list)
        return presetID
    }

}