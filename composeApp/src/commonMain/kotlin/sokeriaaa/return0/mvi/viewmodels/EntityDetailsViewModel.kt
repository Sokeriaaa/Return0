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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.game.entity.GameEntityRepo
import sokeriaaa.return0.applib.repository.game.entity.GamePluginRepo
import sokeriaaa.return0.models.entity.display.ExtendedEntityProfile
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent

class EntityDetailsViewModel(
    val entityName: String,
) : BaseViewModel() {

    // Repo
    private val _archiveRepo: ArchiveRepo by inject()
    private val _entityRepo: GameEntityRepo by inject()
    private val _pluginRepo: GamePluginRepo by inject()
    private val _resourceRepo: ResourceRepo by inject()

    var entityProfile: ExtendedEntityProfile? by mutableStateOf(null)
        private set

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            CommonIntent.Refresh -> viewModelScope.launch {
                refresh()
            }

            else -> Unit
        }
    }

    private suspend fun refresh() {
        entityProfile = _entityRepo.getExtendedEntityProfile(entityName)
    }

    companion object {
        val entityNameKey = object : CreationExtras.Key<String> {}
    }

}