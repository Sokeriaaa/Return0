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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent

abstract class BaseViewModel : ViewModel() {

    var isLoading: Boolean by mutableStateOf(false)
        private set

    private val _snackBarIntents = MutableSharedFlow<CommonIntent.ShowSnackBar>()

    // SnackBar flow.
    val snackBarIntents = _snackBarIntents.asSharedFlow()

    open fun onIntent(intent: BaseIntent) {
        when (intent) {
            CommonIntent.ShowLoading -> isLoading = true
            CommonIntent.HideLoading -> isLoading = false
            is CommonIntent.ShowSnackBar -> viewModelScope.launch {
                _snackBarIntents.emit(intent)
            }
            else -> {}
        }
    }

}