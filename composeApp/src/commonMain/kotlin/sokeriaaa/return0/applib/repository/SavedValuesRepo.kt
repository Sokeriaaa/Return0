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
package sokeriaaa.return0.applib.repository

import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.room.dao.SavedSwitchDao
import sokeriaaa.return0.applib.room.dao.SavedVariableDao
import sokeriaaa.return0.applib.room.helper.TransactionManager
import sokeriaaa.return0.applib.room.table.SavedSwitchTable
import sokeriaaa.return0.applib.room.table.SavedVariableTable

/**
 * Manages saved variables and switches.
 */
class SavedValuesRepo(
    private val transactionManager: TransactionManager,
    private val savedSwitchDao: SavedSwitchDao,
    private val savedVariableDao: SavedVariableDao,
) {

    private val _switchBuffer: MutableMap<String, Boolean> = HashMap()
    private val _variableBuffer: MutableMap<String, Int> = HashMap()

    suspend fun getSwitch(key: String): Boolean {
        return _switchBuffer[key]
            ?: savedSwitchDao.query(AppConstants.CURRENT_SAVE_ID, key)?.value?.also {
                _switchBuffer[key] = it
            }
            ?: false
    }

    suspend fun getVariable(key: String): Int {
        return _variableBuffer[key]
            ?: savedVariableDao.query(AppConstants.CURRENT_SAVE_ID, key)?.value?.also {
                _variableBuffer[key] = it
            }
            ?: 0
    }

    fun setSwitch(key: String, value: Boolean) {
        _switchBuffer[key] = value
    }

    fun setVariable(key: String, value: Int) {
        _variableBuffer[key] = value
    }

    /**
     * When a save is loaded.
     */
    fun load() {
        // Clear the buffer.
        _switchBuffer.clear()
        _variableBuffer.clear()
    }

    /**
     * Flush the buffers to database.
     */
    suspend fun flush() {
        transactionManager.withTransaction {
            _switchBuffer.forEach {
                savedSwitchDao.insertOrUpdate(
                    SavedSwitchTable(
                        saveID = AppConstants.CURRENT_SAVE_ID,
                        key = it.key,
                        value = it.value,
                    )
                )
            }
            _variableBuffer.forEach {
                savedVariableDao.insertOrUpdate(
                    SavedVariableTable(
                        saveID = AppConstants.CURRENT_SAVE_ID,
                        key = it.key,
                        value = it.value,
                    )
                )
            }
        }
    }
}