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
package sokeriaaa.return0.test.models.story.event.condition

import kotlinx.coroutines.test.runTest
import sokeriaaa.return0.models.component.executor.condition.calculatedIn
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
import sokeriaaa.return0.shared.data.models.component.common.Comparator
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.conditions.EventCondition
import sokeriaaa.return0.shared.data.models.title.Title
import sokeriaaa.return0.test.annotations.AppRunner
import sokeriaaa.return0.test.annotations.RunWith
import sokeriaaa.return0.test.models.story.event.BaseEventTest
import sokeriaaa.return0.test.shared.common.helpers.FakeRandom
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AppRunner::class)
class EventConditionExecutorTest : BaseEventTest() {
    // Common
    @Test
    fun `Common_True calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            assertTrue(CommonCondition.True.calculatedIn(context))
        }
    }

    @Test
    fun `Common_False calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            assertFalse(CommonCondition.False.calculatedIn(context))
        }
    }

    @Test
    fun `Common_And calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
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
    }

    @Test
    fun `Common_Or calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
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
    }

    @Test
    fun `Common_Not calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            assertFalse((!CommonCondition.True).calculatedIn(context))
            assertTrue((!CommonCondition.False).calculatedIn(context))
        }
    }

    @Test
    fun `Common_Compare calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
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
    }

    // Event
    @Test
    fun `Event_PlayerTitle calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            context.gameState.player.updateTitle(Title.LEAD)
            assertTrue(
                EventCondition.PlayerTitle(Comparator.GT, Title.SENIOR).calculatedIn(context)
            )
            assertTrue(
                EventCondition.PlayerTitle(Comparator.GTEQ, Title.SENIOR).calculatedIn(context)
            )
            assertFalse(
                EventCondition.PlayerTitle(Comparator.LT, Title.SENIOR).calculatedIn(context)
            )
            assertFalse(
                EventCondition.PlayerTitle(Comparator.LTEQ, Title.SENIOR).calculatedIn(context)
            )
            assertFalse(
                EventCondition.PlayerTitle(Comparator.EQ, Title.SENIOR).calculatedIn(context)
            )
            assertTrue(
                EventCondition.PlayerTitle(Comparator.NEQ, Title.SENIOR).calculatedIn(context)
            )

            assertFalse(
                EventCondition.PlayerTitle(Comparator.GT, Title.LEAD).calculatedIn(context)
            )
            assertTrue(
                EventCondition.PlayerTitle(Comparator.GTEQ, Title.LEAD).calculatedIn(context)
            )
            assertFalse(
                EventCondition.PlayerTitle(Comparator.LT, Title.LEAD).calculatedIn(context)
            )
            assertTrue(
                EventCondition.PlayerTitle(Comparator.LTEQ, Title.LEAD).calculatedIn(context)
            )
            assertTrue(
                EventCondition.PlayerTitle(Comparator.EQ, Title.LEAD).calculatedIn(context)
            )
            assertFalse(
                EventCondition.PlayerTitle(Comparator.NEQ, Title.LEAD).calculatedIn(context)
            )

            assertFalse(
                EventCondition.PlayerTitle(Comparator.GT, Title.STAFF).calculatedIn(context)
            )
            assertFalse(
                EventCondition.PlayerTitle(Comparator.GTEQ, Title.STAFF).calculatedIn(context)
            )
            assertTrue(
                EventCondition.PlayerTitle(Comparator.LT, Title.STAFF).calculatedIn(context)
            )
            assertTrue(
                EventCondition.PlayerTitle(Comparator.LTEQ, Title.STAFF).calculatedIn(context)
            )
            assertFalse(
                EventCondition.PlayerTitle(Comparator.EQ, Title.STAFF).calculatedIn(context)
            )
            assertTrue(
                EventCondition.PlayerTitle(Comparator.NEQ, Title.STAFF).calculatedIn(context)
            )
        }
    }

    @Test
    fun `Event_QuestCompleted calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            context.gameState.quest.acceptedQuest("an_epic_quest")
            context.gameState.quest.completedQuest("an_epic_quest")
            context.gameState.quest.acceptedQuest("another_epic_quest")
            assertTrue(EventCondition.QuestCompleted("an_epic_quest").calculatedIn(context))
            assertFalse(EventCondition.QuestCompleted("another_epic_quest").calculatedIn(context))
            assertFalse(EventCondition.QuestCompleted("unclaimed_quest").calculatedIn(context))
        }
    }

    @Test
    fun `Event_SavedSwitch calculates correctly`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(callback = callback) { context ->
            context.gameState.savedValues.setSwitch("event:common_switch", true)
            assertTrue(EventCondition.SavedSwitch("common_switch").calculatedIn(context))
            assertFalse(EventCondition.SavedSwitch("another_switch").calculatedIn(context))
        }
    }

    @Test
    fun `Common_Chance calculates correctly 1`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(
            random = FakeRandom(0.2F),
            callback = callback,
        ) { context ->
            assertTrue(CommonCondition.Chance(0.5F).calculatedIn(context))
        }
    }

    @Test
    fun `Common_Chance calculates correctly 2`() = runTest {
        val callback = object : TestingCallback() {}
        withContext(
            random = FakeRandom(0.8F),
            callback = callback,
        ) { context ->
            assertFalse(CommonCondition.Chance(0.5F).calculatedIn(context))
        }
    }
}