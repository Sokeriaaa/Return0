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

import app.cash.sqldelight.db.SqlDriver
import sokeriaaa.return0.applib.room.dao.CurrencyDao
import sokeriaaa.return0.applib.room.dao.CurrencyDaoImpl
import sokeriaaa.return0.applib.room.dao.EmulatorEntryDao
import sokeriaaa.return0.applib.room.dao.EmulatorEntryDaoImpl
import sokeriaaa.return0.applib.room.dao.EmulatorIndexDao
import sokeriaaa.return0.applib.room.dao.EmulatorIndexDaoImpl
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.applib.room.dao.EntityDaoImpl
import sokeriaaa.return0.applib.room.dao.EventRelocationDao
import sokeriaaa.return0.applib.room.dao.EventRelocationDaoImpl
import sokeriaaa.return0.applib.room.dao.IndexedHubDao
import sokeriaaa.return0.applib.room.dao.IndexedHubDaoImpl
import sokeriaaa.return0.applib.room.dao.InventoryDao
import sokeriaaa.return0.applib.room.dao.InventoryDaoImpl
import sokeriaaa.return0.applib.room.dao.PluginConstDao
import sokeriaaa.return0.applib.room.dao.PluginConstDaoImpl
import sokeriaaa.return0.applib.room.dao.PluginInventoryDao
import sokeriaaa.return0.applib.room.dao.PluginInventoryDaoImpl
import sokeriaaa.return0.applib.room.dao.QuestDao
import sokeriaaa.return0.applib.room.dao.QuestDaoImpl
import sokeriaaa.return0.applib.room.dao.SaveMetaDao
import sokeriaaa.return0.applib.room.dao.SaveMetaDaoImpl
import sokeriaaa.return0.applib.room.dao.SavedSwitchDao
import sokeriaaa.return0.applib.room.dao.SavedSwitchDaoImpl
import sokeriaaa.return0.applib.room.dao.SavedTimestampDao
import sokeriaaa.return0.applib.room.dao.SavedTimestampDaoImpl
import sokeriaaa.return0.applib.room.dao.SavedVariableDao
import sokeriaaa.return0.applib.room.dao.SavedVariableDaoImpl
import sokeriaaa.return0.applib.room.dao.StatisticsDao
import sokeriaaa.return0.applib.room.dao.StatisticsDaoImpl
import sokeriaaa.return0.applib.room.dao.TeamDao
import sokeriaaa.return0.applib.room.dao.TeamDaoImpl

class AppDatabaseImpl(private val sqDatabase: SQDatabase) : AppDatabase() {

    private val _currencyDao by lazy {
        CurrencyDaoImpl(sqDatabase.sQCurrencyDaoQueries)
    }

    private val _emulatorEntryDao by lazy {
        EmulatorEntryDaoImpl(sqDatabase.sQEmulatorEntryDaoQueries)
    }

    private val _emulatorIndexDao by lazy {
        EmulatorIndexDaoImpl(sqDatabase.sQEmulatorIndexDaoQueries)
    }

    private val _entityDao by lazy {
        EntityDaoImpl(sqDatabase.sQEntityDaoQueries)
    }

    private val _eventRelocationDao by lazy {
        EventRelocationDaoImpl(sqDatabase.sQEventRelocationDaoQueries)
    }

    private val _indexedHubDao by lazy {
        IndexedHubDaoImpl(sqDatabase.sQIndexedHubDaoQueries)
    }

    private val _inventoryDao by lazy {
        InventoryDaoImpl(sqDatabase.sQInventoryDaoQueries)
    }

    private val _pluginConstDao by lazy {
        PluginConstDaoImpl(sqDatabase.sQPluginConstDaoQueries)
    }

    private val _pluginInventoryDao by lazy {
        PluginInventoryDaoImpl(
            sqDatabase.sQPluginInventoryDaoQueries,
            sqDatabase.sQPluginInventoryQueries,
        )
    }

    private val _questDao by lazy {
        QuestDaoImpl(sqDatabase.sQQuestDaoQueries)
    }

    private val _savedSwitchDao by lazy {
        SavedSwitchDaoImpl(sqDatabase.sQSavedSwitchDaoQueries)
    }

    private val _savedTimestampDao by lazy {
        SavedTimestampDaoImpl(sqDatabase.sQSavedTimestampDaoQueries)
    }

    private val _savedVariableDao by lazy {
        SavedVariableDaoImpl(sqDatabase.sQSavedVariableDaoQueries)
    }

    private val _saveMetaDao by lazy {
        SaveMetaDaoImpl(sqDatabase.sQSaveMetaDaoQueries)
    }

    private val _statisticsDao by lazy {
        StatisticsDaoImpl(sqDatabase.sQStatisticsDaoQueries)
    }

    private val _teamDao by lazy {
        TeamDaoImpl(sqDatabase.sQTeamDaoQueries)
    }

    override fun getCurrencyDao(): CurrencyDao = _currencyDao
    override fun getEmulatorEntryDao(): EmulatorEntryDao = _emulatorEntryDao
    override fun getEmulatorIndexDao(): EmulatorIndexDao = _emulatorIndexDao
    override fun getEntityDao(): EntityDao = _entityDao
    override fun getEventRelocationDao(): EventRelocationDao = _eventRelocationDao
    override fun getIndexedHubDao(): IndexedHubDao = _indexedHubDao
    override fun getInventoryDao(): InventoryDao = _inventoryDao
    override fun getPluginConstDao(): PluginConstDao = _pluginConstDao
    override fun getPluginInventoryDao(): PluginInventoryDao = _pluginInventoryDao
    override fun getQuestDao(): QuestDao = _questDao
    override fun getSavedSwitchDao(): SavedSwitchDao = _savedSwitchDao
    override fun getSavedTimestampDao(): SavedTimestampDao = _savedTimestampDao
    override fun getSavedVariableDao(): SavedVariableDao = _savedVariableDao
    override fun getSaveMetaDao(): SaveMetaDao = _saveMetaDao
    override fun getStatisticsDao(): StatisticsDao = _statisticsDao
    override fun getTeamDao(): TeamDao = _teamDao
}

internal expect val driver: SqlDriver