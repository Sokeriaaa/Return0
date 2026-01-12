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

import sokeriaaa.return0.models.component.context.ItemContext
import sokeriaaa.return0.models.component.executor.value.calculatedIn
import sokeriaaa.return0.shared.common.helpers.chance
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.conditions.Condition
import sokeriaaa.return0.shared.data.models.component.conditions.EntityCondition

fun Condition.calculatedIn(context: ItemContext): Boolean {
    return when (this) {
        !is Condition.Item -> false
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
            random = context.random,
        )

        CommonCondition.True -> true

        CommonCondition.False -> false
        // end - CommonCondition
        // start - EntityCondition
        is EntityCondition.Categories.Has -> {
            context.target.category == category || context.target.category2 == category
        }

        is EntityCondition.Categories.HasOneOf -> {
            context.target.category in categories || context.target.category2 in categories
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