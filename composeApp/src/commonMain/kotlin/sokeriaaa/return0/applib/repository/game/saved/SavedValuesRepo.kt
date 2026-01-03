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
package sokeriaaa.return0.applib.repository.game.saved

import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.game.base.BaseGameRepo
import sokeriaaa.return0.applib.room.dao.SavedSwitchDao
import sokeriaaa.return0.applib.room.dao.SavedTimestampDao
import sokeriaaa.return0.applib.room.dao.SavedVariableDao
import sokeriaaa.return0.applib.room.table.SavedSwitchTable
import sokeriaaa.return0.applib.room.table.SavedTimestampTable
import sokeriaaa.return0.applib.room.table.SavedVariableTable

/**
 * Manages saved variables and switches.
 */
class SavedValuesRepo(
    private val savedSwitchDao: SavedSwitchDao,
    private val savedTimestampDao: SavedTimestampDao,
    private val savedVariableDao: SavedVariableDao,
) : BaseGameRepo {

    private val _switchBuffer: MutableMap<String, Boolean> = HashMap()
    private val _timestampBuffer: MutableMap<String, Long> = HashMap()
    private val _variableBuffer: MutableMap<String, Int> = HashMap()

    suspend fun getSwitch(key: String): Boolean {
        return _switchBuffer[key]
            ?: savedSwitchDao.query(AppConstants.CURRENT_SAVE_ID, key)?.value?.also {
                _switchBuffer[key] = it
            }
            ?: false
    }

    suspend fun getTimeStamp(key: String): Long {
        return _timestampBuffer[key]
            ?: savedTimestampDao.query(AppConstants.CURRENT_SAVE_ID, key)?.timestamp?.also {
                _timestampBuffer[key] = it
            }
            ?: 0L
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

    fun setTimestamp(key: String, timestamp: Long) {
        _timestampBuffer[key] = timestamp
    }

    fun setVariable(key: String, value: Int) {
        _variableBuffer[key] = value
    }

    /**
     * When a save is loaded.
     */
    override suspend fun load() {
        // Clear the buffer.
        _switchBuffer.clear()
        _timestampBuffer.clear()
        _variableBuffer.clear()
    }

    /**
     * Flush the buffers to database.
     */
    override suspend fun flush() {
        _switchBuffer.forEach {
            savedSwitchDao.insertOrUpdate(
                SavedSwitchTable(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    key = it.key,
                    value = it.value,
                )
            )
        }
        _timestampBuffer.forEach {
            savedTimestampDao.insertOrUpdate(
                SavedTimestampTable(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    key = it.key,
                    timestamp = it.value,
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