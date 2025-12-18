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
import sokeriaaa.return0.shared.data.models.story.event.condition.EventCondition

suspend fun EventCondition.calculatedIn(context: EventContext): Boolean {
    return when (this) {
        EventCondition.True -> true
        EventCondition.False -> false
        is EventCondition.Compare -> comparator.compare(
            value1.calculatedIn(context),
            value2.calculatedIn(context),
        )

        is EventCondition.PlayerTitle -> TODO()
        is EventCondition.QuestCompleted -> TODO()
        is EventCondition.SavedSwitch -> TODO()
    }
}