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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import sokeriaaa.common.kmp.helpers.TimeHelper
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.data.archive.ArchiveRepo
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.models.action.effect.Effect
import sokeriaaa.return0.models.action.function.CommonFunctions
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.display.EntityProfile
import sokeriaaa.return0.models.entity.display.ExtendedEntityProfile
import sokeriaaa.return0.models.entity.level.EntityLevelHelper
import sokeriaaa.return0.models.entity.level.EntityLevelHelper.expRequiredToReach
import sokeriaaa.return0.models.entity.plugin.EntityPlugin
import sokeriaaa.return0.models.entity.plugin.generatePlugin
import sokeriaaa.return0.models.entity.shield.Shield
import sokeriaaa.return0.shared.data.api.component.extra.extrasGroupOfOrNull
import sokeriaaa.return0.shared.data.api.component.value.plus
import sokeriaaa.return0.shared.data.models.action.effect.EffectModifier
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.combat.EntityState
import sokeriaaa.return0.shared.data.models.component.extras.Extra
import sokeriaaa.return0.shared.data.models.component.values.Value
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst
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
        level: Int,
        isParty: Boolean,
        index: Int = -1,
        growth: EntityGrowth = archive.getEntityGrowthByCategory(entityData.category),
        currentHP: Int? = null,
        currentSP: Int? = null,
        plugin: EntityState.Plugin? = null,
    ): Entity {
        // Get the growth data of the primary category of entity.
        val entity = EntityImpl(
            index = index,
            isParty = isParty,
            name = entityData.name,
            level = level,
            path = entityData.path,
            category = entityData.category,
            category2 = entityData.category2,
            baseATK = (entityData.baseATK * (1 + growth.atkGrowth * level)).toInt(),
            baseDEF = (entityData.baseDEF * (1 + growth.defGrowth * level)).toInt(),
            baseSPD = (entityData.baseSPD * (1 + growth.spdGrowth * level)).toInt(),
            baseHP = (entityData.baseHP * (1 + growth.hpGrowth * level)).toInt(),
            baseSP = entityData.baseSP,
            baseAP = entityData.baseAP,
            functionDataList = entityData.functions.mapNotNull {
                archive.entities.functions[it]
            },
            attackModifier = entityData.attackModifier,
        )
        return if (plugin == null) {
            entity
        } else {
            PluggedEntityImpl(
                entity,
                plugin.pluginData.generatePlugin(plugin.tier, plugin.constMap)
            )
        }.apply {
            hp = currentHP ?: maxhp
            // 25% of maxsp
            sp = currentSP ?: (maxsp / 4)
        }
    }

    /**
     * Get an [Entity] instance for displaying in the profile page.
     */
    suspend fun getEntityStatus(entityName: String): Entity? {
        val entityData = archive.entities[entityName] ?: return null
        val entityTable = getEntityTable(entityName) ?: return null
        return generateEntityInstance(
            entityData = entityData,
            index = -1,
            level = entityTable.level,
            isParty = true,
            currentHP = entityTable.currentHP,
        )
    }

    suspend fun getEntityProfileByTable(table: EntityTable): EntityProfile? {
        val entityData = archive.entities[table.entityName] ?: return null
        // Generate entity
        val entity = generateEntityInstance(
            entityData = entityData,
            index = -1,
            level = table.level,
            isParty = true,
            plugin = plugin.getPluginStateByID(table.pluginID),
        ).apply {
            hp = table.currentHP ?: maxhp
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
            // For now, we display the full sp here.
            sp = entity.maxsp,
            maxSP = entity.maxsp,
        )
    }

    suspend fun getEntityProfile(entityName: String): EntityProfile? {
        return getEntityProfileByTable(getEntityTable(entityName) ?: return null)
    }

    suspend fun getExtendedEntityProfile(entityName: String): ExtendedEntityProfile? {
        val table = getEntityTable(entityName) ?: return null
        val entityData = archive.entities[entityName] ?: return null
        // Generate entity
        val entity = generateEntityInstance(
            entityData = entityData,
            index = -1,
            level = table.level,
            isParty = true,
            plugin = plugin.getPluginStateByID(table.pluginID),
        ).apply {
            hp = table.currentHP ?: maxhp
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
            // For now, we display the full sp here.
            sp = entity.maxsp,
            maxSP = entity.maxsp,
            functions = entityData.functions.mapNotNull { functionName ->
                val skill = entity.functions.firstOrNull { it.name == functionName }
                val functionData = archive.entities.functions[functionName]
                    ?: return@mapNotNull null
                ExtendedEntityProfile.Skill(
                    name = functionName,
                    description = resource.getString("skill.${functionName}.desc"),
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
            updateHP(entityName = it.name, currentHP = it.hp)
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
        val entityData = archive.entities[entityName] ?: return 0 to 0
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
     * Mainly for the HP after a combat finished.
     */
    suspend fun updateHP(
        entityName: String,
        currentHP: Int?,
    ) {
        entityDao.updateHP(AppConstants.CURRENT_SAVE_ID, entityName, currentHP)
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

    fun getPluginOf(entity: Entity): EntityPlugin? = (entity as? PluggedEntityImpl)?.plugin

    private open class EntityImpl(
        override val index: Int,
        override val isParty: Boolean,
        override val name: String,
        override val level: Int,
        override val path: EntityPath,
        override val category: Category,
        override val category2: Category?,
        override val baseATK: Int,
        override val baseDEF: Int,
        override val baseSPD: Int,
        override val baseHP: Int,
        override val baseSP: Int,
        override val baseAP: Int,
        val functionDataList: List<FunctionData>,
        val attackModifier: EntityData.GeneralAttackModifier?
    ) : Entity {
        override var isFailedFlag: Boolean = false
        override var atk: Int = baseATK
            protected set
        override var def: Int = baseDEF
            protected set
        override var spd: Int = baseSPD
            protected set
        override var hp: Int by mutableIntStateOf(0)
        override var maxhp: Int by mutableIntStateOf(baseHP)
            protected set
        override var sp: Int by mutableIntStateOf(0)
        override var maxsp: Int by mutableIntStateOf(baseSP)
            protected set
        override var ap: Float by mutableFloatStateOf(0F)
        override var maxap: Int by mutableIntStateOf(baseAP)
            protected set
        override var critRate: Float = AppConstants.BASE_CRITICAL_RATE
            protected set
        override var critDMG: Float = AppConstants.BASE_CRITICAL_DMG
            protected set
        override var targetRate: Float = AppConstants.BASE_TARGET_RATE
            protected set
        override var hideRate: Float = AppConstants.BASE_HIDE_RATE
            protected set
        override var shieldValue: Int by mutableIntStateOf(0)

        private var _apRecovery: Float = 0F

        override val attackAction: Skill =
            this.generateFunctionFor(CommonFunctions.attack(attackModifier))!!
        override val defendAction: Skill =
            this.generateFunctionFor(CommonFunctions.defend)!!
        override val relaxAction: Skill =
            this.generateFunctionFor(CommonFunctions.relax)!!

        override val functions: List<Skill> = functionDataList.mapNotNull {
            this.generateFunctionFor(it)
        }

        override val effects: List<Effect> get() = _effects
        private val _effects: MutableList<Effect> = ArrayList()

        override val shields: Map<String, Shield> get() = _shields
        private val _shields: MutableMap<String, Shield> = HashMap()

        override var actionsTaken: Int = 0
            protected set

        override val onAttack: Extra? = null
        override val onDefend: Extra? = null
        override val attackRateOffset: Value? = null
        override val defendRateOffset: Value? = null

        init {
            updateAPRecovery()
        }

        override fun tick() {
            // Calculate AP.
            if (ap >= maxap) {
                return
            }
            ap = (ap + _apRecovery).coerceAtMost(maxap.toFloat())
        }

        override fun onAction() {
            actionsTaken++
        }

        override fun attachEffect(effect: Effect) {
            _effects.add(effect)
            if (effect.modifiers.isNotEmpty()) {
                recalculateStats()
            }
        }

        override fun removeEffect(effect: Effect) {
            _effects.remove(effect)
            if (effect.modifiers.isNotEmpty()) {
                recalculateStats(removed = effect)
            }
        }

        override fun attachShield(key: String, value: Int, turns: Int?) {
            val currentShield = shields[key]
            if (currentShield == null || currentShield.value >= value) {
                _shields[key] = Shield(value = value, turnsLeft = turns)
            }
            updateShieldValue()
        }

        override fun removeShield(key: String) {
            _shields.remove(key)
            updateShieldValue()
        }

        override fun cleanUpShields() {
            _shields.asSequence()
                .filter { it.value.value <= 0 || (it.value.turnsLeft ?: Int.MAX_VALUE) <= 0 }
                .map { it.key }
                .toList()
                .forEach {
                    _shields.remove(it)
                }
            updateShieldValue()
        }

        private fun updateShieldValue() {
            shieldValue = shields.values.sumOf { it.value }
        }

        private fun updateAPRecovery() {
            _apRecovery = (1F + 10F * spd / (1000F + spd)) / 3F
        }

        /**
         * When an effect whose effect modifier list is not empty is added or removed,
         *  refresh the stats of entity.
         *
         * @param removed Removed effect, to ensure the stats is reset.
         */
        private fun recalculateStats(removed: Effect? = null) {
            // All final rates for each modifier type. Default is 1.
            val rates: MutableMap<EffectModifier.Types, Float> = HashMap()
            // Put default value 1F for the removed effect to correctly reset the stats.
            removed?.let {
                it.modifiers.forEach { modifier ->
                    rates[modifier.type] = 1F
                }
            }
            // Calculate other effects.
            effects.forEach { effect ->
                effect.modifiers.forEach { modifier ->
                    val finalRate = modifier.offset + (effect.tier - 1) * modifier.tierBonus
                    rates[modifier.type] = (rates[modifier.type] ?: 1F) + finalRate
                }
            }
            // Update stats
            rates.forEach { entry ->
                when (entry.key) {
                    EffectModifier.Types.ATK -> atk =
                        (baseATK * entry.value).toInt().coerceAtLeast(1)

                    EffectModifier.Types.DEF -> def =
                        (baseDEF * entry.value).toInt().coerceAtLeast(1)

                    EffectModifier.Types.SPD -> {
                        spd = (baseSPD * entry.value).toInt().coerceAtLeast(1)
                        updateAPRecovery()
                    }

                    EffectModifier.Types.MAXHP -> {
                        maxhp = (baseHP * entry.value).toInt().coerceAtLeast(1)
                        hp = hp.coerceAtMost(maxhp)
                    }

                    EffectModifier.Types.MAXSP -> {
                        maxsp = (baseSP * entry.value).toInt().coerceAtLeast(1)
                        sp = hp.coerceAtMost(maxsp)
                    }

                    EffectModifier.Types.MAXAP -> {
                        maxap = (baseAP * entry.value).toInt().coerceAtLeast(1)
                    }

                    EffectModifier.Types.CRIT_RATE -> {
                        critRate = (AppConstants.BASE_CRITICAL_RATE + entry.value).coerceAtLeast(0F)
                    }

                    EffectModifier.Types.CRIT_DMG -> {
                        critDMG = (AppConstants.BASE_CRITICAL_DMG + entry.value).coerceAtLeast(0F)
                    }

                    EffectModifier.Types.TARGET_RATE -> {
                        targetRate = (AppConstants.BASE_TARGET_RATE + entry.value).coerceAtLeast(0F)
                    }

                    EffectModifier.Types.HIDE_RATE -> {
                        hideRate = (AppConstants.BASE_HIDE_RATE + entry.value).coerceAtLeast(0F)
                    }
                }
            }
        }
    }

    /**
     * Wrapped entity with plugin installed.
     */
    private class PluggedEntityImpl(
        // TODO change to Entity
        val entity: EntityImpl,
        val plugin: EntityPlugin,
    ) : EntityImpl(
        index = entity.index,
        isParty = entity.isParty,
        name = entity.name,
        level = entity.level,
        path = entity.path,
        category = entity.category,
        category2 = entity.category2,
        baseATK = (entity.baseATK * (1F + (plugin.constMap[PluginConst.ATK] ?: 0) * 0.01F)).toInt(),
        baseDEF = (entity.baseDEF * (1F + (plugin.constMap[PluginConst.DEF] ?: 0) * 0.01F)).toInt(),
        baseSPD = (entity.baseSPD * (1F + (plugin.constMap[PluginConst.SPD] ?: 0) * 0.01F)).toInt(),
        baseHP = (entity.baseHP * (1F + (plugin.constMap[PluginConst.HP] ?: 0) * 0.01F)).toInt(),
        baseSP = (entity.baseSP * (1F + (plugin.constMap[PluginConst.SP] ?: 0) * 0.01F)).toInt(),
        baseAP = entity.baseAP,
        functionDataList = entity.functionDataList,
        attackModifier = entity.attackModifier,
    ) {
        override var critRate: Float =
            entity.critRate + (plugin.constMap[PluginConst.CRIT_RATE] ?: 0) * 0.01F
        override var critDMG: Float =
            entity.critDMG + (plugin.constMap[PluginConst.CRIT_DMG] ?: 0) * 0.01F
        override var targetRate: Float =
            entity.targetRate + (plugin.constMap[PluginConst.TGT_RATE] ?: 0) * 0.01F
        override var hideRate: Float =
            entity.hideRate + (plugin.constMap[PluginConst.HID_RATE] ?: 0) * 0.01F

        override val onAttack: Extra? = if (plugin.path == entity.path) {
            extrasGroupOfOrNull(super.onAttack, plugin.onAttack)
        } else {
            super.onAttack
        }

        override val onDefend: Extra? = if (plugin.path == entity.path) {
            extrasGroupOfOrNull(super.onDefend, plugin.onDefend)
        } else {
            super.onDefend
        }

        override val attackRateOffset: Value? = if (plugin.path == entity.path) {
            when {
                super.attackRateOffset == null -> plugin.attackRateOffset
                plugin.attackRateOffset == null -> super.attackRateOffset
                else -> super.attackRateOffset!! + plugin.attackRateOffset!!
            }
        } else {
            super.attackRateOffset
        }

        override val defendRateOffset: Value? = if (plugin.path == entity.path) {
            when {
                super.defendRateOffset == null -> plugin.defendRateOffset
                plugin.defendRateOffset == null -> super.defendRateOffset
                else -> super.defendRateOffset!! + plugin.defendRateOffset!!
            }
        } else {
            super.defendRateOffset
        }

    }
}