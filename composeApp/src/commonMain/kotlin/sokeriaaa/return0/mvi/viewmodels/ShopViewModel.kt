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
import sokeriaaa.return0.shared.common.helpers.TimeHelper
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
    private val _itemKeyMap: MutableMap<String, ShopItem> = mutableStateMapOf()
    val itemKeyMap: Map<String, ShopItem> = _itemKeyMap
    private val _items: MutableList<ShopItem> = mutableStateListOf()
    val items: List<ShopItem> = _items

    // Currency values
    val tokenValue: Int get() = _gameStateRepo.currency[CurrencyType.TOKEN]
    val cryptoValue: Int get() = _gameStateRepo.currency[CurrencyType.CRYPTO]

    // Cart
    private val _cartKeyAmountMap: MutableMap<String, Int> = mutableStateMapOf()
    private val _cartItems: MutableList<Pair<ShopItem, Int>> = mutableStateListOf()
    val cartItems: List<Pair<ShopItem, Int>> = _cartItems

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            CommonIntent.Refresh -> viewModelScope.launch { refresh() }
            is ShopIntent.Initialize -> {
                if (::shopEvent.isInitialized && shopEvent != intent.shopEvent) {
                    // When the user opened a different shop, clear the cart.
                    _cartKeyAmountMap.clear()
                    _cartItems.clear()
                }
                context = intent.context
                shopEvent = intent.shopEvent
                viewModelScope.launch { refresh() }
            }

            is ShopIntent.AlterCart -> {
                // Calculate new amount.
                val newAmount = ((_cartKeyAmountMap[intent.key] ?: 0) + intent.amountChange)
                    .coerceAtMost(_itemKeyMap[intent.key]?.limit ?: Int.MAX_VALUE)
                if (newAmount > 0) {
                    // Update new amount.
                    _cartKeyAmountMap[intent.key] = newAmount
                } else {
                    // Remove when amount less than or equals 0.
                    _cartKeyAmountMap.remove(intent.key)
                }
                // Refresh.
                refreshCartItems()
            }

            is ShopIntent.RemoveFromCart -> {
                _cartKeyAmountMap.remove(intent.key)
                refreshCartItems()
            }

            is ShopIntent.CheckOut -> viewModelScope.launch {
                val prices = cartItems
                    .groupingBy { it.first.price.second }
                    .fold(initialValue = 0) { acc, entry ->
                        acc + (entry.first.price.first * entry.second)
                    }
                if (checkBalance(prices)) {
                    cartItems.forEach { (item, amount) ->
                        executePurchase(item, amount)
                    }
                    _cartKeyAmountMap.clear()
                    _cartItems.clear()
                    refresh()
                    onIntent(
                        intent = CommonIntent.ShowSnackBar(
                            message = getString(Res.string.game_shop_purchase_successful),
                        ),
                    )
                    intent.onPurchased()
                } else {
                    onIntent(
                        intent = CommonIntent.ShowSnackBar(
                            message = getString(Res.string.game_shop_warn_insufficient),
                        ),
                    )
                }
            }

            is ShopIntent.Buy -> viewModelScope.launch {
                val item = _itemKeyMap[intent.key] ?: return@launch
                if (checkBalance(item.price, intent.amount)) {
                    executePurchase(item, intent.amount)
                    refresh()
                    onIntent(
                        intent = CommonIntent.ShowSnackBar(
                            message = getString(Res.string.game_shop_purchase_successful),
                        ),
                    )
                    intent.onPurchased()
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
        _itemKeyMap.clear()
        _itemKeyMap.putAll(
            shopEvent.entries.map {
                // Check refresh time.
                val refreshAfter = getRefreshTime(it.item.key)
                if (refreshAfter <= context.now) {
                    // Reset purchased count.
                    setAlreadyPurchasedCount(it.item.key, 0)
                }
                // Refresh limit.
                val alreadyPurchasedCount = getAlreadyPurchasedCount(it.item.key)
                val canPurchase = it.limit?.calculatedIn(context)?.minus(alreadyPurchasedCount)

                // Assemble item.
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
                    limit = canPurchase,
                    refreshAfter = it.refreshAfter?.calculateTime(context),
                    item = it.item,
                )
            }.associateBy { it.key }
        )
        // Refresh list
        _items.clear()
        _items.addAll(_itemKeyMap.values.sortedBy { it.sorter })
        // Refresh cart items.
        refreshCartItems()
    }

    private fun refreshCartItems() {
        _cartItems.clear()
        _cartItems.addAll(
            _cartKeyAmountMap.entries.mapNotNull {
                val item = _itemKeyMap[it.key] ?: return@mapNotNull null
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

    private suspend fun getAlreadyPurchasedCount(key: String): Int =
        _gameStateRepo.savedValues.getVariable("shop:${shopEvent.key}:$key")

    private fun setAlreadyPurchasedCount(key: String, value: Int) {
        _gameStateRepo.savedValues.setVariable("shop:${shopEvent.key}:$key", value)
    }

    private suspend fun getRefreshTime(key: String?): Long =
        _gameStateRepo.savedValues.getTimeStamp("shop:${shopEvent.key}:$key")

    private fun setRefreshTime(key: String, refreshAfter: Long) {
        _gameStateRepo.savedValues.setTimestamp("shop:${shopEvent.key}:$key", refreshAfter)
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
        val alreadyPurchasedCount = getAlreadyPurchasedCount(item.key)
        // Update refresh time.
        if (item.refreshAfter != null && alreadyPurchasedCount <= 0) {
            setRefreshTime(item.key, TimeHelper.currentTimeMillis() + item.refreshAfter)
        }
        // Update purchased count.
        if (item.limit != null) {
            setAlreadyPurchasedCount(item.key, alreadyPurchasedCount + amount)
        }
    }
}