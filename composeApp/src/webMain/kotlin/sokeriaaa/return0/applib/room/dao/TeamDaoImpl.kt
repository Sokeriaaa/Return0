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

class TeamDaoImpl(
    val queries: SQTeamDaoQueries,
) : TeamDao {
    override suspend fun query(
        saveID: Int,
        teamID: Int
    ): TeamTable? {
        return queries.query(
            save_id = saveID.toLong(),
            team_id = teamID.toLong(),
            mapper = ::convertTotable,
        ).executeAsOneOrNull()
    }

    override suspend fun queryAll(saveID: Int): List<TeamTable> {
        return queries.queryAll(
            save_id = saveID.toLong(),
            mapper = ::convertTotable,
        ).executeAsList()
    }

    override suspend fun insertOrUpdate(table: TeamTable) {
        queries.insertOrUpdate(
            save_id = table.saveID.toLong(),
            team_id = table.teamID.toLong(),
            name = table.name,
            is_activated = if (table.isActivated) 1L else 0L,
            slot1 = table.slot1,
            slot2 = table.slot2,
            slot3 = table.slot3,
            slot4 = table.slot4,
        )
    }

    override suspend fun insertList(list: List<TeamTable>) {
        list.forEach { insertOrUpdate(it) }
    }

    override suspend fun delete(saveID: Int) {
        queries.delete(save_id = saveID.toLong())
    }

    override suspend fun activateTeam(saveID: Int, teamID: Int) {
        queries.activateTeam(save_id = saveID.toLong(), team_id = teamID.toLong())
    }

    override suspend fun updateMembers(
        saveID: Int,
        teamID: Int,
        slot1: String?,
        slot2: String?,
        slot3: String?,
        slot4: String?
    ) {
        queries.updateMembers(
            save_id = saveID.toLong(),
            team_id = teamID.toLong(),
            slot1 = slot1,
            slot2 = slot2,
            slot3 = slot3,
            slot4 = slot4,
        )
    }

    override suspend fun updateName(saveID: Int, teamID: Int, name: String) {
        queries.updateName(
            save_id = saveID.toLong(),
            team_id = teamID.toLong(),
            name = name,
        )
    }

    override suspend fun getActivatedTeam(saveID: Int): TeamTable? {
        return queries.getActivatedTeam(
            save_id = saveID.toLong(),
            mapper = ::convertTotable,
        ).executeAsOneOrNull()
    }

    @Suppress("LocalVariableName")
    private fun convertTotable(
        save_id: Long,
        team_id: Long,
        name: String?,
        is_activated: Long,
        slot1: String?,
        slot2: String?,
        slot3: String?,
        slot4: String?,
    ): TeamTable = TeamTable(
        saveID = save_id.toInt(),
        teamID = team_id.toInt(),
        name = name,
        isActivated = is_activated == 1L,
        slot1 = slot1,
        slot2 = slot2,
        slot3 = slot3,
        slot4 = slot4,
    )
}