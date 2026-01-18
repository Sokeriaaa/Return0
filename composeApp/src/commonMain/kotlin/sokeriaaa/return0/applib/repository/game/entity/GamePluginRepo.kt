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
package sokeriaaa.return0.applib.repository.game.entity

import androidx.compose.runtime.mutableStateMapOf
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.game.base.BaseGameRepo
import sokeriaaa.return0.applib.room.dao.PluginConstDao
import sokeriaaa.return0.applib.room.dao.PluginInventoryDao
import sokeriaaa.return0.applib.room.table.PluginConstTable
import sokeriaaa.return0.applib.room.table.PluginInventoryTable
import sokeriaaa.return0.models.entity.plugin.EntityPlugin
import sokeriaaa.return0.models.entity.plugin.display.PluginInfo
import sokeriaaa.return0.models.entity.plugin.generatePlugin
import sokeriaaa.return0.shared.data.models.combat.EntityState
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginData
import kotlin.random.Random
import kotlin.random.nextInt

class GamePluginRepo(
    private val archive: ArchiveRepo,
    private val resource: ResourceRepo,
    private val pluginConstDao: PluginConstDao,
    private val pluginInventoryDao: PluginInventoryDao,
) : BaseGameRepo {

    // All plugins in current save mapped by ID.
    private val _pluginMap: MutableMap<Long, PluginInfo> = mutableStateMapOf()
    val pluginMap: Map<Long, PluginInfo> = _pluginMap

    // Mapping between entity and installed plugin ID.
    private val _entityPluginIDMap: MutableMap<String, Long> = mutableStateMapOf()
    val entityPluginIDMap: Map<String, Long> = _entityPluginIDMap

    fun constCountByTier(
        tier: Int,
        random: Random = Random,
    ): Int {
        return when (tier) {
            1 -> random.nextInt(1..3)
            2 -> random.nextInt(3..6)
            3 -> random.nextInt(6..9)
            4 -> random.nextInt(12..18)
            5 -> random.nextInt(24..36)
            else -> error("tier should between 1 and 5, current: $tier")
        }
    }

    /**
     * Generate a brand new plugin with specified key and tier.
     *
     * @return Inserted plugin ID and the plugin info.
     */
    suspend fun generateAndSavePlugin(
        key: String,
        tier: Int,
        random: Random = Random,
    ): Pair<Long, PluginInfo>? {
        // Data
        val pluginData = archive.getPluginData(key) ?: return null
        // Generate const
        val constTypes = PluginConst.entries.shuffled().take(tier)
        val constLevels = IntArray(constTypes.size)
        repeat(constCountByTier(tier, random)) {
            constLevels[random.nextInt(constLevels.size)]++
        }
        // Assemble table and insert
        val constTable = PluginConstTable(
            name = key,
            tier = tier,
            const1 = constTypes.getOrNull(0),
            const1Tier = constLevels.getOrNull(0) ?: 0,
            const2 = constTypes.getOrNull(1),
            const2Tier = constLevels.getOrNull(1) ?: 0,
            const3 = constTypes.getOrNull(2),
            const3Tier = constLevels.getOrNull(2) ?: 0,
            const4 = constTypes.getOrNull(3),
            const4Tier = constLevels.getOrNull(3) ?: 0,
            const5 = constTypes.getOrNull(4),
            const5Tier = constLevels.getOrNull(4) ?: 0,
            const6 = constTypes.getOrNull(5),
            const6Tier = constLevels.getOrNull(5) ?: 0,
        )
        val pluginID = pluginConstDao.insertOrUpdate(constTable)
        // Assemble plugin
        val constMap: Map<PluginConst, Int> = constTypes.zip(constLevels.toList()).toMap()
        return pluginID to PluginInfo(
            id = pluginID,
            name = resource.getString("plugin.${pluginData.key}"),
            description = resource.getString("plugin.${pluginData.key}"),
            tier = tier,
            constMap = constMap,
            data = pluginData,
            isLocked = false,
            installedBy = null,
        )
    }

    private fun assemblePlugin(
        pluginData: PluginData,
        tier: Int,
        constMap: Map<PluginConst, Int>,
    ): EntityPlugin {
        return pluginData.generatePlugin(tier, constMap)
    }

    private fun assemblePlugin(
        pluginData: PluginData,
        table: PluginConstTable,
    ): EntityPlugin {
        return assemblePlugin(pluginData, table.tier, assembleConstMap(table))
    }

    private fun assembleConstMap(table: PluginConstTable): Map<PluginConst, Int> {
        return sequenceOf(
            table.const1 to table.const1Tier,
            table.const2 to table.const2Tier,
            table.const3 to table.const3Tier,
            table.const4 to table.const4Tier,
            table.const5 to table.const5Tier,
            table.const6 to table.const6Tier,
        ).mapNotNull { entry ->
            if (entry.second <= 0) return@mapNotNull null
            entry.first?.let { it to entry.second }
        }.toMap()
    }

    suspend fun getPluginStateByID(pluginID: Long?): EntityState.Plugin? {
        pluginID ?: return null
        val table = pluginConstDao.query(pluginID) ?: return null
        val pluginData = archive.getPluginData(table.name) ?: return null
        return EntityState.Plugin(
            pluginData = pluginData,
            tier = table.tier,
            constMap = assembleConstMap(table)
        )
    }

    /**
     * Get the plugin instance bu ID.
     */
    suspend fun getPluginByID(pluginID: Long): EntityPlugin? {
        val table = pluginConstDao.query(pluginID) ?: return null
        val pluginData = archive.getPluginData(table.name) ?: return null
        return assemblePlugin(pluginData, table)
    }

    /**
     * Obtained a new plugin.
     */
    suspend fun obtainedPlugin(
        pluginInfo: PluginInfo,
    ) {
        _pluginMap[pluginInfo.id] = pluginInfo
        pluginInventoryDao.insertOrUpdate(
            PluginInventoryTable(
                saveID = AppConstants.CURRENT_SAVE_ID,
                pluginID = pluginInfo.id,
            )
        )
    }

    /**
     * Delete a plugin from the current game.
     *
     * This plugin will be removed from player's inventory, but still exists in the const table.
     */
    suspend fun deletePlugin(pluginID: Long) {
        val deletedPlugin = _pluginMap.remove(pluginID)
        deletedPlugin?.installedBy?.let {
            _entityPluginIDMap.remove(it)
        }
        pluginInventoryDao.deletePlugin(
            saveID = AppConstants.CURRENT_SAVE_ID,
            pluginID = pluginID,
        )
    }

    /**
     * Update the installed state of a plugin.
     */
    suspend fun onPluginInstalledTo(
        pluginID: Long,
        installedBy: String? = null
    ) {
        val original = _pluginMap[pluginID]!!
        // Update maps
        _pluginMap[pluginID] = original.copy(installedBy = installedBy)
        if (original.installedBy != null) {
            _entityPluginIDMap.remove(original.installedBy)
        }
        if (installedBy != null) {
            _entityPluginIDMap[installedBy] = pluginID
        }
        // Update database
        pluginInventoryDao.updateInstalledBy(
            saveID = AppConstants.CURRENT_SAVE_ID,
            pluginID = pluginID,
            installedBy = installedBy,
        )
    }

    /**
     * Uninstall a plugin from entity.
     */
    suspend fun onPluginUninstalledFrom(
        entityName: String
    ) {
        // Update maps
        val installedID = _entityPluginIDMap[entityName]
        if (installedID != null) {
            _pluginMap[installedID] = _pluginMap[installedID]!!.copy(installedBy = null)
            _entityPluginIDMap.remove(entityName)
        }
        // Update database
        pluginInventoryDao.uninstalledPluginFor(
            saveID = AppConstants.CURRENT_SAVE_ID,
            entity = entityName,
        )
    }

    override suspend fun load() {
        _pluginMap.clear()
        _entityPluginIDMap.clear()
        for (item in pluginInventoryDao.queryAll(AppConstants.CURRENT_SAVE_ID)) {
            // Data
            val pluginData =
                archive.getPluginData(item.constData.name) ?: continue
            // Info
            val info = PluginInfo(
                id = item.constData.pluginID ?: continue,
                name = resource.getString("plugin.${pluginData.key}"),
                description = resource.getString("plugin.${pluginData.key}"),
                tier = item.constData.tier,
                constMap = assembleConstMap(item.constData),
                data = pluginData,
                isLocked = item.inventory.isLocked,
                installedBy = item.inventory.installedBy,
            )
            // Put values
            _pluginMap[info.id] = info
            if (info.installedBy != null) {
                _entityPluginIDMap[info.installedBy] = info.id
            }
        }
    }

    override suspend fun flush() {

    }

}