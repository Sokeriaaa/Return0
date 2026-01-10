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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.game.inventory.GameInventoryRepo
import sokeriaaa.return0.applib.repository.settings.SettingsRepo
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData

class InventoryViewModel : BaseViewModel() {

    // Repo
    private val _archiveRepo: ArchiveRepo by inject()
    private val _inventoryRepo: GameInventoryRepo by inject()
    private val _resourceRepo: ResourceRepo by inject()
    private val _settingRepo: SettingsRepo by inject()

    var isShowDescription by mutableStateOf(false)
        private set

    private val _items: MutableList<ItemDisplay> = mutableStateListOf()
    val items: List<ItemDisplay> = _items

    init {
        viewModelScope.launch {
            _settingRepo.gameplayDisplayItemDesc.flow.collect {
                isShowDescription = it
            }
        }
    }

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
        _items.clear()
        onIntent(CommonIntent.ShowLoading)
        _inventoryRepo.items.forEach {
            val data = _archiveRepo.getItemData(it.key)
            if (data != null) {
                _items.add(
                    ItemDisplay(
                        name = _resourceRepo.getString("inventory.${it.key}"),
                        description = _resourceRepo.getString("inventory.${it.key}.desc"),
                        itemData = data,
                        amount = it.value,
                    ),
                )
            }
        }
        onIntent(CommonIntent.HideLoading)
    }

    data class ItemDisplay(
        val name: String,
        val description: String,
        val itemData: ItemData,
        val amount: Int,
    )

}