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
package sokeriaaa.return0.test.models.action.function

import sokeriaaa.return0.models.action.function.generateFunctionFor
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.test.applib.modules.TestKoinModules
import sokeriaaa.return0.test.models.entity.DummyEntities
import kotlin.test.Test
import kotlin.test.assertEquals

class FunctionTest {

    @Test
    fun `functions cost SP`() {
        val entity = DummyEntities.generateEntity(baseSP = 99999)
        entity.sp = 200
        entity.generateFunctionFor(
            DummyFunction.generateFunctionData(baseSPCost = 40)
        )?.invokeOn(listOf())
        assertEquals(160, entity.sp)
    }

    @Test
    fun `function times used`() {
        val entity1 = DummyEntities.generateEntity(name = "foo", level = 100, baseHP = 99999)
        val entity2 = DummyEntities.generateEntity(name = "bar", level = 100, baseHP = 99999)
        entity1.hp = entity1.maxhp
        entity2.hp = entity2.maxhp

        val skill = entity1.generateFunctionFor(
            DummyFunction.generateFunctionData(
                basePower = 1,
                attackModifier = FunctionData.AttackModifier(
                    attackTimes = Value(3)
                )
            )
        )!!

        TestKoinModules.withModules {
            skill.invokeOn(listOf(entity2))
            assertEquals(1, skill.timesUsed)
            skill.invokeOn(listOf(entity2))
            assertEquals(2, skill.timesUsed)
            // Reset after entity is defeated or combat is ended.
            skill.reset()
            assertEquals(0, skill.timesUsed)
        }
    }
}