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
import sokeriaaa.return0.shared.data.models.entity.EnemyData
import sokeriaaa.return0.shared.data.models.entity.PartyData
import sokeriaaa.return0.temp.TempData

/**
 * Stores archives data in memory.
 */
class ArchiveRepo internal constructor() {
    private val _partyDataMap: MutableMap<String, PartyData> = HashMap()
    private val _enemyDataMap: MutableMap<String, EnemyData> = HashMap()
    private val _effectDataMap: MutableMap<String, EffectData> = HashMap()

    init {
        // Register common data.
        _effectDataMap[CommonEffects.defense.name] = CommonEffects.defense
        // Temp
        _effectDataMap[TempData.optimizedEffect.name] = TempData.optimizedEffect
    }

    fun getPartyData(name: String): PartyData? = _partyDataMap[name]
    fun getEnemyData(name: String): EnemyData? = _enemyDataMap[name]
    fun getEffectData(name: String): EffectData? = _effectDataMap[name]
}