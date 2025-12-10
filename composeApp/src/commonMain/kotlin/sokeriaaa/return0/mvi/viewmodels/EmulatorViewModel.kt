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
package sokeriaaa.return0.mvi.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import return0.composeapp.generated.resources.Res
import sokeriaaa.return0.applib.repository.ArchiveRepo
import sokeriaaa.return0.applib.room.dao.EmulatorEntryDao
import sokeriaaa.return0.applib.room.dao.EmulatorIndexDao
import sokeriaaa.return0.applib.room.table.EmulatorEntryTable
import sokeriaaa.return0.applib.room.table.EmulatorIndexTable
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.intents.EmulatorIntent
import sokeriaaa.return0.shared.common.helpers.JsonHelper
import sokeriaaa.return0.shared.data.models.action.effect.EffectData
import sokeriaaa.return0.shared.data.models.combat.EnemyState
import sokeriaaa.return0.shared.data.models.combat.PartyState
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.category.CategoryEffectiveness
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * The emulator that allows to start custom combats for testing/debugging.
 */
class EmulatorViewModel : BaseViewModel() {

    private val _archiveRepo: ArchiveRepo by inject()

    private val _availableEntities: MutableList<EntityData> = mutableStateListOf()
    val availableEntities: List<EntityData> = _availableEntities

    private val _parties: MutableList<PartyState> = mutableStateListOf()
    val parties: List<PartyState> = _parties

    private val _enemies: MutableList<EnemyState> = mutableStateListOf()
    val enemies: List<EnemyState> = _enemies

    private val _emulatorIndexDao: EmulatorIndexDao by inject()
    private val _emulatorEntryDao: EmulatorEntryDao by inject()

    init {
        // Temp code: Load jsons from resources.
        viewModelScope.launch {
            // Entities
            JsonHelper.decodeFromString<List<EntityData>>(
                string = Res.readBytes("files/data/entity.json").decodeToString(),
            ).let {
                _archiveRepo.registerEntities(it)
                _availableEntities.addAll(it)
            }
            // Effects
            JsonHelper.decodeFromString<List<EffectData>>(
                string = Res.readBytes("files/data/effect.json").decodeToString(),
            ).let {
                _archiveRepo.registerEffects(it)
            }
            // Category: Entity growth
            JsonHelper.decodeFromString<Map<Category, EntityGrowth>>(
                string = Res.readBytes("files/data/category_entity_growth.json").decodeToString()
            ).let {
                _archiveRepo.registerEntityGrowths(it)
            }
            // Category: Effectiveness
            JsonHelper.decodeFromString<Map<Category, CategoryEffectiveness>>(
                string = Res.readBytes("files/data/category_effectiveness.json").decodeToString()
            ).let {
                _archiveRepo.registerCategoryEffectiveness(it)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            is EmulatorIntent.AddEntity -> {
                when (intent.entityState) {
                    is PartyState -> _parties.add(intent.entityState)
                    is EnemyState -> _enemies.add(intent.entityState)
                }
            }

            is EmulatorIntent.AlterEntity -> {
                when (intent.before) {
                    is PartyState -> {
                        _parties[_parties.indexOf(intent.before)] = intent.after as PartyState
                    }

                    is EnemyState -> {
                        _enemies[_enemies.indexOf(intent.before)] = intent.after as EnemyState
                    }
                }
            }

            is EmulatorIntent.RemoveEntity -> {
                when (intent.entityState) {
                    is PartyState -> _parties.remove(intent.entityState)
                    is EnemyState -> _enemies.remove(intent.entityState)
                }
            }

            is EmulatorIntent.SavePreset -> {
                val createdTime = Clock.System.now().toEpochMilliseconds()
                // Create index.
                viewModelScope.launch {
                    onIntent(CommonIntent.ShowLoading)
                    val presetID = _emulatorIndexDao.insert(
                        EmulatorIndexTable(
                            // Use created time as default name.
                            name = createdTime.toString(),
                            createdTime = createdTime,
                        )
                    ).toInt()
                    // Save entries
                    val list = ArrayList<EmulatorEntryTable>()
                    parties.forEach {
                        list.add(
                            EmulatorEntryTable(
                                presetID = presetID,
                                isParty = true,
                                entityName = it.entityData.name,
                                level = it.level,
                                // Preserved future use: Plugin
                                pluginID = null,
                                // Preserved future use: Boss multiplier
                                bossMultiplier = 1,
                            )
                        )
                    }
                    enemies.forEach {
                        list.add(
                            EmulatorEntryTable(
                                presetID = presetID,
                                isParty = false,
                                entityName = it.entityData.name,
                                level = it.level,
                                // Preserved future use: Plugin
                                pluginID = null,
                                // Preserved future use: Boss multiplier
                                bossMultiplier = 1,
                            )
                        )
                    }
                    // Insert to database
                    _emulatorEntryDao.insertList(list)
                    onIntent(CommonIntent.HideLoading)
                }
            }

            is EmulatorIntent.LoadPreset -> {
                // Replace current selection with the preset selected.
                _parties.clear()
                _parties.addAll(intent.parties)
                _enemies.clear()
                _enemies.addAll(intent.enemies)
            }

            else -> {}
        }
    }

}