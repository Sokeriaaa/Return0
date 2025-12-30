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
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.component.context.ActionExtraContext
import sokeriaaa.return0.models.component.executor.condition.calculatedIn
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.api.component.condition.and
import sokeriaaa.return0.shared.data.api.component.condition.eq
import sokeriaaa.return0.shared.data.api.component.condition.gt
import sokeriaaa.return0.shared.data.api.component.condition.gtEq
import sokeriaaa.return0.shared.data.api.component.condition.lt
import sokeriaaa.return0.shared.data.api.component.condition.ltEq
import sokeriaaa.return0.shared.data.api.component.condition.neq
import sokeriaaa.return0.shared.data.api.component.condition.not
import sokeriaaa.return0.shared.data.api.component.condition.or
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import sokeriaaa.return0.test.shared.common.helpers.FakeRandom
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test condition executors.
 */
class CommonConditionExecutorTest {

    private fun createTestingContext(
        user: Entity = DummyEntities.generateEntity(index = 0, name = "foo"),
        target: Entity = DummyEntities.generateEntity(index = 1, name = "bar"),
        action: (user: Entity) -> Action = {
            it.generateFunctionFor(DummyFunction.generateFunctionData())!!
        },
        random: Random = Random,
        attackDamageResult: ActionResult.Damage? = null,
    ) = ActionExtraContext(
        fromAction = action(user),
        user = user,
        target = target,
        random = random,
        attackDamageResult = attackDamageResult,
    )

    @Test
    fun `Common_True calculates correctly`() {
        val context = createTestingContext()
        assertTrue(CommonCondition.True.calculatedIn(context))
    }

    @Test
    fun `Common_False calculates correctly`() {
        val context = createTestingContext()
        assertFalse(CommonCondition.False.calculatedIn(context))
    }

    @Test
    fun `Common_And calculates correctly`() {
        val context = createTestingContext()
        assertTrue((CommonCondition.True and CommonCondition.True).calculatedIn(context))
        assertFalse((CommonCondition.True and CommonCondition.False).calculatedIn(context))
        assertFalse((CommonCondition.False and CommonCondition.True).calculatedIn(context))
        assertFalse((CommonCondition.False and CommonCondition.False).calculatedIn(context))

        assertTrue(CommonCondition.And().calculatedIn(context))
        assertTrue(
            CommonCondition.And(
                CommonCondition.True,
                CommonCondition.True,
                CommonCondition.True,
                CommonCondition.True,
                CommonCondition.True,
            ).calculatedIn(context)
        )
        assertFalse(
            CommonCondition.And(
                CommonCondition.True,
                CommonCondition.True,
                CommonCondition.True,
                CommonCondition.True,
                CommonCondition.False,
            ).calculatedIn(context)
        )
    }

    @Test
    fun `Common_Or calculates correctly`() {
        val context = createTestingContext()
        assertTrue((CommonCondition.True or CommonCondition.True).calculatedIn(context))
        assertTrue((CommonCondition.True or CommonCondition.False).calculatedIn(context))
        assertTrue((CommonCondition.False or CommonCondition.True).calculatedIn(context))
        assertFalse((CommonCondition.False or CommonCondition.False).calculatedIn(context))

        assertFalse(CommonCondition.Or().calculatedIn(context))
        assertFalse(
            CommonCondition.Or(
                CommonCondition.False,
                CommonCondition.False,
                CommonCondition.False,
                CommonCondition.False,
                CommonCondition.False,
            ).calculatedIn(context)
        )
        assertTrue(
            CommonCondition.Or(
                CommonCondition.True,
                CommonCondition.False,
                CommonCondition.False,
                CommonCondition.False,
                CommonCondition.False,
            ).calculatedIn(context)
        )
    }

    @Test
    fun `Common_Not calculates correctly`() {
        val context = createTestingContext()
        assertFalse((!CommonCondition.True).calculatedIn(context))
        assertTrue((!CommonCondition.False).calculatedIn(context))
    }

