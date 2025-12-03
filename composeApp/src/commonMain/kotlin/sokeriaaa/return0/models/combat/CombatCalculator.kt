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
package sokeriaaa.return0.models.combat

import sokeriaaa.return0.shared.common.helpers.chance
import kotlin.random.Random

/**
 * The calculators for the combat.
 */
object CombatCalculator {
    /**
     * Calculate the damage based on [power], [atk] and [def].
     *  Uses ratio with diminishing returns.
     *
     * This is the base damage calculated **with** current ATK of user and current DEF of target,
     *  but **without** any other multipliers such as critical damage, category rate, etc.
     *
     * No matter how low the ATK is and how high the DEF is, it will damage at least 1.
     *
     * @param power Power of action.
     * @param atk Current ATK of user.
     * @param def Current DEF of target.
     */
    fun baseDamage(
        power: Int,
        atk: Float,
        def: Float,
    ): Float = (power * atk.coerceAtLeast(1F) * 5 / (100 + def.coerceAtLeast(1F)))
        .coerceAtLeast(1F)

    /**
     * Calculate the healing based on [powerAbs] and [atk].
     *
     * Now matter how low the ATK is, it will heal at least 1.
     *
     * @param powerAbs Absolute value of the power.
     * @param atk Current ATK of user.
     */
    fun baseHeal(
        powerAbs: Int,
        atk: Float,
    ): Float = (powerAbs * atk.coerceAtLeast(1F) / 100).coerceAtLeast(1F)

    /**
     * Whether this attack will be critical.
     */
    fun isCritical(
        criticalRate: Float,
        random: Random = Random,
    ): Boolean = chance(success = criticalRate, random = random)

    /**
     * Whether this attack will be missed.
     */
    fun isMissed(
        targetRate: Float,
        hideRate: Float,
        random: Random = Random,
    ): Boolean = chance(success = 1F - targetRate + hideRate, random = random)
}