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

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.game.GameStateRepo
import sokeriaaa.return0.models.component.context.EventContext
import sokeriaaa.return0.models.component.executor.condition.calculatedIn
import sokeriaaa.return0.models.component.executor.value.calculateTime
import sokeriaaa.return0.models.component.executor.value.calculatedIn
import sokeriaaa.return0.models.story.event.interactive.ShopItem
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.intents.ShopIntent
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.shared.data.models.story.event.Event

class ShopViewModel : BaseViewModel() {

    // Repo
    private val _gameStateRepo: GameStateRepo by inject()
    private val _resourceRepo: ResourceRepo by inject()

    // Shop event data
    private lateinit var context: EventContext
    private lateinit var shopEvent: Event.Shop

    // Items
    private val _items: MutableList<ShopItem> = mutableStateListOf()
    val items: List<ShopItem> = _items

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            CommonIntent.Refresh -> viewModelScope.launch { refresh() }
            is ShopIntent.Initialize -> {
                context = intent.context
                shopEvent = intent.shopEvent
                viewModelScope.launch { refresh() }
            }

            else -> {}
        }
    }

    private suspend fun refresh() {
        _items.clear()
        _items.addAll(
            shopEvent.entries.map {
                ShopItem(
                    key = it.item.key,
                    name = _resourceRepo.getString(it.item.resourceKey),
                    price = (it.price?.calculatedIn(context) ?: 0) to
                            (it.currency ?: CurrencyType.TOKEN),
                    isAvailable = it.isAvailable.calculatedIn(context),
                    limit = it.limit?.calculatedIn(context),
                    refreshAfter = it.refreshAfter?.calculateTime(context),
                    item = it.item,
                )
            }.sortedBy { it.sorter }
        )
    }
}