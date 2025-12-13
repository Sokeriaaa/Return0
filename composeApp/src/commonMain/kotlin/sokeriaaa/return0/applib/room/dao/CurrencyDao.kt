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
package sokeriaaa.return0.applib.room.dao

import sokeriaaa.return0.applib.room.table.CurrencyTable
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType

expect interface CurrencyDao {
    /**
     * Query the currency value with specified save ID and currency type.
     */
    suspend fun query(saveID: Int, currency: CurrencyType): CurrencyTable?

    /**
     * Query all the currency value with specified save ID.
     */
    suspend fun queryAll(saveID: Int): List<CurrencyTable>

    /**
     * Insert or update.
     */
    suspend fun insertOrUpdate(table: CurrencyTable)

    /**
     * Insert a list of currency data.
     */
    suspend fun insertList(list: List<CurrencyTable>)

    /**
     * Delete the currency data for specified save ID.
     */
    suspend fun delete(saveID: Int)
}