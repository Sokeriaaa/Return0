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
package sokeriaaa.return0.applib.repository.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
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
import sokeriaaa.return0.applib.room.table.CurrencyTable
import sokeriaaa.return0.applib.room.table.EventRelocationTable
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.combat.PartyState
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.shared.data.models.story.map.MapData
import sokeriaaa.return0.shared.data.models.story.map.MapEvent

class GameStateRepo(
    // Sub-repos
    val archive: ArchiveRepo,
    val gameMap: GameMapRepo,
    val savedValues: SavedValuesRepo,
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
    private val _currencies: MutableMap<CurrencyType, Int> = HashMap()
    val currencies: Map<CurrencyType, Int> = _currencies

    private val _eventRelocateBuffer: MutableMap<String, Pair<String, Int>> = HashMap()

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
     * Update line number.
     */
    fun updateLineNumber(lineNumber: Int) {
        this.lineNumber = lineNumber
    }

    /**
     * Update user position.
     */
    suspend fun updatePosition(
        fileName: String = map.name,
        lineNumber: Int,
    ) {
        if (fileName != map.name) {
            this.map = gameMap.loadMap(fileName)
        }
        this.lineNumber = lineNumber
    }

    /**
     * Teleport an event to specified location.
     */
    fun teleportEvent(
        eventKey: String,
        fileName: String = map.name,
        lineNumber: Int,
    ) {
        _eventRelocateBuffer[eventKey] = fileName to lineNumber
    }

    fun changeCurrency(
        currency: CurrencyType,
        change: Int,
    ) {
        _currencies[currency] = (_currencies[currency] ?: 0) + change
    }

    /**
     * Load events in a map.
     */
    suspend fun loadEvents(
        saveID: Int = -1,
        mapData: MapData = this.map,
    ): List<MapEvent> {
        val events = ArrayList<MapEvent>()
        // Check events in current map
        mapData.events.forEach {
            if (_eventRelocateBuffer.containsKey(it.key)) {
                // Load new location.
                val location = _eventRelocateBuffer[it.key]!!
                events.add(it.copy(lineNumber = location.second))
            } else if (it.key == null) {
                // Events with no key is guaranteed to exist here.
                events.add(it)
            } else {
                // Check database
                val relocation = eventRelocationDao.query(saveID, it.key, map.name)
                if (relocation == null) {
                    // Not relocated
                    events.add(it)
                } else {
                    // Load new location.
                    events.add(it.copy(lineNumber = relocation.lineNumber))
                }
            }
        }
        return events
    }

    /**
     * Load game state from database to this repo.
     */
    suspend fun load() {
        savedValues.load()
        // Position
        saveMetaDao.query(AppConstants.CURRENT_SAVE_ID)?.let {
            // TODO change map
            lineNumber = it.lineNumber
        }
        // Currency
        CurrencyType.entries.forEach { currency ->
            _currencies[currency] =
                currencyDao.query(AppConstants.CURRENT_SAVE_ID, currency)?.amount ?: 0
        }
    }

    /**
     * Flush the temp data to the database.
     */
    suspend fun flush() {
        savedValues.flush()
        transactionManager.withTransaction {
            // Currency
            currencyDao.delete(AppConstants.CURRENT_SAVE_ID)
            currencies.forEach { entry ->
                currencyDao.insertOrUpdate(
                    table = CurrencyTable(
                        saveID = AppConstants.CURRENT_SAVE_ID,
                        currency = entry.key,
                        amount = entry.value,
                    ),
                )
            }
            // Position
            saveMetaDao.updatePosition(
                saveID = AppConstants.CURRENT_SAVE_ID,
                fileName = map.name,
                lineNumber = lineNumber,
            )
            // Events
            _eventRelocateBuffer.forEach { entry ->
                eventRelocationDao.insertOrUpdate(
                    EventRelocationTable(
                        saveID = AppConstants.CURRENT_SAVE_ID,
                        eventKey = entry.key,
                        fileName = entry.value.first,
                        lineNumber = entry.value.second,
                    )
                )
            }
            _eventRelocateBuffer.clear()
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
        val entityData = archive.getEntityData(entityName) ?: return null
        val entityTable = entityDao.getEntity(saveID, entityName) ?: return null
        return PartyState(
            entityData = entityData,
            level = entityTable.level,
            currentHP = if (useCurrentData) entityTable.currentHP else null,
            currentSP = if (useCurrentData) entityTable.currentSP else null,
        )
    }
}