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
package sokeriaaa.return0.models.story.event.interactive

import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData

/**
 * Super class of the interactive items.
 */
interface InteractiveItem {
    /**
     * Unique key in this shop/workbench.
     */
    val key: String

    /**
     * Display name.
     */
    val name: String

    /**
     * Item type
     */
    val itemType: ItemData.Type

    /**
     * Price.
     */
    val price: Pair<Int, CurrencyType>?

    /**
     * Is available.
     */
    val isAvailable: Boolean

    /**
     * Limit.
     */
    val limit: Int?

    /**
     * Refresh after.
     */
    val refreshAfter: Long?

    /**
     * A sorter for the items. The larger this value is, the further down the list.
     */
    val sorter: Int
}