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

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * The manager class for entity animations during the combat.
 */
class EntityAnimatorManager {

    private val _entityAnimators: MutableStateFlow<Map<Int, List<EntityAnimator>>> =
        MutableStateFlow(emptyMap())

    /**
     * Entity animators. User the entity index as key.
     */
    val entityAnimators: StateFlow<Map<Int, List<EntityAnimator>>> = _entityAnimators

    /**
     * Trigger a new animation.
     */
    fun triggerAnimation(index: Int, animator: EntityAnimator) {
        _entityAnimators.update { current ->
            val newList = (current[index] ?: emptyList()) + animator
            current + (index to newList)
        }
    }

    /**
     * Remove a finished animation.
     */
    fun clearAnimator(index: Int, animator: EntityAnimator) {
        _entityAnimators.update { current ->
            val newList = current[index]?.filterNot { it == animator } ?: emptyList()
            current + (index to newList)
        }
    }

    /**
     * Reset the animator.
     */
    fun reset() {
        _entityAnimators.update { emptyMap() }
    }
}