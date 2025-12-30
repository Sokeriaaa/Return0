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
package sokeriaaa.return0.test.models.action

import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.models.action.attachEffect
import sokeriaaa.return0.models.action.effect.generateEffectFor
import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.models.action.instantAPChange
import sokeriaaa.return0.models.action.instantHPChange
import sokeriaaa.return0.models.action.instantSPChange
import sokeriaaa.return0.models.action.removeEffect
import sokeriaaa.return0.models.action.singleExecute
import sokeriaaa.return0.models.combat.CombatCalculator
import sokeriaaa.return0.models.component.context.createExtraContextFor
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.test.applib.modules.TestKoinModules
import sokeriaaa.return0.test.models.action.effect.DummyEffects
import sokeriaaa.return0.test.models.action.function.DummyFunction
import sokeriaaa.return0.test.models.entity.DummyEntities
import sokeriaaa.return0.test.shared.common.helpers.FakeRandom
import sokeriaaa.return0.test.shared.common.helpers.assertFloatEquals
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActionExecutorTest {

    @Test
    fun `damage calculated correctly`() {
        TestKoinModules.withModules {
            repeat(10) {
                val entity1 = DummyEntities.generateEntity(
                    index = 0,
                    category = Category.NORMAL,
                    name = "foo",
                    baseATK = Random.nextInt(40, 80),
                    baseHP = 99999,
                )
                val entity2 = DummyEntities.generateEntity(
                    index = 1,
                    category = Category.NORMAL,
                    name = "bar",
                    baseDEF = Random.nextInt(15, 30),
                    baseHP = 99999,
                )
                val power = Random.nextInt(20, 50)
                val damagingFunctionData = DummyFunction.generateFunctionData(
                    name = "damaging",
                    category = Category.NORMAL,
                    basePower = power,
                    powerBonus = 0,
                )
                val damaging = entity1.generateFunctionFor(damagingFunctionData)!!
                entity1.hp = 10000
                entity2.hp = 10000

                // No missed, non-critical
                val random1 = FakeRandom(1, 1)
                // Execute
                damaging.createExtraContextFor(entity2).singleExecute(random = random1)
                // Calculate expected damage
                val exceptedDamage1 = CombatCalculator.baseDamage(
                    power = power,
                    atk = entity1.atk.toFloat(),
                    def = entity2.def.toFloat(),
                ).toInt()
                assertEquals((10000 - exceptedDamage1).coerceAtLeast(0), entity2.hp)

                entity2.hp = 10000
                // Missed
                val random2 = FakeRandom(0, 1)
                // Execute
                damaging.createExtraContextFor(entity2).singleExecute(random = random2)
                assertEquals(10000, entity2.hp)

                entity2.hp = 10000
                // Critical
                val random3 = FakeRandom(1, 0)
                // Execute
                damaging.createExtraContextFor(entity2).singleExecute(random = random3)
                // Calculate expected damage
                val exceptedDamage3 = (CombatCalculator.baseDamage(
                    power = power,
                    atk = entity1.atk.toFloat(),
                    def = entity2.def.toFloat(),
                ) * (1 + AppConstants.BASE_CRITICAL_DMG)).toInt()
                assertEquals((10000 - exceptedDamage3).coerceAtLeast(0), entity2.hp)
            }
        }
    }

    @Test
    fun `shields nullified full attacking damage correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(
                index = 0,
                category = Category.NORMAL,
                name = "foo",
                baseATK = 100,
                baseHP = 99999,
            )
            val entity2 = DummyEntities.generateEntity(
                index = 1,
                category = Category.NORMAL,
                name = "bar",
                baseDEF = 50,
                baseHP = 99999,
            )
            val damagingFunctionData = DummyFunction.generateFunctionData(
                name = "damaging",
                category = Category.NORMAL,
                basePower = 20,
                powerBonus = 0,
            )
            val damaging = entity1.generateFunctionFor(damagingFunctionData)!!
            // Calculate expected damage
            val exceptedDamage = CombatCalculator.baseDamage(
                power = 20,
                atk = entity1.atk.toFloat(),
                def = entity2.def.toFloat(),
            ).toInt()
            // Init HP
            entity2.hp = exceptedDamage + 50
            // Attach shield
            entity2.attachShield("dummy", exceptedDamage + 50)
            // Execute
            // No missed, non-critical
            val random = FakeRandom(1, 1)
            damaging.createExtraContextFor(entity2).singleExecute(random)

            // Assert...
            assertEquals(exceptedDamage + 50, entity2.hp)
            assertEquals(50, entity2.shields["dummy"]?.value)
        }
    }

    @Test
    fun `shields nullified part of attacking damage correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(
                index = 0,
                category = Category.NORMAL,
                name = "foo",
                baseATK = 100,
                baseHP = 99999,
            )
            val entity2 = DummyEntities.generateEntity(
                index = 1,
                category = Category.NORMAL,
                name = "bar",
                baseDEF = 50,
                baseHP = 99999,
            )
            val damagingFunctionData = DummyFunction.generateFunctionData(
                name = "damaging",
                category = Category.NORMAL,
                basePower = 20,
                powerBonus = 0,
            )
            val damaging = entity1.generateFunctionFor(damagingFunctionData)!!
            // Calculate expected damage
            val exceptedDamage = CombatCalculator.baseDamage(
                power = 20,
                atk = entity1.atk.toFloat(),
                def = entity2.def.toFloat(),
            ).toInt()
            // Init HP
            entity2.hp = exceptedDamage + 50
            // Attach shield that can shield only 1 damage.
            entity2.attachShield("dummy", 1)
            // Execute
            // No missed, non-critical
            val random = FakeRandom(1, 1)
            damaging.createExtraContextFor(entity2).singleExecute(random)

            // Assert...
            assertEquals(51, entity2.hp)
            assertFalse(entity2.shields.containsKey("dummy"))
        }
    }

    @Test
    fun `shields is ignored by functions correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(
                index = 0,
                category = Category.NORMAL,
                name = "foo",
                baseATK = 100,
                baseHP = 99999,
            )
            val entity2 = DummyEntities.generateEntity(
                index = 1,
                category = Category.NORMAL,
                name = "bar",
                baseDEF = 50,
                baseHP = 99999,
            )
            val damagingFunctionData = DummyFunction.generateFunctionData(
                name = "damaging",
                category = Category.NORMAL,
                basePower = 20,
                powerBonus = 0,
                attackModifier = FunctionData.AttackModifier(
                    // This function will ignore the shields and directly damage the entity.
                    ignoresShields = CommonCondition.True,
                )
            )
            val damaging = entity1.generateFunctionFor(damagingFunctionData)!!
            // Calculate expected damage
            val exceptedDamage = CombatCalculator.baseDamage(
                power = 20,
                atk = entity1.atk.toFloat(),
                def = entity2.def.toFloat(),
            ).toInt()
            // Init HP
            entity2.hp = exceptedDamage + 50
            // Attach shield
            entity2.attachShield("dummy", 999999)
            // Execute
            // No missed, non-critical
            val random = FakeRandom(1, 1)
            damaging.createExtraContextFor(entity2).singleExecute(random)

            // Assert...
            assertEquals(50, entity2.hp)
            assertEquals(999999, entity2.shields["dummy"]?.value)
        }
    }

    @Test
    fun `heal calculated correctly`() {
        TestKoinModules.withModules {
            repeat(10) {
                val entity1 = DummyEntities.generateEntity(
                    index = 0,
                    category = Category.NORMAL,
                    name = "foo",
                    baseATK = Random.nextInt(40, 80),
                    baseHP = 99999,
                )
                val entity2 = DummyEntities.generateEntity(
                    index = 1,
                    category = Category.NORMAL,
                    name = "bar",
                    baseHP = 99999,
                )
                val power = Random.nextInt(-50, -10)
                val healingFunctionData = DummyFunction.generateFunctionData(
                    name = "healing",
                    category = Category.NORMAL,
                    basePower = power,
                    powerBonus = 0,
                )
                val healing = entity1.generateFunctionFor(healingFunctionData)!!
                entity1.hp = 1
                entity2.hp = 1

                // Execute
                healing.createExtraContextFor(entity2).singleExecute()
                // Calculate excepted heal
                val exceptedHeal = CombatCalculator.baseHeal(
                    powerAbs = abs(power),
                    atk = entity1.atk.toFloat(),
                ).toInt()
                assertEquals(1 + exceptedHeal, entity2.hp)
            }
        }
    }

    @Test
    fun `instant HP change executed correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500

            // execute 10 random hp changes.
            repeat(10) {
                entity2.hp = 500
                val hpChange = Random.nextInt(-2000, 2000)
                skill.createExtraContextFor(entity2).instantHPChange(hpChange)
                assertEquals((500 + hpChange).coerceIn(0, entity2.maxhp), entity2.hp)
            }
        }
    }

    @Test
    fun `instant HP change fully blocked by shields correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500
            entity2.attachShield("dummy", 300)
            val hpChange = -100
            skill.createExtraContextFor(entity2).instantHPChange(hpChange)
            assertEquals(500, entity2.hp)
            assertEquals(200, entity2.shields["dummy"]?.value)
        }
    }

    @Test
    fun `instant HP change partially blocked by shields correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500
            entity2.attachShield("dummy", 300)
            val hpChange = -400
            skill.createExtraContextFor(entity2).instantHPChange(hpChange)
            assertEquals(400, entity2.hp)
            assertFalse(entity2.shields.containsKey("dummy"))
        }
    }

    @Test
    fun `instant HP change ignores shields correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500
            entity2.attachShield("dummy", 999999)
            val hpChange = -300
            skill.createExtraContextFor(entity2).instantHPChange(hpChange, ignoresShield = true)
            assertEquals(200, entity2.hp)
            assertEquals(999999, entity2.shields["dummy"]?.value)
        }
    }

    @Test
    fun `instant healing is not affected by shields`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500
            entity2.attachShield("dummy", 999999)
            val hpChange = 300
            skill.createExtraContextFor(entity2).instantHPChange(hpChange)
            assertEquals(800, entity2.hp)
            assertEquals(999999, entity2.shields["dummy"]?.value)
        }
    }

    @Test
    fun `instant SP change executed correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500

            // execute 10 random sp changes.
            repeat(10) {
                entity2.sp = 500
                val spChange = Random.nextInt(-2000, 2000)
                skill.createExtraContextFor(entity2).instantSPChange(spChange)
                assertEquals((500 + spChange).coerceIn(0, entity2.maxsp), entity2.sp)
            }
        }
    }

    @Test
    fun `instant AP change executed correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500

            // execute 10 random ap changes.
            repeat(10) {
                entity2.ap = 50F
                val apChange = Random.nextFloat() * 200F - 100F
                skill.createExtraContextFor(entity2).instantAPChange(apChange)
                // AP can exceed the upper and lower limit by functions/effects.
                assertFloatEquals(50F + apChange, entity2.ap)
            }
        }
    }

    @Test
    fun `common effects attached successfully`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500

            val effect = entity1.generateEffectFor(
                effectData = DummyEffects.generateEffectData(),
                tier = 1,
                turns = 1,
            )

            // Execute
            skill.createExtraContextFor(entity2).attachEffect(effect)
            assertTrue(effect in entity2.effects)
        }
    }

    @Test
    fun `stackable effects attached successfully`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500

            val stackable = DummyEffects.generateEffectData(name = "stackable", isStackable = true)

            val effect1 = entity1.generateEffectFor(effectData = stackable, tier = 1, turns = 1)
            val effect2 = entity1.generateEffectFor(effectData = stackable, tier = 2, turns = 2)

            // Execute
            skill.createExtraContextFor(entity2).attachEffect(effect1)
            skill.createExtraContextFor(entity2).attachEffect(effect2)
            assertTrue(effect1 in entity2.effects)
            assertTrue(effect2 in entity2.effects)
        }
    }

    @Test
    fun `non-stackable effects replaces successfully`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500

            val nonStackable = DummyEffects.generateEffectData(name = "non-stackable")

            val effect1 = entity1.generateEffectFor(effectData = nonStackable, tier = 1, turns = 2)
            val effect2 = entity1.generateEffectFor(effectData = nonStackable, tier = 2, turns = 1)

            // Execute
            skill.createExtraContextFor(entity2).attachEffect(effect1)
            skill.createExtraContextFor(entity2).attachEffect(effect2)
            // effect1 is replaced by effect2
            assertFalse(effect1 in entity2.effects)
            assertTrue(effect2 in entity2.effects)
        }
    }

    @Test
    fun `effects removed correctly`() {
        TestKoinModules.withModules {
            val entity1 = DummyEntities.generateEntity(name = "foo", baseHP = 99999)
            val entity2 = DummyEntities.generateEntity(name = "bar", baseHP = 99999)
            val skill = entity1.generateFunctionFor(DummyFunction.generateFunctionData())!!
            entity1.hp = 500
            entity2.hp = 500

            val effect = entity1.generateEffectFor(
                effectData = DummyEffects.generateEffectData(),
                tier = 1,
                turns = 1,
            )

            // Execute
            skill.createExtraContextFor(entity2).attachEffect(effect)
            skill.createExtraContextFor(entity2).removeEffect(effect)

            assertFalse(effect in entity2.effects)
        }
    }
}