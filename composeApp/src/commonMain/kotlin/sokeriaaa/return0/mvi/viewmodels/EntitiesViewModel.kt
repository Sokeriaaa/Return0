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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.game.entity.GameEntityRepo
import sokeriaaa.return0.models.entity.display.EntityProfile
import sokeriaaa.sugarkane.compose.mvi.BaseIntent
import sokeriaaa.sugarkane.compose.mvi.BaseViewModel
import sokeriaaa.sugarkane.compose.mvi.CommonIntent

class EntitiesViewModel : BaseViewModel(), KoinComponent {

    // Repo
    private val _entityRepo: GameEntityRepo by inject()

    // All entities
    private val _entities: MutableList<EntityProfile> = mutableStateListOf()
    val entities: List<EntityProfile> = _entities

    // Sorter
    var orderBy by mutableStateOf(OrderBy.LEVEL)
        private set
    var isDescending by mutableStateOf(true)
        private set

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            CommonIntent.Refresh -> viewModelScope.launch {
                refresh()
            }

            else -> {}
        }
    }

    private suspend fun refresh() {
        _entities.clear()
        _entities.addAll(
            _entityRepo.queryAll().mapNotNull {
                _entityRepo.getEntityProfileByTable(it)
            }
        )

    }

    enum class OrderBy {
        NAME,
        LEVEL,
    }

}