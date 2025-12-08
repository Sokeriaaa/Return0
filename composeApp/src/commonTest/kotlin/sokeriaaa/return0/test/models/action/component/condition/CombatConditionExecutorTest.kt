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
package sokeriaaa.return0.test.models.action.component.condition

import sokeriaaa.return0.models.action.Action
import sokeriaaa.return0.models.action.component.condition.calculatedIn
import sokeriaaa.return0.models.action.component.extra.ActionExtraContext
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.component.conditions.CombatCondition
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CombatConditionExecutorTest {
    /**
     * Create a context with a random damage result.,
     * For testing the EntityValue.
     */
    private fun createTestingContextWithRandomDamageResult(): ActionExtraContext {
        val user: Entity = DummyEntities.generateEntity(index = 0, name = "foo", baseHP = 99999)
        val target: Entity = DummyEntities.generateEntity(index = 1, name = "bar", baseHP = 99999)

        // Generate mock HP and Damage
        val hp = Random.nextInt(10, 500)
        val damage = Random.nextInt(20, 1000)
        target.hp = (hp - damage).coerceAtLeast(0)

        val damageResult = ActionResult.Damage(
            fromIndex = user.index,
            toIndex = target.index,
            finalDamage = damage,
            shieldedDamage = 0,
            damageCoerced = damage.coerceAtMost(hp),
            effectiveness = 0,
            isCritical = false,
        )

        return ActionExtraContext(
            fromAction = user.generateFunctionFor(DummyFunction.generateFunctionData())!!,
            user = user,
            target = target,
            attackDamageResult = damageResult,
        )
    }

    @Test
    fun `Combat_IfCritical calculated correctly`() {
        val user: Entity = DummyEntities.generateEntity(index = 0, name = "foo", baseHP = 99999)
        val target: Entity = DummyEntities.generateEntity(index = 1, name = "bar", baseHP = 99999)
        val action: Action = user.generateFunctionFor(DummyFunction.generateFunctionData())!!
        val nonCritical = ActionResult.Damage(
            fromIndex = user.index,
            toIndex = target.index,
            finalDamage = 42,
            shieldedDamage = 0,
            damageCoerced = 42,
            effectiveness = 0,
            isCritical = false,
        )
        assertFalse(
            CombatCondition.Critical.calculatedIn(
                context = ActionExtraContext(
                    fromAction = action,
                    user = user,
                    target = target,
                    attackDamageResult = nonCritical,
                )
            )
        )
        val critical = ActionResult.Damage(
            fromIndex = user.index,
            toIndex = target.index,
            finalDamage = 42,
            shieldedDamage = 0,
            damageCoerced = 42,
            effectiveness = 0,
            isCritical = true,
        )
        assertTrue(
            CombatCondition.Critical.calculatedIn(
                context = ActionExtraContext(
                    fromAction = action,
                    user = user,
                    target = target,
                    attackDamageResult = critical,
                )
            )
        )
    }
}