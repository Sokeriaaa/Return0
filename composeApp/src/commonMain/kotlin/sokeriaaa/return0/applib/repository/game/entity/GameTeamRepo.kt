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
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.applib.room.dao.TeamDao
import sokeriaaa.return0.shared.data.models.combat.PartyState

class GameTeamRepo(
    private val archive: ArchiveRepo,
    private val entityDao: EntityDao,
    private val teamDao: TeamDao,
) {

    /**
     * Load the activated team as party state for combating.
     */
    suspend fun loadTeam(useCurrentData: Boolean): List<PartyState> {
        val currentTeam =
            teamDao.getActivatedTeam(AppConstants.CURRENT_SAVE_ID) ?: return emptyList()
        return sequenceOf(
            currentTeam.slot1?.let {
                generatePartyStateWithKey(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    entityName = it,
                    useCurrentData = useCurrentData,
                )
            },
            currentTeam.slot2?.let {
                generatePartyStateWithKey(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    entityName = it,
                    useCurrentData = useCurrentData,
                )
            },
            currentTeam.slot3?.let {
                generatePartyStateWithKey(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    entityName = it,
                    useCurrentData = useCurrentData,
                )
            },
            currentTeam.slot4?.let {
                generatePartyStateWithKey(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    entityName = it,
                    useCurrentData = useCurrentData,
                )
            },
        ).filterNotNull().toList()
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