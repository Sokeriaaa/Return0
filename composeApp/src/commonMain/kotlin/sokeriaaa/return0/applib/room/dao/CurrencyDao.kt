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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sokeriaaa.return0.applib.room.table.CurrencyTable
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType

@Dao
interface CurrencyDao {
    /**
     * Query the currency value with specified save ID and currency type.
     */
    @Query(
        "SELECT * FROM `${CurrencyTable.TABLE_NAME}` WHERE `save_id`=:saveID AND `currency`=:currency"
    )
    suspend fun query(saveID: Int, currency: CurrencyType): CurrencyTable?

    /**
     * Query all the currency value with specified save ID.
     */
    @Query(
        "SELECT * FROM `${CurrencyTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun queryAll(saveID: Int): List<CurrencyTable>

    /**
     * Insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: CurrencyTable)

    /**
     * Insert a list of currency data.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<CurrencyTable>)

    /**
     * Delete the currency data for specified save ID.
     */
    @Query(
        "DELETE FROM `${CurrencyTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun delete(saveID: Int)
}