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

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

/**
 * Implement of [AppKeyValues] via DataStore.
 */
class DataStoreKeyValues(
    private val dataStore: DataStore<Preferences>
) : AppKeyValues {
    override fun getIntFlow(key: AppKey<Int>, defaultValue: Int) =
        dataStore.data.map { it[key] ?: defaultValue }

    override fun getIntOrNullFlow(key: AppKey<Int>) =
        dataStore.data.map { it[key] }

    override fun getLongFlow(key: AppKey<Long>, defaultValue: Long) =
        dataStore.data.map { it[key] ?: defaultValue }

    override fun getLongOrNullFlow(key: AppKey<Long>) =
        dataStore.data.map { it[key] }

    override fun getStringFlow(key: AppKey<String>, defaultValue: String) =
        dataStore.data.map { it[key] ?: defaultValue }

    override fun getStringOrNullFlow(key: AppKey<String>) =
        dataStore.data.map { it[key] }

    override fun getFloatFlow(key: AppKey<Float>, defaultValue: Float) =
        dataStore.data.map { it[key] ?: defaultValue }

    override fun getFloatOrNullFlow(key: AppKey<Float>) =
        dataStore.data.map { it[key] }

    override fun getDoubleFlow(key: AppKey<Double>, defaultValue: Double) =
        dataStore.data.map { it[key] ?: defaultValue }

    override fun getDoubleOrNullFlow(key: AppKey<Double>) =
        dataStore.data.map { it[key] }

    override fun getBooleanFlow(key: AppKey<Boolean>, defaultValue: Boolean) =
        dataStore.data.map { it[key] ?: defaultValue }

    override fun getBooleanOrNullFlow(key: AppKey<Boolean>) =
        dataStore.data.map { it[key] }

    override suspend fun set(key: AppKey<Int>, value: Int) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun set(key: AppKey<Long>, value: Long) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun set(key: AppKey<String>, value: String) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun set(key: AppKey<Float>, value: Float) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun set(key: AppKey<Double>, value: Double) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun set(key: AppKey<Boolean>, value: Boolean) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun <T> remove(key: AppKey<T>) {
        dataStore.edit { it.remove(key) }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}