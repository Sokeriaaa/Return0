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
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.generate
import sokeriaaa.return0.models.entity.level.EntityLevelHelper
import sokeriaaa.return0.shared.common.helpers.TimeHelper
import sokeriaaa.return0.shared.data.models.entity.EntityData

class GameEntityRepo(
    private val archive: ArchiveRepo,
    private val entityDao: EntityDao,
) {
    suspend fun queryAll(): List<EntityTable> {
        return entityDao.queryAll(saveID = AppConstants.CURRENT_SAVE_ID)
    }

    /**
     * Obtaining a new entity.
     */
    suspend fun obtainEntity(
        entityName: String,
        level: Int = 1,
        exp: Int = 0,
        currentHP: Int? = null,
        currentSP: Int? = null,
        pluginID: Long? = null,
    ) {
        entityDao.insert(
            EntityTable(
                saveID = AppConstants.CURRENT_SAVE_ID,
                entityName = entityName,
                level = level,
                exp = exp,
                currentHP = currentHP,
                currentSP = currentSP,
                indexedTime = TimeHelper.currentTimeMillis(),
                pluginID = pluginID,
            )
        )
    }

    /**
     * Get the entity table if exists.
     */
    suspend fun getEntityTable(entityName: String): EntityTable? {
        return entityDao.getEntity(AppConstants.CURRENT_SAVE_ID, entityName)
    }

    /**
     * Generate an [Entity] instance with EntityData, index in arena and level.
     */
    fun generateEntityInstance(
        entityData: EntityData,
        index: Int,
        level: Int,
        isParty: Boolean,
        currentHP: Int? = null,
        currentSP: Int? = null,
    ): Entity {
        // Get the growth data of the primary category of entity.
        val growth = archive.getEntityGrowthByCategory(entityData.category)
        return entityData.generate(index, level, growth, isParty).apply {
            hp = currentHP ?: maxhp
            sp = currentSP ?: maxsp
        }
    }

    /**
     * Get an [Entity] instance for displaying in the profile page.
     */
    suspend fun getEntityStatus(entityName: String): Entity? {
        val entityData = archive.getEntityData(entityName) ?: return null
        val entityTable = getEntityTable(entityName) ?: return null
        return generateEntityInstance(
            entityData = entityData,
            index = -1,
            level = entityTable.level,
            isParty = true,
            currentHP = entityTable.currentHP,
            currentSP = entityTable.currentSP,
        )
    }

    /**
     * Save the current entity state to database.
     * Mainly for the HP and SP after a combat finished.
     */
    suspend fun saveEntityState(parties: List<Entity>) {
        parties.forEach {
            updateHPAndSP(entityName = it.name, currentHP = it.hp, currentSP = it.sp)
        }
    }

    /**
     * Obtained exp for an entity.
     *
     * @param allowLevelDown Allow the level to go down under certain situations. Default is `false`
     * @return obtainedExp to newLevel
     */
    suspend fun obtainedExp(
        entityName: String,
        obtainedExp: Float,
        allowLevelDown: Boolean = false
    ): Pair<Int, Int> {
        val entityData = archive.getEntityData(entityName) ?: return 0 to 0
        val entityTable = entityDao.getEntity(
            saveID = AppConstants.CURRENT_SAVE_ID,
            entityName = entityName,
        ) ?: return 0 to 0
        val upperLimit = 100 // TODO Calculate by user title

        // Calculate exp
        val finalObtainedExp = if (entityTable.level >= upperLimit) {
            obtainedExp * AppConstants.ENTITY_LEVEL_LIMIT_MULTIPLIER
        } else {
            obtainedExp
        }.toInt().coerceAtLeast(1)
        entityTable.exp += finalObtainedExp

        // Calculate level
        val newLevel = EntityLevelHelper.levelFromTotalExp(
            totalExp = entityTable.exp,
            levelPacing = entityData.levelPacing,
        ).coerceIn(1..upperLimit)
        if (allowLevelDown || newLevel > entityTable.level) {
            entityTable.level = newLevel
        }

        // Update database
        entityDao.insert(entityTable)

        return finalObtainedExp to entityTable.level
    }

    /**
     * Save the current entity state to database.
     * Mainly for the HP and SP after a combat finished.
     */
    suspend fun updateHPAndSP(
        entityName: String,
        currentHP: Int?,
        currentSP: Int?,
    ) {
        entityDao.updateHP(AppConstants.CURRENT_SAVE_ID, entityName, currentHP)
        entityDao.updateSP(AppConstants.CURRENT_SAVE_ID, entityName, currentSP)
    }
}