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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import return0.composeapp.generated.resources.Res
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.game.GameStateRepo
import sokeriaaa.return0.applib.repository.game.SaveRepo
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.intents.MainIntent
import sokeriaaa.return0.shared.common.helpers.JsonHelper
import sokeriaaa.return0.shared.data.models.action.effect.EffectData
import sokeriaaa.return0.shared.data.models.entity.EntityData
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.category.CategoryEffectiveness

class MainViewModel : BaseViewModel() {

    // Repo
    private val _archiveRepo: ArchiveRepo by inject()
    private val _gameStateRepo: GameStateRepo by inject()
    private val _resourceRepo: ResourceRepo by inject()
    private val _saveRepo: SaveRepo by inject()

    // Data loading
    var loadingProgress by mutableIntStateOf(0)
        private set

    var isLoadingFinished by mutableStateOf(false)
        private set

    var loadingError: Throwable? by mutableStateOf(null)
        private set

    init {
        onIntent(MainIntent.LoadArchives)
    }

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            MainIntent.LoadArchives -> {
                viewModelScope.launch {
                    loadArchives()
                }
            }

            is MainIntent.StartNewGame -> {
                viewModelScope.launch {
                    onIntent(CommonIntent.ShowLoading)
                    _saveRepo.newGame()
                    _gameStateRepo.load()
                    onIntent(CommonIntent.HideLoading)
                    intent.onLoadFinished()
                }
            }

            else -> {}
        }
    }

    /**
     * (Re)load archive data.
     */
    private suspend fun loadArchives() {
        try {
            resetProgress()
            // Entities
            JsonHelper.decodeFromString<List<EntityData>>(
                string = Res.readBytes("files/data/entity.json").decodeToString(),
            ).let {
                _archiveRepo.registerEntities(it)
            }
            updateProgress(1)
            // Effects
            JsonHelper.decodeFromString<List<EffectData>>(
                string = Res.readBytes("files/data/effect.json").decodeToString(),
            ).let {
                _archiveRepo.registerEffects(it)
            }
            updateProgress(2)
            // Category: Entity growth
            JsonHelper.decodeFromString<Map<Category, EntityGrowth>>(
                string = Res.readBytes("files/data/category_entity_growth.json").decodeToString()
            ).let {
                _archiveRepo.registerEntityGrowths(it)
            }
            updateProgress(3)
            // Category: Effectiveness
            JsonHelper.decodeFromString<Map<Category, CategoryEffectiveness>>(
                string = Res.readBytes("files/data/category_effectiveness.json").decodeToString()
            ).let {
                _archiveRepo.registerCategoryEffectiveness(it)
            }
            updateProgress(4)
            // TODO System language.
            _resourceRepo.load("en-us")
            updateProgress(5)
            delay(1000)
            isLoadingFinished = true
        } catch (e: Exception) {
            loadingError = e
        }
    }

    private fun resetProgress() {
        loadingProgress = 0
        isLoadingFinished = false
        loadingError = null
    }

    private fun updateProgress(tasksCompleted: Int) {
        loadingProgress = tasksCompleted * 100 / TOTAL_PROGRESS
    }

    companion object {
        private const val TOTAL_PROGRESS = 5
    }
}