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
package sokeriaaa.return0.applib.modules

import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module
import sokeriaaa.return0.applib.datastore.AppDataStoreFactory
import sokeriaaa.return0.applib.datastore.AppKeyValues
import sokeriaaa.return0.applib.datastore.DataStoreKeyValues
import sokeriaaa.return0.applib.room.AppDatabase
import sokeriaaa.return0.applib.room.getDatabaseBuilder
import sokeriaaa.return0.applib.room.helper.RoomTransaction
import sokeriaaa.return0.applib.room.helper.TransactionManager

actual val platformModules: Module = module {
    // Room builder
    single { getDatabaseBuilder(get()) }
    // Database
    single { AppDatabase.createDatabase(get()) }
    // Database: Transaction
    single<TransactionManager> { RoomTransaction(database = get()) }
    // DataStore
    single { AppDataStoreFactory(get<Context>()) }
    single<AppKeyValues> { DataStoreKeyValues(get<AppDataStoreFactory>().createDataStore("return0")) }
}