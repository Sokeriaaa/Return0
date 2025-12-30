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
package sokeriaaa.return0.applib.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sokeriaaa.return0.applib.room.table.EntityTable

@Dao
actual interface EntityDao {
    @Query(
        "SELECT * FROM `${EntityTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun queryAll(saveID: Int): List<EntityTable>

    @Query(
        "SELECT * FROM `${EntityTable.TABLE_NAME}` WHERE " +
                "`save_id`=:saveID AND `entity_name`=:entityName LIMIT 1"
    )
    actual suspend fun getEntity(
        saveID: Int,
        entityName: String
    ): EntityTable?

    @Query(
        "UPDATE `${EntityTable.TABLE_NAME}` SET " +
                "`current_hp`=:hp WHERE " +
                "`save_id`=:saveID AND `entity_name`=:entityName"
    )
    actual suspend fun updateHP(saveID: Int, entityName: String, hp: Int?)

    @Query(
        "UPDATE `${EntityTable.TABLE_NAME}` SET " +
                "`current_sp`=:sp WHERE " +
                "`save_id`=:saveID AND `entity_name`=:entityName"
    )
    actual suspend fun updateSP(saveID: Int, entityName: String, sp: Int?)

    @Query(
        "UPDATE `${EntityTable.TABLE_NAME}` SET " +
                "`plugin_id`=:pluginID WHERE " +
                "`save_id`=:saveID AND `entity_name`=:entityName"
    )
    actual suspend fun updatePlugin(saveID: Int, entityName: String, pluginID: Long?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insert(entityTable: EntityTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insertList(list: List<EntityTable>)

    @Query(
        "DELETE FROM `${EntityTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun deleteSave(saveID: Int)
}