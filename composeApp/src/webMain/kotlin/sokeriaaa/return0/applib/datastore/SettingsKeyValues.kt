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
package sokeriaaa.return0.applib.datastore

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getBooleanOrNullFlow
import com.russhwolf.settings.coroutines.getDoubleFlow
import com.russhwolf.settings.coroutines.getDoubleOrNullFlow
import com.russhwolf.settings.coroutines.getFloatFlow
import com.russhwolf.settings.coroutines.getFloatOrNullFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getIntOrNullFlow
import com.russhwolf.settings.coroutines.getLongFlow
import com.russhwolf.settings.coroutines.getLongOrNullFlow
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow

/**
 * Implement of [AppKeyValues] via Multiplatform Settings.
 */
@OptIn(ExperimentalSettingsApi::class)
class SettingsKeyValues(private val settings: ObservableSettings) : AppKeyValues {
    override fun getIntFlow(key: String, defaultValue: Int): Flow<Int> =
        settings.getIntFlow(key, defaultValue)

    override fun getIntOrNullFlow(key: String): Flow<Int?> =
        settings.getIntOrNullFlow(key)

    override fun getLongFlow(key: String, defaultValue: Long): Flow<Long> =
        settings.getLongFlow(key, defaultValue)

    override fun getLongOrNullFlow(key: String): Flow<Long?> =
        settings.getLongOrNullFlow(key)

    override fun getStringFlow(key: String, defaultValue: String): Flow<String> =
        settings.getStringFlow(key, defaultValue)

    override fun getStringOrNullFlow(key: String): Flow<String?> =
        settings.getStringOrNullFlow(key)

    override fun getFloatFlow(key: String, defaultValue: Float): Flow<Float> =
        settings.getFloatFlow(key, defaultValue)

    override fun getFloatOrNullFlow(key: String): Flow<Float?> =
        settings.getFloatOrNullFlow(key)

    override fun getDoubleFlow(key: String, defaultValue: Double): Flow<Double> =
        settings.getDoubleFlow(key, defaultValue)

    override fun getDoubleOrNullFlow(key: String): Flow<Double?> =
        settings.getDoubleOrNullFlow(key)

    override fun getBooleanFlow(key: String, defaultValue: Boolean): Flow<Boolean> =
        settings.getBooleanFlow(key, defaultValue)

    override fun getBooleanOrNullFlow(key: String): Flow<Boolean?> =
        settings.getBooleanOrNullFlow(key)

    override suspend fun set(key: String, value: Int) {
        settings[key] = value
    }

    override suspend fun set(key: String, value: Long) {
        settings[key] = value
    }

    override suspend fun set(key: String, value: String) {
        settings[key] = value
    }

    override suspend fun set(key: String, value: Float) {
        settings[key] = value
    }

    override suspend fun set(key: String, value: Double) {
        settings[key] = value
    }

    override suspend fun set(key: String, value: Boolean) {
        settings[key] = value
    }

    override suspend fun remove(key: String) {
        settings.remove(key)
    }

    override suspend fun clear() {
        settings.clear()
    }

}