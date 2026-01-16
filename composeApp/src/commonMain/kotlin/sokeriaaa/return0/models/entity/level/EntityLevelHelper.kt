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
package sokeriaaa.return0.models.entity.level

import sokeriaaa.return0.applib.common.AppConstants
import kotlin.math.sqrt

/**
 * Helper for calculating the entity EXP and level.
 *
 * TotalEXP(level) = levelPacing * level * (level - 1)
 */
object EntityLevelHelper {

    /**
     * Calculate the current level based on total exp.
     */
    fun levelFromTotalExp(totalExp: Int, levelPacing: Int): Int {
        val level = (1 + sqrt(1.0 + 4.0 * totalExp / levelPacing)) / 2
        return level.toInt().coerceIn(1, AppConstants.ENTITY_MAX_LEVEL)
    }

    /**
     * Exp required to reach the level.
     */
    fun expRequiredToReach(level: Int, levelPacing: Int): Int {
        return levelPacing * level * (level - 1)
    }

    /**
     * Exp required for next level.
     */
    fun expRequiredForNextLevel(level: Int, levelPacing: Int): Int {
        return levelPacing * level * 2
    }

    /**
     * Calculate the level progress to next level in 0F..1F
     */
    fun levelProgressByLevel(
        level: Int,
        totalExp: Int,
        levelPacing: Int,
    ): Float {
        if (level >= AppConstants.ENTITY_MAX_LEVEL) {
            return 1F
        }
        val expCurrent = expRequiredToReach(level, levelPacing)
        val expNext = expRequiredToReach(level + 1, levelPacing)
        return levelProgressByExp(totalExp, expCurrent, expNext)
    }

    /**
     * Calculate the level progress to next level in 0F..1F
     */
    fun levelProgressByExp(
        totalExp: Int,
        expCurrent: Int,
        expNext: Int,
    ): Float {
        return ((totalExp - expCurrent).toFloat() / (expNext - expCurrent)).coerceIn(0F, 1F)
    }

    /**
     * Base exp obtained from defeating an enemy after combat finished.
     *
     * Note: Failed entities can not obtain exp from combat.
     */
    fun enemyExp(
        partyLevel: Int,
        enemyLevel: Int,
    ): Float {
        return ((5 + enemyLevel) * 5 * ((enemyLevel + 10F) / (partyLevel + 10F)).coerceAtMost(5F))
    }
}