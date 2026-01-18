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
package sokeriaaa.return0.models.entity.display

import sokeriaaa.return0.models.entity.plugin.display.PluginInfo
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath

class ExtendedEntityProfile(
    name: String,
    level: Int,
    expProgress: Float,
    hp: Int,
    maxHP: Int,
    sp: Int,
    maxSP: Int,
    val functions: List<Skill>,
    val category: Category,
    val category2: Category?,
    val path: EntityPath,
    val atk: Int,
    val def: Int,
    val spd: Int,
    val maxAP: Int,
    val plugin: PluginInfo?,
    val expCurrent: Int,
    val expNext: Int,
    val expTotal: Int,
) : EntityProfile(
    name = name,
    level = level,
    expProgress = expProgress,
    hp = hp,
    maxHP = maxHP,
    sp = sp,
    maxSP = maxSP,
) {
    class Skill(
        val name: String,
        val description: String,
        val category: Category,
        val tier: Int,
        val power: Int,
        val spCost: Int,
        val data: FunctionData,
    )
}
