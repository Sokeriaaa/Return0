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
package sokeriaaa.return0.models.story.event.value

import sokeriaaa.return0.models.story.event.EventContext
import sokeriaaa.return0.models.story.event.condition.calculatedIn
import sokeriaaa.return0.shared.data.models.component.values.CommonValue
import sokeriaaa.return0.shared.data.models.component.values.EventValue
import sokeriaaa.return0.shared.data.models.component.values.Value
import kotlin.math.abs
import kotlin.random.Random

suspend fun sokeriaaa.return0.shared.data.models.story.event.value.EventValue.calculatedIn(context: EventContext): Int {
    return when (this) {
        is sokeriaaa.return0.shared.data.models.story.event.value.EventValue.Constant -> value
        is sokeriaaa.return0.shared.data.models.story.event.value.EventValue.RandomInt -> context.random.nextInt(
            start,
            endInclusive + 1
        )

        is sokeriaaa.return0.shared.data.models.story.event.value.EventValue.SavedVariable -> context.gameState.savedValues.getVariable(
            key
        )

        is sokeriaaa.return0.shared.data.models.story.event.value.EventValue.Currency -> context.gameState.currency[type]
        is sokeriaaa.return0.shared.data.models.story.event.value.EventValue.Inventory -> context.gameState.inventory[key]
    }
}

/**
 * Calculate the [Value.Event] in specified [context].
 * All non-event values will always return 0.
 */
suspend fun Value.calculatedIn(context: EventContext): Int {
    return when (this) {
        // All non-event values will always return 0.
        !is Value.Event -> 0
        // start - CommonValue
        is CommonValue.Constant -> value.toInt()
        is CommonValue.Math.Sum -> values.sumOf { it.calculatedIn(context).toDouble() }.toInt()
        is CommonValue.Math.Times -> value1.calculatedIn(context) * value2.calculatedIn(context)
        is CommonValue.Math.TimesConst -> (value1.calculatedIn(context) * value2).toInt()
        is CommonValue.Math.Div -> value1.calculatedIn(context) / value2.calculatedIn(context)
        is CommonValue.Math.UnaryMinus -> -value1.calculatedIn(context)
        is CommonValue.Math.Shift -> {
            val digit = shift.calculatedIn(context)
            val value = value1.calculatedIn(context)
            return when {
                digit > 0 -> value.shl(digit)
                digit < 0 -> value.shr(-digit)
                else -> value
            }
        }

        is CommonValue.Math.AbsoluteValue -> abs(value.calculatedIn(context))
        is CommonValue.Math.Coerced -> {
            var finalValue = value.calculatedIn(context)
            min?.let {
                finalValue = finalValue.coerceAtLeast(it.calculatedIn(context))
            }
            max?.let {
                finalValue = finalValue.coerceAtMost(it.calculatedIn(context))
            }
            return finalValue
        }

        is CommonValue.Math.MinOf -> values.minOfOrNull { it.calculatedIn(context) } ?: 0
        is CommonValue.Math.MaxOf -> values.maxOfOrNull { it.calculatedIn(context) } ?: 0
        is CommonValue.Math.RandomInt -> Random.nextInt(start, endInclusive + 1)
        is CommonValue.Math.RandomFloat -> (Random.nextFloat() * (end - start) + start).toInt()

        is CommonValue.Conditioned -> {
            return if (condition.calculatedIn(context)) {
                ifTrue
            } else {
                ifFalse
            }?.calculatedIn(context)
                ?: defaultValue?.calculatedIn(context)
                ?: 0
        }

        is CommonValue.ForUser -> 0
        is CommonValue.Swapped -> 0
        is CommonValue.LoadValue -> 0
        // end - CommonValue
        // start - EventValue
        is EventValue.SavedVariable -> context.gameState.savedValues.getVariable(key)
        is EventValue.Currency -> context.gameState.currency[type]
        is EventValue.Inventory -> context.gameState.inventory[key]
        // end - EventValue
    }
}