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
import sokeriaaa.return0.applib.room.dao.EmulatorEntryDao
import sokeriaaa.return0.applib.room.dao.EmulatorEntryDaoImpl
import sokeriaaa.return0.applib.room.dao.EmulatorIndexDao
import sokeriaaa.return0.applib.room.dao.EmulatorIndexDaoImpl
import sokeriaaa.return0.applib.room.dao.EntityDao
import sokeriaaa.return0.applib.room.dao.EntityDaoImpl

class AppDatabaseImpl : AppDatabase() {

    private val _sqDatabase: SQDatabase by lazy {
        SQDatabase(driver = driver)
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

    override fun getEmulatorEntryDao(): EmulatorEntryDao = _emulatorEntryDao
    override fun getEmulatorIndexDao(): EmulatorIndexDao = _emulatorIndexDao
    override fun getEntityDao(): EntityDao = _entityDao
}

internal expect val driver: SqlDriver