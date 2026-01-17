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
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginData

fun PluginData.generatePlugin(
    tier: Int,
    constMap: Map<PluginConst, Int>,
): EntityPlugin = EntityPluginImpl(
    key = key,
    tier = tier,
    path = path,
    constMap = constMap,
    onAttack = onAttack,
    onDefend = onDefend,
    attackRateOffset = attackRateOffset,
    defendRateOffset = defendRateOffset,
)

internal class EntityPluginImpl(
    override val key: String,
    override val tier: Int,
    override val path: EntityPath,
    override val constMap: Map<PluginConst, Int>,
    override val onAttack: Extra?,
    override val onDefend: Extra?,
    override val attackRateOffset: Value?,
    override val defendRateOffset: Value?
) : EntityPlugin