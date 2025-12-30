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
package sokeriaaa.return0.applib.room.dao

import sokeriaaa.return0.applib.room.table.TeamTable

expect interface TeamDao {
    /**
     * Query the team with specified save ID and team ID.
     */
    suspend fun query(saveID: Int, teamID: Int): TeamTable?

    /**
     * Query all the teams with specified save ID.
     */
    suspend fun queryAll(saveID: Int): List<TeamTable>

    /**
     * Insert or update.
     */
    suspend fun insertOrUpdate(table: TeamTable)

    /**
     * Insert a list of teams.
     */
    suspend fun insertList(list: List<TeamTable>)

    /**
     * Delete the teams for specified save ID.
     */
    suspend fun delete(saveID: Int)

    /**
     * Activate a specified team.
     */
    suspend fun activateTeam(saveID: Int = -1, teamID: Int)

    /**
     * Update the members of the team.
     */
    suspend fun updateMembers(
        saveID: Int,
        teamID: Int,
        slot1: String?,
        slot2: String?,
        slot3: String?,
        slot4: String?,
    )

    suspend fun updateName(
        saveID: Int,
        teamID: Int,
        name: String,
    )

    /**
     * Get the activated team.
     */
    suspend fun getActivatedTeam(saveID: Int): TeamTable?
}