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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.game.entity.GameEntityRepo
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.models.entity.generate
import sokeriaaa.return0.models.entity.level.EntityLevelHelper

class EntitiesViewModel : BaseViewModel() {

    // Repo
    private val _archiveRepo: ArchiveRepo by inject()
    private val _entityRepo: GameEntityRepo by inject()

    // All entities
    private val _entities: MutableList<Display> = mutableStateListOf()
    val entities: List<Display> = _entities

    // Sorter
    var orderBy by mutableStateOf(OrderBy.LEVEL)
        private set
    var isDescending by mutableStateOf(true)
        private set

    private suspend fun refresh() {
        _entities.clear()
        _entities.addAll(
            _entityRepo.queryAll().asSequence()
                .mapNotNull { getEntityDisplay(it) }
        )

    }

    private fun getEntityDisplay(table: EntityTable): Display? {
        val entityData = _archiveRepo.getEntityData(table.entityName) ?: return null

        // Get the growth data of the primary category of entity.
        val growth = _archiveRepo.getEntityGrowthByCategory(entityData.category)
        // Generate entity
        val entity = entityData.generate(
            index = -1,
            level = table.level,
            growth = growth,
            isParty = true,
        ).apply {
            hp = table.currentHP ?: maxhp
            sp = table.currentSP ?: maxsp
        }
        // Assemble display
        return Display(
            name = table.entityName,
            level = table.level,
            expProgress = EntityLevelHelper.levelProgress(
                table.level,
                table.exp,
                entityData.levelPacing
            ),
            hp = entity.hp,
            maxHP = entity.maxhp,
            sp = entity.sp,
            maxSP = entity.maxsp,
        )
    }

    /**
     * Entity item display.
     */
    data class Display(
        val name: String,
        val level: Int,
        val expProgress: Float,
        val hp: Int,
        val maxHP: Int,
        val sp: Int,
        val maxSP: Int,
    )

    enum class OrderBy {
        NAME,
        LEVEL,
    }

}