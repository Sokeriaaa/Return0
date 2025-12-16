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

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.GameStateRepo
import sokeriaaa.return0.mvi.intents.CommonIntent

class MainViewModel : BaseViewModel() {

    /**
     * Game state repo.
     */
    private val _gameStateRepo: GameStateRepo by inject()

    fun startNewGame(
        onLoadFinished: () -> Unit
    ) {
        viewModelScope.launch {
            onIntent(CommonIntent.ShowLoading)
            _gameStateRepo.newGame()
            onIntent(CommonIntent.HideLoading)
            onLoadFinished()
        }
    }
}