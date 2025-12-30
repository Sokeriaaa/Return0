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
package sokeriaaa.return0.models.component.executor.condition

import sokeriaaa.return0.models.component.context.ActionContext
import sokeriaaa.return0.models.component.executor.value.calculatedIn
import sokeriaaa.return0.shared.common.helpers.chance
import sokeriaaa.return0.shared.data.models.component.conditions.CombatCondition
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.conditions.Condition
import sokeriaaa.return0.shared.data.models.component.conditions.EntityCondition

/**
 * Calculate the [Condition.Combat] in specified [context].
 * All non-combat conditions will always return false.
 */
fun Condition.calculatedIn(context: ActionContext): Boolean {
    return when (this) {
        // All non-combat conditions will always return false.
        !is Condition.Combat -> false
        // start - CommonCondition
        is CommonCondition.And -> {
            for (condition in conditions) {
                if (!condition.calculatedIn(context)) {
                    return false
                }
            }
            return true
        }

        is CommonCondition.Or -> {
            for (condition in conditions) {
                if (condition.calculatedIn(context)) {
                    return true
                }
            }
            return false
        }

        is CommonCondition.Not -> !condition.calculatedIn(context)

        is CommonCondition.CompareValues -> {
            comparator.compare(value1.calculatedIn(context), value2.calculatedIn(context))
        }

        is CommonCondition.Chance -> chance(
            success = success.calculatedIn(context),
            base = base.calculatedIn(context),
            random = context.random,
        )

        CommonCondition.True -> true

        CommonCondition.False -> false
        // end - CommonCondition
        // start - CombatCondition
        CombatCondition.Critical -> context.attackDamageResult?.isCritical == true
        // end - CombatCondition
        // start - EntityCondition
        is EntityCondition.Categories.Has -> {
            context.target.category == category || context.target.category2 == category
        }

        is EntityCondition.Categories.HasOneOf -> {
            context.target.category in categories || context.target.category2 in categories
        }

        is EntityCondition.Effects.Has -> {
            context.target.effects.any { it.name == effectName }
        }

        EntityCondition.Effects.HasAny -> {
            context.target.effects.any()
        }

        is EntityCondition.Shields.Has -> {
            context.target.shields.containsKey(key)
        }

        is EntityCondition.Shields.HasAny -> {
            context.target.shields.isNotEmpty()
        }

        is EntityCondition.Status.HPRate -> {
            comparator.compare(
                value1 = context.target.hp.toFloat(),
                value2 = context.target.maxhp * rate.calculatedIn(context),
            )
        }

        is EntityCondition.Status.SPRate -> {
            comparator.compare(
                value1 = context.target.sp.toFloat(),
                value2 = context.target.maxsp * rate.calculatedIn(context),
            )
        }
        EntityCondition.Status.IsFailed -> context.target.hp <= 0
        // end - EntityCondition
    }
}