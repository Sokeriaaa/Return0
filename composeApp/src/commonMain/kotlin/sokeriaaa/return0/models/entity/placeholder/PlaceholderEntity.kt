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
package sokeriaaa.return0.models.entity.placeholder

import sokeriaaa.return0.models.action.effect.Effect
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.shield.Shield
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath

object PlaceholderEntity : Entity {
    override val index: Int = placeholderError
    override val isParty: Boolean = placeholderError
    override var isFailedFlag: Boolean = placeholderError
    override val name: String = placeholderError
    override val level: Int = placeholderError
    override val path: EntityPath = placeholderError
    override val category: Category = placeholderError
    override val category2: Category = placeholderError
    override val atk: Int = placeholderError
    override val def: Int = placeholderError
    override val spd: Int = placeholderError
    override val baseATK: Int = placeholderError
    override val baseDEF: Int = placeholderError
    override val baseSPD: Int = placeholderError
    override var hp: Int = placeholderError
    override val maxhp: Int = placeholderError
    override var sp: Int = placeholderError
    override val maxsp: Int = placeholderError
    override var ap: Float = placeholderError
    override val maxap: Int = placeholderError
    override val baseHP: Int = placeholderError
    override val baseSP: Int = placeholderError
    override val baseAP: Int = placeholderError
    override val critRate: Float = placeholderError
    override val critDMG: Float = placeholderError
    override val targetRate: Float = placeholderError
    override val hideRate: Float = placeholderError
    override val shieldValue: Int = placeholderError
    override val attackAction: Skill = placeholderError
    override val defendAction: Skill = placeholderError
    override val relaxAction: Skill = placeholderError
    override val functions: List<Skill> = placeholderError
    override val effects: List<Effect> = placeholderError
    override val shields: Map<String, Shield> = placeholderError

    override fun tick() = placeholderError
    override fun attachEffect(effect: Effect) = placeholderError
    override fun removeEffect(effect: Effect) = placeholderError
    override fun attachShield(key: String, value: Int, turns: Int?) = placeholderError
    override fun removeShield(key: String) = placeholderError
    override fun cleanUpShields() = placeholderError
}

private val placeholderError: Nothing =
    error(
        "Trying to execute/calculate for a placeholder entity. " +
                "(Are you trying to access the \"user\" in an ExecuteExtra event?)"
    )