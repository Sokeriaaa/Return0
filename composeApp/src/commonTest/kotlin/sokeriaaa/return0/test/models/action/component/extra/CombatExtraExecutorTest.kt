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
package sokeriaaa.return0.test.models.action.component.extra

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.ArchiveRepo
import sokeriaaa.return0.models.action.Action
import sokeriaaa.return0.models.action.component.extra.ActionExtraContext
import sokeriaaa.return0.models.action.component.extra.executedIn
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.test.applib.modules.TestKoinModules
import sokeriaaa.return0.test.models.action.effect.DummyEffects
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import sokeriaaa.return0.test.shared.common.helpers.assertFloatEquals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CombatExtraExecutorTest : KoinComponent {

    private fun createTestingContext(
        user: Entity = DummyEntities.generateEntity(
            index = 0,
            name = "foo",
            baseHP = 99999,
            baseSP = 99999,
            baseAP = 100,
        ),
        target: Entity = DummyEntities.generateEntity(
            index = 1,
            name = "bar",
            baseHP = 99999,
            baseSP = 99999,
            baseAP = 100,
        ),
        action: (user: Entity) -> Action = {
            it.generateFunctionFor(DummyFunction.generateFunctionData())!!
        },
        attackDamageResult: ActionResult.Damage? = null,
    ) = ActionExtraContext(
        fromAction = action(user),
        user = user,
        target = target,
        attackDamageResult = attackDamageResult,
    )

    @Test
    fun `Combat_HPChange executes correctly`() {
        val context = createTestingContext()
        context.target.hp = 1000
        // Damage 100
        CombatExtra.HPChange(Value(-100)).executedIn(context)
        assertEquals(900, context.target.hp)
        // Heal 150
        CombatExtra.HPChange(Value(150)).executedIn(context)
        assertEquals(1050, context.target.hp)
        // HP Coerced
        CombatExtra.HPChange(Value(Int.MIN_VALUE)).executedIn(context)
        assertEquals(0, context.target.hp)
        CombatExtra.HPChange(Value(Int.MAX_VALUE)).executedIn(context)
        assertEquals(context.target.maxhp, context.target.hp)
    }

    @Test
    fun `Combat_HPChange integrates with shields correctly`() {
        val context = createTestingContext()
        context.target.hp = 1000
        // Shield 150
        context.target.attachShield("shield", 150)
        // Damage 100
        CombatExtra.HPChange(Value(-100)).executedIn(context)
        assertEquals(1000, context.target.hp)
        assertEquals(50, context.target.shields["shield"]?.value)
        // Damage 100 that ignores shields
        CombatExtra.HPChange(
            hpChange = Value(-100),
            ignoresShield = CommonCondition.True,
        ).executedIn(context)
        assertEquals(900, context.target.hp)
        assertEquals(50, context.target.shields["shield"]?.value)
    }

    @Test
    fun `Combat_SPChange executes correctly`() {
        val context = createTestingContext()
        context.target.sp = 100
        // SP -10
        CombatExtra.SPChange(Value(-10)).executedIn(context)
        assertEquals(90, context.target.sp)
        // SP +15
        CombatExtra.SPChange(Value(15)).executedIn(context)
        assertEquals(105, context.target.sp)
        // SP Coerced
        CombatExtra.SPChange(Value(Int.MIN_VALUE)).executedIn(context)
        assertEquals(0, context.target.sp)
        CombatExtra.SPChange(Value(Int.MAX_VALUE)).executedIn(context)
        assertEquals(context.target.maxsp, context.target.sp)
    }

    @Test
    fun `Combat_APChange executes correctly`() {
        val context = createTestingContext()
        context.target.ap = 50F
        // AP -10
        CombatExtra.APChange(Value(-10)).executedIn(context)
        assertFloatEquals(40F, context.target.ap)
        // AP +15
        CombatExtra.APChange(Value(15)).executedIn(context)
        assertFloatEquals(55F, context.target.ap)

        // AP can break the upper and lower limits by the extras.
        CombatExtra.APChange(Value(-100)).executedIn(context)
        assertFloatEquals(-45F, context.target.ap)
        CombatExtra.APChange(Value(1000)).executedIn(context)
        assertFloatEquals(955F, context.target.ap)
    }

    @Test
    fun `Combat_Effects executes correctly`() {
        val context = createTestingContext()
        val foo = DummyEffects.generateEffectData(name = "foo", isDebuff = true)
        val bar = DummyEffects.generateEffectData(name = "bar", isDebuff = false)
        val baz = DummyEffects.generateEffectData(name = "baz", isDebuff = false)
        TestKoinModules.withModules {
            // Register effects
            val archiveRepo: ArchiveRepo by inject()
            archiveRepo.registerEffects(listOf(foo, bar, baz))

            // Attach effects
            CombatExtra.AttachEffect(name = foo.name, tier = Value(1), turns = Value(1))
                .executedIn(context)
            CombatExtra.AttachEffect(name = bar.name, tier = Value(2), turns = Value(2))
                .executedIn(context)
            assertEquals(
                expected = 1,
                actual = context.target.effects.firstOrNull { it.name == foo.name }?.turnsLeft,
            )
            assertEquals(
                expected = 2,
                actual = context.target.effects.firstOrNull { it.name == bar.name }?.turnsLeft,
            )
            assertEquals(
                expected = null,
                actual = context.target.effects.firstOrNull { it.name == baz.name }?.turnsLeft,
            )

            // Remove Effect
            CombatExtra.RemoveEffect(name = foo.name).executedIn(context)
            assertEquals(
                expected = null,
                actual = context.target.effects.firstOrNull { it.name == foo.name }?.turnsLeft,
            )
            assertEquals(
                expected = 2,
                actual = context.target.effects.firstOrNull { it.name == bar.name }?.turnsLeft,
            )
            assertEquals(
                expected = null,
                actual = context.target.effects.firstOrNull { it.name == baz.name }?.turnsLeft,
            )

            // Remove all effects
            fun testRemoveAll(buff: Boolean, debuff: Boolean) {
                // Add all 3 effects.
                CombatExtra.AttachEffect(name = foo.name, tier = Value(1), turns = Value(1))
                    .executedIn(context)
                CombatExtra.AttachEffect(name = bar.name, tier = Value(2), turns = Value(2))
                    .executedIn(context)
                CombatExtra.AttachEffect(name = baz.name, tier = Value(3), turns = Value(3))
                    .executedIn(context)
                // Execute remove all
                CombatExtra.RemoveAllEffect(buff = buff, debuff = debuff).executedIn(context)
                // Assert
                assertEquals(
                    expected = if (debuff) null else 1,
                    actual = context.target.effects.firstOrNull { it.name == foo.name }?.turnsLeft,
                )
                assertEquals(
                    expected = if (buff) null else 2,
                    actual = context.target.effects.firstOrNull { it.name == bar.name }?.turnsLeft,
                )
                assertEquals(
                    expected = if (buff) null else 3,
                    actual = context.target.effects.firstOrNull { it.name == baz.name }?.turnsLeft,
                )
                // Clean up effects from entity.
                CombatExtra.RemoveAllEffect(buff = true, debuff = true).executedIn(context)
            }

            testRemoveAll(buff = true, debuff = true)
            testRemoveAll(buff = true, debuff = false)
            testRemoveAll(buff = false, debuff = true)
            testRemoveAll(buff = false, debuff = false)

            // Clean up repo.
            archiveRepo.reset()
        }
    }

    @Test
    fun `Combat_Shields executes correctly`() {
        val context = createTestingContext()

        // Attach shields
        CombatExtra.AttachShield(key = "foo", value = Value(111)).executedIn(context)
        CombatExtra.AttachShield(key = "bar", value = Value(222)).executedIn(context)
        CombatExtra.AttachShield(key = "baz", value = Value(333)).executedIn(context)
        assertEquals(111, context.target.shields["foo"]?.value)
        assertEquals(222, context.target.shields["bar"]?.value)
        assertEquals(333, context.target.shields["baz"]?.value)

        // Remove one shield
        CombatExtra.RemoveShield("bar").executedIn(context)
        assertEquals(111, context.target.shields["foo"]?.value)
        assertFalse(context.target.shields.containsKey("bar"))
        assertEquals(333, context.target.shields["baz"]?.value)

        // Remove all shields
        CombatExtra.RemoveAllShields.executedIn(context)
        assertFalse(context.target.shields.containsKey("foo"))
        assertFalse(context.target.shields.containsKey("bar"))
        assertFalse(context.target.shields.containsKey("baz"))
    }
}