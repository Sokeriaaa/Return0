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
package sokeriaaa.return0.applib.repository.game.entity

import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.room.dao.TeamDao
import sokeriaaa.return0.applib.room.table.TeamTable

class GameTeamRepo(
    private val entityRepo: GameEntityRepo,
    private val teamDao: TeamDao,
) {

    /**
     * Update the team data.
     */
    suspend fun updateTeam(
        teamID: Int,
        name: String,
        isActivated: Boolean,
        slot1: String?,
        slot2: String?,
        slot3: String?,
        slot4: String?,
    ) {
        teamDao.insertOrUpdate(
            TeamTable(
                saveID = AppConstants.CURRENT_SAVE_ID,
                teamID = teamID,
                name = name,
                isActivated = isActivated,
                slot1 = slot1,
                slot2 = slot2,
                slot3 = slot3,
                slot4 = slot4,
            )
        )
    }

    /**
     * Activate team.
     */
    suspend fun activateTeam(
        teamID: Int,
    ) {
        teamDao.activateTeam(AppConstants.CURRENT_SAVE_ID, teamID)
    }

    /**
     * Load the current team levels in a pair list.
     */
    suspend fun loadTeamLevelPairs(): List<Pair<String, Int>> {
        val currentTeam =
            teamDao.getActivatedTeam(AppConstants.CURRENT_SAVE_ID) ?: return emptyList()
        val entityKeys = sequenceOf(
            currentTeam.slot1,
            currentTeam.slot2,
            currentTeam.slot3,
            currentTeam.slot4,
        ).filterNotNull()
        val results = ArrayList<Pair<String, Int>>()
        entityKeys.forEach { key ->
            entityRepo.getEntityTable(key)?.let {
                results.add(key to it.level)
            }
        }
        return results
    }

    /**
     * Recover the HP and SP of all the entities in current team.
     */
    suspend fun recoverAll() {
        val currentTeam = teamDao.getActivatedTeam(AppConstants.CURRENT_SAVE_ID) ?: return
        sequenceOf(
            currentTeam.slot1,
            currentTeam.slot2,
            currentTeam.slot3,
            currentTeam.slot4,
        ).filterNotNull()
            .forEach {
                entityRepo.updateHPAndSP(entityName = it, currentHP = null, currentSP = null)
            }
    }
}