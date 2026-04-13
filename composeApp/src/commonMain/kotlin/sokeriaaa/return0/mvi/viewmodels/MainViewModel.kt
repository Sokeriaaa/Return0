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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import return0.composeapp.generated.resources.Res
import sokeriaaa.return0.applib.locale.LocaleHelper
import sokeriaaa.return0.applib.locale.getSystemLanguage
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.data.archive.ArchiveRepo
import sokeriaaa.return0.applib.repository.game.GameStateRepo
import sokeriaaa.return0.applib.repository.save.SaveRepo
import sokeriaaa.return0.mvi.intents.MainIntent
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.category.CategoryEffectiveness
import sokeriaaa.sugarkane.compose.mvi.intent.BaseIntent
import sokeriaaa.sugarkane.compose.mvi.intent.CommonIntent
import sokeriaaa.sugarkane.compose.mvi.viewmodel.BaseViewModel
import sokeriaaa.sugarkane.kelp.serialization.JsonHelper

class MainViewModel : BaseViewModel(), KoinComponent {

    // Repo
    private val _archiveRepo: ArchiveRepo by inject()
    private val _gameStateRepo: GameStateRepo by inject()
    private val _resourceRepo: ResourceRepo by inject()
    private val _saveRepo: SaveRepo by inject()

    // Data loading
    var loadingProgress by mutableIntStateOf(0)
        private set

    var totalProgress by mutableIntStateOf(1)
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
            _archiveRepo.loadAll(
                onProgress = { current, max ->
                    totalProgress = max
                    loadingProgress = current
                }
            )
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
            // TODO System language.
            _resourceRepo.load(LocaleHelper.getSystemLanguage())
            delay(1000)
            isLoadingFinished = true
        } catch (e: Exception) {
            loadingError = e
        }
    }

    private fun resetProgress() {
        loadingProgress = 0
        totalProgress = 1
        isLoadingFinished = false
        loadingError = null
    }
}