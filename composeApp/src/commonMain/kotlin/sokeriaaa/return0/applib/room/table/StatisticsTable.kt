package sokeriaaa.return0.applib.room.table

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
/**
 * Statistics table.
 */
expect class StatisticsTable(
    saveID: Int,
    tokensEarned: Long = 0,
    cryptosEarned: Long = 0,
    totalDamage: Long = 0,
    totalHeal: Long = 0,
    enemiesDefeated: Long = 0,
    linesMoved: Long = 0,
) {
    /**
     * The save ID. -1 presents the temporary save the user is current playing.
     */
    var saveID: Int

    var tokensEarned: Long
    var cryptosEarned: Long
    var totalDamage: Long
    var totalHeal: Long
    var enemiesDefeated: Long
    var linesMoved: Long
}