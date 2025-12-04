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

import sokeriaaa.return0.models.action.Action
import sokeriaaa.return0.models.action.component.extra.ActionExtraContext
import sokeriaaa.return0.models.action.component.extra.executedIn
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.api.component.condition.IF
import sokeriaaa.return0.shared.data.api.component.extra.extrasGroupOf
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.extras.CommonExtra
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import sokeriaaa.return0.test.shared.common.helpers.assertFloatEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class CommonExtraExecutorTest {

    private fun createTestingContext(
        user: Entity = DummyEntities.generateEntity(index = 0, name = "foo", baseHP = 99999),
        target: Entity = DummyEntities.generateEntity(index = 1, name = "bar", baseHP = 99999),
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

    // An extra that damage 100 to the target.
    private val damage100 = CombatExtra.HPChange(Value(-100))

    // An extra that damage 200 to the target.
    private val damage200 = CombatExtra.HPChange(Value(-200))

    // An extra that damage 300 to the target.
    private val damage300 = CombatExtra.HPChange(Value(-300))

    @Test
    fun `Common_Conditioned executes correctly`() {
        val context = createTestingContext()

        // True
        context.target.hp = 500
        IF(CommonCondition.True)
            .then(ifTrue = damage200, ifFalse = damage100)
            .executedIn(context)
        assertEquals(300, context.target.hp)

        // False
        context.target.hp = 500
        IF(CommonCondition.False)
            .then(ifTrue = damage200, ifFalse = damage100)
            .executedIn(context)
        assertEquals(400, context.target.hp)
    }

    @Test
    fun `Common_Grouped executes correctly`() {
        val context = createTestingContext()
        context.target.hp = 1000

        // Execute damage100, damage200, damage300 one by one.
        extrasGroupOf(damage100, damage200, damage300).executedIn(context)
        assertEquals(400, context.target.hp)
    }

    @Test
    fun `Common_SaveValue executes correctly`() {
        val context = createTestingContext()
        CommonExtra.SaveValue(key = "key", value = Value(42)).executedIn(context)
        assertFloatEquals(42F, context.fromAction.values["key"] ?: 0F)
    }

    @Test
    fun `Common_ForUser executes correctly`() {
        val context = createTestingContext()
        context.user.hp = 1000
        context.target.hp = 1000
        CommonExtra.ForUser(damage100).executedIn(context)
        assertEquals(900, context.user.hp)
        assertEquals(1000, context.target.hp)
    }

    @Test
    fun `Common_Swapped executes correctly`() {
        val context = createTestingContext()
        context.user.hp = 1000
        context.target.hp = 1000
        CommonExtra.Swapped(damage100).executedIn(context)
        assertEquals(900, context.user.hp)
        assertEquals(1000, context.target.hp)
    }
}