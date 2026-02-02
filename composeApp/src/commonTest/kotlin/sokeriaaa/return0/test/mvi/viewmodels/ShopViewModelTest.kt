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
package sokeriaaa.return0.test.mvi.viewmodels

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import sokeriaaa.return0.mvi.intents.ShopIntent
import sokeriaaa.return0.mvi.viewmodels.ShopViewModel
import sokeriaaa.return0.shared.data.api.story.event.interactive.buildShop
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.test.annotations.AppRunner
import sokeriaaa.return0.test.annotations.RunWith
import sokeriaaa.return0.test.models.story.event.BaseEventTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AppRunner::class)
class ShopViewModelTest : BaseEventTest() {

    @Test
    fun `Initialize executes correctly`() {
        withContext(callback = object : TestingCallback() {}) {
            withViewModel {
                onIntent(
                    ShopIntent.Initialize(
                        context = it,
                        shopEvent = buildShop("testing_shop") {
                            inventory("awesome_item") soldFor 100.token
                            inventory("epic_item") soldFor 5.crypto
                        },
                    ),
                )
                // Keys
                assertEquals(
                    expected = listOf("awesome_item", "epic_item"),
                    actual = items.keys.toList(),
                )
                // Prices
                assertEquals(
                    expected = listOf(100 to CurrencyType.TOKEN, 5 to CurrencyType.CRYPTO),
                    actual = items.values.map { it.price },
                )
            }
        }
    }

    @Test
    fun `AlterCart executes correctly - Normal`() {
        withContext(callback = object : TestingCallback() {}) {
            withViewModel {
                // Initialize shop
                onIntent(
                    ShopIntent.Initialize(
                        context = it,
                        shopEvent = buildShop("testing_shop") {
                            inventory("awesome_item") soldFor 100.token
                            inventory("epic_item") soldFor 5.crypto
                        },
                    ),
                )
                // Add to cart
                onIntent(ShopIntent.AlterCart("awesome_item", 42))
                assertEquals(
                    expected = mapOf("awesome_item" to 42),
                    actual = cartItems.associate { it.first.key to it.second },
                )
                // Decrease amount
                onIntent(ShopIntent.AlterCart("awesome_item", -24))
                assertEquals(
                    expected = mapOf("awesome_item" to 18),
                    actual = cartItems.associate { it.first.key to it.second },
                )
            }
        }
    }

    @Test
    fun `AlterCart executes correctly - Upper limit`() {
        withContext(callback = object : TestingCallback() {}) {
            withViewModel {
                // Initialize shop
                onIntent(
                    ShopIntent.Initialize(
                        context = it,
                        shopEvent = buildShop("testing_shop") {
                            inventory("awesome_item") soldFor 100.token limitFor 10
                            inventory("epic_item") soldFor 5.crypto
                        },
                    ),
                )
                // Add to cart
                onIntent(ShopIntent.AlterCart("awesome_item", 42))
                assertEquals(
                    expected = mapOf("awesome_item" to 10),
                    actual = cartItems.associate { it.first.key to it.second },
                )
            }
        }
    }

    @Test
    fun `RemoveFromCart executes correctly`() {
        withContext(callback = object : TestingCallback() {}) {
            withViewModel {
                // Initialize shop
                onIntent(
                    ShopIntent.Initialize(
                        context = it,
                        shopEvent = buildShop("testing_shop") {
                            inventory("awesome_item") soldFor 100.token
                            inventory("epic_item") soldFor 5.crypto
                        },
                    ),
                )
                // Add to cart
                onIntent(ShopIntent.AlterCart("awesome_item", 42))
                assertEquals(
                    expected = mapOf("awesome_item" to 42),
                    actual = cartItems.associate { it.first.key to it.second },
                )
                // Remove from cart
                onIntent(ShopIntent.RemoveFromCart("awesome_item"))
                assertFalse { cartItems.any { it.first.key == "awesome_item" } }
            }
        }
    }

