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
     * Show a tips.
     */
    data class Tips(
        val text: String,
    ) : EventEffect

    /**
     * Show choice panel.
     *
     * @param selected Selected indices for a single effect.
     */
    data class ShowChoice(
        val choices: List<String>,
        val selected: Set<Int>? = null,
    ) : EventEffect

    /**
     * Display a snack bar.
     */
    data class ShowSnackBar(val text: String) : EventEffect

    /**
     * Start combat.
     */
    data class StartCombat(
        val config: ArenaConfig,
    ) : EventEffect

    data class RouteHub(
        val currentLocation: Pair<String, Int>,
    ) : EventEffect

    /**
     * Move player in the map animatedly.
     */
    data class MovePlayer(val line: Int) : EventEffect

    /**
     * Teleport the player to a specified location.
     */
    data class TeleportPlayer(val fileName: String, val line: Int) : EventEffect

    /**
     * Show the map again after being hidden by [HideMap].
     */
    data object ShowMap : EventEffect

    /**
     * Hide the map display during this event. The map will display automatically after the parent
     *  event is finished.
     */
    data object HideMap : EventEffect

    /**
     * One shot effect to shake the map.
     */
    data object ShakeMap : EventEffect

    /**
     * Request the user to choose an entity in current team.
     */
    data object ChooseEntity : EventEffect

    /**
     * Display a dialog to remind the player to save current progress.
     */
    data object RequestSave : EventEffect

    /**
     * Refresh the events of current map.
     */
    data object RefreshEvents : EventEffect

    /**
     * A special event effect that requests the user to input "return 0;" in a text field.
     */
    data object TypeReturn0: EventEffect

    /**
     * Finish the event.
     */
    data object EventFinished : EventEffect
}