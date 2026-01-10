/**
 * Copyright (C) 2026 Sokeriaaa
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
package sokeriaaa.return0.applib.repository.settings

import kotlinx.coroutines.flow.Flow
import sokeriaaa.return0.applib.datastore.AppKey
import sokeriaaa.return0.applib.datastore.AppKeyValues
import sokeriaaa.return0.applib.datastore.AppKeys

class SettingsRepo(
    private val keyValues: AppKeyValues,
) {

    val gameplayDisplayItemDesc: Entry<Boolean> =
        BooleanEntry(AppKeys.GAMEPLAY_DISPLAY_ITEM_DESC, true)

    val combatAuto: Entry<Boolean> =
        BooleanEntry(AppKeys.COMBAT_AUTO, false)

    /**
     * KeyValue Entry.
     */
    abstract class Entry<T>(
        protected val key: AppKey<T>,
        val defaultValue: T,
    ) {
        abstract val flow: Flow<T>
        abstract suspend fun set(value: T)
    }

    private inner class BooleanEntry(
        key: AppKey<Boolean>,
        defaultValue: Boolean = false,
    ) : Entry<Boolean>(key, defaultValue) {
        override val flow: Flow<Boolean>
            get() = keyValues.getBooleanFlow(key, defaultValue)

        override suspend fun set(value: Boolean) {
            keyValues[key] = value
        }
    }

}