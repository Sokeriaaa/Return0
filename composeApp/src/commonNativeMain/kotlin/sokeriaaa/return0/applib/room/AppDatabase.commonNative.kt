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
package sokeriaaa.return0.applib.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import sokeriaaa.return0.applib.room.dao.CurrencyDao
import sokeriaaa.return0.applib.room.dao.EmulatorEntryDao
import sokeriaaa.return0.applib.room.dao.EmulatorIndexDao
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.applib.room.dao.InventoryDao
import sokeriaaa.return0.applib.room.dao.QuestDao
import sokeriaaa.return0.applib.room.dao.SaveMetaDao
import sokeriaaa.return0.applib.room.dao.SavedSwitchDao
import sokeriaaa.return0.applib.room.dao.SavedVariableDao
import sokeriaaa.return0.applib.room.dao.StatisticsDao
import sokeriaaa.return0.applib.room.table.CurrencyTable
import sokeriaaa.return0.applib.room.table.EmulatorEntryTable
import sokeriaaa.return0.applib.room.table.EmulatorIndexTable
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.applib.room.table.InventoryTable
import sokeriaaa.return0.applib.room.table.QuestTable
import sokeriaaa.return0.applib.room.table.SaveMetaTable
import sokeriaaa.return0.applib.room.table.SavedSwitchTable
import sokeriaaa.return0.applib.room.table.SavedVariableTable
import sokeriaaa.return0.applib.room.table.StatisticsTable

@Database(
    version = 1,
    entities = [
        CurrencyTable::class,
        EmulatorEntryTable::class,
        EmulatorIndexTable::class,
        EntityTable::class,
        InventoryTable::class,
        QuestTable::class,
        SaveMetaTable::class,
        SavedSwitchTable::class,
        SavedVariableTable::class,
        StatisticsTable::class,
    ]
)
@ConstructedBy(AppDatabaseConstructor::class)
actual abstract class AppDatabase : RoomDatabase() {
    actual abstract fun getCurrencyDao(): CurrencyDao
    actual abstract fun getEmulatorEntryDao(): EmulatorEntryDao
    actual abstract fun getEmulatorIndexDao(): EmulatorIndexDao
    actual abstract fun getEntityDao(): EntityDao
    actual abstract fun getInventoryDao(): InventoryDao
    actual abstract fun getQuestDao(): QuestDao
    actual abstract fun getSavedSwitchDao(): SavedSwitchDao
    actual abstract fun getSavedVariableDao(): SavedVariableDao
    actual abstract fun getSaveMetaDao(): SaveMetaDao
    actual abstract fun getStatisticsDao(): StatisticsDao

    companion object {
        const val DB_NAME = "return0_database"

        fun createDatabase(builder: Builder<AppDatabase>): AppDatabase {
            return builder
                .addMigrations(
                    // TODO Future migrations insert here.
                )
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }
    }
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}