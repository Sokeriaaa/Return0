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
actual interface TeamDao {
    @Query(
        "SELECT * FROM `${TeamTable.TABLE_NAME}` WHERE `save_id`=:saveID AND `team_id`=:teamID"
    )
    actual suspend fun query(saveID: Int, teamID: Int): TeamTable?

    @Query(
        "SELECT * FROM `${TeamTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun queryAll(saveID: Int): List<TeamTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insertOrUpdate(table: TeamTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insertList(list: List<TeamTable>)

    @Query(
        "DELETE FROM `${TeamTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun delete(saveID: Int)

    @Query(
        "UPDATE `${TeamTable.TABLE_NAME}` " +
                "SET `is_activated`=(CASE WHEN `team_id`=:teamID THEN 1 ELSE 0 END) " +
                "WHERE `save_id`=:saveID"
    )
    actual suspend fun activateTeam(saveID: Int, teamID: Int)

    @Query(
        "SELECT * FROM `${TeamTable.TABLE_NAME}` " +
                "WHERE `save_id`=:saveID AND `is_activated`=1 LIMIT 1"
    )
    actual suspend fun getActivatedTeam(saveID: Int): TeamTable?
}