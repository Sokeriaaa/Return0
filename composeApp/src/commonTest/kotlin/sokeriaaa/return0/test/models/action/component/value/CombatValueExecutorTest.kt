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
package sokeriaaa.return0.test.models.action.component.value

import sokeriaaa.return0.models.action.component.value.calculatedIn
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.component.context.ActionExtraContext
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.shared.data.models.component.values.CombatValue
import sokeriaaa.return0.shared.data.models.component.values.EntityValue
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import sokeriaaa.return0.test.shared.common.helpers.assertFloatEquals
import kotlin.random.Random
import kotlin.test.Test

class CombatValueExecutorTest {
    /**
     * Create a context with a random damage result.,
     * For testing the EntityValue.
     */
    private fun createTestingContextWithRandomDamageResult(): ActionExtraContext {
        val user: Entity = DummyEntities.generateEntity(index = 0, name = "foo", baseHP = 99999)
        val target: Entity = DummyEntities.generateEntity(index = 1, name = "bar", baseHP = 99999)

        // Generate random damage result.
        val damageResult = ActionResult.Damage(
            fromIndex = user.index,
            toIndex = target.index,
            finalDamage = Random.nextInt(20, 1000),
            shieldedDamage = Random.nextInt(20, 1000),
            damageCoerced = Random.nextInt(20, 1000),
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
    fun `Combat_LoadValue calculates correctly`() {
        val context = createTestingContextWithRandomDamageResult()
        context.fromAction.values["test"] = 42F
        val loadValue = CombatValue.LoadValue("test")
        assertFloatEquals(42F, loadValue.calculatedIn(context))
    }

    @Test
    fun `Combat_ForUser calculates correctly`() {
        val context = createTestingContextWithRandomDamageResult()
        assertFloatEquals(
            context.user.baseATK.toFloat(),
            CombatValue.ForUser(EntityValue.BaseATK).calculatedIn(context)
        )
    }

    @Test
    fun `Combat_Swapped calculates correctly`() {
        val context = createTestingContextWithRandomDamageResult()
        assertFloatEquals(
            context.user.baseATK.toFloat(),
            CombatValue.Swapped(EntityValue.BaseATK).calculatedIn(context)
        )
    }

    @Test
    fun `Combat_Damage calculates correctly`() {
        val context = createTestingContextWithRandomDamageResult()
        assertFloatEquals(
            expected = context.attackDamageResult?.finalDamage?.toFloat() ?: 0F,
            actual = CombatValue.Damage.calculatedIn(context),
        )
        assertFloatEquals(
            expected = context.attackDamageResult?.damageCoerced?.toFloat() ?: 0F,
            actual = CombatValue.DamageCoerced.calculatedIn(context),
        )
        assertFloatEquals(
            expected = context.attackDamageResult?.shieldedDamage?.toFloat() ?: 0F,
            actual = CombatValue.ShieldedDamage.calculatedIn(context),
        )
    }
}