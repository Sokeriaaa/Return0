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

/**
 * Implement of [AppKeyValues] via Multiplatform Settings.
 */
@OptIn(ExperimentalSettingsApi::class)
class SettingsKeyValues(private val settings: ObservableSettings) : AppKeyValues {
    override fun getIntFlow(key: AppKey<Int>, defaultValue: Int) =
        settings.getIntFlow(key.name, defaultValue)

    override fun getIntOrNullFlow(key: AppKey<Int>) =
        settings.getIntOrNullFlow(key.name)

    override fun getLongFlow(key: AppKey<Long>, defaultValue: Long) =
        settings.getLongFlow(key.name, defaultValue)

    override fun getLongOrNullFlow(key: AppKey<Long>) =
        settings.getLongOrNullFlow(key.name)

    override fun getStringFlow(key: AppKey<String>, defaultValue: String) =
        settings.getStringFlow(key.name, defaultValue)

    override fun getStringOrNullFlow(key: AppKey<String>) =
        settings.getStringOrNullFlow(key.name)

    override fun getFloatFlow(key: AppKey<Float>, defaultValue: Float) =
        settings.getFloatFlow(key.name, defaultValue)

    override fun getFloatOrNullFlow(key: AppKey<Float>) =
        settings.getFloatOrNullFlow(key.name)

    override fun getDoubleFlow(key: AppKey<Double>, defaultValue: Double) =
        settings.getDoubleFlow(key.name, defaultValue)

    override fun getDoubleOrNullFlow(key: AppKey<Double>) =
        settings.getDoubleOrNullFlow(key.name)

    override fun getBooleanFlow(key: AppKey<Boolean>, defaultValue: Boolean) =
        settings.getBooleanFlow(key.name, defaultValue)

    override fun getBooleanOrNullFlow(key: AppKey<Boolean>) =
        settings.getBooleanOrNullFlow(key.name)

    override suspend fun set(key: AppKey<Int>, value: Int) {
        settings[key.name] = value
    }

    override suspend fun set(key: AppKey<Long>, value: Long) {
        settings[key.name] = value
    }

    override suspend fun set(key: AppKey<String>, value: String) {
        settings[key.name] = value
    }

    override suspend fun set(key: AppKey<Float>, value: Float) {
        settings[key.name] = value
    }

    override suspend fun set(key: AppKey<Double>, value: Double) {
        settings[key.name] = value
    }

    override suspend fun set(key: AppKey<Boolean>, value: Boolean) {
        settings[key.name] = value
    }

    override suspend fun <T> remove(key: AppKey<T>) {
        settings.remove(key.name)
    }

    override suspend fun clear() {
        settings.clear()
    }

}