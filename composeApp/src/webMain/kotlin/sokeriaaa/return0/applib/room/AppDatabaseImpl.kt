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
import sokeriaaa.return0.applib.room.dao.InventoryDao
import sokeriaaa.return0.applib.room.dao.InventoryDaoImpl
import sokeriaaa.return0.applib.room.dao.QuestDao
import sokeriaaa.return0.applib.room.dao.QuestDaoImpl
import sokeriaaa.return0.applib.room.dao.SaveMetaDao
import sokeriaaa.return0.applib.room.dao.SaveMetaDaoImpl
import sokeriaaa.return0.applib.room.dao.SavedSwitchDao
import sokeriaaa.return0.applib.room.dao.SavedSwitchDaoImpl
import sokeriaaa.return0.applib.room.dao.SavedVariableDao
import sokeriaaa.return0.applib.room.dao.SavedVariableDaoImpl
import sokeriaaa.return0.applib.room.dao.StatisticsDao
import sokeriaaa.return0.applib.room.dao.StatisticsDaoImpl

class AppDatabaseImpl : AppDatabase() {

    private val _sqDatabase: SQDatabase by lazy {
        SQDatabase(driver = driver)
    }

    private val _currencyDao by lazy {
        CurrencyDaoImpl(_sqDatabase.sQCurrencyQueries)
    }

    private val _emulatorEntryDao by lazy {
        EmulatorEntryDaoImpl(_sqDatabase.sQEmulatorEntryQueries)
    }

    private val _emulatorIndexDao by lazy {
        EmulatorIndexDaoImpl(_sqDatabase.sQEmulatorIndexQueries)
    }

    private val _entityDao by lazy {
        EntityDaoImpl(_sqDatabase.sQEntityQueries)
    }

    private val _inventoryDao by lazy {
        InventoryDaoImpl(_sqDatabase.sQInventoryQueries)
    }

    private val _questDao by lazy {
        QuestDaoImpl(_sqDatabase.sQQuestQueries)
    }

    private val _savedSwitchDao by lazy {
        SavedSwitchDaoImpl(_sqDatabase.sQSavedSwitchesQueries)
    }

    private val _savedVariableDao by lazy {
        SavedVariableDaoImpl(_sqDatabase.sQSavedVariablesQueries)
    }

    private val _saveMetaDao by lazy {
        SaveMetaDaoImpl(_sqDatabase.sQSaveMetaQueries)
    }

    private val _statisticsDao by lazy {
        StatisticsDaoImpl(_sqDatabase.sQStatisticsQueries)
    }

    override fun getCurrencyDao(): CurrencyDao = _currencyDao
    override fun getEmulatorEntryDao(): EmulatorEntryDao = _emulatorEntryDao
    override fun getEmulatorIndexDao(): EmulatorIndexDao = _emulatorIndexDao
    override fun getEntityDao(): EntityDao = _entityDao
    override fun getInventoryDao(): InventoryDao = _inventoryDao
    override fun getQuestDao(): QuestDao = _questDao
    override fun getSavedSwitchDao(): SavedSwitchDao = _savedSwitchDao
    override fun getSavedVariableDao(): SavedVariableDao = _savedVariableDao
    override fun getSaveMetaDao(): SaveMetaDao = _saveMetaDao
    override fun getStatisticsDao(): StatisticsDao = _statisticsDao
}

internal expect val driver: SqlDriver