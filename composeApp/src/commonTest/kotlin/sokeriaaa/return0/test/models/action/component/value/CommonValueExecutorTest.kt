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

import kotlinx.coroutines.test.runTest
import sokeriaaa.return0.models.action.Action
import sokeriaaa.return0.models.action.component.value.calculatedIn
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.component.context.ActionExtraContext
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.api.component.condition.IF
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.api.component.value.abs
import sokeriaaa.return0.shared.data.api.component.value.absoluteValue
import sokeriaaa.return0.shared.data.api.component.value.coerceAtLeast
import sokeriaaa.return0.shared.data.api.component.value.coerceAtMost
import sokeriaaa.return0.shared.data.api.component.value.coerceIn
import sokeriaaa.return0.shared.data.api.component.value.div
import sokeriaaa.return0.shared.data.api.component.value.maxOf
import sokeriaaa.return0.shared.data.api.component.value.minOf
import sokeriaaa.return0.shared.data.api.component.value.shl
import sokeriaaa.return0.shared.data.api.component.value.shr
import sokeriaaa.return0.shared.data.api.component.value.sumOf
import sokeriaaa.return0.shared.data.api.component.value.times
import sokeriaaa.return0.shared.data.api.component.value.unaryMinus
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.shared.data.models.component.values.CommonValue
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import sokeriaaa.return0.test.shared.common.helpers.FakeRandom
import sokeriaaa.return0.test.shared.common.helpers.assertFloatEquals
import kotlin.random.Random
import kotlin.test.Test

/**
 * Test value executors.
 */
class CommonValueExecutorTest {

