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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import sokeriaaa.return0.applib.room.table.PluginInventoryTable
import sokeriaaa.return0.applib.room.table.PluginItem

@Dao
interface PluginInventoryDao {

    @Transaction
    @Query(
        "SELECT * FROM `${PluginInventoryTable.TABLE_NAME}` " +
                "WHERE `save_id`=:saveID AND `plugin_id`=:pluginID LIMIT 1"
    )
    suspend fun query(saveID: Int, pluginID: Long): PluginItem?

    @Transaction
    @Query(
        "SELECT * FROM `${PluginInventoryTable.TABLE_NAME}` " +
                "WHERE `save_id`=:saveID"
    )
    suspend fun queryAll(saveID: Int): List<PluginItem>

    /**
     * Insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: PluginInventoryTable)

    /**
     * Insert a list of tables.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<PluginInventoryTable>)

    /**
     * Change the install status for a plugin.
     */
    @Query(
        "UPDATE `${PluginInventoryTable.TABLE_NAME}` SET `installed_by`=:installedBy " +
                "WHERE `save_id`=:saveID AND `plugin_id`=:pluginID"
    )
    suspend fun updateInstalledBy(saveID: Int, pluginID: Long, installedBy: String?)

    /**
     * Uninstall the plugin from an entity.
     */
    @Query(
        "UPDATE `${PluginInventoryTable.TABLE_NAME}` SET `installed_by`=NULL " +
                "WHERE `save_id`=:saveID AND `installed_by`=:entity"
    )
    suspend fun uninstalledPluginFor(saveID: Int, entity: String)

    /**
     * Delete a plugin.
     */
    @Query(
        "DELETE FROM `${PluginInventoryTable.TABLE_NAME}` WHERE `save_id`=:saveID AND `plugin_id`=:pluginID"
    )
    suspend fun deletePlugin(saveID: Int, pluginID: Long)

    /**
     * Delete plugins of a save.
     */
    @Query(
        "DELETE FROM `${PluginInventoryTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun delete(saveID: Int)
}