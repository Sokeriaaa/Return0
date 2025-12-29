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
package sokeriaaa.return0.models.action.placeholder

import sokeriaaa.return0.models.action.Action
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.placeholder.PlaceholderEntity
import sokeriaaa.return0.shared.data.models.component.extras.Extra

object PlaceholderAction : Action {
    override val user: Entity = PlaceholderEntity
    override val name: String = placeholderError
    override val tier: Int = placeholderError
    override val values: MutableMap<String, Float> = placeholderError
    override val timesUsed: Int = placeholderError
    override val timesRepeated: Int = placeholderError
    override val extra: Extra = placeholderError
    override fun reset() = placeholderError
}

private val placeholderError: Nothing =
    error(
        "Trying to execute/calculate for a placeholder action. " +
                "(Are you trying to access the \"action\" in an ExecuteExtra event?)"
    )