    @Test
    fun `Buy executes correctly - Normal`() {
        withContext(callback = object : TestingCallback() {}) {
            withViewModel {
                // Initialize currency
                it.gameState.currency[CurrencyType.TOKEN] += 5000
                // Initialize shop
                onIntent(
                    ShopIntent.Initialize(
                        context = it,
                        shopEvent = buildShop("testing_shop") {
                            inventory("awesome_item") soldFor 100.token
                            inventory("epic_item") soldFor 5.crypto
                        },
                    ),
                )
                // Buy
                onIntent(
                    intent = ShopIntent.Buy(
                        key = "awesome_item",
                        amount = 1,
                        onPurchased = {
                            // Asserts
                            assertEquals(4900, it.gameState.currency[CurrencyType.TOKEN])
                            assertEquals(1, it.gameState.inventory["awesome_item"])
                        },
                    )
                )
            }
        }
    }

    @Test
    fun `Buy executes correctly - Insufficient balance`() {
        withContext(callback = object : TestingCallback() {}) {
            withViewModel {
                // Initialize currency
                it.gameState.currency[CurrencyType.TOKEN] += 5000
                // Initialize shop
                onIntent(
                    ShopIntent.Initialize(
                        context = it,
                        shopEvent = buildShop("testing_shop") {
                            inventory("awesome_item") soldFor 100.token
                            inventory("epic_item") soldFor 5.crypto
                        },
                    ),
                )
                // Buy
                onIntent(
                    intent = ShopIntent.Buy(
                        key = "awesome_item",
                        amount = 100,
                        onPurchased = {
                            // Purchase failed
                            assertEquals(5000, it.gameState.currency[CurrencyType.TOKEN])
                            assertEquals(0, it.gameState.inventory["awesome_item"])
                        },
                    ),
                )
            }
        }
    }

    @Test
    fun `Checkout executes correctly - Normal`() {
        withContext(callback = object : TestingCallback() {}) {
            withViewModel {
                // Initialize currency
                it.gameState.currency[CurrencyType.TOKEN] += 5000
                it.gameState.currency[CurrencyType.CRYPTO] += 10
                // Initialize shop
                onIntent(
                    ShopIntent.Initialize(
                        context = it,
                        shopEvent = buildShop("testing_shop") {
                            inventory("awesome_item") soldFor 100.token
                            inventory("epic_item") soldFor 5.crypto
                        },
                    ),
                )
                // Add to cart
                onIntent(ShopIntent.AlterCart("awesome_item", 3))
                onIntent(ShopIntent.AlterCart("epic_item", 1))
                onIntent(
                    intent = ShopIntent.CheckOut(
                        onPurchased = {
                            // Asserts
                            assertEquals(4700, it.gameState.currency[CurrencyType.TOKEN])
                            assertEquals(5, it.gameState.currency[CurrencyType.CRYPTO])
                            assertEquals(3, it.gameState.inventory["awesome_item"])
                            assertEquals(1, it.gameState.inventory["epic_item"])
                        }
                    ),
                )
            }
        }
    }

    @Test
    fun `Checkout executes correctly - Insufficient balance`() {
        withContext(callback = object : TestingCallback() {}) {
            withViewModel {
                // Initialize currency
                it.gameState.currency[CurrencyType.TOKEN] += 5000
                it.gameState.currency[CurrencyType.CRYPTO] += 10
                // Initialize shop
                onIntent(
                    ShopIntent.Initialize(
                        context = it,
                        shopEvent = buildShop("testing_shop") {
                            inventory("awesome_item") soldFor 100.token
                            inventory("epic_item") soldFor 5.crypto
                        },
                    ),
                )
                // Add to cart
                onIntent(ShopIntent.AlterCart("awesome_item", 3))
                onIntent(ShopIntent.AlterCart("epic_item", 10))
                onIntent(ShopIntent.CheckOut {})
                // Purchase failed
                assertEquals(5000, it.gameState.currency[CurrencyType.TOKEN])
                assertEquals(10, it.gameState.currency[CurrencyType.CRYPTO])
                assertEquals(0, it.gameState.inventory["awesome_item"])
                assertEquals(0, it.gameState.inventory["epic_item"])
            }
        }
    }

    private inline fun withViewModel(
        block: ShopViewModel.() -> Unit,
    ) {
        Dispatchers.setMain(StandardTestDispatcher())
        val viewModel = ShopViewModel()
        viewModel.block()
        Dispatchers.resetMain()
    }
}