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
package sokeriaaa.return0.ui.main.game.events.shop

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_shop
import return0.composeapp.generated.resources.game_shop_cart_empty
import return0.composeapp.generated.resources.game_shop_checkout
import return0.composeapp.generated.resources.game_shop_price_total
import return0.composeapp.generated.resources.ic_outline_check_24
import return0.composeapp.generated.resources.ic_outline_shopping_cart_checkout_24
import return0.composeapp.generated.resources.ic_outline_upcoming_24
import return0.composeapp.generated.resources.ok
import sokeriaaa.return0.models.story.event.interactive.ShopItem
import sokeriaaa.return0.mvi.intents.ShopIntent
import sokeriaaa.return0.mvi.viewmodels.ShopViewModel
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.shared.data.models.story.event.interactive.ItemEntry
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData
import sokeriaaa.return0.ui.common.res.InventoryRes
import sokeriaaa.return0.ui.common.screen.EmptyScreen
import sokeriaaa.return0.ui.common.widgets.AmountSelector
import sokeriaaa.return0.ui.common.widgets.currency.CurrencyRow
import sokeriaaa.return0.ui.common.widgets.item.CommonItemCard
import sokeriaaa.return0.ui.theme.AppColor
import sokeriaaa.sugarkane.compose.mvi.intent.BaseIntent
import sokeriaaa.sugarkane.compose.widgets.button.AppBackIconButton
import sokeriaaa.sugarkane.compose.widgets.button.AppButton
import sokeriaaa.sugarkane.compose.widgets.button.AppIconButton
import sokeriaaa.sugarkane.compose.widgets.scaffold.AppScaffold

@Composable
fun ShoppingCartScreen(
    viewModel: ShopViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            ShoppingCartTitle(
                modifier = Modifier.fillMaxWidth(),
                tokenValue = viewModel.tokenValue,
                cryptoValue = viewModel.cryptoValue,
                onBack = { mainNavHostController.navigateUp() },
            )
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues = paddingValues),
            targetState = viewModel.cartItems.isEmpty(),
            label = "ShoppingCartScreen",
        ) { isEmpty ->
            if (isEmpty) {
                EmptyScreen(
                    modifier = Modifier.fillMaxSize(),
                    iconRes = Res.drawable.ic_outline_upcoming_24,
                    label = stringResource(Res.string.game_shop_cart_empty),
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.weight(1F),
                    ) {
                        items(
                            items = viewModel.cartItems,
                            key = { it.first.key },
                        ) {
                            ShoppingCartItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 4.dp,
                                    )
                                    .animateItem(),
                                item = it.first,
                                amount = it.second,
                                onIntent = viewModel::onIntent,
                            )
                        }
                    }
                    // Total
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Text(stringResource(Res.string.game_shop_price_total))
                        Column(
                            modifier = Modifier.weight(1F),
                            horizontalAlignment = Alignment.End,
                        ) {
                            viewModel.cartItems
                                .groupingBy { it.first.price.second }
                                .fold(initialValue = 0) { acc, entry ->
                                    acc + (entry.first.price.first * entry.second)
                                }
                                .forEach {
                                    CurrencyRow(
                                        value = it.value,
                                        currencyType = it.key,
                                    )
                                }
                        }
                    }
                    // Actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                    ) {
                        Spacer(modifier = Modifier.weight(1F))
                        Spacer(modifier = Modifier.width(4.dp))
                        AppButton(
                            modifier = Modifier.weight(1F),
                            iconRes = Res.drawable.ic_outline_shopping_cart_checkout_24,
                            text = stringResource(Res.string.game_shop_checkout),
                            onClick = {
                                viewModel.onIntent(
                                    ShopIntent.CheckOut(onPurchased = {})
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShoppingCartTitle(
    modifier: Modifier = Modifier,
    tokenValue: Int,
    cryptoValue: Int,
    onBack: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(stringResource(Res.string.game_shop)) },
        navigationIcon = { AppBackIconButton(onClick = onBack) },
        actions = {
            Column(
                modifier = Modifier.padding(end = 4.dp),
                horizontalAlignment = Alignment.End,
            ) {
                CurrencyRow(
                    value = tokenValue,
                    currencyType = CurrencyType.TOKEN,
                )
                CurrencyRow(
                    value = cryptoValue,
                    currencyType = CurrencyType.CRYPTO,
                )
            }
        }
    )
}

@Composable
private fun ShoppingCartItem(
    modifier: Modifier = Modifier,
    item: ShopItem,
    amount: Int,
    onIntent: (BaseIntent) -> Unit,
) {
    CommonItemCard(
        modifier = modifier,
        iconRes = InventoryRes.iconOfType(item.itemType),
        label = item.name,
        supportingText = item.description,
        colors = CardDefaults.cardColors(
            containerColor = if (item.sorter == 0) {
                AppColor.alignColor(
                    source = when (item.rarity) {
                        ItemData.Rarity.COMMON -> AppColor.colorScheme.common
                        ItemData.Rarity.UNCOMMON -> AppColor.colorScheme.uncommon
                        ItemData.Rarity.RARE -> AppColor.colorScheme.rare
                        ItemData.Rarity.EPIC -> AppColor.colorScheme.epic
                        ItemData.Rarity.LEGENDARY -> AppColor.colorScheme.legendary
                    },
                    target = MaterialTheme.colorScheme.primaryContainer
                )
            } else {
                MaterialTheme.colorScheme.surfaceDim
            },
        ),
        trailingContent = {
            var isAlteringAmount by remember { mutableStateOf(false) }
            var selectedAmount by remember { mutableStateOf(0) }
            val focusManager = LocalFocusManager.current
            LaunchedEffect(isAlteringAmount) {
                if (isAlteringAmount) {
                    selectedAmount = amount
                }
            }
            if (isAlteringAmount) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AmountSelector(
                        amount = selectedAmount,
                        minimum = 0,
                        maximum = item.limit ?: Int.MAX_VALUE,
                        onAmountChange = { selectedAmount = it },
                        focusManager = focusManager,
                    )
                    AppIconButton(
                        iconRes = Res.drawable.ic_outline_check_24,
                        contentDescription = stringResource(Res.string.ok),
                        onClick = {
                            // Clear focus before sending intent.
                            focusManager.clearFocus()
                            onIntent(
                                ShopIntent.AlterCart(
                                    key = item.key,
                                    amountChange = selectedAmount - amount,
                                ),
                            )
                            isAlteringAmount = false
                        }
                    )
                }
            } else {
                // Amount & Price
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    OutlinedCard(
                        onClick = { isAlteringAmount = true },
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            text = "x$amount",
                        )
                    }
                    // Total
                    CurrencyRow(
                        value = item.price.first * amount,
                        currencyType = item.price.second,
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun ShoppingCartItemPreview() {
    ShoppingCartItem(
        item = ShopItem(
            key = "example_item",
            name = "Example Item",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            itemType = ItemData.Type.CONSUMABLE,
            rarity = ItemData.Rarity.COMMON,
            price = 100 to CurrencyType.TOKEN,
            isAvailable = true,
            limit = null,
            refreshAfter = null,
            item = ItemEntry.Inventory(
                inventoryKey = "example_item",
            ),
        ),
        amount = 42,
        onIntent = {},
    )
}
