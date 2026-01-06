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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sokeriaaa.return0.applib.room.table.TeamTable

@Dao
interface TeamDao {
    /**
     * Query the team with specified save ID and team ID.
     */
    @Query(
        "SELECT * FROM `${TeamTable.TABLE_NAME}` WHERE `save_id`=:saveID AND `team_id`=:teamID"
    )
    suspend fun query(saveID: Int, teamID: Int): TeamTable?

    /**
     * Query all the teams with specified save ID.
     */
    @Query(
        "SELECT * FROM `${TeamTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun queryAll(saveID: Int): List<TeamTable>

    /**
     * Insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: TeamTable)

    /**
     * Insert a list of teams.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<TeamTable>)

    /**
     * Delete the teams for specified save ID.
     */
    @Query(
        "DELETE FROM `${TeamTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun delete(saveID: Int)

    /**
     * Activate a specified team.
     */
    @Query(
        "UPDATE `${TeamTable.TABLE_NAME}` " +
                "SET `is_activated`=(CASE WHEN `team_id`=:teamID THEN 1 ELSE 0 END) " +
                "WHERE `save_id`=:saveID"
    )
    suspend fun activateTeam(saveID: Int = -1, teamID: Int)

    /**
     * Update the members of the team.
     */
    @Query(
        "UPDATE `${TeamTable.TABLE_NAME}` SET `slot1`=:slot1, `slot2`=:slot2, " +
                "`slot3`=:slot3, `slot4`=:slot4 WHERE `save_id`=:saveID AND `team_id`=:teamID"
    )
    suspend fun updateMembers(
        saveID: Int,
        teamID: Int,
        slot1: String?,
        slot2: String?,
        slot3: String?,
        slot4: String?,
    )

    /**
     * Update team name.
     */
    @Query(
        "UPDATE `${TeamTable.TABLE_NAME}` SET `name`=:name " +
                "WHERE `save_id`=:saveID AND `team_id`=:teamID"
    )
    suspend fun updateName(
        saveID: Int,
        teamID: Int,
        name: String,
    )

    /**
     * Get the activated team.
     */
    @Query(
        "SELECT * FROM `${TeamTable.TABLE_NAME}` " +
                "WHERE `save_id`=:saveID AND `is_activated`=1 LIMIT 1"
    )
    suspend fun getActivatedTeam(saveID: Int): TeamTable?
}