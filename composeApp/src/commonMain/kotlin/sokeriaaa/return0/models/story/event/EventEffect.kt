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
package sokeriaaa.return0.models.story.event

import sokeriaaa.return0.shared.data.models.combat.ArenaConfig
import sokeriaaa.return0.shared.data.models.story.event.Event

/**
 * Event effects emitted to the GameScreen.
 */
sealed interface EventEffect {
    /**
     * Show conversation text.
     */
    data class ShowText(
        val type: Type,
        val text: String,
    ) : EventEffect {
        sealed class Type {
            data object Narrator : Type()
            data object User : Type()
            data class NPC(val name: String) : Type()
        }
    }

    /**
     * Show choice panel.
     */
    data class ShowChoice(val choices: List<Pair<String, Event>>) : EventEffect

    /**
     * Display a snack bar.
     */
    data class ShowSnackBar(val text: String) : EventEffect

    /**
     * Start combat.
     */
    data class StartCombat(
        val config: ArenaConfig,
        val success: Event,
        val failure: Event,
    ) : EventEffect

    /**
     * Move player in the map animatedly.
     */
    data class MovePlayer(val line: Int) : EventEffect

    /**
     * Teleport the player to a specified location.
     */
    data class TeleportPlayer(val map: String, val line: Int) : EventEffect

    /**
     * Display a dialog to remind the player to save current progress.
     */
    data object RequestSave : EventEffect

    /**
     * Refresh the events of current map.
     */
    data object RefreshEvents : EventEffect

    /**
     * Finish the event.
     */
    data object EventFinished : EventEffect
}