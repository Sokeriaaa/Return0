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
package sokeriaaa.return0.mvi.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.core.component.inject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_team_warn_empty_team_exists
import return0.composeapp.generated.resources.game_team_warn_limit
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.game.entity.GameEntityRepo
import sokeriaaa.return0.applib.repository.game.entity.GameTeamRepo
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.models.entity.display.EntityProfile
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.intents.TeamsIntent

class TeamsViewModel : BaseViewModel() {

    // Repo
    private val _entityRepo: GameEntityRepo by inject()
    private val _teamRepo: GameTeamRepo by inject()

    // Team display.
    private val _teams: MutableList<TeamDisplay> = mutableStateListOf()
    val teams: List<TeamDisplay> = _teams

    // Entities of activated team.
    var activatedTeamEntities: Array<EntityProfile?> = arrayOfNulls(AppConstants.ARENA_MAX_PARTY)
        private set

    // The index of activated team.
    var activatedTeamIndex: Int by mutableIntStateOf(0)
        private set

    // Available entities
    private var _availableEntities: MutableList<EntityTable> = mutableStateListOf()
    val availableEntities: List<EntityTable> = _availableEntities

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            CommonIntent.Refresh -> viewModelScope.launch {
                refreshEntities()
                refreshTeams()
            }

            is TeamsIntent.ActivateTeam -> viewModelScope.launch {
                _teamRepo.activateTeam(intent.teamIndex)
                activatedTeamIndex = intent.teamIndex
                refreshTeams()
            }

            TeamsIntent.RequestCreateTeam -> viewModelScope.launch {
                // Check if the upper limit is reached.
                if (teams.size >= AppConstants.MAXIMUM_TEAMS) {
                    onIntent(
                        CommonIntent.ShowSnackBar(
                            getString(Res.string.game_team_warn_limit)
                        )
                    )
                    return@launch
                }
                // Check if an empty team exists.
                if (teams.any { t -> t.entities.all { e -> e == null } }) {
                    onIntent(
                        CommonIntent.ShowSnackBar(
                            getString(Res.string.game_team_warn_empty_team_exists)
                        )
                    )
                    return@launch
                }
                // Create a new team
                val newIndex = teams.size
                _teamRepo.createOrUpdateTeam(teamID = newIndex)
                refreshTeams()
            }

            is TeamsIntent.RenameTeam -> viewModelScope.launch {
                _teamRepo.updateTeamName(
                    teamID = intent.teamIndex,
                    name = intent.name,
                )
                refreshTeams()
            }

            is TeamsIntent.SwitchEntity -> {
                viewModelScope.launch {
                    val entity = intent.newEntity?.let { _entityRepo.getEntityProfile(it) }
                    _teams[intent.teamIndex].entities[intent.entityIndex] = entity
                    _teamRepo.updateTeamMember(
                        teamID = intent.teamIndex,
                        slot1 = _teams[intent.teamIndex].entities[0]?.name,
                        slot2 = _teams[intent.teamIndex].entities[1]?.name,
                        slot3 = _teams[intent.teamIndex].entities[2]?.name,
                        slot4 = _teams[intent.teamIndex].entities[3]?.name,
                    )
                    refreshTeams()
                }
            }

            else -> {}
        }
    }

    private suspend fun refreshTeams() {
        // Reset
        _teams.clear()
        activatedTeamEntities = arrayOfNulls(AppConstants.ARENA_MAX_PARTY)
        activatedTeamIndex = 0
        // Load
        _teamRepo.loadAllTeams().forEachIndexed { index, team ->
            val teamEntities: Array<EntityProfile?> = arrayOfNulls(AppConstants.ARENA_MAX_PARTY)
            val names: Array<String?> = arrayOf(team.slot1, team.slot2, team.slot3, team.slot4)
            for (i in 0..<AppConstants.ARENA_MAX_PARTY) {
                teamEntities[i] = _entityRepo.getEntityProfile(
                    entityName = names.getOrNull(i) ?: continue,
                ) ?: continue
            }
            _teams.add(
                TeamDisplay(
                    isActivated = team.isActivated,
                    name = team.name,
                    entities = teamEntities,
                ),
            )
            if (team.isActivated) {
                activatedTeamEntities = teamEntities
                activatedTeamIndex = index
            }
        }
    }

    private suspend fun refreshEntities() {
        _availableEntities.clear()
        _availableEntities.addAll(_entityRepo.queryAll())
    }

    class TeamDisplay(
        val isActivated: Boolean,
        val name: String?,
        val entities: Array<EntityProfile?> = arrayOfNulls(AppConstants.ARENA_MAX_PARTY),
    ) {
        init {
            require(entities.size == AppConstants.ARENA_MAX_PARTY) {
                "The length of entities should be ${AppConstants.ARENA_MAX_PARTY}, " +
                        "currently ${entities.size}"
            }
        }
    }
}