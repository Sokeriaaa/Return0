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

actual class AppKey<T>(actual val name: String)

actual fun intPreferencesKey(name: String): AppKey<Int> = AppKey(name)
actual fun doublePreferencesKey(name: String): AppKey<Double> = AppKey(name)
actual fun stringPreferencesKey(name: String): AppKey<String> = AppKey(name)
actual fun booleanPreferencesKey(name: String): AppKey<Boolean> = AppKey(name)
actual fun floatPreferencesKey(name: String): AppKey<Float> = AppKey(name)
actual fun longPreferencesKey(name: String): AppKey<Long> = AppKey(name)