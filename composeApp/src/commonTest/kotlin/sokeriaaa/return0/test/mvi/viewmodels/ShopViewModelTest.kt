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
import sokeriaaa.return0.models.component.context.EventContext
import sokeriaaa.return0.mvi.intents.ShopIntent
import sokeriaaa.return0.mvi.viewmodels.ShopViewModel
import sokeriaaa.return0.shared.data.api.story.event.interactive.buildShop
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.test.applib.modules.TestKoinModules
import sokeriaaa.return0.test.models.story.event.BaseEventTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ShopViewModelTest : BaseEventTest() {

    @Test
    fun `Initialize executes correctly`() {
        withViewModel {
            withContext(callback = object : TestingCallback() {}) {
                onIntent(
                    ShopIntent.Initialize(
                        context = it,
                        shopEvent = buildShop("testing_shop") {
                            inventory("awesome_item") soldFor 100.token
                            inventory("epic_item") soldFor 5.crypto
                        },
                    ),
                )
            }
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

    private inline fun withViewModel(
        block: ShopViewModel.(EventContext) -> Unit,
    ) {
        TestKoinModules.withModules {
            Dispatchers.setMain(StandardTestDispatcher())
            val viewModel = ShopViewModel()
            Dispatchers.resetMain()
        }
    }
}