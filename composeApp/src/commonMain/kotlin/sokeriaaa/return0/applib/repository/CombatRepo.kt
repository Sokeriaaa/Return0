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
package sokeriaaa.return0.applib.repository

import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.generate
import sokeriaaa.return0.shared.data.models.entity.EntityData

/**
 * The Repo for calculations and instance generations in the combat.
 */
class CombatRepo internal constructor(
    private val archiveRepo: ArchiveRepo,
) {

    /**
     * Generate an Entity instance with EntityData, index in arena and level.
     */
    fun generateEntity(
        entityData: EntityData,
        index: Int,
        level: Int,
        isParty: Boolean,
    ): Entity {
        // Get the growth data of the primary category of entity.
        val growth = archiveRepo.getEntityGrowthByCategory(entityData.category)
        return entityData.generate(index, level, growth, isParty)
    }

}