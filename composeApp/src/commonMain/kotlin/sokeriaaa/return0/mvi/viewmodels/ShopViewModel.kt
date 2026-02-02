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
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.core.component.inject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_shop_purchase_successful
import return0.composeapp.generated.resources.game_shop_warn_insufficient
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
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
import sokeriaaa.return0.shared.data.models.story.event.interactive.ItemEntry
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData
import kotlin.random.Random

class ShopViewModel : BaseViewModel() {

    // Repo
    private val _archiveRepo: ArchiveRepo by inject()
    private val _gameStateRepo: GameStateRepo by inject()
    private val _resourceRepo: ResourceRepo by inject()

    // Shop event data
    private lateinit var context: EventContext
    private lateinit var shopEvent: Event.Shop

    // Items
    private val _items: MutableMap<String, ShopItem> = mutableStateMapOf()
    val items: Map<String, ShopItem> = _items

    // Currency values
    val tokenValue: Int get() = _gameStateRepo.currency[CurrencyType.TOKEN]
    val cryptoValue: Int get() = _gameStateRepo.currency[CurrencyType.CRYPTO]

    // Cart
    private val _cart: MutableMap<String, Int> = mutableStateMapOf()
    val cart: Map<String, Int> = _cart

    private val _cartItems: MutableList<Pair<ShopItem, Int>> = mutableStateListOf()
    val cartItems: List<Pair<ShopItem, Int>> = _cartItems

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            CommonIntent.Refresh -> viewModelScope.launch { refresh() }
            is ShopIntent.Initialize -> {
                _cart.clear()
                context = intent.context
                shopEvent = intent.shopEvent
                viewModelScope.launch { refresh() }
            }

            is ShopIntent.AlterCart -> {
                _cart[intent.key] = (_cart[intent.key] ?: 0) + intent.amountChange
                if (_cart[intent.key] == 0) {
                    _cart.remove(intent.key)
                }
                refreshCartItems()
            }

            is ShopIntent.RemoveFromCart -> {
                _cart.remove(intent.key)
                refreshCartItems()
            }

            ShopIntent.CheckOut -> viewModelScope.launch {
                val prices = cartItems
                    .groupingBy { it.first.price.second }
                    .fold(initialValue = 0) { acc, entry ->
                        acc + (entry.first.price.first * entry.second)
                    }
                if (checkBalance(prices)) {
                    cartItems.forEach { (item, amount) ->
                        executePurchase(item, amount)
                    }
                    _cart.clear()
                    _cartItems.clear()
                    refresh()
                    onIntent(
                        intent = CommonIntent.ShowSnackBar(
                            message = getString(Res.string.game_shop_purchase_successful),
                        ),
                    )
                } else {
                    onIntent(
                        intent = CommonIntent.ShowSnackBar(
                            message = getString(Res.string.game_shop_warn_insufficient),
                        ),
                    )
                }
            }

            is ShopIntent.Buy -> viewModelScope.launch {
                val item = items[intent.key] ?: return@launch
                if (checkBalance(item.price, intent.amount)) {
                    executePurchase(item, intent.amount)
                    refresh()
                    onIntent(
                        intent = CommonIntent.ShowSnackBar(
                            message = getString(Res.string.game_shop_purchase_successful),
                        ),
                    )
                } else {
                    onIntent(
                        intent = CommonIntent.ShowSnackBar(
                            message = getString(Res.string.game_shop_warn_insufficient),
                        ),
                    )
                }
            }

            else -> {}
        }
    }

    private suspend fun refresh() {
        _items.clear()
        _items.putAll(
            shopEvent.entries.map {
                ShopItem(
                    key = it.item.key,
                    name = _resourceRepo.getString(it.item.resourceKey),
                    description = _resourceRepo.getString(it.item.resourceDescKey),
                    itemType = when (val item = it.item) {
                        is ItemEntry.Inventory -> _archiveRepo.getItemData(item.inventoryKey)
                            ?.types?.firstOrNull() ?: ItemData.Type.OTHER

                        is ItemEntry.Plugin -> ItemData.Type.PLUGIN
                    },
                    rarity = when (val item = it.item) {
                        is ItemEntry.Inventory -> _archiveRepo.getItemData(item.inventoryKey)
                            ?.rarity ?: ItemData.Rarity.COMMON

                        is ItemEntry.Plugin ->
                            ItemData.Rarity.entries[(item.tier - 1.coerceAtLeast(0))]
                    },
                    price = (it.price?.calculatedIn(context) ?: 0) to
                            (it.currency ?: CurrencyType.TOKEN),
                    isAvailable = it.isAvailable.calculatedIn(context),
                    limit = it.limit?.calculatedIn(context),
                    refreshAfter = it.refreshAfter?.calculateTime(context),
                    item = it.item,
                )
            }.sortedBy { it.sorter }.associateBy { it.key }
        )
        refreshCartItems()
    }

    private fun refreshCartItems() {
        _cartItems.clear()
        _cartItems.addAll(
            cart.entries.mapNotNull {
                val item = items[it.key] ?: return@mapNotNull null
                // Trim the cart item amount to the new upper limit.
                val updatedAmount = it.value.coerceAtMost(item.limit ?: Int.MAX_VALUE)
                if (updatedAmount <= 0) return@mapNotNull null
                item to updatedAmount
            }
        )
    }

    private fun checkBalance(
        price: Pair<Int, CurrencyType>,
        amount: Int,
    ): Boolean = _gameStateRepo.currency[price.second] >= price.first * amount

    private fun checkBalance(
        prices: Map<CurrencyType, Int>,
    ): Boolean = prices.all {
        _gameStateRepo.currency[it.key] >= it.value
    }

    private suspend fun executePurchase(
        item: ShopItem,
        amount: Int,
    ) {
        // Currency change.
        val finalPrice = item.price.first * amount
        _gameStateRepo.currency[item.price.second] -= finalPrice
        // Obtain items
        when (item.item) {
            is ItemEntry.Inventory -> {
                _gameStateRepo.inventory[item.item.inventoryKey] += amount
            }

            is ItemEntry.Plugin -> {
                repeat(amount) {
                    val pluginID = _gameStateRepo.plugin.generateAndSavePlugin(
                        // TODO generate a random plugin on null key.
                        key = item.item.pluginKey ?: TODO(),
                        tier = if (item.item.tier == 0) {
                            Random.nextInt(1, 6)
                        } else {
                            item.item.tier
                        }
                    )
                    _gameStateRepo.plugin.obtainedPlugin(pluginID)
                }
            }
        }
    }
}