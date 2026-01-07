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
package sokeriaaa.return0.applib.room.dao

import sokeriaaa.return0.applib.room.table.PluginConstTable
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst

class PluginConstDaoImpl(
    private val queries: SQPluginConstDaoQueries,
) : PluginConstDao {
    override suspend fun query(pluginID: Int): PluginConstTable? {
        return queries
            .query(pluginID.toLong(), mapper = ::convertToTable)
            .executeAsOneOrNull()
    }

    override suspend fun insertOrUpdate(table: PluginConstTable) {
        queries.insertOrUpdate(
            plugin_id = table.pluginID?.toLong(),
            name = table.name,
            tier = table.tier.toLong(),
            const1 = table.const1?.name,
            const1_tier = table.const1Tier.toLong(),
            const2 = table.const2?.name,
            const2_tier = table.const2Tier.toLong(),
            const3 = table.const3?.name,
            const3_tier = table.const3Tier.toLong(),
            const4 = table.const4?.name,
            const4_tier = table.const4Tier.toLong(),
            const5 = table.const5?.name,
            const5_tier = table.const5Tier.toLong(),
            const6 = table.const6?.name,
            const6_tier = table.const6Tier.toLong(),
        )
    }

    override suspend fun insertList(list: List<PluginConstTable>) {
        list.forEach { insertOrUpdate(it) }
    }

    override suspend fun delete(pluginID: Int) {
        queries.delete(pluginID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun convertToTable(
        plugin_id: Long,
        name: String,
        tier: Long,
        const1: String?,
        const1_tier: Long,
        const2: String?,
        const2_tier: Long,
        const3: String?,
        const3_tier: Long,
        const4: String?,
        const4_tier: Long,
        const5: String?,
        const5_tier: Long,
        const6: String?,
        const6_tier: Long
    ): PluginConstTable = PluginConstTable(
        pluginID = plugin_id.toInt(),
        name = name,
        tier = tier.toInt(),
        const1 = const1?.let(PluginConst::valueOf),
        const1Tier = const1_tier.toInt(),
        const2 = const2?.let(PluginConst::valueOf),
        const2Tier = const2_tier.toInt(),
        const3 = const3?.let(PluginConst::valueOf),
        const3Tier = const3_tier.toInt(),
        const4 = const4?.let(PluginConst::valueOf),
        const4Tier = const4_tier.toInt(),
        const5 = const5?.let(PluginConst::valueOf),
        const5Tier = const5_tier.toInt(),
        const6 = const6?.let(PluginConst::valueOf),
        const6Tier = const6_tier.toInt(),
    )
}