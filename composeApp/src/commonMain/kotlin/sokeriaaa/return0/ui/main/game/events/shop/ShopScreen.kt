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

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import return0.composeapp.generated.resources.game_shop_buy
import return0.composeapp.generated.resources.game_shop_cart
import return0.composeapp.generated.resources.game_shop_cart_add
import return0.composeapp.generated.resources.game_shop_not_available
import return0.composeapp.generated.resources.game_shop_qty
import return0.composeapp.generated.resources.game_shop_restock_after
import return0.composeapp.generated.resources.game_shop_sold_out
import return0.composeapp.generated.resources.game_shop_warn_cart_lost
import return0.composeapp.generated.resources.game_shop_warn_leave
import return0.composeapp.generated.resources.ic_outline_add_shopping_cart_24
import return0.composeapp.generated.resources.ic_outline_payments_24
import return0.composeapp.generated.resources.ic_outline_shopping_cart_24
import sokeriaaa.kmpshared.helpers.TimeHelper
import sokeriaaa.return0.models.story.event.interactive.ShopItem
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.GameIntent
import sokeriaaa.return0.mvi.intents.ShopIntent
import sokeriaaa.return0.mvi.viewmodels.GameViewModel
import sokeriaaa.return0.mvi.viewmodels.ShopViewModel
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.shared.data.models.story.event.interactive.ItemEntry
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData
import sokeriaaa.return0.ui.common.AppAdaptiveScaffold
import sokeriaaa.return0.ui.common.AppBackHandler
import sokeriaaa.return0.ui.common.event.interactive.ShopDisplayItem
import sokeriaaa.return0.ui.common.rememberAppAdaptiveScaffoldState
import sokeriaaa.return0.ui.common.widgets.AmountSelectorContent
import sokeriaaa.return0.ui.common.widgets.AppAlertDialog
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton
import sokeriaaa.return0.ui.common.widgets.AppButton
import sokeriaaa.return0.ui.common.widgets.AppFilledTonalButton
import sokeriaaa.return0.ui.common.widgets.AppFilledTonalIconButton
import sokeriaaa.return0.ui.common.widgets.currency.CurrencyRow
import sokeriaaa.return0.ui.nav.Scene
import sokeriaaa.return0.ui.nav.navigateSingleTop

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    val state = rememberAppAdaptiveScaffoldState(windowAdaptiveInfo)
    val gameViewModel: GameViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    )
    var isShowingLeaveWarning: Boolean by remember { mutableStateOf(false) }
    val navigateUp: () -> Unit = {
        gameViewModel.onIntent(GameIntent.EventContinue)
        mainNavHostController.navigateUp()
    }
    val onBack: () -> Unit = {
        if (state.isWideScreen || !state.isShowingPane) {
            if (viewModel.cartItems.isEmpty()) {
                navigateUp()
            } else {
                isShowingLeaveWarning = true
            }
        } else {
            state.hidePane()
        }
    }

    var selectedItem: ShopItem? by remember { mutableStateOf(null) }

    AppBackHandler(onBack = onBack)
    AppAdaptiveScaffold(
        viewModel = viewModel,
        topBar = {
            ShopScreenTitle(
                modifier = Modifier.fillMaxWidth(),
                tokenValue = viewModel.tokenValue,
                cryptoValue = viewModel.cryptoValue,
                itemCount = viewModel.cartItems.sumOf { it.second },
                onOpenCart = { mainNavHostController.navigateSingleTop(Scene.ShoppingCart.route) },
                onBack = onBack,
            )
        },
        state = state,
        mainContent = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    items = viewModel.items,
                    key = { it.key },
                ) {
                    ShopDisplayItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 10.dp,
                                vertical = 4.dp,
                            )
                            .animateItem(),
                        item = it,
                        onClick = {
                            selectedItem = it
                            state.showPane()
                        }
                    )
                }
            }
        },
        paneContent = {
            selectedItem?.let {
                ShopDetails(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    item = it,
                    onIntent = viewModel::onIntent,
                    onPurchased = {
                        // Refresh item.
                        selectedItem = viewModel.itemKeyMap[it.key]
                    }
                )
            }
        },
    )
    if (isShowingLeaveWarning) {
        AppAlertDialog(
            title = stringResource(Res.string.game_shop_warn_leave),
            text = stringResource(Res.string.game_shop_warn_cart_lost),
            onDismiss = { isShowingLeaveWarning = false },
            onConfirmed = {
                isShowingLeaveWarning = false
                navigateUp()
            }
        )
    }
}

