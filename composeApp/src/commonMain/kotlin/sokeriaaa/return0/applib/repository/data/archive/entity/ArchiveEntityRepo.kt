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
package sokeriaaa.return0.applib.repository.data.archive.entity

import sokeriaaa.return0.applib.repository.data.archive.entity.effect.ArchiveEffectRepo
import sokeriaaa.return0.applib.repository.data.archive.entity.function.ArchiveFunctionRepo
import sokeriaaa.return0.shared.data.models.entity.EntityData

/**
 * Entity archives.
 */
class ArchiveEntityRepo(
    val effects: ArchiveEffectRepo,
    val functions: ArchiveFunctionRepo,
) {
    // All entity data.
    private val _entityNameDataMap: MutableMap<String, EntityData> = HashMap()

    operator fun get(name: String): EntityData? = _entityNameDataMap[name]
    operator fun set(name: String, entityData: EntityData) {
        _entityNameDataMap[name] = entityData
    }

    fun add(entityData: EntityData) {
        this[entityData.name] = entityData
    }

    fun addAll(vararg entityData: EntityData) {
        entityData.forEach { this[it.name] = it }
    }

    /**
     * Returns all available entity data.
     */
    fun toList(): List<EntityData> = _entityNameDataMap.values.toList()

    /**
     * Resets all data.
     */
    fun reset() {
        _entityNameDataMap.clear()
        effects.reset()
        functions.reset()
    }
}
