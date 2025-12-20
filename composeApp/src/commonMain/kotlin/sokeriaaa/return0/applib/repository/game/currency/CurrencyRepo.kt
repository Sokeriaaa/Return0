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
package sokeriaaa.return0.applib.repository.game.currency

import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.game.base.BaseGameRepo
import sokeriaaa.return0.applib.room.dao.CurrencyDao
import sokeriaaa.return0.applib.room.table.CurrencyTable
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType

class CurrencyRepo(
    private val currencyDao: CurrencyDao,
) : BaseGameRepo {

    private val _currencies: MutableMap<CurrencyType, Int> = HashMap()

    operator fun get(type: CurrencyType): Int {
        return _currencies[type] ?: 0
    }

    operator fun set(type: CurrencyType, value: Int) {
        _currencies[type] = value
    }

    override suspend fun load() {
        CurrencyType.entries.forEach { currency ->
            _currencies[currency] =
                currencyDao.query(AppConstants.CURRENT_SAVE_ID, currency)?.amount ?: 0
        }
    }

    override suspend fun flush() {
        currencyDao.delete(AppConstants.CURRENT_SAVE_ID)
        _currencies.forEach { entry ->
            currencyDao.insertOrUpdate(
                table = CurrencyTable(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    currency = entry.key,
                    amount = entry.value,
                ),
            )
        }
    }
}