@Composable
private fun ShopDetails(
    modifier: Modifier = Modifier,
    item: ShopItem,
    onIntent: (BaseIntent) -> Unit,
    onPurchased: () -> Unit,
) {
    var amount: Int by remember { mutableIntStateOf(1) }
    val animatedTotalPrice by animateIntAsState(
        targetValue = item.price.first * amount,
        label = "AnimatedTotalPrice",
    )
    // Reset amount when item is changed.
    LaunchedEffect(item.name) {
        amount = 1
    }
    Column(modifier = modifier) {
        // Display
        Column(
            modifier = Modifier
                .weight(1F)
                .verticalScroll(state = rememberScrollState()),
        ) {
            // Name
            Text(
                text = item.name,
                style = MaterialTheme.typography.displaySmall
            )
            // Price
            CurrencyRow(
                modifier = Modifier.padding(top = 4.dp),
                value = item.price.first,
                currencyType = item.price.second,
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Description
            Text(item.description)
        }
        // Buy panel
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!item.isAvailable) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = stringResource(Res.string.game_shop_not_available),
                )
            } else if (item.limit == null || item.limit > 0) {
                val focusManager = LocalFocusManager.current
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.game_shop_qty),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    AmountSelectorContent(
                        amount = amount,
                        maximum = item.limit ?: Int.MAX_VALUE,
                        focusManager = focusManager,
                        onAmountChange = { amount = it },
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    // Total
                    CurrencyRow(
                        value = animatedTotalPrice,
                        currencyType = item.price.second,
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    AppFilledTonalButton(
                        modifier = Modifier.weight(1F),
                        iconRes = Res.drawable.ic_outline_add_shopping_cart_24,
                        text = stringResource(Res.string.game_shop_cart_add),
                        onClick = {
                            // Clear focus before sending intent.
                            focusManager.clearFocus()
                            onIntent(
                                ShopIntent.AlterCart(
                                    key = item.key,
                                    amountChange = amount
                                ),
                            )
                            // Reset amount after adding to cart.
                            amount = 1
                        },
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    AppButton(
                        modifier = Modifier.weight(1F),
                        iconRes = Res.drawable.ic_outline_payments_24,
                        text = stringResource(Res.string.game_shop_buy),
                        onClick = {
                            // Clear focus before sending intent.
                            focusManager.clearFocus()
                            onIntent(
                                ShopIntent.Buy(
                                    key = item.key,
                                    amount = amount,
                                    onPurchased = onPurchased,
                                )
                            )
                            // Reset amount after purchasing.
                            amount = 1
                        },
                    )
                }
            } else {
                Text(
                    modifier = Modifier.padding(vertical = 24.dp),
                    text = if (item.refreshAfter == null) {
                        stringResource(Res.string.game_shop_sold_out)
                    } else {
                        stringResource(
                            resource = Res.string.game_shop_restock_after,
                            /* restockAfter = */ TimeHelper.millisToString(item.refreshAfter),
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShopScreenTitle(
    modifier: Modifier = Modifier,
    tokenValue: Int,
    cryptoValue: Int,
    itemCount: Int,
    onOpenCart: () -> Unit,
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
            BadgedBox(
                badge = {
                    when {
                        itemCount > 99 -> Badge { Text("99+") }
                        itemCount > 0 -> Badge { Text("$itemCount") }
                    }
                }
            ) {
                AppFilledTonalIconButton(
                    iconRes = Res.drawable.ic_outline_shopping_cart_24,
                    contentDescription = stringResource(Res.string.game_shop_cart),
                    onClick = onOpenCart,
                )
            }
        }
    )
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun ShopScreenTitlePreview() {
    ShopScreenTitle(
        tokenValue = 123456,
        cryptoValue = 123,
        itemCount = 42,
        onOpenCart = {},
        onBack = {},
    )
}

@Preview
@Composable
private fun ShopDetailsPreview() {
    ShopDetails(
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
        onIntent = {},
        onPurchased = {},
    )
}
