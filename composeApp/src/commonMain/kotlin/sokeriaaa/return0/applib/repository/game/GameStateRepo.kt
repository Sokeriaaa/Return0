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

import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.game.base.BaseGameRepo
import sokeriaaa.return0.applib.repository.game.currency.CurrencyRepo
import sokeriaaa.return0.applib.repository.game.map.GameMapRepo
import sokeriaaa.return0.applib.repository.game.saved.SavedValuesRepo
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
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.combat.PartyState

class GameStateRepo(
    // Sub-repos
    val archive: ArchiveRepo,
    val currency: CurrencyRepo,
    val map: GameMapRepo,
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
) : BaseGameRepo {

    /**
     * Load game state from database to this repo.
     */
    override suspend fun load() {
        currency.load()
        map.load()
        savedValues.load()
    }

    /**
     * Flush the temp data to the database.
     */
    override suspend fun flush() {
        transactionManager.withTransaction {
            currency.flush()
            map.flush()
            savedValues.flush()
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