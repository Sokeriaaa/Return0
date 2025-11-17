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
package sokeriaaa.return0.models.action.function

import sokeriaaa.return0.models.action.Action
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.action.function.FunctionTarget
import sokeriaaa.return0.shared.data.models.component.result.ActionResult

/**
 * Function.
 *
 * Aliased for "Skill" to avoid the conflict with "Function" class in kotlin.
 */
interface Skill : Action {
    /**
     * Function target.
     */
    val functionTarget: FunctionTarget

    /**
     * This function is guaranteed to invoke on the target, can not be missed.
     * (But can still be nullified by effects or equipments.)
     */
    val bullseye: Boolean

    /**
     * Current power of this function
     */
    val power: Int

    /**
     * Base power with tier bonus included, but without any effects.
     */
    val basePower: Int

    /**
     * SP required for calling this function currently.
     */
    val spCost: Int

    /**
     * Base SP cost with tier bonus included, but without any effects.
     */
    val baseSPCost: Int

    /**
     * Attack modifier for this function.
     */
    val attackModifier: FunctionData.AttackModifier?

    /**
     * Invokes this function on target(s).
     */
    fun invokeOn(targets: List<Entity>): List<ActionResult>
}