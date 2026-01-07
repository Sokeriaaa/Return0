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

import sokeriaaa.return0.applib.room.sq.SQPluginInventoryQueries
import sokeriaaa.return0.applib.room.table.PluginConstTable
import sokeriaaa.return0.applib.room.table.PluginInventoryTable
import sokeriaaa.return0.applib.room.table.PluginItem
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst

class PluginInventoryDaoImpl(
    private val queries: SQPluginInventoryDaoQueries,
    // TODO Embedded entities are not supported yet.
    // TODO Use manual implementation.
    private val queriesManual: SQPluginInventoryQueries,
) : PluginInventoryDao {
    override suspend fun query(
        saveID: Int,
        pluginID: Int
    ): PluginItem? {
        return queriesManual.queryPluginItem(
            save_id = saveID.toLong(),
            plugin_id = pluginID.toLong(),
            mapper = ::pluginItemMapper
        ).executeAsOneOrNull()
    }

    override suspend fun queryAll(saveID: Int): List<PluginItem> {
        return queriesManual.queryAllPluginItems(
            save_id = saveID.toLong(),
            mapper = ::pluginItemMapper
        ).executeAsList()
    }

    override suspend fun insertOrUpdate(table: PluginInventoryTable) {
        queries.insertOrUpdate(
            save_id = table.saveID.toLong(),
            plugin_id = table.pluginID.toLong(),
            installed_by = table.installedBy,
            param1 = table.param1,
            param2 = table.param2,
            param3 = table.param3,
            param4 = table.param4,
            param5 = table.param5,
        )
    }

    override suspend fun insertList(list: List<PluginInventoryTable>) {
        list.forEach { insertOrUpdate(it) }
    }

    override suspend fun deletePlugin(saveID: Int, pluginID: Int) {
        queries.deletePlugin(saveID.toLong(), pluginID.toLong())
    }

    override suspend fun delete(saveID: Int) {
        queries.delete(saveID.toLong())
    }

    @Suppress("LocalVariableName")
    private fun pluginItemMapper(
        save_id: Long,
        plugin_id: Long,
        installed_by: String?,
        param1: String?,
        param2: String?,
        param3: String?,
        param4: String?,
        param5: String?,
        const_plugin_id: Long,
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
        const6_tier: Long,
    ): PluginItem = PluginItem(
        inventory = PluginInventoryTable(
            saveID = save_id.toInt(),
            pluginID = plugin_id.toInt(),
            installedBy = installed_by,
            param1 = param1,
            param2 = param2,
            param3 = param3,
            param4 = param4,
            param5 = param5
        ),
        constData = PluginConstTable(
            pluginID = const_plugin_id.toInt(),
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
    )
}