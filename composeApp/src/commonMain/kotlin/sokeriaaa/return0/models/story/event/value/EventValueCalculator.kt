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
import sokeriaaa.return0.shared.data.models.story.event.value.EventValue

suspend fun EventValue.calculatedIn(context: EventContext): Int {
    return when (this) {
        is EventValue.Constant -> value
        is EventValue.RandomInt -> context.random.nextInt(start, endInclusive + 1)
        is EventValue.SavedVariable -> TODO()
        is EventValue.Currency -> context.gameState.currencies[type] ?: 0
        is EventValue.Inventory -> TODO()
    }
}