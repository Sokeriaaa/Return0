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
package sokeriaaa.return0.applib.room.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType

@Entity(
    tableName = CurrencyTable.TABLE_NAME,
    primaryKeys = ["save_id", "currency"],
)
actual class CurrencyTable actual constructor(
    @ColumnInfo(name = "save_id")
    actual var saveID: Int,
    @ColumnInfo(name = "currency")
    actual var currency: CurrencyType,
    @ColumnInfo(name = "amount")
    actual var amount: Int
) {
    companion object {
        const val TABLE_NAME = "return0_currencies"
    }
}