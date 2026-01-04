/**
 * Copyright (C) 2025 Sokeriaaa
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
package sokeriaaa.return0.applib.repository.game.inventory

import androidx.compose.runtime.mutableStateMapOf
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.game.base.BaseGameRepo
import sokeriaaa.return0.applib.room.dao.InventoryDao
import sokeriaaa.return0.applib.room.table.InventoryTable

class GameInventoryRepo(
    private val inventoryDao: InventoryDao,
) : BaseGameRepo {

    /**
     * All items
     */
    private val _items: MutableMap<String, Int> = mutableStateMapOf()
    val items: Map<String, Int> = _items

    /**
     * Has item.
     */
    fun has(key: String, amount: Int = 1): Boolean = _items[key]?.let { it >= amount } ?: false

    operator fun get(key: String): Int = _items[key] ?: 0
    operator fun set(key: String, value: Int) {
        _items[key] = value
    }

    fun obtainLoot(loot: Map<String, Int>) {
        loot.forEach { (key: String, value: Int) ->
            this[key] += value
        }
    }

    override suspend fun load() {
        _items.clear()
        inventoryDao.queryAll(AppConstants.CURRENT_SAVE_ID).forEach {
            _items[it.key] = it.amount
        }
    }

    override suspend fun flush() {
        inventoryDao.delete(AppConstants.CURRENT_SAVE_ID)
        inventoryDao.insertList(
            _items.map {
                InventoryTable(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    key = it.key,
                    amount = it.value,
                )
            }
        )
    }

}