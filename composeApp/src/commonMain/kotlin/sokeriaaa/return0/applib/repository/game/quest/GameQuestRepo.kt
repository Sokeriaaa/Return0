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
package sokeriaaa.return0.applib.repository.game.quest

import androidx.compose.runtime.mutableStateMapOf
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.game.base.BaseGameRepo
import sokeriaaa.return0.applib.room.dao.QuestDao
import sokeriaaa.return0.applib.room.table.QuestTable

class GameQuestRepo(
    private val questDao: QuestDao,
) : BaseGameRepo {

    private val _activatedQuests: MutableMap<String, Long?> = mutableStateMapOf()
    val activatedQuests: Map<String, Long?> = _activatedQuests

    private val _acceptedBuffer: MutableMap<String, Long?> = HashMap()
    private val _completedBuffer: MutableList<String> = ArrayList()

    fun acceptedQuest(key: String, expiredAt: Long? = null) {
        _activatedQuests[key] = expiredAt
        _acceptedBuffer[key] = expiredAt
    }

    fun completedQuest(key: String) {
        _activatedQuests.remove(key)
        _acceptedBuffer.remove(key)
        _completedBuffer.add(key)
    }

    suspend fun isCompleted(key: String): Boolean {
        return (key in _completedBuffer) ||
                questDao.query(AppConstants.CURRENT_SAVE_ID, key)?.completed == true
    }

    override suspend fun load() {
        _activatedQuests.clear()
        _acceptedBuffer.clear()
        _completedBuffer.clear()
        questDao.queryActivated(saveID = AppConstants.CURRENT_SAVE_ID).forEach {
            _activatedQuests[it.key] = it.expiredAt
        }
    }

    override suspend fun flush() {
        questDao.insertList(
            _acceptedBuffer.map {
                QuestTable(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    key = it.key,
                    expiredAt = it.value,
                    completed = false,
                )
            }
        )
        questDao.insertList(
            _completedBuffer.map {
                QuestTable(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    key = it,
                    expiredAt = null,
                    completed = true,
                )
            }
        )
        _acceptedBuffer.clear()
        _completedBuffer.clear()
    }

}