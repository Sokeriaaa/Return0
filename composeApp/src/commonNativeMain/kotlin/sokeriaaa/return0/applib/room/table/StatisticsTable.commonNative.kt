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
import androidx.room.PrimaryKey

@Entity(
    tableName = StatisticsTable.TABLE_NAME,
)
actual class StatisticsTable actual constructor(
    @PrimaryKey
    @ColumnInfo(name = "save_id")
    actual var saveID: Int,
    @ColumnInfo(name = "tokens_earned")
    actual var tokensEarned: Long,
    @ColumnInfo(name = "cryptos_earned")
    actual var cryptosEarned: Long,
    @ColumnInfo(name = "total_damage")
    actual var totalDamage: Long,
    @ColumnInfo(name = "total_heal")
    actual var totalHeal: Long,
    @ColumnInfo(name = "enemies_defeated")
    actual var enemiesDefeated: Long,
    @ColumnInfo(name = "lines_moved")
    actual var linesMoved: Long
) {
    companion object {
        const val TABLE_NAME = "return0_statistics"
    }
}