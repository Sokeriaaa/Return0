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

import sokeriaaa.return0.models.action.attachShield
import sokeriaaa.return0.models.action.effect.generateEffectFor
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.component.context.ActionExtraContext
import sokeriaaa.return0.models.component.executor.value.calculatedIn
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.generate
import sokeriaaa.return0.shared.data.models.component.values.EntityValue
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.test.models.action.effect.DummyEffects
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import sokeriaaa.return0.test.shared.common.helpers.assertFloatEquals
import kotlin.random.Random
import kotlin.test.Test

class EntityValueExecutorTest {

    /**
     * Create a context with a target that has random status and level,
     * For testing the EntityValue.
     */
    private fun createTestingContextWithRandomTarget(): ActionExtraContext {
        val user: Entity = DummyEntities.generateEntity(index = 0, name = "foo")
        // Generate a random dummy EntityData
        val dummyData = DummyEntities.generateEntityData(
            name = "bar",
            baseATK = Random.nextInt(50, 80),
            baseDEF = Random.nextInt(10, 40),
            baseSPD = Random.nextInt(20, 50),
            baseHP = Random.nextInt(150, 400),
            baseSP = Random.nextInt(80, 200),
        )
        // Generate a random EntityGrowth
        val growth = EntityGrowth(
            atkGrowth = Random.nextFloat() * 0.1F + 0.2F,
            defGrowth = Random.nextFloat() * 0.1F + 0.2F,
            spdGrowth = Random.nextFloat() * 0.1F + 0.2F,
            hpGrowth = Random.nextFloat() * 0.1F + 0.2F,
        )
        // Generate the Entity with random level.
        val level = Random.nextInt(1, 101)
        val target = dummyData.generate(
            index = 1,
            level = level,
            growth = growth,
            isParty = false,
        )
        // Fills the random rate of HP, SP and AP.
        target.hp = (Random.nextFloat() * target.maxhp).toInt()
        target.sp = (Random.nextFloat() * target.maxsp).toInt()
        target.ap = Random.nextFloat() * target.maxap
        return ActionExtraContext(
            fromAction = user.generateFunctionFor(DummyFunction.generateFunctionData())!!,
            user = user,
            target = target,
        )
    }

    @Test
    fun `Entity_ATK calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(context.target.atk.toFloat(), EntityValue.ATK.calculatedIn(context))
    }

    @Test
    fun `Entity_DEF calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(context.target.def.toFloat(), EntityValue.DEF.calculatedIn(context))
    }

    @Test
    fun `Entity_SPD calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(context.target.spd.toFloat(), EntityValue.SPD.calculatedIn(context))
    }

    @Test
    fun `Entity_HP calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(context.target.hp.toFloat(), EntityValue.HP.calculatedIn(context))
    }

    @Test
    fun `Entity_SP calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(context.target.sp.toFloat(), EntityValue.SP.calculatedIn(context))
    }

    @Test
    fun `Entity_AP calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(context.target.ap, EntityValue.AP.calculatedIn(context))
    }

    @Test
    fun `Entity_HPRate calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.hp.toFloat() / context.target.maxhp,
            actual = EntityValue.HPRate.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_SPRate calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.sp.toFloat() / context.target.maxsp,
            actual = EntityValue.SPRate.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_APRate calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.ap / context.target.maxap,
            actual = EntityValue.APRate.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_MAXHP calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(context.target.maxhp.toFloat(), EntityValue.MAXHP.calculatedIn(context))
    }

    @Test
    fun `Entity_MAXSP calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(context.target.maxsp.toFloat(), EntityValue.MAXSP.calculatedIn(context))
    }

    @Test
    fun `Entity_MAXAP calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(context.target.maxap.toFloat(), EntityValue.MAXAP.calculatedIn(context))
    }

    @Test
    fun `Entity_BaseATK calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.baseATK.toFloat(),
            actual = EntityValue.BaseATK.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_BaseDEF calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.baseDEF.toFloat(),
            actual = EntityValue.BaseDEF.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_BaseSPD calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.baseSPD.toFloat(),
            actual = EntityValue.BaseSPD.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_BaseHP calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.baseHP.toFloat(),
            actual = EntityValue.BaseHP.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_BaseSP calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.baseSP.toFloat(),
            actual = EntityValue.BaseSP.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_BaseAP calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.baseAP.toFloat(),
            actual = EntityValue.BaseAP.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_CriticalRate calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.critRate,
            actual = EntityValue.CriticalRate.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_CriticalDMG calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        assertFloatEquals(
            expected = context.target.critDMG,
            actual = EntityValue.CriticalDMG.calculatedIn(context),
        )
    }

    @Test
    fun `Entity_EffectTurns calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        val foo = context.user.generateEffectFor(
            effectData = DummyEffects.generateEffectData(name = "foo"),
            tier = 1,
            turns = 1,
        )
        val bar = context.user.generateEffectFor(
            effectData = DummyEffects.generateEffectData(name = "bar"),
            tier = 2,
            turns = 2,
        )
        context.target.attachEffect(foo)
        context.target.attachEffect(bar)
        assertFloatEquals(1F, EntityValue.TurnsLeftOf("foo").calculatedIn(context))
        assertFloatEquals(2F, EntityValue.TurnsLeftOf("bar").calculatedIn(context))
        assertFloatEquals(3F, EntityValue.TurnsLeftOfAllEffects.calculatedIn(context))
    }

    @Test
    fun `Entity_ShieldValue calculates correctly`() {
        val context = createTestingContextWithRandomTarget()
        context.attachShield("foo", 111)
        context.attachShield("bar", 222)
        assertFloatEquals(111F, EntityValue.ShieldValueOf("foo").calculatedIn(context))
        assertFloatEquals(222F, EntityValue.ShieldValueOf("bar").calculatedIn(context))
        assertFloatEquals(333F, EntityValue.SumOfShieldValue.calculatedIn(context))

    }
}