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
package sokeriaaa.return0.applib.repository.game.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.game.base.BaseGameRepo
import sokeriaaa.return0.applib.room.dao.SaveMetaDao
import sokeriaaa.return0.shared.data.models.title.Title

/**
 * Stores player meta.
 */
class GamePlayerRepo(
    private val saveMetaDao: SaveMetaDao,
) : BaseGameRepo {

    var title: Title by mutableStateOf(Title.INTERN)
        private set

    fun updateTitle(title: Title) {
        this.title = title
    }

    override suspend fun load() {
        title = saveMetaDao.query(AppConstants.CURRENT_SAVE_ID)?.title ?: Title.INTERN
    }

    override suspend fun flush() {
        saveMetaDao.updateTitle(AppConstants.CURRENT_SAVE_ID, title)
    }
}