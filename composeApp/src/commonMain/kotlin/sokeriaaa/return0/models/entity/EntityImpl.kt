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
package sokeriaaa.return0.models.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.models.action.effect.Effect
import sokeriaaa.return0.models.action.function.CommonFunctions
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.entity.shield.Shield
import sokeriaaa.return0.shared.data.models.action.effect.EffectModifier
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.category.Category

fun EntityData.generate(
    index: Int,
    level: Int,
    growth: EntityGrowth,
    isParty: Boolean,
): Entity = EntityImpl(
    index = index,
    isParty = isParty,
    name = this.name,
    level = level,
    category = this.category,
    category2 = this.category2,
    baseATK = (this.baseATK * (1 + growth.atkGrowth * level)).toInt(),
    baseDEF = (this.baseDEF * (1 + growth.defGrowth * level)).toInt(),
    baseSPD = (this.baseSPD * (1 + growth.spdGrowth * level)).toInt(),
    baseHP = (this.baseHP * (1 + growth.hpGrowth * level)).toInt(),
    baseSP = (this.baseSP * (1 + growth.spGrowth * level)).toInt(),
    baseAP = this.baseAP,
    functions = this.functions,
    attackModifier = this.attackModifier,
)

internal class EntityImpl(
    override val index: Int,
    override val isParty: Boolean,
    override val name: String,
    override val level: Int,
    override val category: Category,
    override val category2: Category?,
    override val baseATK: Int,
    override val baseDEF: Int,
    override val baseSPD: Int,
    override val baseHP: Int,
    override val baseSP: Int,
    override val baseAP: Int,
    functions: List<FunctionData>,
    attackModifier: EntityData.GeneralAttackModifier?
) : Entity {
    override var isFailedFlag: Boolean = false
    override var atk: Int = baseATK
        private set
    override var def: Int = baseDEF
        private set
    override var spd: Int = baseSPD
        private set
    override var hp: Int by mutableIntStateOf(0)
    override var maxhp: Int by mutableIntStateOf(baseHP)
        private set
    override var sp: Int by mutableIntStateOf(0)
    override var maxsp: Int by mutableIntStateOf(baseSP)
        private set
    override var ap: Float by mutableFloatStateOf(0F)
    override var maxap: Int by mutableIntStateOf(baseAP)
        private set
    override var critRate: Float = AppConstants.BASE_CRITICAL_RATE
        private set
    override var critDMG: Float = AppConstants.BASE_CRITICAL_DMG
        private set
    override var targetRate: Float = AppConstants.BASE_TARGET_RATE
        private set
    override var hideRate: Float = AppConstants.BASE_HIDE_RATE
        private set

    private var _apRecovery: Float = 0F

    override val attackAction: Skill =
        this.generateFunctionFor(CommonFunctions.attack(attackModifier))!!
    override val defendAction: Skill =
        this.generateFunctionFor(CommonFunctions.defend)!!
    override val relaxAction: Skill =
        this.generateFunctionFor(CommonFunctions.relax)!!

    override val functions: List<Skill> = functions.mapNotNull {
        this.generateFunctionFor(it)
    }

    override val effects: List<Effect> get() = _effects
    private val _effects: MutableList<Effect> = ArrayList()

    override val shields: Map<String, Shield> get() = _shields
    private val _shields: MutableMap<String, Shield> = HashMap()

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
        if (currentShield != null && currentShield.value < value) {
            _shields[key] = Shield(value = value, turnsLeft = turns)
        }
    }

    override fun removeShield(key: String) {
        _shields.remove(key)
    }

    override fun cleanUpShields() {
        _shields.asSequence()
            .filter { it.value.value <= 0 || (it.value.turnsLeft ?: Int.MAX_VALUE) <= 0 }
            .map { it.key }
            .toList()
            .forEach {
                _shields.remove(it)
            }
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
                EffectModifier.Types.ATK -> atk = (baseATK * entry.value).toInt().coerceAtLeast(1)
                EffectModifier.Types.DEF -> def = (baseDEF * entry.value).toInt().coerceAtLeast(1)
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