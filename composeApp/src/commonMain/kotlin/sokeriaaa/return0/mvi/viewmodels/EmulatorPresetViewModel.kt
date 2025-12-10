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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.ArchiveRepo
import sokeriaaa.return0.applib.room.dao.EmulatorEntryDao
import sokeriaaa.return0.applib.room.dao.EmulatorIndexDao
import sokeriaaa.return0.applib.room.table.EmulatorEntryTable
import sokeriaaa.return0.applib.room.table.EmulatorIndexTable
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.EmulatorPresetIntent
import sokeriaaa.return0.shared.data.models.combat.EnemyState
import sokeriaaa.return0.shared.data.models.combat.PartyState

class EmulatorPresetViewModel : BaseViewModel() {

    private val _archiveRepo: ArchiveRepo by inject()

    private val _emulatorIndexDao: EmulatorIndexDao by inject()
    private val _emulatorEntryDao: EmulatorEntryDao by inject()

    /**
     * Emulator preset index list.
     */
    private val _emulatorIndexList: MutableList<EmulatorIndexTable> = mutableStateListOf()
    val emulatorIndexList: List<EmulatorIndexTable> = _emulatorIndexList

    /**
     * Selected preset index.
     */
    var selectedPresetIndex: EmulatorIndexTable? by mutableStateOf(null)
        private set

    /**
     * The entries of the selected preset.
     */
    var selectedEntries: List<EmulatorEntryTable>? by mutableStateOf(null)
        private set

    /**
     * Refresh
     */
    fun refreshPresetIndices() {
        viewModelScope.launch {
            _emulatorIndexList.clear()
            _emulatorIndexList.addAll(_emulatorIndexDao.queryAll())
        }
    }

    /**
     * Convert current preset to ready-to-play entity states.
     */
    fun getEntityState(): Pair<List<PartyState>, List<EnemyState>> {
        val parties = ArrayList<PartyState>()
        val enemies = ArrayList<EnemyState>()
        selectedEntries?.forEach {
            val entityData = _archiveRepo.getEntityData(it.entityName) ?: return@forEach
            if (it.isParty) {
                parties.add(PartyState(entityData, it.level))
            } else {
                enemies.add(EnemyState(entityData, it.level))
            }
        }
        return parties to enemies
    }

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            is EmulatorPresetIntent.OpenPresetDialog -> {
                selectedPresetIndex = intent.presetIndex
                // Query entries
                viewModelScope.launch {
                    selectedEntries = intent.presetIndex.presetID?.let {
                        _emulatorEntryDao.queryList(it)
                    }
                }
            }

            EmulatorPresetIntent.DismissPresetDialog -> {
                selectedPresetIndex = null
                selectedEntries = null
            }

            is EmulatorPresetIntent.RenamePreset -> {
                viewModelScope.launch {
                    selectedPresetIndex?.presetID?.let {
                        _emulatorIndexDao.rename(it, intent.name)
                        selectedPresetIndex = null
                        selectedEntries = null
                    }
                }
            }

            EmulatorPresetIntent.DeletePreset -> {
                viewModelScope.launch {
                    selectedPresetIndex?.presetID?.let {
                        _emulatorIndexDao.delete(it)
                        selectedPresetIndex = null
                        selectedEntries = null
                    }
                }
            }

            else -> {}
        }
    }
}