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
package sokeriaaa.return0.applib.repository.save

import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.room.dao.CurrencyDao
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.applib.room.dao.EventRelocationDao
import sokeriaaa.return0.applib.room.dao.InventoryDao
import sokeriaaa.return0.applib.room.dao.QuestDao
import sokeriaaa.return0.applib.room.dao.SaveMetaDao
import sokeriaaa.return0.applib.room.dao.SavedSwitchDao
import sokeriaaa.return0.applib.room.dao.SavedVariableDao
import sokeriaaa.return0.applib.room.dao.StatisticsDao
import sokeriaaa.return0.applib.room.dao.TeamDao
import sokeriaaa.return0.applib.room.helper.TransactionManager
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.applib.room.table.SaveMetaTable
import sokeriaaa.return0.applib.room.table.StatisticsTable
import sokeriaaa.return0.applib.room.table.TeamTable
import sokeriaaa.return0.shared.data.models.title.Title
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Manages all saves in database.
 */
class SaveRepo(
    // Transaction manager.
    private val transactionManager: TransactionManager,
    // Database dao
    private val currencyDao: CurrencyDao,
    private val entityDao: EntityDao,
    private val eventRelocationDao: EventRelocationDao,
    private val inventoryDao: InventoryDao,
    private val questDao: QuestDao,
    private val savedSwitchDao: SavedSwitchDao,
    private val savedVariableDao: SavedVariableDao,
    private val saveMetaDao: SaveMetaDao,
    private val statisticsDao: StatisticsDao,
    private val teamDao: TeamDao,
) {
    /**
     * Generate a new save for specified save ID and **replace the old one**.
     */
    @OptIn(ExperimentalTime::class)
    suspend fun newGame(saveID: Int = -1) {
        transactionManager.withTransaction {
            val time = Clock.System.now().toEpochMilliseconds()
            // Remove the old one.
            currencyDao.delete(saveID = saveID)
            entityDao.deleteSave(saveID = saveID)
            eventRelocationDao.deleteSave(saveID = saveID)
            inventoryDao.delete(saveID = saveID)
            questDao.delete(saveID = saveID)
            savedSwitchDao.delete(saveID = saveID)
            savedVariableDao.delete(saveID = saveID)
            teamDao.delete(saveID = saveID)
            // Insert/Replace
            saveMetaDao.insertOrUpdate(
                SaveMetaTable(
                    saveID = saveID,
                    createdTime = time,
                    savedTime = time,
                    title = Title.INTERN,
                    fileName = AppConstants.ENTRANCE_MAP,
                    lineNumber = 1,
                )
            )
            statisticsDao.insertOrUpdate(
                StatisticsTable(
                    saveID = saveID,
                    tokensEarned = 0,
                    cryptosEarned = 0,
                    totalDamage = 0,
                    totalHeal = 0,
                    enemiesDefeated = 0,
                    linesMoved = 0,
                )
            )
            // TODO Temp code for adding entities for testing.
            // TODO Will be removed later.
            entityDao.insert(
                EntityTable(
                    saveID = saveID,
                    entityName = "Object",
                    indexedTime = time
                )
            )
            entityDao.insert(
                EntityTable(
                    saveID = saveID,
                    entityName = "Iterator",
                    indexedTime = time
                )
            )
            entityDao.insert(
                EntityTable(
                    saveID = saveID,
                    entityName = "System",
                    indexedTime = time
                )
            )
            teamDao.insertOrUpdate(
                TeamTable(
                    saveID = saveID,
                    teamID = 1,
                    name = "testing",
                    isActivated = true,
                    slot1 = "Object",
                    slot2 = "Iterator",
                    slot3 = "System",
                    slot4 = null,
                )
            )
        }
    }

    /**
     * Migrate one save to another slot. Typically form -1 to N, or from N to -1.
     * The destination slot will be cleared.
     */
    @OptIn(ExperimentalTime::class)
    suspend fun migrateSave(fromID: Int, toID: Int) {
        if (fromID == toID) {
            return
        }
        val time = Clock.System.now().toEpochMilliseconds()
        transactionManager.withTransaction {
            // Currency
            val currencyFrom = currencyDao.queryAll(saveID = fromID).onEach { it.saveID = toID }
            currencyDao.delete(saveID = toID)
            currencyDao.insertList(list = currencyFrom)
            // Entity
            val entityFrom = entityDao.queryAll(saveID = fromID).onEach { it.saveID = toID }
            entityDao.deleteSave(saveID = toID)
            entityDao.insertList(list = entityFrom)
            // Event relocations
            val eventRelocationFrom =
                eventRelocationDao.queryAll(saveID = fromID).onEach { it.saveID = toID }
            eventRelocationDao.deleteSave(saveID = toID)
            eventRelocationDao.insertOrUpdateList(list = eventRelocationFrom)
            // Inventory
            val inventoryFrom = inventoryDao.queryAll(saveID = fromID).onEach { it.saveID = toID }
            inventoryDao.delete(saveID = toID)
            inventoryDao.insertList(list = inventoryFrom)
            // Quest
            val questFrom = questDao.queryAll(saveID = fromID).onEach { it.saveID = toID }
            questDao.delete(saveID = toID)
            questDao.insertList(list = questFrom)
            // Saved switch
            val savedSwitchFrom =
                savedSwitchDao.queryAll(saveID = fromID).onEach { it.saveID = toID }
            savedSwitchDao.delete(saveID = toID)
            savedSwitchDao.insertList(list = savedSwitchFrom)
            // Saved variable
            val savedVariableFrom =
                savedVariableDao.queryAll(saveID = fromID).onEach { it.saveID = toID }
            savedVariableDao.delete(saveID = toID)
            savedVariableDao.insertList(list = savedVariableFrom)
            // Save meta
            saveMetaDao.query(saveID = fromID)
                ?.apply { saveID = toID }
                ?.let {
                    it.savedTime = time
                    saveMetaDao.insertOrUpdate(table = it)
                }
            // Statistics
            statisticsDao.query(saveID = fromID)
                ?.apply { saveID = toID }
                ?.let { statisticsDao.insertOrUpdate(table = it) }
            // Team
            val teamFrom =
                teamDao.queryAll(saveID = fromID).onEach { it.saveID = toID }
            teamDao.delete(saveID = toID)
            teamDao.insertList(list = teamFrom)
        }
    }
}