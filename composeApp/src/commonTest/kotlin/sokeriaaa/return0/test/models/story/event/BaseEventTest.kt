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
package sokeriaaa.return0.test.models.story.event

import sokeriaaa.return0.models.story.event.EventContext
import sokeriaaa.return0.models.story.event.EventEffect
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.test.applib.modules.TestKoinModules
import kotlin.random.Random

abstract class BaseEventTest {

    protected inline fun withContext(
        key: String? = null,
        random: Random = Random,
        callback: EventContext.Callback,
        action: (EventContext) -> Unit
    ) {
        TestKoinModules.withModules {
            action(
                EventContext(
                    key = key,
                    random = random,
                    callback = callback,
                )
            )
        }
    }

    /**
     * Register entity data for testing.
     */
    protected fun registerTestingEntities(context: EventContext) {
        context.archive.registerEntity(
            EntityData(
                name = "foo",
                path = EntityPath.RUNTIME,
                category = Category.CLASS,
                baseATK = 100,
                baseDEF = 100,
                baseSPD = 100,
                baseHP = 500,
                baseSP = 500,
                baseAP = 100,
                functions = emptyList(),
            )
        )
        context.archive.registerEntity(
            EntityData(
                name = "bar",
                path = EntityPath.RUNTIME,
                category = Category.CLASS,
                baseATK = 100,
                baseDEF = 100,
                baseSPD = 100,
                baseHP = 500,
                baseSP = 500,
                baseAP = 100,
                functions = emptyList(),
            )
        )
        context.archive.registerEntity(
            EntityData(
                name = "baz",
                path = EntityPath.RUNTIME,
                category = Category.CLASS,
                baseATK = 100,
                baseDEF = 100,
                baseSPD = 100,
                baseHP = 500,
                baseSP = 500,
                baseAP = 100,
                functions = emptyList(),
            )
        )
        context.archive.registerEntity(
            EntityData(
                name = "qux",
                path = EntityPath.RUNTIME,
                category = Category.CLASS,
                baseATK = 100,
                baseDEF = 100,
                baseSPD = 100,
                baseHP = 500,
                baseSP = 500,
                baseAP = 100,
                functions = emptyList(),
            )
        )
        context.archive.registerEntity(
            EntityData(
                name = "quux",
                path = EntityPath.RUNTIME,
                category = Category.CLASS,
                baseATK = 100,
                baseDEF = 100,
                baseSPD = 100,
                baseHP = 500,
                baseSP = 500,
                baseAP = 100,
                functions = emptyList(),
            )
        )
        context.archive.registerEntity(
            EntityData(
                name = "corge",
                path = EntityPath.RUNTIME,
                category = Category.CLASS,
                baseATK = 100,
                baseDEF = 100,
                baseSPD = 100,
                baseHP = 500,
                baseSP = 500,
                baseAP = 100,
                functions = emptyList(),
            )
        )
    }

    /**
     * Register testing teams. Put "foo" and "bar" in team 1.
     */
    protected suspend fun registerTestingTeams(context: EventContext) {
        // Put "foo" and "bar" in team 1.
        context.gameState.team.updateTeam(
            teamID = 1,
            name = "Team 1",
            isActivated = true,
            slot1 = "foo",
            slot2 = "bar",
            slot3 = null,
            slot4 = null
        )
    }

    /**
     * Register testing teams. Put 4 entities in team 1.
     */
    protected suspend fun registerFullTestingTeams(context: EventContext) {
        // Put "foo" and "bar" in team 1.
        context.gameState.team.updateTeam(
            teamID = 1,
            name = "Team 1",
            isActivated = true,
            slot1 = "foo",
            slot2 = "bar",
            slot3 = "baz",
            slot4 = "qux"
        )
    }

    /**
     * A testing event callback.
     */
    protected abstract class TestingCallback : EventContext.Callback {
        private val _collectedEffects: MutableList<EventEffect> = ArrayList()
        val collectedEffects: List<EventEffect> = _collectedEffects

        override suspend fun waitForUserContinue() {}
        override suspend fun waitForChoice(): Int = 0
        override suspend fun waitForMoveFinished() {}
        override suspend fun waitForCombatResult(): Boolean = true
        override suspend fun onEffect(effect: EventEffect) {
            _collectedEffects.add(effect)
        }
    }
}