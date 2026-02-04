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
package sokeriaaa.return0.ui.common.event.interactive

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_shop_available_count
import return0.composeapp.generated.resources.game_shop_not_available
import return0.composeapp.generated.resources.game_shop_on_sale
import return0.composeapp.generated.resources.game_shop_restock_after
import return0.composeapp.generated.resources.game_shop_sold_out
import sokeriaaa.return0.models.story.event.interactive.ShopItem
import sokeriaaa.return0.shared.common.helpers.TimeHelper
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.shared.data.models.story.event.interactive.ItemEntry
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData
import sokeriaaa.return0.ui.common.res.InventoryRes
import sokeriaaa.return0.ui.common.widgets.currency.CurrencyRow
import sokeriaaa.return0.ui.common.widgets.item.ClickableCommonItemCard
import sokeriaaa.return0.ui.theme.AppColor

@Composable
fun ShopDisplayItem(
    modifier: Modifier = Modifier,
    item: ShopItem,
    onClick: () -> Unit,
) {
    ClickableCommonItemCard(
        modifier = modifier,
        iconRes = InventoryRes.iconOfType(item.itemType),
        iconContentDescription = null,
        label = item.name,
        supportingText = when {
            // Not available
            !item.isAvailable -> stringResource(Res.string.game_shop_not_available)
            // No limit
            item.limit == null -> stringResource(Res.string.game_shop_on_sale)
            // Has limit
            item.limit > 0 -> stringResource(
                resource = Res.string.game_shop_available_count,
                /* count = */ item.limit,
            )
            // Sold out
            else -> if (item.refreshAfter == null) {
                stringResource(Res.string.game_shop_sold_out)
            } else {
                stringResource(
                    resource = Res.string.game_shop_restock_after,
                    /* restockAfter = */ TimeHelper.millisToString(item.refreshAfter),
                )
            }
        },
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
            // Price
            CurrencyRow(
                modifier = Modifier.padding(start = 4.dp),
                value = item.price.first,
                currencyType = item.price.second,
            )
        },
        onClick = onClick,
    )
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun ShopDisplayItemExample1() {
    ShopDisplayItem(
        item = ShopItem(
            key = "awesome_item",
            name = "Awesome Item",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            itemType = ItemData.Type.CONSUMABLE,
            rarity = ItemData.Rarity.UNCOMMON,
            price = 100 to CurrencyType.TOKEN,
            isAvailable = true,
            limit = null,
            refreshAfter = null,
            item = ItemEntry.Inventory(inventoryKey = "awesome_item")
        ),
        onClick = {},
    )
}

@Preview
@Composable
private fun ShopDisplayItemExample2() {
    ShopDisplayItem(
        item = ShopItem(
            key = "limited_item",
            name = "Limited Item",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            itemType = ItemData.Type.CONSUMABLE,
            rarity = ItemData.Rarity.LEGENDARY,
            price = 200 to CurrencyType.TOKEN,
            isAvailable = true,
            limit = 5,
            refreshAfter = null,
            item = ItemEntry.Inventory(inventoryKey = "limited_item")
        ),
        onClick = {},
    )
}

@Preview
@Composable
private fun ShopDisplayItemExample3() {
    ShopDisplayItem(
        item = ShopItem(
            key = "sold_out",
            name = "Sold Out",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            itemType = ItemData.Type.CONSUMABLE,
            rarity = ItemData.Rarity.COMMON,
            price = 500 to CurrencyType.TOKEN,
            isAvailable = true,
            limit = 0,
            refreshAfter = null,
            item = ItemEntry.Inventory(inventoryKey = "sold_out")
        ),
        onClick = {},
    )
}

@Preview
@Composable
private fun ShopDisplayItemExample4() {
    ShopDisplayItem(
        item = ShopItem(
            key = "restock_later",
            name = "Restock Later",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            itemType = ItemData.Type.CONSUMABLE,
            rarity = ItemData.Rarity.COMMON,
            price = 100 to CurrencyType.TOKEN,
            isAvailable = true,
            limit = 0,
            refreshAfter = TimeHelper.ONE_HOUR * 3,
            item = ItemEntry.Inventory(inventoryKey = "restock_later")
        ),
        onClick = {},
    )
}

@Preview
@Composable
private fun ShopDisplayItemExample5() {
    ShopDisplayItem(
        item = ShopItem(
            key = "future_item",
            name = "Future Item",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            itemType = ItemData.Type.CONSUMABLE,
            rarity = ItemData.Rarity.COMMON,
            price = 5 to CurrencyType.CRYPTO,
            isAvailable = false,
            limit = null,
            refreshAfter = TimeHelper.ONE_HOUR * 3,
            item = ItemEntry.Inventory(inventoryKey = "future_item")
        ),
        onClick = {},
    )
}