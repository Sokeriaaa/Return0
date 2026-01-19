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

import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.generate
import sokeriaaa.return0.models.entity.plugin.EntityPlugin
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.EntityData.GeneralAttackModifier
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath

/**
 * Dummy entities for testing.
 */
object DummyEntities {

    /**
     * Generate a dummy entity data with the specified params.
     */
    fun generateEntityData(
        name: String = "Dummy",
        path: EntityPath = EntityPath.UNSPECIFIED,
        category: Category = Category.CLASS,
        category2: Category? = null,
        baseATK: Int = 50,
        baseDEF: Int = 20,
        baseSPD: Int = 30,
        baseHP: Int = 300,
        baseSP: Int = 200,
        baseAP: Int = 100,
        functions: List<FunctionData> = emptyList(),
        attackModifier: GeneralAttackModifier? = null
    ): EntityData = EntityData(
        name = name,
        path = path,
        category = category,
        category2 = category2,
        baseATK = baseATK,
        baseDEF = baseDEF,
        baseSPD = baseSPD,
        baseHP = baseHP,
        baseSP = baseSP,
        baseAP = baseAP,
        functions = functions,
        attackModifier = attackModifier,
    )

    /**
     * Generate a dummy entity with the specified params.
     */
    fun generateEntity(
        index: Int = -1,
        name: String = "Dummy",
        level: Int = 1,
        path: EntityPath = EntityPath.UNSPECIFIED,
        growth: EntityGrowth = defaultEntityGrowth,
        isParty: Boolean = true,
        category: Category = Category.CLASS,
        category2: Category? = null,
        baseATK: Int = 50,
        baseDEF: Int = 20,
        baseSPD: Int = 30,
        baseHP: Int = 300,
        baseSP: Int = 200,
        baseAP: Int = 100,
        functions: List<FunctionData> = emptyList(),
        attackModifier: GeneralAttackModifier? = null,
        plugin: EntityPlugin? = null,
    ): Entity = generateEntityData(
        name = name,
        path = path,
        category = category,
        category2 = category2,
        baseATK = baseATK,
        baseDEF = baseDEF,
        baseSPD = baseSPD,
        baseHP = baseHP,
        baseSP = baseSP,
        baseAP = baseAP,
        functions = functions,
        attackModifier = attackModifier,
    ).generate(
        index = index,
        level = level,
        growth = growth,
        isParty = isParty,
        plugin = plugin,
    )

    val defaultEntityGrowth = EntityGrowth(
        atkGrowth = 0.25F,
        defGrowth = 0.25F,
        spdGrowth = 0.25F,
        hpGrowth = 0.25F,
        spGrowth = 0.25F,
    )

}