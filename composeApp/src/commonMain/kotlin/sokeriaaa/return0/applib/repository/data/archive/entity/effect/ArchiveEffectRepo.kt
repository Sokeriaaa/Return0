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
package sokeriaaa.return0.applib.repository.data.archive.entity.effect

import sokeriaaa.return0.models.action.effect.CommonEffects
import sokeriaaa.return0.shared.data.models.action.effect.EffectData

/**
 * Effect archives.
 */
class ArchiveEffectRepo {
    // All effect data.
    private val _effectNameDataMap: MutableMap<String, EffectData> = HashMap()

    init {
        // Register common data.
        _effectNameDataMap[CommonEffects.defense.name] = CommonEffects.defense
    }

    operator fun get(name: String): EffectData? = _effectNameDataMap[name]
    operator fun set(name: String, effectData: EffectData) {
        _effectNameDataMap[name] = effectData
    }

    fun add(effectData: EffectData) {
        this[effectData.name] = effectData
    }

    fun addAll(vararg effectData: EffectData) {
        effectData.forEach { this[it.name] = it }
    }

    /**
     * Returns all available effect data.
     */
    fun toList(): List<EffectData> = _effectNameDataMap.values.toList()

    /**
     * Reset all data.
     */
    fun reset() {
        _effectNameDataMap.clear()
    }
}
