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

import sokeriaaa.return0.models.action.effect.Effect
import sokeriaaa.return0.models.action.effect.generateEffectFor
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.component.context.ActionExtraContext
import sokeriaaa.return0.models.component.executor.value.calculatedIn
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.component.values.ActionValue
import sokeriaaa.return0.test.models.action.effect.DummyEffects
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import sokeriaaa.return0.test.shared.common.helpers.assertFloatEquals
import kotlin.random.Random
import kotlin.test.Test

class ActionValueExecutorTest {

    /**
     * Create a context with a function that has random properties,
     * For testing the ActionValue.
     */
    private fun createTestingContextWithRandomFunction(): ActionExtraContext {
        val user: Entity = DummyEntities.generateEntity(
            index = 0,
            name = "foo",
            level = Random.nextInt(1, 101),
        )
        val target: Entity = DummyEntities.generateEntity(
            index = 1,
            name = "bar",
            level = Random.nextInt(1, 101),
        )
        val action = user.generateFunctionFor(
            functionData = DummyFunction.generateFunctionData(
                name = "action",
                basePower = Random.nextInt(200),
                powerBonus = Random.nextInt(20),
                baseSPCost = Random.nextInt(200),
                growth = listOf(0, 20, 40, 60, 80, 100),
            ),
        )!!
        return ActionExtraContext(
            fromAction = action,
            user = user,
            target = target,
        )
    }

    /**
     * Create a context with an effect that has random properties,
     * For testing the ActionValue.
     */
    private fun createTestingContextWithRandomEffect(): ActionExtraContext {
        val user: Entity = DummyEntities.generateEntity(
            index = 0,
            name = "foo",
            level = Random.nextInt(1, 101),
        )
        val target: Entity = DummyEntities.generateEntity(
            index = 1,
            name = "bar",
            level = Random.nextInt(1, 101),
        )
        val action = user.generateEffectFor(
            effectData = DummyEffects.generateEffectData(
                name = "effect",
            ),
            tier = Random.nextInt(1, 10),
            turns = Random.nextInt(1, 10),
        )
        return ActionExtraContext(
            fromAction = action,
            user = user,
            target = target,
        )
    }

    @Test
    fun `Action_Tier calculates correctly`() {
        val contextFunction = createTestingContextWithRandomFunction()
        val contextEffect = createTestingContextWithRandomEffect()
        assertFloatEquals(
            expected = contextFunction.fromAction.tier.toFloat(),
            actual = ActionValue.Tier.calculatedIn(contextFunction),
        )
        assertFloatEquals(
            expected = contextEffect.fromAction.tier.toFloat(),
            actual = ActionValue.Tier.calculatedIn(contextEffect),
        )
    }

    @Test
    fun `Action_TimesUsed calculates correctly`() {
        val contextFunction = createTestingContextWithRandomFunction()
        val contextEffect = createTestingContextWithRandomEffect()
        assertFloatEquals(
            expected = contextFunction.fromAction.timesUsed.toFloat(),
            actual = ActionValue.TimesUsed.calculatedIn(contextFunction),
        )
        assertFloatEquals(
            expected = contextEffect.fromAction.timesUsed.toFloat(),
            actual = ActionValue.TimesUsed.calculatedIn(contextEffect),
        )
    }

    @Test
    fun `Action_TimesRepeated calculates correctly`() {
        val contextFunction = createTestingContextWithRandomFunction()
        val contextEffect = createTestingContextWithRandomEffect()
        assertFloatEquals(
            expected = contextFunction.fromAction.timesRepeated.toFloat(),
            actual = ActionValue.TimesRepeated.calculatedIn(contextFunction),
        )
        assertFloatEquals(
            expected = contextEffect.fromAction.timesRepeated.toFloat(),
            actual = ActionValue.TimesRepeated.calculatedIn(contextEffect),
        )
    }

    @Test
    fun `Action_EffectTurnsLeft calculates correctly`() {
        val contextFunction = createTestingContextWithRandomFunction()
        val contextEffect = createTestingContextWithRandomEffect()
        assertFloatEquals(
            expected = (contextFunction.fromAction as? Effect)?.turnsLeft?.toFloat() ?: 0F,
            actual = ActionValue.Effects.TurnsLeft.calculatedIn(contextFunction),
        )
        assertFloatEquals(
            expected = (contextEffect.fromAction as? Effect)?.turnsLeft?.toFloat() ?: 0F,
            actual = ActionValue.Effects.TurnsLeft.calculatedIn(contextEffect),
        )
    }

    @Test
    fun `Action_SkillPower calculates correctly`() {
        val contextFunction = createTestingContextWithRandomFunction()
        val contextEffect = createTestingContextWithRandomEffect()
        assertFloatEquals(
            expected = (contextFunction.fromAction as? Skill)?.power?.toFloat() ?: 0F,
            actual = ActionValue.Skills.Power.calculatedIn(contextFunction),
        )
        assertFloatEquals(
            expected = (contextEffect.fromAction as? Skill)?.power?.toFloat() ?: 0F,
            actual = ActionValue.Skills.Power.calculatedIn(contextEffect),
        )
    }

    @Test
    fun `Action_SkillBasePower calculates correctly`() {
        val contextFunction = createTestingContextWithRandomFunction()
        val contextEffect = createTestingContextWithRandomEffect()
        assertFloatEquals(
            expected = (contextFunction.fromAction as? Skill)?.basePower?.toFloat() ?: 0F,
            actual = ActionValue.Skills.BasePower.calculatedIn(contextFunction),
        )
        assertFloatEquals(
            expected = (contextEffect.fromAction as? Skill)?.basePower?.toFloat() ?: 0F,
            actual = ActionValue.Skills.BasePower.calculatedIn(contextEffect),
        )
    }
}