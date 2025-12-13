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
package sokeriaaa.return0.applib.room.table

/**
 * Quest table.
 */
expect class QuestTable(
    saveID: Int,
    key: String,
    expiredAt: Long? = null,
    completed: Boolean = false,
) {

    /**
     * The save ID. -1 presents the temporary save the user is current playing.
     */
    var saveID: Int

    /**
     * The key of quest.
     */
    var key: String

    /**
     * Expired time in millis. `null` presents this quest will never expire.
     * Expired quests will be removed from the database, whether it's completed or not.
     */
    var expiredAt: Long?

    /**
     * This quest is completed.
     */
    var completed: Boolean
}