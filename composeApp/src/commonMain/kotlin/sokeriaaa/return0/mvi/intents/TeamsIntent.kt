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

sealed class TeamsIntent : BaseIntent {
    data class ActivateTeam(
        val teamIndex: Int,
    ) : TeamsIntent()

    data object RequestCreateTeam : TeamsIntent()

    data class RenameTeam(
        val teamIndex: Int,
        val name: String,
    ) : TeamsIntent()

    data class SwitchEntity(
        val teamIndex: Int,
        val entityIndex: Int,
        val newEntity: String?,
    ) : TeamsIntent()
}