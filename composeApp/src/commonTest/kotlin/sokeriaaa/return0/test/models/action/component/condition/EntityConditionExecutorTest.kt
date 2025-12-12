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
import sokeriaaa.return0.models.action.effect.generateEffectFor
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.component.common.Comparator
import sokeriaaa.return0.shared.data.models.component.conditions.EntityCondition
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.test.models.action.effect.DummyEffects
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EntityConditionExecutorTest {

    private fun createTestingContext(
        user: Entity = DummyEntities.generateEntity(index = 0, name = "foo"),
        target: Entity = DummyEntities.generateEntity(index = 1, name = "bar"),
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
    fun `Entity_Categories calculated correctly`() {
        val context = createTestingContext(
            target = DummyEntities.generateEntity(
                index = 1,
                name = "bar",
                category = Category.CLASS,
                category2 = Category.INTERFACE,
            )
        )

        assertTrue(EntityCondition.Categories.Has(Category.CLASS).calculatedIn(context))
        assertTrue(EntityCondition.Categories.Has(Category.INTERFACE).calculatedIn(context))
        assertFalse(EntityCondition.Categories.Has(Category.REFLECT).calculatedIn(context))

        assertTrue(
            EntityCondition.Categories.HasOneOf(
                Category.CLASS,
                Category.CONCURRENT,
                Category.MEMORY
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Categories.HasOneOf(
                Category.INTERFACE,
                Category.CONCURRENT,
                Category.MEMORY
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Categories.HasOneOf(
                Category.REFLECT,
                Category.CONCURRENT,
                Category.MEMORY
            ).calculatedIn(context)
        )
    }

    @Test
    fun `Entity_Effects calculated correctly`() {
        val context = createTestingContext()
        val effect = context.user.generateEffectFor(
            effectData = DummyEffects.generateEffectData(name = "effect"),
            tier = 1,
            turns = 1,
        )

        // No effect
        assertFalse(EntityCondition.Effects.Has("effect").calculatedIn(context))
        assertFalse(EntityCondition.Effects.Has("another_effect").calculatedIn(context))
        assertFalse(EntityCondition.Effects.HasAny.calculatedIn(context))

        // Effect attached
        context.target.attachEffect(effect)
        assertTrue(EntityCondition.Effects.Has("effect").calculatedIn(context))
        assertFalse(EntityCondition.Effects.Has("another_effect").calculatedIn(context))
        assertTrue(EntityCondition.Effects.HasAny.calculatedIn(context))
    }

    @Test
    fun `Entity_Shields calculated correctly`() {
        val context = createTestingContext()

        // No shields
        assertFalse(EntityCondition.Shields.Has("shield").calculatedIn(context))
        assertFalse(EntityCondition.Shields.Has("another_shield").calculatedIn(context))
        assertFalse(EntityCondition.Shields.HasAny.calculatedIn(context))

        // Shield attached
        context.target.attachShield("shield", 123)
        assertTrue(EntityCondition.Shields.Has("shield").calculatedIn(context))
        assertFalse(EntityCondition.Shields.Has("another_shield").calculatedIn(context))
        assertTrue(EntityCondition.Shields.HasAny.calculatedIn(context))
    }

    @Test
    fun `Entity_Status_HP calculated correctly`() {
        val context = createTestingContext()
        context.target.hp = 42
        val rate = context.target.hp.toFloat() / context.target.maxhp
        assertFalse(
            EntityCondition.Status.HPLessThan(
                rate = Value(rate),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPLessThan(
                rate = Value(rate),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.HPLessThan(
                rate = Value(rate - 0.1F),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.HPLessThan(
                rate = Value(rate - 0.1F),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPLessThan(
                rate = Value(rate + 0.1F),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPLessThan(
                rate = Value(rate + 0.1F),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )

        assertFalse(
            EntityCondition.Status.HPMoreThan(
                rate = Value(rate),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPMoreThan(
                rate = Value(rate),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPMoreThan(
                rate = Value(rate - 0.1F),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPMoreThan(
                rate = Value(rate - 0.1F),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.HPMoreThan(
                rate = Value(rate + 0.1F),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.HPMoreThan(
                rate = Value(rate + 0.1F),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )

        assertFalse(
            EntityCondition.Status.HPRate(
                comparator = Comparator.LT,
                rate = Value(rate),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPRate(
                comparator = Comparator.LTEQ,
                rate = Value(rate),
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.HPRate(
                comparator = Comparator.LT,
                rate = Value(rate - 0.1F),
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.HPRate(
                comparator = Comparator.LTEQ,
                rate = Value(rate - 0.1F),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPRate(
                comparator = Comparator.LT,
                rate = Value(rate + 0.1F),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPRate(
                comparator = Comparator.LTEQ,
                rate = Value(rate + 0.1F),
            ).calculatedIn(context)
        )


        assertFalse(
            EntityCondition.Status.HPRate(
                comparator = Comparator.GT,
                rate = Value(rate),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPRate(
                comparator = Comparator.GTEQ,
                rate = Value(rate),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPRate(
                comparator = Comparator.GT,
                rate = Value(rate - 0.1F),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.HPRate(
                comparator = Comparator.GTEQ,
                rate = Value(rate - 0.1F),
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.HPRate(
                comparator = Comparator.GT,
                rate = Value(rate + 0.1F),
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.HPRate(
                comparator = Comparator.GTEQ,
                rate = Value(rate + 0.1F),
            ).calculatedIn(context)
        )
    }

    @Test
    fun `Entity_Status_SP calculated correctly`() {
        val context = createTestingContext()
        context.target.sp = 42
        val rate = context.target.sp.toFloat() / context.target.maxsp
        assertFalse(
            EntityCondition.Status.SPLessThan(
                rate = Value(rate),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPLessThan(
                rate = Value(rate),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.SPLessThan(
                rate = Value(rate - 0.1F),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.SPLessThan(
                rate = Value(rate - 0.1F),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPLessThan(
                rate = Value(rate + 0.1F),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPLessThan(
                rate = Value(rate + 0.1F),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )

        assertFalse(
            EntityCondition.Status.SPMoreThan(
                rate = Value(rate),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPMoreThan(
                rate = Value(rate),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPMoreThan(
                rate = Value(rate - 0.1F),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPMoreThan(
                rate = Value(rate - 0.1F),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.SPMoreThan(
                rate = Value(rate + 0.1F),
                isIncludeEquals = false,
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.SPMoreThan(
                rate = Value(rate + 0.1F),
                isIncludeEquals = true,
            ).calculatedIn(context)
        )

        assertFalse(
            EntityCondition.Status.SPRate(
                comparator = Comparator.LT,
                rate = Value(rate),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPRate(
                comparator = Comparator.LTEQ,
                rate = Value(rate),
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.SPRate(
                comparator = Comparator.LT,
                rate = Value(rate - 0.1F),
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.SPRate(
                comparator = Comparator.LTEQ,
                rate = Value(rate - 0.1F),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPRate(
                comparator = Comparator.LT,
                rate = Value(rate + 0.1F),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPRate(
                comparator = Comparator.LTEQ,
                rate = Value(rate + 0.1F),
            ).calculatedIn(context)
        )


        assertFalse(
            EntityCondition.Status.SPRate(
                comparator = Comparator.GT,
                rate = Value(rate),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPRate(
                comparator = Comparator.GTEQ,
                rate = Value(rate),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPRate(
                comparator = Comparator.GT,
                rate = Value(rate - 0.1F),
            ).calculatedIn(context)
        )
        assertTrue(
            EntityCondition.Status.SPRate(
                comparator = Comparator.GTEQ,
                rate = Value(rate - 0.1F),
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.SPRate(
                comparator = Comparator.GT,
                rate = Value(rate + 0.1F),
            ).calculatedIn(context)
        )
        assertFalse(
            EntityCondition.Status.SPRate(
                comparator = Comparator.GTEQ,
                rate = Value(rate + 0.1F),
            ).calculatedIn(context)
        )
    }
}