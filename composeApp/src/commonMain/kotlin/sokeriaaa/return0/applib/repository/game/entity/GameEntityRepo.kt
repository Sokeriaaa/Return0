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

import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.models.entity.Entity

class GameEntityRepo(
    private val archive: ArchiveRepo,
    private val entityDao: EntityDao,
) {

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
}