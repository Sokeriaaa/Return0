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

import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.combat.ArenaConfig

/**
 * Intents for [sokeriaaa.return0.mvi.viewmodels.CombatViewModel].
 */
sealed class CombatIntent : BaseIntent {

    /**
     * Prepare the combat with specified config.
     */
    data class Prepare(val config: ArenaConfig) : CombatIntent()

    /**
     * Start the combat.
     */
    data object StartCombat : CombatIntent()

    /**
     * The user chose an action.
     */
    data class ChooseAction(val function: Skill) : CombatIntent()

    /**
     * The user chose a (list of) target(s) for an action.
     */
    data class ChooseTarget(val targets: List<Entity>) : CombatIntent()

    /**
     * The user left the target choosing panel, then back to the action selection.
     */
    data object LeaveChooseTarget : CombatIntent()

    /**
     * The user turned on/off auto-combat.
     */
    data object ToggleAutoCombat : CombatIntent()

}