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
package sokeriaaa.return0.applib.room.helper

import androidx.room3.Transactor
import androidx.room3.useWriterConnection
import sokeriaaa.return0.applib.room.AppDatabase

class RoomTransactionManager(private val database: AppDatabase) : TransactionManager {
    override suspend fun <T> withTransaction(block: suspend () -> T): T {
        return database.useWriterConnection {
            it.withTransaction(
                type = Transactor.SQLiteTransactionType.IMMEDIATE,
                block = { block() },
            )
        }
    }
}