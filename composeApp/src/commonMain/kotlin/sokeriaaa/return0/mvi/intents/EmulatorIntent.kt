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

import sokeriaaa.return0.shared.data.models.entity.EnemyData
import sokeriaaa.return0.shared.data.models.entity.PartyData

sealed class EmulatorIntent : BaseIntent {
    data class AddParty(val partyData: PartyData) : EmulatorIntent()
    data class AlterParty(val before: PartyData, val after: PartyData) : EmulatorIntent()
    data class RemoveParty(val partyData: PartyData) : EmulatorIntent()
    data class AddEnemy(val enemyData: EnemyData) : EmulatorIntent()
    data class AlterEnemy(val before: EnemyData, val after: EnemyData) : EmulatorIntent()
    data class RemoveEnemy(val enemyData: EnemyData) : EmulatorIntent()
    data object StartCombat : EmulatorIntent()
}