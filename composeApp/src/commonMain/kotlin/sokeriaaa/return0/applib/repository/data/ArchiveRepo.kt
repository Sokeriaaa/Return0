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
package sokeriaaa.return0.applib.repository.data

import org.jetbrains.compose.resources.MissingResourceException
import return0.composeapp.generated.resources.Res
import sokeriaaa.return0.models.action.effect.CommonEffects
import sokeriaaa.return0.shared.common.helpers.JsonHelper
import sokeriaaa.return0.shared.data.models.action.effect.EffectData
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.category.CategoryEffectiveness
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginData
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData
import sokeriaaa.return0.shared.data.models.story.quest.QuestData

/**
 * Stores archives data in memory.
 */
class ArchiveRepo internal constructor() {
    private val _entityDataMap: MutableMap<String, EntityData> = HashMap()
    private val _effectDataMap: MutableMap<String, EffectData> = HashMap()
    private val _entityGrowthMap: MutableMap<Category, EntityGrowth> = HashMap()
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

    fun getEntityData(name: String): EntityData? = _entityDataMap[name]
    fun getEffectData(name: String): EffectData? = _effectDataMap[name]

    fun getEntityGrowthByCategory(category: Category): EntityGrowth = _entityGrowthMap[category]
        ?: error("EntityGrowth of the category $category is not registered.")

    fun getCategoryEffectiveness(category: Category): CategoryEffectiveness =
        _categoryEffectivenessMap[category] ?: CategoryEffectiveness(emptyMap(), emptyMap())

    suspend fun getItemData(key: String): ItemData? = try {
        JsonHelper.decodeFromString(
            string = Res.readBytes("files/data/inventory/$key.json").decodeToString()
        )
    } catch (_: MissingResourceException) {
        null
    }

    suspend fun getPluginData(key: String): PluginData? = try {
        JsonHelper.decodeFromString(
            string = Res.readBytes("files/data/plugin/$key.json").decodeToString()
        )
    } catch (_: MissingResourceException) {
        null
    }

    suspend fun getQuestData(key: String): QuestData? = try {
        JsonHelper.decodeFromString(
            string = Res.readBytes("files/data/quest/$key.json").decodeToString()
        )
    } catch (_: MissingResourceException) {
        null
    }

    /**
     * Returns all available entity data.
     */
    fun availableEntities(): List<EntityData> {
        return _entityDataMap.values.toList()
    }

    fun reset() {
        _entityDataMap.clear()
        _effectDataMap.clear()
        _entityGrowthMap.clear()
        _categoryEffectivenessMap.clear()
    }
}