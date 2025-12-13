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

expect abstract class AppDatabase {
    abstract fun getCurrencyDao(): CurrencyDao
    abstract fun getEmulatorEntryDao(): EmulatorEntryDao
    abstract fun getEmulatorIndexDao(): EmulatorIndexDao
    abstract fun getEntityDao(): EntityDao
    abstract fun getInventoryDao(): InventoryDao
    abstract fun getQuestDao(): QuestDao
    abstract fun getSavedSwitchDao(): SavedSwitchDao
    abstract fun getSavedVariableDao(): SavedVariableDao
    abstract fun getSaveMetaDao(): SaveMetaDao
    abstract fun getStatisticsDao(): StatisticsDao
}