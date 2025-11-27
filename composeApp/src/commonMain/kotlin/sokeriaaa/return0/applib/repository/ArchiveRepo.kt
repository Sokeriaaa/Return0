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
package sokeriaaa.return0.applib.repository

import sokeriaaa.return0.models.action.effect.CommonEffects
import sokeriaaa.return0.shared.data.models.action.effect.EffectData
import sokeriaaa.return0.shared.data.models.entity.EnemyRewardTable
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.category.CategoryEffectiveness

/**
 * Stores archives data in memory.
 */
class ArchiveRepo internal constructor() {
    private val _entityDataMap: MutableMap<String, EntityData> = HashMap()
    private val _effectDataMap: MutableMap<String, EffectData> = HashMap()
    private val _entityGrowthMap: MutableMap<Category, EntityGrowth> = HashMap()
    private val _entityRewardMap: MutableMap<String, EnemyRewardTable> = HashMap()
    private val _categoryEffectivenessMap: MutableMap<Category, CategoryEffectiveness> = HashMap()

    init {
        // Register common data.
        _effectDataMap[CommonEffects.defense.name] = CommonEffects.defense
    }

    fun registerEntity(entityData: EntityData) {
        _entityDataMap[entityData.name] = entityData
    }

    fun registerEntities(entityDataList: List<EntityData>) {
        entityDataList.forEach(::registerEntity)
    }

    fun registerEntities(entityDataMap: Map<String, EntityData>) {
        _entityDataMap.putAll(entityDataMap)
    }

    fun registerEffect(effectData: EffectData) {
        _effectDataMap[effectData.name] = effectData
    }

    fun registerEffects(effectDataList: List<EffectData>) {
        effectDataList.forEach(::registerEffect)
    }

    fun registerEffects(effectDataMap: Map<String, EffectData>) {
        _effectDataMap.putAll(effectDataMap)
    }

    fun registerEntityGrowths(entityGrowthMap: Map<Category, EntityGrowth>) {
        _entityGrowthMap.putAll(entityGrowthMap)
    }

    fun registerCategoryEffectiveness(map: Map<Category, CategoryEffectiveness>) {
        _categoryEffectivenessMap.putAll(map)
    }

    fun registerEntityRewardTable(map: Map<String, EnemyRewardTable>) {
        _entityRewardMap.putAll(map)
    }

    fun getEntityData(name: String): EntityData? = _entityDataMap[name]
    fun getEffectData(name: String): EffectData? = _effectDataMap[name]

    fun getEntityGrowthByCategory(category: Category): EntityGrowth = _entityGrowthMap[category]
        ?: error("EntityGrowth of the category $category is not registered.")

    fun getCategoryEffectiveness(category: Category): CategoryEffectiveness =
        _categoryEffectivenessMap[category] ?: CategoryEffectiveness(emptyList(), emptyList())

    fun reset() {
        _entityDataMap.clear()
        _effectDataMap.clear()
        _entityGrowthMap.clear()
        _entityRewardMap.clear()
        _categoryEffectivenessMap.clear()
    }
}