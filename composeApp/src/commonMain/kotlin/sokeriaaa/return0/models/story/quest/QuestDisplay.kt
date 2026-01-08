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
package sokeriaaa.return0.models.story.quest

import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType

data class QuestDisplay(
    val name: String,
    val description: String,
    val priority: Int,
    val navigation: Pair<String, Int>? = null,
    val currencyRewards: Map<CurrencyType, Int>? = null,
    val inventoryRewards: Map<String, Int>? = null,
    val expiredAt: Long? = null,
)