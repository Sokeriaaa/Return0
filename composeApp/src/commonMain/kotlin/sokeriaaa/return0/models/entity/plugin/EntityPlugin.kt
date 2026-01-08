/**
 * Copyright (C) 2026 Sokeriaaa
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
package sokeriaaa.return0.models.entity.plugin

import sokeriaaa.return0.shared.data.models.component.extras.Extra
import sokeriaaa.return0.shared.data.models.component.values.Value
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst

/**
 * Instance of entity plugin.
 */
interface EntityPlugin {
    /**
     * Name of this plugin.
     */
    val name: String

    /**
     * Plugin tier (1~5)
     */
    val tier: Int

    /**
     * Entity path of this plugin. If the path of entity and plugin are not identical,
     * only const values will take effect, other effects are disabled.
     */
    val path: EntityPath

    /**
     * Const values (buffs) of this plugin. Applied on base value.
     */
    val constMap: Map<PluginConst, Int>

    /**
     * Executes when this entity attacked successfully on an enemy.
     */
    val onAttack: Extra?

    /**
     * Executes when this entity is attacked successfully by an enemy.
     */
    val onDefend: Extra?

    /**
     * Damage multiplier when attacking enemy.
     */
    val attackRate: Value?

    /**
     * Damage multiplier when being attacked by an enemy.
     */
    val defendRate: Value?
}