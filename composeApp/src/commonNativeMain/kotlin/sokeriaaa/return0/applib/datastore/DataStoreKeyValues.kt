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
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implement of [AppKeyValues] via DataStore.
 */
class DataStoreKeyValues(
    private val dataStore: DataStore<Preferences>
) : AppKeyValues {
    override fun getIntFlow(key: String, defaultValue: Int): Flow<Int> =
        dataStore.data.map { it[intPreferencesKey(key)] ?: defaultValue }

    override fun getIntOrNullFlow(key: String): Flow<Int?> =
        dataStore.data.map { it[intPreferencesKey(key)] }

    override fun getLongFlow(key: String, defaultValue: Long): Flow<Long> =
        dataStore.data.map { it[longPreferencesKey(key)] ?: defaultValue }

    override fun getLongOrNullFlow(key: String): Flow<Long?> =
        dataStore.data.map { it[longPreferencesKey(key)] }

    override fun getStringFlow(key: String, defaultValue: String): Flow<String> =
        dataStore.data.map { it[stringPreferencesKey(key)] ?: defaultValue }

    override fun getStringOrNullFlow(key: String): Flow<String?> =
        dataStore.data.map { it[stringPreferencesKey(key)] }

    override fun getFloatFlow(key: String, defaultValue: Float): Flow<Float> =
        dataStore.data.map { it[floatPreferencesKey(key)] ?: defaultValue }

    override fun getFloatOrNullFlow(key: String): Flow<Float?> =
        dataStore.data.map { it[floatPreferencesKey(key)] }

    override fun getDoubleFlow(key: String, defaultValue: Double): Flow<Double> =
        dataStore.data.map { it[doublePreferencesKey(key)] ?: defaultValue }

    override fun getDoubleOrNullFlow(key: String): Flow<Double?> =
        dataStore.data.map { it[doublePreferencesKey(key)] }

    override fun getBooleanFlow(key: String, defaultValue: Boolean): Flow<Boolean> =
        dataStore.data.map { it[booleanPreferencesKey(key)] ?: defaultValue }

    override fun getBooleanOrNullFlow(key: String): Flow<Boolean?> =
        dataStore.data.map { it[booleanPreferencesKey(key)] }

    override suspend fun set(key: String, value: Int) {
        dataStore.edit { it[intPreferencesKey(key)] = value }
    }

    override suspend fun set(key: String, value: Long) {
        dataStore.edit { it[longPreferencesKey(key)] = value }
    }

    override suspend fun set(key: String, value: String) {
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    override suspend fun set(key: String, value: Float) {
        dataStore.edit { it[floatPreferencesKey(key)] = value }
    }

    override suspend fun set(key: String, value: Double) {
        dataStore.edit { it[doublePreferencesKey(key)] = value }
    }

    override suspend fun set(key: String, value: Boolean) {
        dataStore.edit { it[booleanPreferencesKey(key)] = value }
    }

    override suspend fun remove(key: String) {
        dataStore.edit { it.remove(stringPreferencesKey(key)) }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}