    private fun createTestingContext(
        user: Entity = DummyEntities.generateEntity(index = 0, name = "foo", baseATK = 123),
        target: Entity = DummyEntities.generateEntity(index = 1, name = "bar", baseATK = 456),
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
    fun `Common_Constant calculates correctly`() {
        val context = createTestingContext()
        // Int
        val constant1 = Value(42)
        assertFloatEquals(42F, constant1.calculatedIn(context))
        // Float
        val constant2 = Value(42F)
        assertFloatEquals(42F, constant2.calculatedIn(context))
    }

    @Test
    fun `Common_Sum calculates correctly`() {
        val context = createTestingContext()
        val sum = sumOf(Value(1F), Value(2), Value(3F))
        assertFloatEquals(6F, sum.calculatedIn(context))
    }

    @Test
    fun `Common_Times calculates correctly`() {
        val context = createTestingContext()
        val times = Value(4) * Value(2F)
        assertFloatEquals(8F, times.calculatedIn(context))
    }

    @Test
    fun `Common_TimesConst calculates correctly`() {
        val context = createTestingContext()
        // Int
        val times1 = Value(4) * 2
        assertFloatEquals(8F, times1.calculatedIn(context))
        // Float
        val times2 = Value(4) * 3F
        assertFloatEquals(12F, times2.calculatedIn(context))
    }

    @Test
    fun `Common_Div calculates correctly`() {
        val context = createTestingContext()
        val div = Value(42F) / Value(7)
        assertFloatEquals(6F, div.calculatedIn(context))
    }

    @Test
    fun `Common_UnaryMinus calculates correctly`() {
        val context = createTestingContext()
        val unaryMinus1 = -Value(42F)
        assertFloatEquals(-42F, unaryMinus1.calculatedIn(context))
        val unaryMinus2 = -(-Value(42F))
        assertFloatEquals(42F, unaryMinus2.calculatedIn(context))
    }

    @Test
    fun `Common_Shift calculates correctly`() {
        val context = createTestingContext()
        val shift1 = Value(8) shr Value(2F)
        assertFloatEquals(2F, shift1.calculatedIn(context))
        val shift2 = Value(5) shl Value(2F)
        assertFloatEquals(20F, shift2.calculatedIn(context))
    }

    @Test
    fun `Common_AbsoluteValue calculates correctly`() {
        val context = createTestingContext()
        val abs1 = abs(Value(42))
        assertFloatEquals(42F, abs1.calculatedIn(context))
        val abs2 = abs(Value(-42))
        assertFloatEquals(42F, abs2.calculatedIn(context))
        val abs3 = Value(42).absoluteValue
        assertFloatEquals(42F, abs3.calculatedIn(context))
        val abs4 = Value(-42).absoluteValue
        assertFloatEquals(42F, abs4.calculatedIn(context))
    }

    @Test
    fun `Common_Coerced at least or most calculates correctly`() {
        val context = createTestingContext()
        // Coerce at least / Constant
        val coerced1 = Value(1).coerceAtLeast(0)
        assertFloatEquals(1F, coerced1.calculatedIn(context))
        val coerced2 = Value(1).coerceAtLeast(2)
        assertFloatEquals(2F, coerced2.calculatedIn(context))
        // Coerce at most / Constant
        val coerced3 = Value(1).coerceAtMost(0)
        assertFloatEquals(0F, coerced3.calculatedIn(context))
        val coerced4 = Value(1).coerceAtMost(2)
        assertFloatEquals(1F, coerced4.calculatedIn(context))
        // Coerce at least / Variable
        val coerced5 = Value(1).coerceAtLeast(Value(0))
        assertFloatEquals(1F, coerced5.calculatedIn(context))
        val coerced6 = Value(1).coerceAtLeast(Value(2))
        assertFloatEquals(2F, coerced6.calculatedIn(context))
        // Coerce at most / Variable
        val coerced7 = Value(1).coerceAtMost(Value(0))
        assertFloatEquals(0F, coerced7.calculatedIn(context))
        val coerced8 = Value(1).coerceAtMost(Value(2))
        assertFloatEquals(1F, coerced8.calculatedIn(context))
    }

    @Test
    fun `Common_Coerced in calculates correctly`() {
        val context = createTestingContext()
        val coerced1 = Value(1).coerceIn(0..2)
        assertFloatEquals(1F, coerced1.calculatedIn(context))
        val coerced2 = Value(3).coerceIn(0..2)
        assertFloatEquals(2F, coerced2.calculatedIn(context))
        val coerced3 = Value(-1).coerceIn(0..2)
        assertFloatEquals(0F, coerced3.calculatedIn(context))
        val coerced4 = Value(1).coerceIn(0F..2F)
        assertFloatEquals(1F, coerced4.calculatedIn(context))
        val coerced5 = Value(3).coerceIn(0F..2F)
        assertFloatEquals(2F, coerced5.calculatedIn(context))
        val coerced6 = Value(-1).coerceIn(0F..2F)
        assertFloatEquals(0F, coerced6.calculatedIn(context))
    }

    @Test
    fun `Common_MinOf calculates correctly`() {
        val context = createTestingContext()
        val minOf = minOf(Value(1), Value(2F), Value(3))
        assertFloatEquals(1F, minOf.calculatedIn(context))
    }

    @Test
    fun `Common_MaxOf calculates correctly`() {
        val context = createTestingContext()
        val maxOf = maxOf(Value(1), Value(2F), Value(3))
        assertFloatEquals(3F, maxOf.calculatedIn(context))
    }

    @Test
    fun `Common_RandomInt calculates correctly`() {
        val context = createTestingContext(random = FakeRandom(7))
        assertFloatEquals(7F, CommonValue.Math.RandomInt(5, 10).calculatedIn(context))
    }

    @Test
    fun `Common_RandomFloat calculates correctly`() = runTest {
        val context = createTestingContext(random = FakeRandom(0.4F))
        assertFloatEquals(7F, CommonValue.Math.RandomFloat(5F, 10F).calculatedIn(context))
    }

    @Test
    fun `Common_Conditioned calculates correctly`() {
        val context = createTestingContext()
        val conditioned1 = IF(CommonCondition.True)
            .then(ifTrue = Value(4), ifFalse = Value(2F))
        assertFloatEquals(4F, conditioned1.calculatedIn(context))
        val conditioned2 = IF(CommonCondition.False)
            .then(ifTrue = Value(4), ifFalse = Value(2F))
        assertFloatEquals(2F, conditioned2.calculatedIn(context))
        // Test default value
        val conditioned3 = IF(CommonCondition.False)
            .then(ifTrue = Value(4), defaultValue = Value(3F))
        assertFloatEquals(3F, conditioned3.calculatedIn(context))
        // returns 0 when a default value is not exists.
        val conditioned4 = IF(CommonCondition.False).then(ifTrue = Value(4))
        assertFloatEquals(0F, conditioned4.calculatedIn(context))
    }
}