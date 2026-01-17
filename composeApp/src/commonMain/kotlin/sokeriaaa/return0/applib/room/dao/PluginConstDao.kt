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
import sokeriaaa.return0.applib.room.table.PluginConstTable

@Dao
interface PluginConstDao {

    /**
     * Query by ID.
     */
    @Query(
        "SELECT * FROM `${PluginConstTable.TABLE_NAME}` WHERE `plugin_id`=:pluginID"
    )
    suspend fun query(pluginID: Long): PluginConstTable?

    /**
     * Insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: PluginConstTable): Long

    @Query("SELECT last_insert_rowid()")
    suspend fun selectLastInsertRowId(): Long

    /**
     * Insert a list of tables.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<PluginConstTable>)

    /**
     * Delete a plugin.
     */
    @Query(
        "DELETE FROM `${PluginConstTable.TABLE_NAME}` WHERE `plugin_id`=:pluginID"
    )
    suspend fun delete(pluginID: Int)

}