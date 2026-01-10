/**
 * Copyright (C) 2026 Sokeriaaa
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General expect License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Affero General expect License for more details.
 *
 * You should have received a copy of the GNU Affero General expect License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */
package sokeriaaa.return0.applib.datastore

import androidx.datastore.preferences.core.Preferences

actual typealias AppKey<T> = Preferences.Key<T>

actual fun intPreferencesKey(name: String): AppKey<Int> =
    androidx.datastore.preferences.core.intPreferencesKey(name)

actual fun doublePreferencesKey(name: String): AppKey<Double> =
    androidx.datastore.preferences.core.doublePreferencesKey(name)

actual fun stringPreferencesKey(name: String): AppKey<String> =
    androidx.datastore.preferences.core.stringPreferencesKey(name)

actual fun booleanPreferencesKey(name: String): AppKey<Boolean> =
    androidx.datastore.preferences.core.booleanPreferencesKey(name)

actual fun floatPreferencesKey(name: String): AppKey<Float> =
    androidx.datastore.preferences.core.floatPreferencesKey(name)

actual fun longPreferencesKey(name: String): AppKey<Long> =
    androidx.datastore.preferences.core.longPreferencesKey(name)