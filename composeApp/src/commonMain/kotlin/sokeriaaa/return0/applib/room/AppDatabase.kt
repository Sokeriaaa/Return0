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

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.sqlite.SQLiteDriver
import sokeriaaa.return0.applib.room.dao.CurrencyDao
import sokeriaaa.return0.applib.room.dao.EmulatorEntryDao
import sokeriaaa.return0.applib.room.dao.EmulatorIndexDao
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.applib.room.dao.EventRelocationDao
import sokeriaaa.return0.applib.room.dao.IndexedHubDao
import sokeriaaa.return0.applib.room.dao.InventoryDao
import sokeriaaa.return0.applib.room.dao.PluginConstDao
import sokeriaaa.return0.applib.room.dao.PluginInventoryDao
import sokeriaaa.return0.applib.room.dao.QuestDao
import sokeriaaa.return0.applib.room.dao.SaveMetaDao
import sokeriaaa.return0.applib.room.dao.SavedSwitchDao
import sokeriaaa.return0.applib.room.dao.SavedTimestampDao
import sokeriaaa.return0.applib.room.dao.SavedVariableDao
import sokeriaaa.return0.applib.room.dao.StatisticsDao
import sokeriaaa.return0.applib.room.dao.TeamDao
import sokeriaaa.return0.applib.room.table.CurrencyTable
import sokeriaaa.return0.applib.room.table.EmulatorEntryTable
import sokeriaaa.return0.applib.room.table.EmulatorIndexTable
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.applib.room.table.EventRelocationTable
import sokeriaaa.return0.applib.room.table.IndexedHubTable
import sokeriaaa.return0.applib.room.table.InventoryTable
import sokeriaaa.return0.applib.room.table.PluginConstTable
import sokeriaaa.return0.applib.room.table.PluginInventoryTable
import sokeriaaa.return0.applib.room.table.QuestTable
import sokeriaaa.return0.applib.room.table.SaveMetaTable
import sokeriaaa.return0.applib.room.table.SavedSwitchTable
import sokeriaaa.return0.applib.room.table.SavedTimestampTable
import sokeriaaa.return0.applib.room.table.SavedVariableTable
import sokeriaaa.return0.applib.room.table.StatisticsTable
import sokeriaaa.return0.applib.room.table.TeamTable
import kotlin.coroutines.CoroutineContext

@Database(
    version = 1,
    entities = [
        CurrencyTable::class,
        EmulatorEntryTable::class,
        EmulatorIndexTable::class,
        EntityTable::class,
        EventRelocationTable::class,
        IndexedHubTable::class,
        InventoryTable::class,
        PluginConstTable::class,
        PluginInventoryTable::class,
        QuestTable::class,
        SaveMetaTable::class,
        SavedSwitchTable::class,
        SavedTimestampTable::class,
        SavedVariableTable::class,
        StatisticsTable::class,
        TeamTable::class,
    ]
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao
    abstract fun getEmulatorEntryDao(): EmulatorEntryDao
    abstract fun getEmulatorIndexDao(): EmulatorIndexDao
    abstract fun getEntityDao(): EntityDao
    abstract fun getEventRelocationDao(): EventRelocationDao
    abstract fun getIndexedHubDao(): IndexedHubDao
    abstract fun getInventoryDao(): InventoryDao
    abstract fun getPluginConstDao(): PluginConstDao
    abstract fun getPluginInventoryDao(): PluginInventoryDao
    abstract fun getQuestDao(): QuestDao
    abstract fun getSavedSwitchDao(): SavedSwitchDao
    abstract fun getSavedTimestampDao(): SavedTimestampDao
    abstract fun getSavedVariableDao(): SavedVariableDao
    abstract fun getSaveMetaDao(): SaveMetaDao
    abstract fun getStatisticsDao(): StatisticsDao
    abstract fun getTeamDao(): TeamDao

    companion object {
        const val DATABASE_NAME = "return0_database"

        fun createDatabase(
            builder: Builder<AppDatabase>,
            driver: SQLiteDriver,
            queryCoroutineContext: CoroutineContext,
        ): AppDatabase {
            return builder
                .addMigrations(
                    // TODO Future migrations insert here.
                )
                .setDriver(driver)
                .setQueryCoroutineContext(queryCoroutineContext)
                .build()
        }
    }
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}