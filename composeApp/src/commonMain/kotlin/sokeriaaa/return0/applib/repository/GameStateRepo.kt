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
package sokeriaaa.return0.applib.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.room.dao.CurrencyDao
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.applib.room.dao.InventoryDao
import sokeriaaa.return0.applib.room.dao.QuestDao
import sokeriaaa.return0.applib.room.dao.SaveMetaDao
import sokeriaaa.return0.applib.room.dao.SavedSwitchDao
import sokeriaaa.return0.applib.room.dao.SavedVariableDao
import sokeriaaa.return0.applib.room.dao.StatisticsDao
import sokeriaaa.return0.applib.room.dao.TeamDao
import sokeriaaa.return0.applib.room.helper.TransactionManager
import sokeriaaa.return0.applib.room.table.CurrencyTable
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.applib.room.table.SaveMetaTable
import sokeriaaa.return0.applib.room.table.StatisticsTable
import sokeriaaa.return0.applib.room.table.TeamTable
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.combat.PartyState
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.shared.data.models.story.map.MapData
import sokeriaaa.return0.shared.data.models.title.Title
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GameStateRepo(
    // Archive repo
    private val archiveRepo: ArchiveRepo,
    // Transaction manager.
    private val transactionManager: TransactionManager,
    // Database dao
    private val currencyDao: CurrencyDao,
    private val entityDao: EntityDao,
    private val inventoryDao: InventoryDao,
    private val questDao: QuestDao,
    private val savedSwitchDao: SavedSwitchDao,
    private val savedVariableDao: SavedVariableDao,
    private val saveMetaDao: SaveMetaDao,
    private val statisticsDao: StatisticsDao,
    private val teamDao: TeamDao,
) {
    private val _currencies: MutableMap<CurrencyType, Int> = HashMap()
    val currencies: Map<CurrencyType, Int> = _currencies

    /**
     * Current map.
     */
    var map: MapData by mutableStateOf(
        // TODO Testing
        MapData(
            name = AppConstants.ENTRANCE_MAP,
            lines = 100,
            buggyRange = listOf(20 to 80),
            buggyEntries = listOf(
                MapData.BuggyEntry(
                    listOf("Object"),
                )
            ),
            difficulty = 1,
            events = emptyList()
        )
    )
        private set

    /**
     * Current line number.
     */
    var lineNumber: Int by mutableIntStateOf(1)
        private set

    /**
     * Update user position.
     */
    fun updatePosition(
        fileName: String,
        lineNumber: Int,
    ) {
        // TODO Update map
        this.lineNumber = lineNumber
    }

    /**
     * Update line number.
     */
    fun updateLineNumber(lineNumber: Int) {
        this.lineNumber = lineNumber
    }

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
                )
            )
            teamDao.insertOrUpdate(
                TeamTable(
                    saveID = saveID,
                    teamID = 1,
                    name = "testing",
                    isActivated = true,
                    slot1 = "Object",
                    slot2 = null,
                    slot3 = null,
                    slot4 = null,
                )
            )
        }
        load()
    }

    /**
     * Load game state from database to this repo.
     */
    suspend fun load(saveID: Int = -1) {
        // Position
        saveMetaDao.query(saveID = saveID)?.let {
            // TODO change map
            lineNumber = it.lineNumber
        }
        // Currency
        CurrencyType.entries.forEach { currency ->
            _currencies[currency] = currencyDao.query(saveID, currency)?.amount ?: 0
        }
    }

    /**
     * Load the activated team as party state for combating.
     */
    suspend fun loadTeam(saveID: Int = -1, useCurrentData: Boolean): List<PartyState> {
        val currentTeam = teamDao.getActivatedTeam(saveID) ?: return emptyList()
        return sequenceOf(
            currentTeam.slot1?.let { generatePartyStateWithKey(saveID, it, useCurrentData) },
            currentTeam.slot2?.let { generatePartyStateWithKey(saveID, it, useCurrentData) },
            currentTeam.slot3?.let { generatePartyStateWithKey(saveID, it, useCurrentData) },
            currentTeam.slot4?.let { generatePartyStateWithKey(saveID, it, useCurrentData) },
        ).filterNotNull().toList()
    }

    /**
     * Save the current entity state to database.
     * Mainly for the HP and SP after a combat finished.
     */
    suspend fun saveEntityState(saveID: Int = -1, parties: List<Entity>) {
        parties.forEach {
            entityDao.updateHP(saveID, it.name, it.hp)
            entityDao.updateSP(saveID, it.name, it.sp)
        }
    }

    /**
     * Generate the party state with the given entity name.
     */
    private suspend fun generatePartyStateWithKey(
        saveID: Int = -1,
        entityName: String,
        useCurrentData: Boolean,
    ): PartyState? {
        val entityData = archiveRepo.getEntityData(entityName) ?: return null
        val entityTable = entityDao.getEntity(saveID, entityName) ?: return null
        return PartyState(
            entityData = entityData,
            level = entityTable.level,
            currentHP = if (useCurrentData) entityTable.currentHP else null,
            currentSP = if (useCurrentData) entityTable.currentSP else null,
        )
    }

    /**
     * Save the currency state to database.
     */
    suspend fun saveCurrency(saveID: Int = -1) {
        transactionManager.withTransaction {
            // Currency
            currencyDao.delete(saveID)
            currencies.forEach { entry ->
                currencyDao.insertOrUpdate(
                    table = CurrencyTable(
                        saveID = saveID,
                        currency = entry.key,
                        amount = entry.value,
                    ),
                )
            }
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