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
import sokeriaaa.return0.models.story.event.EventEffect
import sokeriaaa.return0.models.story.event.executedIn
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.shared.data.models.story.event.Event
import sokeriaaa.return0.shared.data.models.story.map.MapData
import sokeriaaa.return0.shared.data.models.story.map.MapEvent
import sokeriaaa.return0.test.annotations.AppRunner
import sokeriaaa.return0.test.annotations.RunWith
import sokeriaaa.return0.test.shared.common.helpers.FakeRandom
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AppRunner::class)
class EventExecutorTest : BaseEventTest() {

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
                lineNumber = Value(15)
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
            Event.TeleportUserTo(
                map = "another_map",
                lineNumber = Value(15)
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
            events = listOf(
                MapEvent(
                    enabled = CommonCondition.True,
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
                lineNumber = Value(12)
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
                change = Value(42)
            ).executedIn(context)
            assertEquals(
                expected = 42,
                actual = context.gameState.inventory["awesome_item"],
            )

            Event.InventoryChange(
                inventoryKey = "awesome_item",
                change = Value(42)
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
                change = Value(42)
            ).executedIn(context)
            assertEquals(
                expected = 42,
                actual = context.gameState.currency[CurrencyType.TOKEN],
            )

            Event.CurrencyChange(
                currency = CurrencyType.TOKEN,
                change = Value(42)
            ).executedIn(context)
            assertEquals(
                expected = 84,
                actual = context.gameState.currency[CurrencyType.TOKEN],
            )
        }
    }

    @Test
    fun `ObtainPlugin executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(
            callback = callback,
            // Generate a plugin with the constant of a random type +3%
            random = FakeRandom(3, 0, 0, 0)
        ) { context ->
            // FIXME: Handle the Context for Res.readBytes() or replace it.
            Event.ObtainPlugin(
                key = "growth_factor_array",
                tier = Value(1),
            ).executedIn(context)
            val pluginInfo = context.gameState.plugin.pluginMap.values.lastOrNull()
            assertEquals(3, pluginInfo?.constMap?.values?.firstOrNull())
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
                switch = CommonCondition.True,
            ).executedIn(context)
            assertTrue(
                context.gameState.savedValues.getSwitch("switch1")
            )
            Event.SaveSwitch(
                key = "switch1",
                switch = CommonCondition.False,
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
                variable = Value(42)
            ).executedIn(context)
            assertEquals(
                expected = 42,
                actual = context.gameState.savedValues.getVariable("var0"),
            )
            Event.SaveVariable(
                key = "var0",
                variable = Value(999)
            ).executedIn(context)
            assertEquals(
                expected = 999,
                actual = context.gameState.savedValues.getVariable("var0"),
            )
        }
    }

    @Test
    fun `ObtainEntity executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            assertEquals(null, context.gameState.entity.getEntityTable("foo"))

