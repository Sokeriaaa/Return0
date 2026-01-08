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
package sokeriaaa.return0.models.entity

import sokeriaaa.return0.models.entity.plugin.EntityPlugin
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst

/**
 * Wrapped entity with plugin installed.
 */
internal class PluggedEntity(
    val entity: EntityImpl,
    val plugin: EntityPlugin,
) : EntityImpl(
    index = entity.index,
    isParty = entity.isParty,
    name = entity.name,
    level = entity.level,
    path = entity.path,
    category = entity.category,
    category2 = entity.category2,
    baseATK = (entity.baseATK * (1F + (plugin.constMap[PluginConst.ATK] ?: 0) * 0.01F)).toInt(),
    baseDEF = (entity.baseDEF * (1F + (plugin.constMap[PluginConst.DEF] ?: 0) * 0.01F)).toInt(),
    baseSPD = (entity.baseSPD * (1F + (plugin.constMap[PluginConst.SPD] ?: 0) * 0.01F)).toInt(),
    baseHP = (entity.baseHP * (1F + (plugin.constMap[PluginConst.HP] ?: 0) * 0.01F)).toInt(),
    baseSP = (entity.baseSP * (1F + (plugin.constMap[PluginConst.SP] ?: 0) * 0.01F)).toInt(),
    baseAP = entity.baseAP,
    functionDataList = entity.functionDataList,
    attackModifier = entity.attackModifier,
) {
    override var critRate: Float =
        entity.critRate + (plugin.constMap[PluginConst.CRIT_RATE] ?: 0) * 0.01F
    override var critDMG: Float =
        entity.critDMG + (plugin.constMap[PluginConst.CRIT_DMG] ?: 0) * 0.01F
    override var targetRate: Float =
        entity.targetRate + (plugin.constMap[PluginConst.TGT_RATE] ?: 0) * 0.01F
    override var hideRate: Float =
        entity.hideRate + (plugin.constMap[PluginConst.HID_RATE] ?: 0) * 0.01F
}