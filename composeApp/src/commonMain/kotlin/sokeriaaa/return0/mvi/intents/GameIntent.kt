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
package sokeriaaa.return0.mvi.intents

import sokeriaaa.return0.shared.data.models.story.map.MapEvent

sealed class GameIntent : BaseIntent {
    data class LoadGame(val saveID: Int) : GameIntent()
    data object QuitGame : GameIntent()

    /**
     * Send a request for moving the player to specified line animatedly.
     *
     * @param line The target line number.
     * @param isEncounterDisabled When `true`, player will not encounter enemies on the way.
     */
    data class RequestMoveTo(
        val line: Int,
        val isByEvent: Boolean = false,
        val isEncounterDisabled: Boolean = false,
    ) : GameIntent()

    data class TeleportTo(
        val fileName: String,
        val line: Int,
    ) : GameIntent()

    data object RefreshMap : GameIntent()
    data object RefreshEvent : GameIntent()

    data class ExecuteEvent(val mapEvent: MapEvent) : GameIntent()
    data object EventContinue : GameIntent()
    data class EventChoice(val index: Int) : GameIntent()
    data class EventCombatResult(val result: Boolean) : GameIntent()
}