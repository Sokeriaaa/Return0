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
import sokeriaaa.return0.applib.room.table.SaveMetaTable
import sokeriaaa.return0.shared.data.models.title.Title

@Dao
interface SaveMetaDao {

    /**
     * Query all the saves in current device.
     */
    @Query(
        "SELECT * FROM `${SaveMetaTable.TABLE_NAME}`"
    )
    suspend fun queryAll(): List<SaveMetaTable>

    /**
     * Query the save with the specified ID.
     */
    @Query(
        "SELECT * FROM `${SaveMetaTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun query(saveID: Int): SaveMetaTable?

    /**
     * Insert or update the save.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(table: SaveMetaTable)

    /**
     * Update player's position.
     */
    @Query(
        "UPDATE `${SaveMetaTable.TABLE_NAME}` SET `file_name`=:fileName, " +
                "`line_number`=:lineNumber WHERE `save_id`=:saveID"
    )
    suspend fun updatePosition(saveID: Int, fileName: String, lineNumber: Int)

    /**
     * Update player's title.
     */
    @Query(
        "UPDATE `${SaveMetaTable.TABLE_NAME}` SET `title`=:title " +
                "WHERE `save_id`=:saveID"
    )
    suspend fun updateTitle(saveID: Int, title: Title)

    /**
     * Delete the save with specified ID.
     */
    @Query(
        "DELETE FROM `${SaveMetaTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    suspend fun delete(saveID: Int)
}