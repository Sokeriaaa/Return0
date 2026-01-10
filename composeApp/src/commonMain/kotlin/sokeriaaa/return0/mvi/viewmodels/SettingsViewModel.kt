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
package sokeriaaa.return0.mvi.viewmodels

import androidx.lifecycle.viewmodel.CreationExtras
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.settings.SettingsRepo
import sokeriaaa.return0.applib.repository.settings.SettingsRepo.Entry

class SettingsViewModel(
    val isInGame: Boolean,
) : BaseViewModel() {

    // Repo
    private val _settingsRepo: SettingsRepo by inject()

    val gameplayDisplayItemDesc: Entry<Boolean> = _settingsRepo.gameplayDisplayItemDesc
    val combatAuto: Entry<Boolean> = _settingsRepo.combatAuto

    companion object {
        val isInGameKey = object : CreationExtras.Key<Boolean> {}
    }
}