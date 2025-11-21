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
package sokeriaaa.return0.temp

import sokeriaaa.return0.models.entity.category.Category
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.api.component.value.times
import sokeriaaa.return0.shared.data.models.action.effect.EffectData
import sokeriaaa.return0.shared.data.models.action.effect.EffectModifier
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.action.function.FunctionTarget
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.extras.CommonExtra
import sokeriaaa.return0.shared.data.models.component.values.CombatValue
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth

/**
 * This is a misc temporary class for storing some data of entities, stories and effects.
 * This class will be removed in the future and replace with api queries.
 */
object TempData {

    /**
     * A common function.
     */
    private val notifyFunction = FunctionData(
        name = "notify",
        category = Category.CLASS,
        target = FunctionTarget.SingleEnemy,
        bullseye = false,
        basePower = 40,
        powerBonus = 0,
        baseSPCost = 25,
        spCostBonus = 0,
        growth = null,
    )

    /**
     * Multi-timed attack example.
     */
    private val forEachFunction = FunctionData(
        name = "forEach",
        category = Category.STREAM,
        target = FunctionTarget.SingleEnemy,
        bullseye = false,
        basePower = 5,
        powerBonus = 0,
        baseSPCost = 40,
        spCostBonus = 0,
        growth = null,
        attackModifier = FunctionData.AttackModifier(
            attackTimes = Value(6, 10)
        )
    )

    /**
     * Multi-timed + absorb attack example.
     *
     * Heal the user for 20% of the damage (coerced to target's current HP.)
     */
    private val getPropertyFunction = FunctionData(
        name = "getProperty",
        category = Category.MEMORY,
        target = FunctionTarget.SingleEnemy,
        bullseye = false,
        basePower = 10,
        powerBonus = 0,
        baseSPCost = 50,
        spCostBonus = 0,
        growth = null,
        attackModifier = FunctionData.AttackModifier(
            attackTimes = Value(2, 5)
        ),
        extra = CommonExtra.ForUser(
            extra = CombatExtra.HPChange(
                hpChange = CombatValue.DamageCoerced * 0.2F
            )
        )
    )

    /**
     * ATK boost + effect attach.
     */
    private val optimizeFunction = FunctionData(
        name = "optimize",
        category = Category.MEMORY,
        target = FunctionTarget.SingleTeamMate,
        bullseye = true,
        basePower = 0,
        powerBonus = 0,
        baseSPCost = 100,
        spCostBonus = 0,
        growth = null,
        extra = CombatExtra.AttachEffect(
            name = "optimize",
            tier = Value(1),
            turns = Value(3),
        )
    )

    val objectEntity = EntityData(
        name = "Object",
        category = Category.CLASS,
        baseATK = 56,
        baseDEF = 27,
        baseSPD = 28,
        baseHP = 314,
        baseSP = 288,
        baseAP = 120,
        functions = listOf(
            notifyFunction,
        ),
    )

    val iteratorEntity = EntityData(
        name = "Iterator",
        category = Category.STREAM,
        baseATK = 73,
        baseDEF = 20,
        baseSPD = 48,
        baseHP = 227,
        baseSP = 215,
        baseAP = 110,
        functions = listOf(
            forEachFunction,
        ),
        attackModifier = EntityData.GeneralAttackModifier(
            power = 10,
            attackTimes = 3,
        )
    )

    val systemEntity = EntityData(
        name = "System",
        category = Category.MEMORY,
        baseATK = 50,
        baseDEF = 34,
        baseSPD = 24,
        baseHP = 368,
        baseSP = 225,
        baseAP = 130,
        functions = listOf(
            getPropertyFunction,
            optimizeFunction,
        ),
    )

    /**
     * An effect boosts ATK.
     */
    val optimizedEffect = EffectData(
        name = "optimize",
        isDebuff = false,
        modifiers = listOf(
            EffectModifier(
                type = EffectModifier.Types.ATK,
                offset = 0.2F,
                tierBonus = 0.05F,
            )
        )
    )

    /**
     * Common Growth.
     */
    val growth = EntityGrowth(
        atkGrowth = 0.25f,
        defGrowth = 0.25f,
        spdGrowth = 0.25f,
        hpGrowth = 0.25f,
        spGrowth = 0.25f,
    )

}