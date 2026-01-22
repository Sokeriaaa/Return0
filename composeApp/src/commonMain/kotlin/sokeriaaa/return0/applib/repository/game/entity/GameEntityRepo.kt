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
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.display.EntityProfile
import sokeriaaa.return0.models.entity.display.ExtendedEntityProfile
import sokeriaaa.return0.models.entity.generate
import sokeriaaa.return0.models.entity.level.EntityLevelHelper
import sokeriaaa.return0.models.entity.level.EntityLevelHelper.expRequiredToReach
import sokeriaaa.return0.models.entity.plugin.generatePlugin
import sokeriaaa.return0.shared.common.helpers.TimeHelper
import sokeriaaa.return0.shared.data.models.combat.EntityState
import sokeriaaa.return0.shared.data.models.entity.EntityData
import kotlin.math.max

class GameEntityRepo(
    private val archive: ArchiveRepo,
    private val plugin: GamePluginRepo,
    private val resource: ResourceRepo,
    private val entityDao: EntityDao,
) {
    suspend fun queryAll(): List<EntityTable> {
        return entityDao.queryAll(saveID = AppConstants.CURRENT_SAVE_ID)
    }

    /**
     * Obtaining a new entity.
     *
     * @return Whether the player had already obtained this entity. (currentEntity == null)
     */
    suspend fun obtainEntity(
        entityName: String,
        level: Int = 1,
        exp: Int = 0,
        currentHP: Int? = null,
        currentSP: Int? = null,
        pluginID: Long? = null,
    ): Boolean {
        val currentEntity = entityDao.getEntity(
            saveID = AppConstants.CURRENT_SAVE_ID,
            entityName = entityName,
        )
        entityDao.insert(
            EntityTable(
                saveID = AppConstants.CURRENT_SAVE_ID,
                entityName = entityName,
                level = if (currentEntity == null) {
                    level
                } else {
                    max(level, currentEntity.level)
                },
                exp = if (currentEntity == null) {
                    exp
                } else {
                    max(exp, currentEntity.exp)
                },
                // For the other status, use the original's.
                currentHP = currentEntity?.currentHP ?: currentHP,
                currentSP = currentEntity?.currentSP ?: currentSP,
                indexedTime = currentEntity?.indexedTime ?: TimeHelper.currentTimeMillis(),
                pluginID = currentEntity?.pluginID ?: pluginID,
            )
        )
        return currentEntity == null
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
        plugin: EntityState.Plugin? = null,
    ): Entity {
        // Get the growth data of the primary category of entity.
        val growth = archive.getEntityGrowthByCategory(entityData.category)
        return entityData.generate(
            index = index,
            level = level,
            growth = growth,
            isParty = isParty,
            plugin = plugin?.let {
                it.pluginData.generatePlugin(it.tier, it.constMap)
            }
        ).apply {
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

    fun getEntityProfileByTable(table: EntityTable): EntityProfile? {
        val entityData = archive.getEntityData(table.entityName) ?: return null

        // Get the growth data of the primary category of entity.
        val growth = archive.getEntityGrowthByCategory(entityData.category)
        // Generate entity
        val entity = entityData.generate(
            index = -1,
            level = table.level,
            growth = growth,
            isParty = true,
            plugin = plugin.assemblePlugin(table.pluginID),
        ).apply {
            hp = table.currentHP ?: maxhp
            sp = table.currentSP ?: maxsp
        }
        // Assemble display
        return EntityProfile(
            name = table.entityName,
            level = table.level,
            expProgress = EntityLevelHelper.levelProgressByLevel(
                level = table.level,
                totalExp = table.exp,
                levelPacing = entityData.levelPacing,
            ),
            hp = entity.hp,
            maxHP = entity.maxhp,
            sp = entity.sp,
            maxSP = entity.maxsp,
        )
    }

    suspend fun getEntityProfile(entityName: String): EntityProfile? {
        return getEntityProfileByTable(getEntityTable(entityName) ?: return null)
    }

    suspend fun getExtendedEntityProfile(entityName: String): ExtendedEntityProfile? {
        val table = getEntityTable(entityName) ?: return null
        val entityData = archive.getEntityData(entityName) ?: return null

        // Get the growth data of the primary category of entity.
        val growth = archive.getEntityGrowthByCategory(entityData.category)
        // Generate entity
        val entity = entityData.generate(
            index = -1,
            level = table.level,
            growth = growth,
            isParty = true,
            plugin = plugin.assemblePlugin(table.pluginID),
        ).apply {
            hp = table.currentHP ?: maxhp
            sp = table.currentSP ?: maxsp
        }
        val expCurrent = expRequiredToReach(table.level, entityData.levelPacing)
        val expNext = expRequiredToReach(table.level + 1, entityData.levelPacing)
        // Assemble display
        return ExtendedEntityProfile(
            name = table.entityName,
            level = table.level,
            expProgress = EntityLevelHelper.levelProgressByExp(
                totalExp = table.exp,
                expCurrent = expCurrent,
                expNext = expNext,
            ),
            hp = entity.hp,
            maxHP = entity.maxhp,
            sp = entity.sp,
            maxSP = entity.maxsp,
            functions = entityData.functions.map { functionData ->
                val skill = entity.functions.firstOrNull { it.name == functionData.name }
                ExtendedEntityProfile.Skill(
                    name = functionData.name,
                    description = resource.getString("skill.${functionData.name}.desc"),
                    category = functionData.category,
                    tier = skill?.tier ?: 0,
                    power = skill?.power ?: functionData.basePower,
                    spCost = skill?.spCost ?: functionData.baseSPCost,
                    data = functionData,
                )
            },
            category = entity.category,
            category2 = entity.category2,
            path = entity.path,
            atk = entity.atk,
            def = entity.def,
            spd = entity.spd,
            maxAP = entity.maxap,
            plugin = plugin.pluginMap[table.pluginID],
            expCurrent = expCurrent,
            expNext = expNext,
            expTotal = table.exp
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

    /**
     * Switch the plugin installed by an entity.
     */
    suspend fun switchPlugin(
        entityName: String,
        newPluginID: Long?,
    ) {
        entityDao.updatePlugin(AppConstants.CURRENT_SAVE_ID, entityName, newPluginID)
        if (newPluginID == null) {
            // Notify the plugin repo
            plugin.onPluginUninstalledFrom(entityName)
        } else {
            // Remove the plugin from formal entity.
            val wasInstalledBy = plugin.pluginMap[newPluginID]?.installedBy
            if (entityName == wasInstalledBy) {
                return
            }
            wasInstalledBy?.let {
                entityDao.updatePlugin(AppConstants.CURRENT_SAVE_ID, it, null)
            }
            // Notify the plugin repo
            plugin.onPluginInstalledTo(newPluginID, installedBy = entityName)
        }
    }
}