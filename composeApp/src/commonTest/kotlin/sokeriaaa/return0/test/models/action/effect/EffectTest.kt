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
package sokeriaaa.return0.test.models.action.effect

import sokeriaaa.return0.models.action.effect.generateEffectFor
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.test.models.entity.DummyEntities
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EffectTest {

    @Test
    fun `effect with a higher tier is stronger`() {
        val user = DummyEntities.generateEntity()
        val effectData = DummyEffects.generateEffectData()
        val effect1 = user.generateEffectFor(effectData, 1, 10)
        val effect2 = user.generateEffectFor(effectData, 2, 1)
        assertTrue(effect2 > effect1)
    }

    @Test
    fun `effect with longer turns left is stronger`() {
        val user = DummyEntities.generateEntity()
        val effectData = DummyEffects.generateEffectData()
        val effect1 = user.generateEffectFor(effectData, 1, 5)
        val effect2 = user.generateEffectFor(effectData, 1, 10)
        assertTrue(effect2 > effect1)

        effect2.turnsLeft = 4
        assertTrue(effect2 < effect1)
    }

    @Test
    fun `different effects cannot be compared`() {
        val user = DummyEntities.generateEntity()
        val effect1 = user.generateEffectFor(
            effectData = DummyEffects.generateEffectData(name = "foo"),
            tier = 1,
            turns = 5,
        )
        val effect2 = user.generateEffectFor(
            effectData = DummyEffects.generateEffectData(name = "bar"),
            tier = 1,
            turns = 10,
        )
        assertFails { effect1.compareTo(effect2) }
    }

    @Test
    fun `effect applied on the entity`() {
        val user1 = DummyEntities.generateEntity(name = "foo")
        val user2 = DummyEntities.generateEntity(name = "bar")
        user1.hp = 100
        user2.hp = 100
        // Create a HP draining effect that drains the HP for 10 after the entity taking action.
        val hpDraining = user1.generateEffectFor(
            effectData = DummyEffects.generateEffectData(
                extra = CombatExtra.HPChange(hpChange = Value(-10))
            ),
            tier = 1,
            turns = 5,
        )

        hpDraining.applyOn(user2)
        assertEquals(90, user2.hp)
    }

    @Test
    fun `skip the first turn for self-attached effects`() {
        val user = DummyEntities.generateEntity(baseHP = 99999)
        user.hp = 50

        // Create a HP recovering effect that recovers the HP for 10 after the entity taking action.
        val hpRecover = user.generateEffectFor(
            effectData = DummyEffects.generateEffectData(
                extra = CombatExtra.HPChange(hpChange = Value(10))
            ),
            tier = 1,
            turns = 5,
        )

        // Skip the first turn
        hpRecover.applyOn(user)
        assertEquals(50, user.hp)

        // Applied normally.
        hpRecover.applyOn(user)
        assertEquals(60, user.hp)
    }

    @Test
    fun `remove effects after turns left goes to 0`() {
        val user1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
        val user2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
        user1.hp = 1000
        user2.hp = 1000
        // Create a HP draining effect that drains the HP for 10 after the entity taking action.
        val hpDraining = user1.generateEffectFor(
            effectData = DummyEffects.generateEffectData(
                extra = CombatExtra.HPChange(hpChange = Value(-10))
            ),
            tier = 1,
            turns = 2,
        )
        user2.attachEffect(hpDraining)

        // first turn (1 left)
        val result1 = hpDraining.applyOn(user2)
        assertEquals(1, hpDraining.turnsLeft)
        assertTrue(user2.effects.contains(hpDraining))
        assertTrue(result1.none { it is ActionResult.RemoveEffect })

        // second turn (0 left, removed)
        val result2 = hpDraining.applyOn(user2)
        assertEquals(0, hpDraining.turnsLeft)
        assertFalse(user2.effects.contains(hpDraining))
        assertTrue(result2.any { (it as? ActionResult.RemoveEffect)?.effectName == hpDraining.name })
    }

}