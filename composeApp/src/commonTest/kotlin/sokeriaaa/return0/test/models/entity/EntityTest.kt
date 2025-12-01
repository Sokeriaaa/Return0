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

import sokeriaaa.return0.models.entity.generate
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
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

    // TODO Test effect attaching/removing.
}