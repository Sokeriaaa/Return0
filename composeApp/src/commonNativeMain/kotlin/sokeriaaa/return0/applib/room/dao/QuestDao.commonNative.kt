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
import sokeriaaa.return0.applib.room.table.QuestTable

@Dao
actual interface QuestDao {
    @Query(
        "SELECT * FROM `${QuestTable.TABLE_NAME}` WHERE `save_id`=:saveID AND `quest_key`=:key"
    )
    actual suspend fun query(saveID: Int, key: String): QuestTable?

    @Query(
        "SELECT * FROM `${QuestTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun queryAll(saveID: Int): List<QuestTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insertOrUpdate(table: QuestTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    actual suspend fun insertList(list: List<QuestTable>)

    @Query(
        "DELETE FROM `${QuestTable.TABLE_NAME}` WHERE `save_id`=:saveID"
    )
    actual suspend fun delete(saveID: Int)
}