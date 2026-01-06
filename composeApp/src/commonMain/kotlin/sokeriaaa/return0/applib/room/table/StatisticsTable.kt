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

/**
 * Statistics table.
 *
 * @param saveID The save ID. -1 presents the temporary save the user is current playing.
 * @param tokensEarned
 * @param cryptosEarned
 * @param totalDamage
 * @param totalHeal
 * @param enemiesDefeated
 * @param linesMoved
 */
@Entity(
    tableName = StatisticsTable.TABLE_NAME,
)
data class StatisticsTable(
    @PrimaryKey
    @ColumnInfo(name = "save_id")
    var saveID: Int,
    @ColumnInfo(name = "tokens_earned")
    var tokensEarned: Long = 0,
    @ColumnInfo(name = "cryptos_earned")
    var cryptosEarned: Long = 0,
    @ColumnInfo(name = "total_damage")
    var totalDamage: Long = 0,
    @ColumnInfo(name = "total_heal")
    var totalHeal: Long = 0,
    @ColumnInfo(name = "enemies_defeated")
    var enemiesDefeated: Long = 0,
    @ColumnInfo(name = "lines_moved")
    var linesMoved: Long = 0,
) {
    companion object {
        const val TABLE_NAME = "return0_statistics"
    }
}