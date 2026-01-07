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

import sokeriaaa.return0.applib.room.table.StatisticsTable

class StatisticsDaoImpl(
    private val queries: SQStatisticsDaoQueries,
) : StatisticsDao {
    override suspend fun queryAll(): List<StatisticsTable> {
        return queries.queryAll(mapper = ::convertTotable).executeAsList()
    }

    override suspend fun query(saveID: Int): StatisticsTable? {
        return queries.query(saveID.toLong(), mapper = ::convertTotable)
            .executeAsOneOrNull()
    }

    override suspend fun insertOrUpdate(table: StatisticsTable) {
        queries.insertOrUpdate(
            save_id = table.saveID.toLong(),
            tokens_earned = table.tokensEarned,
            cryptos_earned = table.cryptosEarned,
            total_damage = table.totalDamage,
            total_heal = table.totalHeal,
            enemies_defeated = table.enemiesDefeated,
            lines_moved = table.linesMoved,
        )
    }

    override suspend fun delete(saveID: Int) {
        queries.delete(saveID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertTotable(
        save_id: Long,
        tokens_earned: Long,
        cryptos_earned: Long,
        total_damage: Long,
        total_heal: Long,
        enemies_defeated: Long,
        lines_moved: Long,
    ): StatisticsTable = StatisticsTable(
        saveID = save_id.toInt(),
        tokensEarned = tokens_earned,
        cryptosEarned = cryptos_earned,
        totalDamage = total_damage,
        totalHeal = total_heal,
        enemiesDefeated = enemies_defeated,
        linesMoved = lines_moved,
    )
}