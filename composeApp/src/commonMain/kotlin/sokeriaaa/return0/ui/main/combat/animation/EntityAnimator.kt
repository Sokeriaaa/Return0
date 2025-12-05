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
package sokeriaaa.return0.ui.main.combat.animation

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * An animator for the entities during the combat.
 */
sealed class EntityAnimator {
    data object Shake : EntityAnimator()
    data class Glow(val color: Color) : EntityAnimator()

    data class FloatingText(
        val id: Long = Random.nextLong(),
        val text: String,
        val color: Color,
        val isCritical: Boolean = false,
        val xOffset: Float = Random.nextFloat() * 100F - 50F,
        val yOffset: Float = Random.nextFloat() * -20F - 40F,
    ) : EntityAnimator()
}