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
package sokeriaaa.return0.models.story.event.condition

import sokeriaaa.return0.models.story.event.EventContext
import sokeriaaa.return0.models.story.event.value.calculatedIn
import sokeriaaa.return0.shared.common.helpers.chance
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.conditions.Condition
import sokeriaaa.return0.shared.data.models.component.conditions.EventCondition

/**
 * Calculate the [Condition.Event] in specified [context].
 * All non-event conditions will always return false.
 */
suspend fun Condition.calculatedIn(context: EventContext): Boolean {
    return when (this) {
        // All non-event conditions will always return false.
        !is Condition.Event -> false
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

        // FIXME: accuracy loss
        is CommonCondition.Chance -> chance(
            success = success.calculatedIn(context),
            base = base.calculatedIn(context),
            random = context.random,
        )

        CommonCondition.True -> true

        CommonCondition.False -> false
        // end - CommonCondition

        // start - EventCondition
        is EventCondition.PlayerTitle -> comparator.compare(
            context.gameState.player.title.ordinal,
            title.ordinal,
        )

        is EventCondition.QuestCompleted -> context.gameState.quest.isCompleted(key)
        is EventCondition.SavedSwitch -> context.gameState.savedValues.getSwitch(key)
        // end - EventCondition
    }
}