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
package sokeriaaa.return0.models.action

import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.component.extras.Extra

/**
 * The super class of all actions, include functions (skills), items and effects.
 */
interface Action {
    /**
     * User of this function or origin of this effect.
     */
    val user: Entity

    /**
     * Name of this action.
     *
     * Used as the key, once defined, this can not be changed.
     */
    val name: String

    /**
     * Tier of this action.
     */
    val tier: Int

    /**
     * Saved values for this action.
     *
     * Will be cleared after failure of entity or end of combat.
     *
     * @see sokeriaaa.return0.shared.data.models.component.extras.CommonExtra.SaveValue
     * @see sokeriaaa.return0.shared.data.models.component.values.CommonValue.LoadValue
     */
    val values: MutableMap<String, Float>

    /**
     * Times this function invoked (include missed and nullified attack)
     *  or this effect executed in a single combat.
     *
     * Will be cleared after failure of entity or end of combat.
     */
    val timesUsed: Int

    /**
     * Times this function executed in a single invoke (include missed and nullified attack)
     * for each target.
     *
     * Will be cleared after failure of entity or end of combat.
     */
    val timesRepeated: Int

    /**
     * The extra execution.
     *
     * For attacking functions (skills) which have positive power,
     *  this extra will be executed when making a successful attack.
     *
     * For functions have 0 or negative power, this extra will be executed instantly
     *  when function is invoked.
     *
     * For effects, this extra will be executed after current entity taking any action.
     */
    val extra: Extra?

    fun reset()
}