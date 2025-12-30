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
package sokeriaaa.return0.test.models.story.event.value

import kotlinx.coroutines.test.runTest
import sokeriaaa.return0.models.component.executor.value.calculatedIn
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
import sokeriaaa.return0.shared.data.models.component.values.CommonValue
import sokeriaaa.return0.shared.data.models.component.values.EventValue
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.test.annotations.AppRunner
import sokeriaaa.return0.test.annotations.RunWith
import sokeriaaa.return0.test.models.story.event.BaseEventTest
import sokeriaaa.return0.test.shared.common.helpers.FakeRandom
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(AppRunner::class)
class EventValueExecutorTest : BaseEventTest() {

    // Common
    @Test
    fun `Common_Constant calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Int
            val constant1 = Value(42)
            assertEquals(42, constant1.calculatedIn(context))
            // Float
            val constant2 = Value(42F)
            assertEquals(42, constant2.calculatedIn(context))
        }
    }

    @Test
    fun `Common_Sum calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val sum = sumOf(Value(1F), Value(2), Value(3F))
            assertEquals(6, sum.calculatedIn(context))
        }
    }

    @Test
    fun `Common_Times calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val times = Value(4) * Value(2F)
            assertEquals(8, times.calculatedIn(context))
        }
    }

    @Test
    fun `Common_TimesConst calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Int
            val times1 = Value(4) * 2
            assertEquals(8, times1.calculatedIn(context))
            // Float
            val times2 = Value(4) * 3F
            assertEquals(12, times2.calculatedIn(context))
        }
    }

    @Test
    fun `Common_Div calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val div = Value(42F) / Value(7)
            assertEquals(6, div.calculatedIn(context))
        }
    }

    @Test
    fun `Common_UnaryMinus calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val unaryMinus1 = -Value(42F)
            assertEquals(-42, unaryMinus1.calculatedIn(context))
            val unaryMinus2 = -(-Value(42F))
            assertEquals(42, unaryMinus2.calculatedIn(context))
        }
    }

    @Test
    fun `Common_Shift calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val shift1 = Value(8) shr Value(2F)
            assertEquals(2, shift1.calculatedIn(context))
            val shift2 = Value(5) shl Value(2F)
            assertEquals(20, shift2.calculatedIn(context))
        }
    }

    @Test
    fun `Common_AbsoluteValue calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val abs1 = abs(Value(42))
            assertEquals(42, abs1.calculatedIn(context))
            val abs2 = abs(Value(-42))
            assertEquals(42, abs2.calculatedIn(context))
            val abs3 = Value(42).absoluteValue
            assertEquals(42, abs3.calculatedIn(context))
            val abs4 = Value(-42).absoluteValue
            assertEquals(42, abs4.calculatedIn(context))
        }
    }

    @Test
    fun `Common_Coerced at least or most calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            // Coerce at least / Constant
            val coerced1 = Value(1).coerceAtLeast(0)
            assertEquals(1, coerced1.calculatedIn(context))
            val coerced2 = Value(1).coerceAtLeast(2)
            assertEquals(2, coerced2.calculatedIn(context))
            // Coerce at most / Constant
            val coerced3 = Value(1).coerceAtMost(0)
            assertEquals(0, coerced3.calculatedIn(context))
            val coerced4 = Value(1).coerceAtMost(2)
            assertEquals(1, coerced4.calculatedIn(context))
            // Coerce at least / Variable
            val coerced5 = Value(1).coerceAtLeast(Value(0))
            assertEquals(1, coerced5.calculatedIn(context))
            val coerced6 = Value(1).coerceAtLeast(Value(2))
            assertEquals(2, coerced6.calculatedIn(context))
            // Coerce at most / Variable
            val coerced7 = Value(1).coerceAtMost(Value(0))
            assertEquals(0, coerced7.calculatedIn(context))
            val coerced8 = Value(1).coerceAtMost(Value(2))
            assertEquals(1, coerced8.calculatedIn(context))
        }
    }

    @Test
    fun `Common_Coerced in calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val coerced1 = Value(1).coerceIn(0..2)
            assertEquals(1, coerced1.calculatedIn(context))
            val coerced2 = Value(3).coerceIn(0..2)
            assertEquals(2, coerced2.calculatedIn(context))
            val coerced3 = Value(-1).coerceIn(0..2)
            assertEquals(0, coerced3.calculatedIn(context))
            val coerced4 = Value(1).coerceIn(0F..2F)
            assertEquals(1, coerced4.calculatedIn(context))
            val coerced5 = Value(3).coerceIn(0F..2F)
            assertEquals(2, coerced5.calculatedIn(context))
            val coerced6 = Value(-1).coerceIn(0F..2F)
            assertEquals(0, coerced6.calculatedIn(context))
        }
    }

    @Test
    fun `Common_MinOf calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val minOf = minOf(Value(1), Value(2F), Value(3))
            assertEquals(1, minOf.calculatedIn(context))
        }
    }

    @Test
    fun `Common_MaxOf calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val maxOf = maxOf(Value(1), Value(2F), Value(3))
            assertEquals(3, maxOf.calculatedIn(context))
        }
    }

    @Test
    fun `Common_RandomInt calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(
            random = FakeRandom(7),
            callback = callback,
        ) { context ->
            assertEquals(7, CommonValue.Math.RandomInt(5, 10).calculatedIn(context))
        }
    }

    @Test
    fun `Common_RandomFloat calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(
            random = FakeRandom(0.4F),
            callback = callback,
        ) { context ->
            assertEquals(7, CommonValue.Math.RandomFloat(5F, 10F).calculatedIn(context))
        }
    }

    @Test
    fun `Common_Conditioned calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            val conditioned1 = IF(CommonCondition.True)
                .then(ifTrue = Value(4), ifFalse = Value(2F))
            assertEquals(4, conditioned1.calculatedIn(context))
            val conditioned2 = IF(CommonCondition.False)
                .then(ifTrue = Value(4), ifFalse = Value(2F))
            assertEquals(2, conditioned2.calculatedIn(context))
            // Test default value
            val conditioned3 = IF(CommonCondition.False)
                .then(ifTrue = Value(4), defaultValue = Value(3F))
            assertEquals(3, conditioned3.calculatedIn(context))
            // returns 0 when a default value is not exists.
            val conditioned4 = IF(CommonCondition.False).then(ifTrue = Value(4))
            assertEquals(0, conditioned4.calculatedIn(context))
        }
    }

    // Event
    @Test
    fun `Event_SavedVariable calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            context.gameState.savedValues.setVariable("common_value", 42)
            assertEquals(42, EventValue.SavedVariable("common_value").calculatedIn(context))
            assertEquals(0, EventValue.SavedVariable("another_value").calculatedIn(context))
        }
    }

    @Test
    fun `Event_Currency calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            context.gameState.currency[CurrencyType.TOKEN] = 42
            assertEquals(42, EventValue.Currency(CurrencyType.TOKEN).calculatedIn(context))
        }
    }

    @Test
    fun `Event_Inventory calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            context.gameState.inventory["epic_item"]++
            assertEquals(1, EventValue.Inventory("epic_item").calculatedIn(context))
        }
    }
}