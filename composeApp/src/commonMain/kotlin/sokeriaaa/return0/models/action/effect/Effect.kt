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
package sokeriaaa.return0.models.action.effect

import sokeriaaa.return0.models.action.Action
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.action.effect.EffectModifier
import sokeriaaa.return0.shared.data.models.component.result.ActionResult

interface Effect : Action, Comparable<Effect> {
    val abbr: String
    val isDebuff: Boolean
    val isStackable: Boolean
    val isRemovable: Boolean
    val isFreeze: Boolean
    val modifiers: List<EffectModifier>
    var turnsLeft: Int

    /**
     * Apply this effect on target.
     */
    fun applyOn(target: Entity): List<ActionResult>

    /**
     * Compare two effects with the same name.
     */
    override fun compareTo(other: Effect): Int {
        require(this.name == other.name) {
            "Effects with different names cannot be compared. Current effect names are: " +
                    "this=\"${this.name}\", other=\"${other.name}\""
        }
        return if (this.tier == other.tier) {
            this.turnsLeft - other.turnsLeft
        } else {
            this.tier - other.tier
        }
    }
}