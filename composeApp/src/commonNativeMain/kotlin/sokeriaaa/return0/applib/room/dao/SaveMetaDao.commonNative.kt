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

@Dao
actual interface SaveMetaDao {
    @Query(
        "SELECT * FROM `${SaveMetaTable.TABLE_NAME}`"
    )
    actual suspend fun queryAll(): List<SaveMetaTable>

    @Query(
        "SELECT * FROM `${SaveMetaTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun query(saveID: Int): SaveMetaTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insertOrUpdate(table: SaveMetaTable)

    @Query(
        "UPDATE `${SaveMetaTable.TABLE_NAME}` SET `file_name`=:fileName, " +
                "`line_number`=:lineNumber WHERE `save_id`=:saveID"
    )
    actual suspend fun updatePosition(saveID: Int, fileName: String, lineNumber: Int)

    @Query(
        "DELETE FROM `${SaveMetaTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun delete(saveID: Int)
}