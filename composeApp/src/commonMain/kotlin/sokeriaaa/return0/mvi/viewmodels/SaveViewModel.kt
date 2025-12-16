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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.GameStateRepo
import sokeriaaa.return0.applib.room.dao.SaveMetaDao
import sokeriaaa.return0.applib.room.table.SaveMetaTable
import sokeriaaa.return0.mvi.intents.CommonIntent

class SaveViewModel(
    private val isSaving: Boolean,
) : BaseViewModel() {

    private var _saveMap: Map<Int, SaveMetaTable> by mutableStateOf(emptyMap())
    val saveMap: Map<Int, SaveMetaTable> get() = _saveMap

    /**
     * Game state Repo.
     */
    private val _gameStateRepo: GameStateRepo by inject()

    private val _saveMetaDao: SaveMetaDao by inject()

    fun refresh() {
        viewModelScope.launch {
            onIntent(CommonIntent.ShowLoading)
            _saveMap = _saveMetaDao.queryAll().associateBy { it.saveID }
            onIntent(CommonIntent.HideLoading)
        }
    }

    fun readFrom(
        saveID: Int,
        onFinished: () -> Unit,
    ) {
        viewModelScope.launch {
            onIntent(CommonIntent.ShowLoading)
            _gameStateRepo.migrateSave(saveID, -1)
            _gameStateRepo.load()
            onIntent(CommonIntent.HideLoading)
            onFinished()
        }
    }

    fun saveTo(
        saveID: Int,
        onFinished: () -> Unit,
    ) {
        viewModelScope.launch {
            onIntent(CommonIntent.ShowLoading)
            _gameStateRepo.migrateSave(-1, saveID)
            onIntent(CommonIntent.HideLoading)
            onFinished()
        }
    }

    companion object {
        val isSavingKey = object : CreationExtras.Key<Boolean> {}
    }

}