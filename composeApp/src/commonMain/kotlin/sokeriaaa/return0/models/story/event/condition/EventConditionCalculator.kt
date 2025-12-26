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

suspend fun sokeriaaa.return0.shared.data.models.story.event.condition.EventCondition.calculatedIn(
    context: EventContext
): Boolean {
    return when (this) {
        sokeriaaa.return0.shared.data.models.story.event.condition.EventCondition.True -> true
        sokeriaaa.return0.shared.data.models.story.event.condition.EventCondition.False -> false
        is sokeriaaa.return0.shared.data.models.story.event.condition.EventCondition.Compare -> comparator.compare(
            value1.calculatedIn(context),
            value2.calculatedIn(context),
        )

        is sokeriaaa.return0.shared.data.models.story.event.condition.EventCondition.PlayerTitle -> comparator.compare(
            context.gameState.player.getPlaterTitle().ordinal,
            title.ordinal,
        )

        is sokeriaaa.return0.shared.data.models.story.event.condition.EventCondition.QuestCompleted -> context.gameState.quest.isCompleted(
            key
        )

        is sokeriaaa.return0.shared.data.models.story.event.condition.EventCondition.SavedSwitch -> context.gameState.savedValues.getSwitch(
            key
        )
    }
}

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

        is CommonCondition.Compare -> if (isIncludeEquals) {
            value1.calculatedIn(context) >= value2.calculatedIn(context)
        } else {
            value1.calculatedIn(context) > value2.calculatedIn(context)
        }

        is CommonCondition.CompareValues -> {
            comparator.compare(value1.calculatedIn(context), value2.calculatedIn(context))
        }

        is CommonCondition.Chance -> chance(
            success = success.calculatedIn(context),
            base = base.calculatedIn(context),
        )

        CommonCondition.True -> true

        CommonCondition.False -> false
        // end - CommonCondition

        // start - EventCondition
        is EventCondition.PlayerTitle -> comparator.compare(
            context.gameState.player.getPlaterTitle().ordinal,
            title.ordinal,
        )

        is EventCondition.QuestCompleted -> context.gameState.quest.isCompleted(key)
        is EventCondition.SavedSwitch -> context.gameState.savedValues.getSwitch(key)
        // end - EventCondition
    }
}