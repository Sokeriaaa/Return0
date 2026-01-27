/**
 * Copyright (C) 2026 Sokeriaaa
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
package sokeriaaa.return0.models.component.executor.value

import sokeriaaa.return0.models.component.context.EventContext
import sokeriaaa.return0.shared.common.helpers.TimeHelper
import sokeriaaa.return0.shared.data.models.component.values.TimeValue
import sokeriaaa.return0.shared.data.models.component.values.Value

suspend fun Value.Time.calculateTime(context: EventContext): Long = when (this) {
    TimeValue.Now -> context.now
    is TimeValue.After -> context.now + millis
    is TimeValue.NextDay -> TimeHelper.nextDay(context.now) + offsetMillis
    is TimeValue.NextWeek -> TimeHelper.nextSunday(context.now) + offsetMillis
    is TimeValue.Saved -> context.gameState.savedValues.getTimeStamp("event:$key")
    is TimeValue.Custom -> timeInMillis
}