            Event.ObtainEntity(entityName = "foo", level = 42).executedIn(context)
            assertEquals(42, context.gameState.entity.getEntityTable("foo")?.level)
        }
    }


    @Test
    fun `ObtainEntity executes onDuplicate`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            Event.ObtainEntity(
                entityName = "foo",
                level = 42,
                // Current HP is 10.
                currentHP = 10,
                // Obtain "bar" on duplicate.
                onDuplicate = Event.ObtainEntity(
                    entityName = "bar",
                    level = 1,
                )
            ).executedIn(context)
            // Not duplicated.
            assertEquals(null, context.gameState.entity.getEntityTable("bar"))
            assertEquals(10, context.gameState.entity.getEntityTable("foo")?.currentHP)

            Event.ObtainEntity(
                entityName = "foo",
                level = 24,
                currentHP = 20,
                // Obtain "baz" on duplicate.
                onDuplicate = Event.ObtainEntity(
                    entityName = "baz",
                    level = 1,
                )
            ).executedIn(context)
            // Still level 42.
            assertEquals(42, context.gameState.entity.getEntityTable("foo")?.level)
            assertEquals(1, context.gameState.entity.getEntityTable("baz")?.level)
            // HP not replaced, even if the new one is higher.
            assertEquals(10, context.gameState.entity.getEntityTable("foo")?.currentHP)

            Event.ObtainEntity(
                entityName = "foo",
                level = 100,
                currentHP = 30,
                // Obtain "qux" on duplicate.
                onDuplicate = Event.ObtainEntity(
                    entityName = "qux",
                    level = 1,
                )
            ).executedIn(context)
            // Use the higher level.
            assertEquals(100, context.gameState.entity.getEntityTable("foo")?.level)
            assertEquals(1, context.gameState.entity.getEntityTable("qux")?.level)
            // HP not replaced, even if the new one is higher.
            assertEquals(10, context.gameState.entity.getEntityTable("foo")?.currentHP)

        }
    }

    @Test
    fun `ShowMap executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            Event.ShowMap.executedIn(context)
            assertEquals(
                expected = listOf(EventEffect.ShowMap),
                actual = callback.collectedEffects,
            )
        }
    }

    @Test
    fun `HideMap executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            Event.HideMap.executedIn(context)
            assertEquals(
                expected = listOf(EventEffect.HideMap),
                actual = callback.collectedEffects,
            )
        }
    }

    @Test
    fun `ShakeMap executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            Event.ShakeMap.executedIn(context)
            assertEquals(
                expected = listOf(EventEffect.ShakeMap),
                actual = callback.collectedEffects,
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
            context.gameState.entity.updateHP("foo", 50)
            context.gameState.entity.updateHP("bar", 50)
            context.gameState.entity.updateHP("baz", 50)
            registerTestingTeams(context)

            // Recover
            Event.RecoverAll.executedIn(context)

            // Get status
            val foo = context.gameState.entity.getEntityTable("foo")
            val bar = context.gameState.entity.getEntityTable("bar")
            val baz = context.gameState.entity.getEntityTable("baz")

            // Assert
            assertEquals(null, foo?.currentHP)
            assertEquals(null, bar?.currentHP)
            assertEquals(50, baz?.currentHP)
        }
    }

    @Test
    fun `ExecuteExtra executes correctly`() = runTest {
        val callback = object : TestingCallback() {
            override suspend fun waitForChoice(): Int {
                // Always choose the entity with index 1 ("bar" here)
                return 1
            }
        }
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo", level = 20)
            context.gameState.entity.obtainEntity("bar", level = 25)
            context.gameState.entity.updateHP("foo", 50)
            context.gameState.entity.updateHP("bar", 50)
            registerTestingTeams(context)

            // Heal 10 for bar
            Event.ExecuteExtra(
                extra = CombatExtra.HPChange(Value(10)),
                // If canceled, hide the map.
                onCanceled = Event.HideMap,
            ).executedIn(context)

            // Assert
            assertEquals(
                expected = 50,
                actual = context.gameState.entity.getEntityTable("foo")?.currentHP,
            )
            assertEquals(
                expected = 60,
                actual = context.gameState.entity.getEntityTable("bar")?.currentHP,
            )
            assertEquals(
                expected = listOf(EventEffect.ChooseEntity),
                actual = callback.collectedEffects,
            )
        }
    }

    @Test
    fun `ExecuteExtra cancels correctly`() = runTest {
        val callback = object : TestingCallback() {
            override suspend fun waitForChoice(): Int {
                // Always cancel the execution
                return -1
            }
        }
        withContext(callback = callback) { context ->
            // Initialize entities.
            registerTestingEntities(context)
            context.gameState.entity.obtainEntity("foo", level = 20)
            context.gameState.entity.obtainEntity("bar", level = 25)
            context.gameState.entity.updateHP("foo", 50)
            context.gameState.entity.updateHP("bar", 50)
            registerTestingTeams(context)

            // Heal 10
            Event.ExecuteExtra(
                extra = CombatExtra.HPChange(Value(10)),
                // If canceled, hide the map.
                onCanceled = Event.HideMap,
            ).executedIn(context)

            // Assert
            assertEquals(
                expected = 50,
                actual = context.gameState.entity.getEntityTable("foo")?.currentHP,
            )
            assertEquals(
                expected = 50,
                actual = context.gameState.entity.getEntityTable("bar")?.currentHP,
            )
            assertEquals(
                expected = listOf(EventEffect.ChooseEntity, EventEffect.HideMap),
                actual = callback.collectedEffects,
            )
        }
    }

    @Test
    fun `RequestSave executes correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            Event.RequestSave.executedIn(context)
            assertEquals(
                expected = listOf(EventEffect.RequestSave),
                actual = callback.collectedEffects,
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
            context.gameState.entity.updateHP("foo", 50)
            context.gameState.entity.updateHP("bar", 50)
            registerTestingTeams(context)

            // Create ArenaConfig.
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = listOf(
                        "baz" to Value(26),
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
                        "baz" to Value(26),
                    ),
                    additionalParties = listOf(
                        "baz" to Value(42),
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
                        "baz" to Value(26),
                    ),
                    additionalParties = listOf(
                        "quux" to Value(42),
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
                        "baz" to Value(26),
                    ),
                    additionalParties = listOf(
                        "qux" to Value(42),
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
                        "baz" to Value(26),
                    ),
                    additionalParties = listOf(
                        "quux" to Value(42),
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
                        "baz" to Value(26),
                    ),
                    additionalParties = listOf(
                        "foo" to Value(1),
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
            context.gameState.entity.updateHP("foo", 50)
            context.gameState.entity.updateHP("bar", 50)
            registerTestingTeams(context)

            // Create ArenaConfig.
            Event.Combat(
                config = Event.Combat.Config(
                    enemies = listOf(
                        "baz" to Value(26),
                    ),
                    statusOverride = mapOf(
                        "foo" to Event.Combat.Config.StatusOverride(
                            level = Value(100),
                            hp = Value(420),
                            sp = Value(500),
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
                expected = listOf(500, null),
                actual = arenaConfig.parties.map { it.currentSP },
            )
        }
    }

}