    @Test
    fun `Common_Compare calculates correctly`() {
        val context = createTestingContext()
        assertTrue((Value(42) gt Value(40)).calculatedIn(context))
        assertFalse((Value(42) gt Value(42)).calculatedIn(context))
        assertFalse((Value(42) gt Value(44)).calculatedIn(context))
        assertTrue((Value(42) gt 40).calculatedIn(context))
        assertFalse((Value(42) gt 42).calculatedIn(context))
        assertFalse((Value(42) gt 44).calculatedIn(context))
        assertTrue((Value(42) gt 40F).calculatedIn(context))
        assertFalse((Value(42) gt 42F).calculatedIn(context))
        assertFalse((Value(42) gt 44F).calculatedIn(context))

        assertTrue((Value(42) gtEq Value(40)).calculatedIn(context))
        assertTrue((Value(42) gtEq Value(42)).calculatedIn(context))
        assertFalse((Value(42) gtEq Value(44)).calculatedIn(context))
        assertTrue((Value(42) gtEq 40).calculatedIn(context))
        assertTrue((Value(42) gtEq 42).calculatedIn(context))
        assertFalse((Value(42) gtEq 44).calculatedIn(context))
        assertTrue((Value(42) gtEq 40F).calculatedIn(context))
        assertTrue((Value(42) gtEq 42F).calculatedIn(context))
        assertFalse((Value(42) gtEq 44F).calculatedIn(context))

        assertFalse((Value(42) lt Value(40)).calculatedIn(context))
        assertFalse((Value(42) lt Value(42)).calculatedIn(context))
        assertTrue((Value(42) lt Value(44)).calculatedIn(context))
        assertFalse((Value(42) lt 40).calculatedIn(context))
        assertFalse((Value(42) lt 42).calculatedIn(context))
        assertTrue((Value(42) lt 44).calculatedIn(context))
        assertFalse((Value(42) lt 40F).calculatedIn(context))
        assertFalse((Value(42) lt 42F).calculatedIn(context))
        assertTrue((Value(42) lt 44F).calculatedIn(context))

        assertFalse((Value(42) ltEq Value(40)).calculatedIn(context))
        assertTrue((Value(42) ltEq Value(42)).calculatedIn(context))
        assertTrue((Value(42) ltEq Value(44)).calculatedIn(context))
        assertFalse((Value(42) ltEq 40).calculatedIn(context))
        assertTrue((Value(42) ltEq 42).calculatedIn(context))
        assertTrue((Value(42) ltEq 44).calculatedIn(context))
        assertFalse((Value(42) ltEq 40F).calculatedIn(context))
        assertTrue((Value(42) ltEq 42F).calculatedIn(context))
        assertTrue((Value(42) ltEq 44F).calculatedIn(context))

        assertTrue((Value(42) eq Value(42)).calculatedIn(context))
        assertFalse((Value(42) eq Value(40)).calculatedIn(context))
        assertTrue((Value(42) eq 42).calculatedIn(context))
        assertFalse((Value(42) eq 40).calculatedIn(context))
        assertTrue((Value(42) eq 42F).calculatedIn(context))
        assertFalse((Value(42) eq 40F).calculatedIn(context))

        assertFalse((Value(42) neq Value(42)).calculatedIn(context))
        assertTrue((Value(42) neq Value(40)).calculatedIn(context))
        assertFalse((Value(42) neq 42).calculatedIn(context))
        assertTrue((Value(42) neq 40).calculatedIn(context))
        assertFalse((Value(42) neq 42F).calculatedIn(context))
        assertTrue((Value(42) neq 40F).calculatedIn(context))
    }

    @Test
    fun `Common_Chance calculates correctly 1`() {
        val context = createTestingContext(random = FakeRandom(0.2F))
        assertTrue(CommonCondition.Chance(0.5F).calculatedIn(context))
    }

    @Test
    fun `Common_Chance calculates correctly 2`() {
        val context = createTestingContext(random = FakeRandom(0.8F))
        assertFalse(CommonCondition.Chance(0.5F).calculatedIn(context))
    }
}