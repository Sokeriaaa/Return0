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
package sokeriaaa.return0.applib.modules

import androidx.room3.Room
import androidx.room3.RoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import sokeriaaa.return0.applib.datastore.AppDataStoreFactory
import sokeriaaa.return0.applib.datastore.AppKeyValues
import sokeriaaa.return0.applib.datastore.DataStoreKeyValues
import sokeriaaa.return0.applib.room.AppDatabase
import java.io.File

actual val subPlatformModules: Module = module {
    // Database: Room builder
    single<RoomDatabase.Builder<AppDatabase>> {
        val dbFile = File(AppDatabase.DATABASE_NAME + ".db")
        Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
    }
    // DataStore
    single { AppDataStoreFactory() }
    single<AppKeyValues> { DataStoreKeyValues(get<AppDataStoreFactory>().createDataStore("return0")) }
}