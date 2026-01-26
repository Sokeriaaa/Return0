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
package sokeriaaa.return0.test.models.action.function

import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.action.function.FunctionData.AttackModifier
import sokeriaaa.return0.shared.data.models.action.function.FunctionTarget
import sokeriaaa.return0.shared.data.models.component.extras.Extra
import sokeriaaa.return0.shared.data.models.entity.category.Category

object DummyFunction {

    fun generateFunctionData(
        name: String = "dummy",
        category: Category = Category.CLASS,
        target: FunctionTarget = FunctionTarget.SingleEnemy,
        bullseye: Boolean = false,
        basePower: Int = 0,
        powerBonus: Int = 0,
        baseSPCost: Int = 0,
        growth: List<Int>? = null,
        attackModifier: AttackModifier? = null,
        extra: Extra? = null,
    ): FunctionData = FunctionData(
        name = name,
        category = category,
        target = target,
        bullseye = bullseye,
        basePower = basePower,
        powerBonus = powerBonus,
        baseSPCost = baseSPCost,
        growth = growth,
        attackModifier = attackModifier,
        extra = extra,
    )

}