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
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.game.quest.GameQuestRepo
import sokeriaaa.return0.models.story.quest.QuestDisplay
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent

class QuestsViewModel : BaseViewModel() {
    // Repo
    private val _archiveRepo: ArchiveRepo by inject()
    private val _questRepo: GameQuestRepo by inject()
    private val _resourceRepo: ResourceRepo by inject()

    private val _activatedQuests: MutableList<QuestDisplay> = mutableStateListOf()
    val activatedQuests: List<QuestDisplay> = _activatedQuests

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
        onIntent(CommonIntent.ShowLoading)
        _activatedQuests.clear()
        _activatedQuests.addAll(
            _questRepo.activatedQuests.mapNotNull {
                val data = _archiveRepo.getQuestData(it.key) ?: return@mapNotNull null
                QuestDisplay(
                    name = _resourceRepo.getString("quest.${it.key}"),
                    description = _resourceRepo.getString("quest.${it.key}.desc"),
                    priority = data.priority,
                    navigation = data.navigation,
                    currencyRewards = data.currencyRewards,
                    inventoryRewards = data.inventoryRewards,
                    expiredAt = it.value,
                )
            }
        )
        _activatedQuests.sortBy { it.priority }
        onIntent(CommonIntent.HideLoading)
    }

}