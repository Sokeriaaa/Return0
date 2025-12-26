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

import kotlinx.coroutines.test.runTest
import sokeriaaa.return0.models.story.event.EventContext
import sokeriaaa.return0.models.story.event.EventEffect
import sokeriaaa.return0.models.story.event.executedIn
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.shared.data.models.story.event.Event
import sokeriaaa.return0.shared.data.models.story.event.condition.EventCondition
import sokeriaaa.return0.shared.data.models.story.event.value.EventValue
import sokeriaaa.return0.shared.data.models.story.map.MapData
import sokeriaaa.return0.shared.data.models.story.map.MapEvent
import sokeriaaa.return0.test.annotations.AppRunner
import sokeriaaa.return0.test.annotations.RunWith
import sokeriaaa.return0.test.applib.modules.TestKoinModules
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AppRunner::class)
class EventExecutorTest {

    @Test
    fun `Text executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            Event.Text.Narrator(
                messageRes = "res.some.message"
            ).executedIn(context)
            assertEquals(
                expected = EventEffect.ShowText(
                    type = EventEffect.ShowText.Type.Narrator,
                    text = context.resources.getString("res.some.message")
                ),
                actual = callback.collectedEffects.lastOrNull(),
            )

            Event.Text.User(
                messageRes = "res.another.message"
            ).executedIn(context)
            assertEquals(
                expected = EventEffect.ShowText(
                    type = EventEffect.ShowText.Type.User,
                    text = context.resources.getString("res.another.message")
                ),
                actual = callback.collectedEffects.lastOrNull(),
            )

            Event.Text.NPC(
                nameRes = "res.a.cool.npc",
                messageRes = "res.hello.world"
            ).executedIn(context)
            assertEquals(
                expected = EventEffect.ShowText(
                    type = EventEffect.ShowText.Type.NPC(
                        context.resources.getString("res.a.cool.npc")
                    ),
                    text = context.resources.getString("res.hello.world")
                ),
                actual = callback.collectedEffects.lastOrNull(),
            )
        }
    }

    @Test
    fun `Choice executes correctly`() = runTest {
        val callback = object : TestingCallback() {
            override suspend fun waitForChoice(): Int {
                // We always choose the index 1 here.
                return 1
            }
        }
        withContext(callback = callback) { context ->
            Event.Choice(
                "foo" to Event.Text.Narrator("res.foo"),
                "bar" to Event.Text.Narrator("res.bar"),
                "baz" to Event.Text.Narrator("res.baz"),
            ).executedIn(context)

            assertEquals(
                expected = context.resources.getString("res.bar"),
                actual = (callback.collectedEffects.lastOrNull() as? EventEffect.ShowText)?.text,
            )
        }
    }

    @Test
    fun `Combat executes correctly`() = runTest {
        val callback = object : TestingCallback() {
            override suspend fun waitForCombatResult(): Boolean {
                // The combat is always victory.
                return true
            }
        }
        withContext(callback = callback) { context ->
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = emptyList(),
                ),
                success = Event.Text.Narrator("res.foo"),
                failure = Event.Text.Narrator("res.bar"),
            ).executedIn(context)

            assertTrue(callback.collectedEffects.getOrNull(0) is EventEffect.StartCombat)
            assertEquals(
                expected = context.resources.getString("res.foo"),
                actual = (callback.collectedEffects.getOrNull(1) as? EventEffect.ShowText)?.text,
            )
        }
    }

    @Test
    fun `MoveUserTo executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            Event.MoveUserTo(
                lineNumber = EventValue.Constant(15)
            ).executedIn(context)
            assertEquals(
                expected = 15,
                actual = (callback.collectedEffects.lastOrNull() as? EventEffect.MovePlayer)?.line,
            )
        }
    }

    @Test
    fun `TeleportUserTo executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // FIXME: Handle the Context for Res.readBytes() or replace it.
            Event.TeleportUserTo(
                map = "another_map",
                lineNumber = EventValue.Constant(15)
            ).executedIn(context)
            assertEquals(
                expected = 15,
                actual = (callback.collectedEffects.lastOrNull() as? EventEffect.TeleportPlayer)
                    ?.line,
            )
        }
    }

    @Test
    fun `TeleportThisEventTo executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        val mapData = MapData(
            name = "generated_${Random.nextInt(10000)}",
            lines = 100,
            buggyRange = emptyList(),
            buggyEntries = emptyList(),
            difficulty = 1,
            events = listOf(
                MapEvent(
                    enabled = EventCondition.True,
                    key = "event_key",
                    trigger = MapEvent.Trigger.INTERACTED,
                    lineNumber = 1,
                    event = Event.Empty
                )
            ),
        )
        withContext(
            key = "event_key",
            callback = callback
        ) { context ->
            Event.TeleportThisEventTo(
                lineNumber = EventValue.Constant(12)
            ).executedIn(context)
            assertTrue(
                context.gameState.map.loadEvents(mapData = mapData)
                    .any { it.key == "event_key" && it.lineNumber == 12 }
            )
        }
    }

    @Test
    fun `InventoryChange executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            Event.InventoryChange(
                inventoryKey = "awesome_item",
                change = EventValue.Constant(42)
            ).executedIn(context)
            assertEquals(
                expected = 42,
                actual = context.gameState.inventory["awesome_item"],
            )

            Event.InventoryChange(
                inventoryKey = "awesome_item",
                change = EventValue.Constant(42)
            ).executedIn(context)
            assertEquals(
                expected = 84,
                actual = context.gameState.inventory["awesome_item"],
            )
        }
    }

    @Test
    fun `CurrencyChange executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            Event.CurrencyChange(
                currency = CurrencyType.TOKEN,
                change = EventValue.Constant(42)
            ).executedIn(context)
            assertEquals(
                expected = 42,
                actual = context.gameState.currency[CurrencyType.TOKEN],
            )

            Event.CurrencyChange(
                currency = CurrencyType.TOKEN,
                change = EventValue.Constant(42)
            ).executedIn(context)
            assertEquals(
                expected = 84,
                actual = context.gameState.currency[CurrencyType.TOKEN],
            )
        }
    }

    @Test
    fun `Quest executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            assertFalse(
                context.gameState.quest.isCompleted("an_epic_quest")
            )

            Event.ClaimQuest(
                key = "an_epic_quest"
            ).executedIn(context)
            assertFalse(
                context.gameState.quest.isCompleted("an_epic_quest")
            )

            Event.CompleteQuest(
                key = "an_epic_quest"
            ).executedIn(context)
            assertTrue(
                context.gameState.quest.isCompleted("an_epic_quest")
            )
        }
    }

    @Test
    fun `SaveSwitch executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            assertFalse(
                context.gameState.savedValues.getSwitch("switch1")
            )
            Event.SaveSwitch(
                key = "switch1",
                switch = EventCondition.True,
            ).executedIn(context)
            assertTrue(
                context.gameState.savedValues.getSwitch("switch1")
            )
            Event.SaveSwitch(
                key = "switch1",
                switch = EventCondition.False,
            ).executedIn(context)
            assertFalse(
                context.gameState.savedValues.getSwitch("switch1")
            )
        }
    }

    @Test
    fun `SaveVariable executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            assertEquals(
                expected = 0,
                actual = context.gameState.savedValues.getVariable("var0"),
            )
            Event.SaveVariable(
                key = "var0",
                variable = EventValue.Constant(42)
            ).executedIn(context)
            assertEquals(
                expected = 42,
                actual = context.gameState.savedValues.getVariable("var0"),
            )
            Event.SaveVariable(
                key = "var0",
                variable = EventValue.Constant(999)
            ).executedIn(context)
            assertEquals(
                expected = 999,
                actual = context.gameState.savedValues.getVariable("var0"),
            )
        }
    }

    @Test
    fun `RecoverAll executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo")
            context.gameState.entity.obtainEntity("bar")
            context.gameState.entity.obtainEntity("baz")
            context.gameState.entity.updateHPAndSP("foo", 50, 50)
            context.gameState.entity.updateHPAndSP("bar", 50, 50)
            context.gameState.entity.updateHPAndSP("baz", 50, 50)
            registerTestingTeams(context)

            // Recover
            Event.RecoverAll.executedIn(context)

            // Get status
            val foo = context.gameState.entity.getEntityTable("foo")
            val bar = context.gameState.entity.getEntityTable("bar")
            val baz = context.gameState.entity.getEntityTable("baz")

            // Assert
            assertEquals(null, foo?.currentHP)
            assertEquals(null, foo?.currentSP)
            assertEquals(null, bar?.currentHP)
            assertEquals(null, bar?.currentSP)
            assertEquals(50, baz?.currentHP)
            assertEquals(50, baz?.currentSP)
        }
    }

    @Test
    fun `RequestSave executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            Event.RequestSave.executedIn(context)
            assertTrue(
                callback.collectedEffects.lastOrNull() is EventEffect.RequestSave,
            )
        }
    }

    // Test Event.Combat.Config -> ArenaConfig
    @Test
    fun `ArenaConfig assembled correctly - common`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo", level = 20)
            context.gameState.entity.obtainEntity("bar", level = 25)
            context.gameState.entity.updateHPAndSP("foo", 50, 42)
            context.gameState.entity.updateHPAndSP("bar", 50, 42)
            registerTestingTeams(context)

            // Create ArenaConfig.
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = listOf(
                        "baz" to EventValue.Constant(26),
                    )
                ),
                success = Event.Empty,
                failure = Event.Empty,
            ).executedIn(context)
            assertTrue(callback.collectedEffects.firstOrNull() is EventEffect.StartCombat)

            // assert
            val arenaConfig = (callback.collectedEffects.first() as EventEffect.StartCombat).config
            assertEquals(
                expected = listOf("foo" to 20, "bar" to 25),
                actual = arenaConfig.parties.map { it.entityData.name to it.level },
            )
            assertEquals(
                expected = listOf(50, 50),
                actual = arenaConfig.parties.map { it.currentHP },
            )
            assertEquals(
                expected = listOf(42, 42),
                actual = arenaConfig.parties.map { it.currentSP },
            )
            assertEquals(
                expected = listOf("baz" to 26),
                actual = arenaConfig.enemies.map { it.entityData.name to it.level },
            )
        }
    }

    @Test
    fun `ArenaConfig assembled correctly - additional`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo", level = 20)
            context.gameState.entity.obtainEntity("bar", level = 25)
            registerTestingTeams(context)

            // Create ArenaConfig.
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = listOf(
                        "baz" to EventValue.Constant(26),
                    ),
                    additionalParties = listOf(
                        "baz" to EventValue.Constant(42),
                    )
                ),
                success = Event.Empty,
                failure = Event.Empty,
            ).executedIn(context)
            assertTrue(callback.collectedEffects.firstOrNull() is EventEffect.StartCombat)

            // assert
            val arenaConfig = (callback.collectedEffects.first() as EventEffect.StartCombat).config
            assertEquals(
                expected = listOf("foo" to 20, "bar" to 25, "baz" to 42),
                actual = arenaConfig.parties.map { it.entityData.name to it.level },
            )
        }
    }

    @Test
    fun `ArenaConfig assembled correctly - additional - replacing`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo", level = 20)
            context.gameState.entity.obtainEntity("bar", level = 25)
            context.gameState.entity.obtainEntity("baz", level = 30)
            context.gameState.entity.obtainEntity("qux", level = 35)
            registerFullTestingTeams(context)

            // Create ArenaConfig.
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = listOf(
                        "baz" to EventValue.Constant(26),
                    ),
                    additionalParties = listOf(
                        "quux" to EventValue.Constant(42),
                    )
                ),
                success = Event.Empty,
                failure = Event.Empty,
            ).executedIn(context)
            assertTrue(callback.collectedEffects.firstOrNull() is EventEffect.StartCombat)

            // assert
            val arenaConfig = (callback.collectedEffects.first() as EventEffect.StartCombat).config
            assertEquals(
                expected = listOf("foo" to 20, "bar" to 25, "baz" to 30, "quux" to 42),
                actual = arenaConfig.parties.map { it.entityData.name to it.level },
            )
        }
    }

    @Test
    fun `ArenaConfig assembled correctly - additional - replacingSame`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo", level = 20)
            context.gameState.entity.obtainEntity("bar", level = 25)
            context.gameState.entity.obtainEntity("baz", level = 30)
            context.gameState.entity.obtainEntity("qux", level = 35)
            registerFullTestingTeams(context)

            // Create ArenaConfig.
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = listOf(
                        "baz" to EventValue.Constant(26),
                    ),
                    additionalParties = listOf(
                        "qux" to EventValue.Constant(42),
                    )
                ),
                success = Event.Empty,
                failure = Event.Empty,
            ).executedIn(context)
            assertTrue(callback.collectedEffects.firstOrNull() is EventEffect.StartCombat)

            // assert
            val arenaConfig = (callback.collectedEffects.first() as EventEffect.StartCombat).config
            assertEquals(
                expected = listOf("foo" to 20, "bar" to 25, "baz" to 30, "qux" to 42),
                actual = arenaConfig.parties.map { it.entityData.name to it.level },
            )
        }
    }

    @Test
    fun `ArenaConfig assembled correctly - additional - onlyAdditional`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo", level = 20)
            context.gameState.entity.obtainEntity("bar", level = 25)
            context.gameState.entity.obtainEntity("baz", level = 30)
            context.gameState.entity.obtainEntity("qux", level = 35)
            registerFullTestingTeams(context)

            // Create ArenaConfig.
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = listOf(
                        "baz" to EventValue.Constant(26),
                    ),
                    additionalParties = listOf(
                        "quux" to EventValue.Constant(42),
                    ),
                    useOnlyAdditional = true,
                ),
                success = Event.Empty,
                failure = Event.Empty,
            ).executedIn(context)
            assertTrue(callback.collectedEffects.firstOrNull() is EventEffect.StartCombat)

            // assert
            val arenaConfig = (callback.collectedEffects.first() as EventEffect.StartCombat).config
            assertEquals(
                expected = listOf("quux" to 42),
                actual = arenaConfig.parties.map { it.entityData.name to it.level },
            )
        }
    }

    @Test
    fun `ArenaConfig assembled correctly - additional - onlyAdditional - level`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo", level = 20)
            context.gameState.entity.obtainEntity("bar", level = 25)
            context.gameState.entity.obtainEntity("baz", level = 30)
            context.gameState.entity.obtainEntity("qux", level = 35)
            registerFullTestingTeams(context)

            // Create ArenaConfig.
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = listOf(
                        "baz" to EventValue.Constant(26),
                    ),
                    additionalParties = listOf(
                        "foo" to EventValue.Constant(1),
                    ),
                    useOnlyAdditional = true,
                ),
                success = Event.Empty,
                failure = Event.Empty,
            ).executedIn(context)
            assertTrue(callback.collectedEffects.firstOrNull() is EventEffect.StartCombat)

            // assert
            val arenaConfig = (callback.collectedEffects.first() as EventEffect.StartCombat).config
            assertEquals(
                expected = listOf("foo" to 1),
                actual = arenaConfig.parties.map { it.entityData.name to it.level },
            )
        }
    }

    @Test
    fun `ArenaConfig assembled correctly - override`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo", level = 20)
            context.gameState.entity.obtainEntity("bar", level = 25)
            context.gameState.entity.updateHPAndSP("foo", 50, 42)
            context.gameState.entity.updateHPAndSP("bar", 50, 42)
            registerTestingTeams(context)

            // Create ArenaConfig.
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = listOf(
                        "baz" to EventValue.Constant(26),
                    ),
                    statusOverride = mapOf(
                        "foo" to Event.Combat.Config.StatusOverride(
                            level = EventValue.Constant(100),
                            hp = EventValue.Constant(420),
                            sp = EventValue.Constant(500),
                        )
                    )
                ),
                success = Event.Empty,
                failure = Event.Empty,
            ).executedIn(context)
            assertTrue(callback.collectedEffects.firstOrNull() is EventEffect.StartCombat)

            // assert
            val arenaConfig = (callback.collectedEffects.first() as EventEffect.StartCombat).config
            assertEquals(
                expected = listOf("foo" to 100, "bar" to 25),
                actual = arenaConfig.parties.map { it.entityData.name to it.level },
            )
            assertEquals(
                expected = listOf(420, 50),
                actual = arenaConfig.parties.map { it.currentHP },
            )
            assertEquals(
                expected = listOf(500, 42),
                actual = arenaConfig.parties.map { it.currentSP },
            )
        }
    }

    private inline fun withContext(
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
    private fun registerTestingEntities(context: EventContext) {
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
    private suspend fun registerTestingTeams(context: EventContext) {
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
    private suspend fun registerFullTestingTeams(context: EventContext) {
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
    private abstract class TestingCallback : EventContext.Callback {
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