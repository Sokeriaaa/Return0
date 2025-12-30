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
package sokeriaaa.return0.models.action.function

import sokeriaaa.return0.models.action.singleExecute
import sokeriaaa.return0.models.component.context.createExtraContextFor
import sokeriaaa.return0.models.component.executor.value.calculatedIn
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.action.function.FunctionTarget
import sokeriaaa.return0.shared.data.models.component.extras.Extra
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.shared.data.models.entity.category.Category
import kotlin.math.roundToInt

fun Entity.generateFunctionFor(functionData: FunctionData): Skill? {
    val skillTier: Int = if (functionData.growth.isNullOrEmpty()) {
        1
    } else {
        val index = functionData.growth.indexOfLast { this.level >= it }
        if (index == -1) {
            return null
        } else {
            index + 1
        }
    }
    return SkillImpl(
        name = functionData.name,
        user = this,
        tier = skillTier,
        category = functionData.category,
        functionTarget = functionData.target,
        basePower = functionData.basePower + functionData.powerBonus * (skillTier - 1),
        baseSPCost = functionData.baseSPCost + functionData.spCostBonus * (skillTier - 1),
        bullseye = functionData.bullseye,
        attackModifier = functionData.attackModifier,
        extra = functionData.extra,
    )
}

internal class SkillImpl(
    override val name: String,
    override val user: Entity,
    override val tier: Int,
    override val category: Category,
    override val functionTarget: FunctionTarget,
    override val basePower: Int,
    override val baseSPCost: Int,
    override val bullseye: Boolean,
    override val attackModifier: FunctionData.AttackModifier?,
    override val extra: Extra?,
) : Skill {
    override val values: MutableMap<String, Float> = HashMap()
    override var power: Int = basePower
        private set
    override var spCost: Int = baseSPCost
        private set
    override var timesUsed: Int = 0
        private set
    override var timesRepeated: Int = 0
        private set

    override fun invokeOn(targets: List<Entity>): List<ActionResult> {
        val results = ArrayList<ActionResult>()
        targets.forEach { target ->
            val context = createExtraContextFor(target)
            val times = attackModifier?.attackTimes?.calculatedIn(context)?.roundToInt() ?: 1
            repeat(times) {
                context.singleExecute()
                timesRepeated++
            }
            results.addAll(context.actionResults)
            timesRepeated = 0
        }
        user.sp -= spCost
        timesUsed++
        return results
    }

    override fun reset() {
        values.clear()
        timesUsed = 0
        timesRepeated = 0
    }
}