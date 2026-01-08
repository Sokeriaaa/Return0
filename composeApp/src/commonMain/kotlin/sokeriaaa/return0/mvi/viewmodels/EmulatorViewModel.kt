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
import org.jetbrains.compose.resources.getString
import org.koin.core.component.inject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.emulator_preset_save_success
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.emulator.EmulatorRepo
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.intents.EmulatorIntent
import sokeriaaa.return0.shared.common.helpers.TimeHelper
import sokeriaaa.return0.shared.data.models.combat.EnemyState
import sokeriaaa.return0.shared.data.models.combat.PartyState
import sokeriaaa.return0.shared.data.models.entity.EntityData

/**
 * The emulator that allows to start custom combats for testing/debugging.
 */
class EmulatorViewModel : BaseViewModel() {

    // Repo
    private val _archiveRepo: ArchiveRepo by inject()
    private val _emulatorRepo: EmulatorRepo by inject()

    val availableEntities: List<EntityData> = _archiveRepo.availableEntities()

    private val _parties: MutableList<PartyState> = mutableStateListOf()
    val parties: List<PartyState> = _parties

    private val _enemies: MutableList<EnemyState> = mutableStateListOf()
    val enemies: List<EnemyState> = _enemies

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
                val createdTime = TimeHelper.currentTimeMillis()
                // Create index.
                viewModelScope.launch {
                    onIntent(CommonIntent.ShowLoading)
                    _emulatorRepo.savePreset(parties, enemies)
                    onIntent(CommonIntent.HideLoading)
                    onIntent(
                        CommonIntent.ShowSnackBar(
                            getString(Res.string.emulator_preset_save_success),
                        ),
                    )
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