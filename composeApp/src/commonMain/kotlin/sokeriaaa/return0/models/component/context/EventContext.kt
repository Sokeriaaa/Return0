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
package sokeriaaa.return0.models.component.context

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.game.GameStateRepo
import sokeriaaa.return0.models.story.event.EventEffect
import sokeriaaa.return0.shared.common.helpers.TimeHelper
import kotlin.random.Random

/**
 * Context class for executing events.
 */
class EventContext(
    val key: String?,
    override val random: Random = Random.Default,
    val location: Pair<String, Int>? = null,
    val now: Long = TimeHelper.currentTimeMillis(),
    val callback: Callback,
) : BaseContext, KoinComponent {
    val gameState: GameStateRepo by inject()
    val resources: ResourceRepo by inject()
    val archive: ArchiveRepo by inject()

    interface Callback {
        suspend fun waitForUserContinue()
        suspend fun waitForChoice(): Int
        suspend fun waitForMoveFinished()
        suspend fun waitForCombatResult(): Boolean
        suspend fun onEffect(effect: EventEffect)
    }
}