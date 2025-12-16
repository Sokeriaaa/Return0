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
package sokeriaaa.return0.applib.common

object AppConstants {
    const val MAXIMUM_SAVES = 5
    const val MAXIMUM_TEAMS = 5

    const val ENTRANCE_MAP = "entrance"

    const val BASE_CRITICAL_RATE: Float = 0.05F
    const val BASE_CRITICAL_DMG: Float = 0.5F
    const val BASE_TARGET_RATE: Float = 0.98F
    const val BASE_HIDE_RATE: Float = 0.02F

    const val DEFAULT_ATTACK_POWER: Int = 25

    const val ARENA_TICK_MILLIS = 16
    const val ARENA_PLAY_SPEED = 50L
    const val ARENA_MAX_PARTY = 4
    const val ARENA_MAX_ENEMY = 5
}