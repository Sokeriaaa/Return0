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
package sokeriaaa.return0.test.models.entity

import sokeriaaa.return0.models.action.effect.generateEffectFor
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.generate
import sokeriaaa.return0.shared.data.models.action.effect.EffectModifier
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.test.models.action.effect.DummyEffects
import sokeriaaa.return0.test.shared.common.helpers.assertFloatEquals
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EntityTest {

    @Test
    fun `entity data generator calculated correctly`() {
        // Try 10 different values.
        repeat(10) {
            // Generate a random dummy EntityData
            val dummyData = DummyEntities.generateEntityData(
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
                spGrowth = Random.nextFloat() * 0.1F + 0.2F,
            )
            // Generate the Entity with random level.
            val level = Random.nextInt(1, 101)
            val dummyEntity = dummyData.generate(
                index = -1,
                level = level,
                growth = growth,
                isParty = false,
            )
            // Calculate data
            assertEquals(
                expected = (dummyData.baseATK * (1 + growth.atkGrowth * level)).toInt(),
                actual = dummyEntity.baseATK,
            )
            assertEquals(
                expected = (dummyData.baseDEF * (1 + growth.defGrowth * level)).toInt(),
                actual = dummyEntity.baseDEF,
            )
            assertEquals(
                expected = (dummyData.baseSPD * (1 + growth.spdGrowth * level)).toInt(),
                actual = dummyEntity.baseSPD,
            )
            assertEquals(
                expected = (dummyData.baseHP * (1 + growth.hpGrowth * level)).toInt(),
                actual = dummyEntity.baseHP,
            )
            assertEquals(
                expected = (dummyData.baseSP * (1 + growth.spGrowth * level)).toInt(),
                actual = dummyEntity.baseSP,
            )
        }
    }

    @Test
    fun `entity AP ticking`() {
        val dummyEntity = DummyEntities.generateEntity(level = 100, baseAP = 100)
        // Init AP
        dummyEntity.ap = 80F
        // Tick (Recover AP)
        dummyEntity.tick()
        assertTrue(dummyEntity.ap > 80F)

        // AP is full
        dummyEntity.ap = 100F
        dummyEntity.tick()
        assertEquals(100F, dummyEntity.ap)

        // AP is higher than MAXAP
        dummyEntity.ap = 120F
        dummyEntity.tick()
        assertEquals(120F, dummyEntity.ap)
    }

    @Test
    fun `the higher SPD the entity has, the faster AP recovers`() {
        val entity1 = DummyEntities.generateEntity(name = "foo", baseAP = 100, baseSPD = 100)
        val entity2 = DummyEntities.generateEntity(name = "bar", baseAP = 100, baseSPD = 200)
        entity1.tick()
        entity2.tick()
        assertTrue(entity1.ap < entity2.ap)
    }

    @Test
    fun `entity is failed or not`() {
        val dummyEntity = DummyEntities.generateEntity(level = 100)
        // Init HP
        dummyEntity.hp = 100
        assertEquals(100, dummyEntity.hp)
        // Damage
        dummyEntity.hp -= 40
        assertEquals(60, dummyEntity.hp)
        assertEquals(false, dummyEntity.isFailed())
        // Failed
        dummyEntity.hp -= 60
        assertEquals(0, dummyEntity.hp)
        assertEquals(true, dummyEntity.isFailed())
    }

    @Test
    fun `effect attached and removed`() {
        val dummyEntity = DummyEntities.generateEntity()
        val dummyEffect = dummyEntity.generateEffectFor(
            effectData = DummyEffects.generateEffectData(),
            tier = 1,
            turns = 1,
        )

        // Attach
        dummyEntity.attachEffect(dummyEffect)
        assertTrue(dummyEntity.effects.contains(dummyEffect))
        // Remove
        dummyEntity.removeEffect(dummyEffect)
        assertFalse(dummyEntity.effects.contains(dummyEffect))
    }

    @Test
    fun `effect modifiers calculation`() {
        // Return a float array for all states of the entity.
        fun getStatusArray(entity: Entity): FloatArray = floatArrayOf(
            entity.atk.toFloat(),
            entity.def.toFloat(),
            entity.spd.toFloat(),
            entity.maxhp.toFloat(),
            entity.maxsp.toFloat(),
            entity.maxap.toFloat(),
            entity.critRate,
            entity.critDMG,
            entity.targetRate,
            entity.hideRate,
        )

        val entity = DummyEntities.generateEntity()
        val originalState = getStatusArray(entity)

        // Generate an effect with all types of modifiers.
        val offsetArray = FloatArray(10) { Random.nextFloat() }
        val bonusArray = FloatArray(10) { Random.nextFloat() }
        val modifiers = EffectModifier.Types.entries
            .mapIndexed { index, type ->
                EffectModifier(
                    type = type,
                    offset = offsetArray[index],
                    tierBonus = bonusArray[index],
                )
            }
        val effectData = DummyEffects.generateEffectData(modifiers = modifiers)

        // Test tier 1.
        val tier1Effect = entity.generateEffectFor(
            effectData = effectData,
            tier = 1,
            turns = 1,
        )
        entity.attachEffect(tier1Effect)

        // Calculate values
        val tier1Status = getStatusArray(entity)
        // ATK ~ MAXAP
        assertEquals((originalState[0] * (1 + offsetArray[0])).toInt(), tier1Status[0].toInt())
        assertEquals((originalState[1] * (1 + offsetArray[1])).toInt(), tier1Status[1].toInt())
        assertEquals((originalState[2] * (1 + offsetArray[2])).toInt(), tier1Status[2].toInt())
        assertEquals((originalState[3] * (1 + offsetArray[3])).toInt(), tier1Status[3].toInt())
        assertEquals((originalState[4] * (1 + offsetArray[4])).toInt(), tier1Status[4].toInt())
        assertEquals((originalState[5] * (1 + offsetArray[5])).toInt(), tier1Status[5].toInt())
        // CRIT_RATE~HIDE_RATE
        assertFloatEquals(1 + originalState[6] + offsetArray[6], tier1Status[6])
        assertFloatEquals(1 + originalState[7] + offsetArray[7], tier1Status[7])
        assertFloatEquals(1 + originalState[8] + offsetArray[8], tier1Status[8])
        assertFloatEquals(1 + originalState[9] + offsetArray[9], tier1Status[9])

        entity.removeEffect(tier1Effect)

        // Test a higher tier
        val tier = Random.nextInt(2, 10)
        val higherTierEffect = entity.generateEffectFor(
            effectData = effectData,
            tier = tier,
            turns = 1,
        )
        entity.attachEffect(higherTierEffect)

        // Calculate values
        val higherTierStatus = getStatusArray(entity)
        // ATK ~ MAXAP
        assertEquals(
            expected = (originalState[0] * (1 + offsetArray[0] + bonusArray[0] * (tier - 1))).toInt(),
            actual = higherTierStatus[0].toInt(),
        )
        assertEquals(
            expected = (originalState[1] * (1 + offsetArray[1] + bonusArray[1] * (tier - 1))).toInt(),
            actual = higherTierStatus[1].toInt(),
        )
        assertEquals(
            expected = (originalState[2] * (1 + offsetArray[2] + bonusArray[2] * (tier - 1))).toInt(),
            actual = higherTierStatus[2].toInt(),
        )
        assertEquals(
            expected = (originalState[3] * (1 + offsetArray[3] + bonusArray[3] * (tier - 1))).toInt(),
            actual = higherTierStatus[3].toInt(),
        )
        assertEquals(
            expected = (originalState[4] * (1 + offsetArray[4] + bonusArray[4] * (tier - 1))).toInt(),
            actual = higherTierStatus[4].toInt(),
        )
        assertEquals(
            expected = (originalState[5] * (1 + offsetArray[5] + bonusArray[5] * (tier - 1))).toInt(),
            actual = higherTierStatus[5].toInt(),
        )
        // CRIT_RATE~HIDE_RATE
        assertFloatEquals(
            expected = 1 + originalState[6] + offsetArray[6] + bonusArray[6] * (tier - 1),
            actual = higherTierStatus[6],
        )
        assertFloatEquals(
            expected = 1 + originalState[7] + offsetArray[7] + bonusArray[7] * (tier - 1),
            actual = higherTierStatus[7],
        )
        assertFloatEquals(
            expected = 1 + originalState[8] + offsetArray[8] + bonusArray[8] * (tier - 1),
            actual = higherTierStatus[8],
        )
        assertFloatEquals(
            expected = 1 + originalState[9] + offsetArray[9] + bonusArray[9] * (tier - 1),
            actual = higherTierStatus[9],
        )
    }
}