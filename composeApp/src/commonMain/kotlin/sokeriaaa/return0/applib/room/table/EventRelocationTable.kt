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
 * Relocating events.
 */
expect class EventRelocationTable(
    saveID: Int,
    eventKey: String,
    fileName: String,
    lineNumber: Int,
) {

    /**
     * The save ID. -1 presents the temporary save the user is current playing.
     */
    var saveID: Int

    /**
     * Event key.
     */
    var eventKey: String

    /**
     * Current file name. (For game map)
     */
    var fileName: String

    /**
     * Current line number. (For game map)
     */
    var lineNumber: Int
}