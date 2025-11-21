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

import sokeriaaa.return0.shared.data.models.combat.EntityState

sealed class EmulatorIntent : BaseIntent {
    data class AddEntity(val entityState: EntityState) : EmulatorIntent()
    data class AlterEntity(val before: EntityState, val after: EntityState) : EmulatorIntent()
    data class RemoveEntity(val entityState: EntityState) : EmulatorIntent()
    data object StartCombat : EmulatorIntent()
}