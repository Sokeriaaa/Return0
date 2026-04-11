package sokeriaaa.return0.applib.repository.data.archive

import org.jetbrains.compose.resources.MissingResourceException
import return0.composeapp.generated.resources.Res
import sokeriaaa.kmpshared.helpers.JsonHelper
import sokeriaaa.return0.applib.repository.data.archive.entity.ArchiveEntityRepo
import sokeriaaa.return0.shared.data.models.Manifest
import sokeriaaa.return0.shared.data.models.action.effect.EffectData
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.category.CategoryEffectiveness
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginData
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData
import sokeriaaa.return0.shared.data.models.story.quest.QuestData

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
/**
 * Stores archives data in memory.
 */
class ArchiveRepo internal constructor(
    val entities: ArchiveEntityRepo,
) {
    private val _entityGrowthMap: MutableMap<Category, EntityGrowth> = HashMap()
    private val _categoryEffectivenessMap: MutableMap<Category, CategoryEffectiveness> = HashMap()


    suspend fun loadAll(
        onProgress: (current: Int, max: Int) -> Unit,
    ) {
        val manifest: Manifest = JsonHelper.decodeFromString(
            Res.readBytes("files/data/manifest.json").decodeToString(),
        )
        val maxProgress = manifest.entryCount
        var currentProgress = 0
        // Load entities.
        manifest.entities.list.forEach {
            val entityData: EntityData = JsonHelper.decodeFromString(
                Res.readBytes("files/data/entity/$it.json").decodeToString(),
            )
            entities[entityData.name] = entityData
            currentProgress++
            onProgress(currentProgress, maxProgress)
        }
        manifest.entities.effects.list.forEach {
            val effectData: EffectData = JsonHelper.decodeFromString(
                Res.readBytes("files/data/entity/effect/$it.json").decodeToString(),
            )
            entities.effects.add(effectData)
            currentProgress++
            onProgress(currentProgress, maxProgress)
        }
        manifest.entities.skills.list.forEach {
            val functionData: FunctionData = JsonHelper.decodeFromString(
                Res.readBytes("files/data/entity/skill/$it.json").decodeToString(),
            )
            entities.functions.add(functionData)
            currentProgress++
            onProgress(currentProgress, maxProgress)
        }
    }

    fun registerEntityGrowths(entityGrowthMap: Map<Category, EntityGrowth>) {
        _entityGrowthMap.putAll(entityGrowthMap)
    }

    fun registerCategoryEffectiveness(map: Map<Category, CategoryEffectiveness>) {
        _categoryEffectivenessMap.putAll(map)
    }

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

    fun reset() {
        entities.reset()
        _entityGrowthMap.clear()
        _categoryEffectivenessMap.clear()
    }
}