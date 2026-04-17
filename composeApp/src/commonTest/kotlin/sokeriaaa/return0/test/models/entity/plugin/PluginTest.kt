/**
 * Copyright (C) 2026 Sokeriaaa
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
package sokeriaaa.return0.test.models.entity.plugin

import org.koin.core.component.KoinComponent
import sokeriaaa.return0.models.entity.plugin.generatePlugin
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.combat.EntityState
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginData
import sokeriaaa.return0.test.annotations.AppRunner
import sokeriaaa.return0.test.annotations.RunWith
import sokeriaaa.return0.test.applib.modules.TestKoinModules
import sokeriaaa.return0.test.models.entity.DummyEntities
import sokeriaaa.sugarkane.wrench.helper.AssertHelper.assertFloatEquals
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(AppRunner::class)
class PluginTest : KoinComponent {

    @BeforeTest
    fun beforeTest() {
        TestKoinModules.start()
    }

    @AfterTest
    fun afterTest() {
        TestKoinModules.stop()
    }

    @Test
    fun `plugin data generator calculated correctly`() {
        val pluginData = PluginData(
            key = "foo",
            nameRes = "plugin.foo",
            descriptionRes = "plugin.foo.desc",
            path = EntityPath.PROTOCOL,
        )
        val plugin = pluginData.generatePlugin(
            tier = 1,
            constMap = mapOf(
                PluginConst.ATK to 42,
            ),
        )

        assertEquals("foo", plugin.key)
        assertEquals(EntityPath.PROTOCOL, plugin.path)
        assertEquals(1, plugin.tier)
        assertEquals(mapOf(PluginConst.ATK to 42), plugin.constMap)
    }

    @Test
    fun `plugin const applied correctly`() {
        val plugin = EntityState.Plugin(
            pluginData = PluginData(
                key = "foo",
                nameRes = "plugin.foo",
                descriptionRes = "plugin.foo.desc",
                path = EntityPath.PROTOCOL,
            ),
            tier = 1,
            constMap = mapOf(
                PluginConst.ATK to 1,
                PluginConst.DEF to 2,
                PluginConst.SPD to 3,
                PluginConst.HP to 4,
                PluginConst.SP to 5,
                PluginConst.CRIT_RATE to 6,
                PluginConst.CRIT_DMG to 7,
                PluginConst.TGT_RATE to 8,
                PluginConst.HID_RATE to 9,
            ),
        )
        // Identical path
        val dummyEntity1 = DummyEntities.generateEntity(
            path = EntityPath.PROTOCOL,
            level = 100,
        )
        val pluggedEntity1 = DummyEntities.generateEntity(
            path = EntityPath.PROTOCOL,
            level = 100,
            plugin = plugin,
        )
        assertEquals((dummyEntity1.atk * 1.01F).toInt(), pluggedEntity1.atk)
        assertEquals((dummyEntity1.def * 1.02F).toInt(), pluggedEntity1.def)
        assertEquals((dummyEntity1.spd * 1.03F).toInt(), pluggedEntity1.spd)
        assertEquals((dummyEntity1.maxhp * 1.04F).toInt(), pluggedEntity1.maxhp)
        assertEquals((dummyEntity1.maxsp * 1.05F).toInt(), pluggedEntity1.maxsp)
        assertFloatEquals(dummyEntity1.critRate + 0.06F, pluggedEntity1.critRate)
        assertFloatEquals(dummyEntity1.critDMG + 0.07F, pluggedEntity1.critDMG)
        assertFloatEquals(dummyEntity1.targetRate + 0.08F, pluggedEntity1.targetRate)
        assertFloatEquals(dummyEntity1.hideRate + 0.09F, pluggedEntity1.hideRate)

        // Different path, constants still take effect.
        val dummyEntity2 = DummyEntities.generateEntity(
            path = EntityPath.HEAP,
            level = 100,
        )
        val pluggedEntity2 = DummyEntities.generateEntity(
            path = EntityPath.HEAP,
            level = 100,
            plugin = plugin,
        )
        assertEquals((dummyEntity2.atk * 1.01F).toInt(), pluggedEntity2.atk)
        assertEquals((dummyEntity2.def * 1.02F).toInt(), pluggedEntity2.def)
        assertEquals((dummyEntity2.spd * 1.03F).toInt(), pluggedEntity2.spd)
        assertEquals((dummyEntity2.maxhp * 1.04F).toInt(), pluggedEntity2.maxhp)
        assertEquals((dummyEntity2.maxsp * 1.05F).toInt(), pluggedEntity2.maxsp)
        assertFloatEquals(dummyEntity2.critRate + 0.06F, pluggedEntity2.critRate)
        assertFloatEquals(dummyEntity2.critDMG + 0.07F, pluggedEntity2.critDMG)
        assertFloatEquals(dummyEntity2.targetRate + 0.08F, pluggedEntity2.targetRate)
        assertFloatEquals(dummyEntity2.hideRate + 0.09F, pluggedEntity2.hideRate)
    }

    @Test
    fun `plugin effects applied correctly`() {
        val plugin = EntityState.Plugin(
            pluginData = PluginData(
                key = "foo",
                nameRes = "plugin.foo",
                descriptionRes = "plugin.foo.desc",
                path = EntityPath.PROTOCOL,
                onAttack = CombatExtra.HPChange(Value(42)),
                onDefend = CombatExtra.HPChange(Value(24)),
                attackRateOffset = Value(0.42F),
                defendRateOffset = Value(0.24F),
            ),
            tier = 1,
            constMap = emptyMap(),
        )
        // Identical path
        val pluggedEntity1 = DummyEntities.generateEntity(
            path = EntityPath.PROTOCOL,
            level = 100,
            plugin = plugin,
        )
        assertEquals(CombatExtra.HPChange(Value(42)), pluggedEntity1.onAttack)
        assertEquals(CombatExtra.HPChange(Value(24)), pluggedEntity1.onDefend)
        assertEquals(Value(0.42F), pluggedEntity1.attackRateOffset)
        assertEquals(Value(0.24F), pluggedEntity1.defendRateOffset)

        // Different path, all effects are nullified.
        val pluggedEntity2 = DummyEntities.generateEntity(
            path = EntityPath.HEAP,
            level = 100,
            plugin = plugin,
        )
        assertEquals(null, pluggedEntity2.onAttack)
        assertEquals(null, pluggedEntity2.onDefend)
        assertEquals(null, pluggedEntity2.attackRateOffset)
        assertEquals(null, pluggedEntity2.defendRateOffset)
    }
}