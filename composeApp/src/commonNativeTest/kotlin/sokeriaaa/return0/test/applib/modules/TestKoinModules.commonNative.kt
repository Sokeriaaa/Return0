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
package sokeriaaa.return0.test.applib.modules

import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import sokeriaaa.return0.applib.room.AppDatabase
import sokeriaaa.return0.applib.room.helper.RoomTransaction
import sokeriaaa.return0.applib.room.helper.TransactionManager
import sokeriaaa.return0.test.applib.room.getTestDatabaseBuilder

actual val platformModules: Module = module {
    // Room builder
    single { getTestDatabaseBuilder() }
    // Database
    single {
        get<RoomDatabase.Builder<AppDatabase>>()
            .setQueryCoroutineContext(Dispatchers.Unconfined) // Standard for testing
            .build()
    }
    // Database: Transaction
    single<TransactionManager> { RoomTransaction(database = get()) }
}