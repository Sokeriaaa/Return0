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
import sokeriaaa.return0.shared.data.models.story.event.interactive.ItemEntry
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData

data class WorkbenchItem(
    override val key: String,
    override val name: String,
    override val itemType: ItemData.Type,
    override val price: Pair<Int, CurrencyType>?,
    override val isAvailable: Boolean,
    override val limit: Int?,
    override val refreshAfter: Long?,
    val input: List<ItemEntry>,
    val output: List<ItemEntry>,
) : InteractiveItem {
    override val sorter: Int = when {
        !isAvailable -> 3
        limit == 0 -> if (refreshAfter == null) 2 else 1
        else -> 0
    }
}