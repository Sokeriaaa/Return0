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

expect class AppKey<T> {
    val name: String
}

expect fun intPreferencesKey(name: String): AppKey<Int>
expect fun doublePreferencesKey(name: String): AppKey<Double>
expect fun stringPreferencesKey(name: String): AppKey<String>
expect fun booleanPreferencesKey(name: String): AppKey<Boolean>
expect fun floatPreferencesKey(name: String): AppKey<Float>
expect fun longPreferencesKey(name: String): AppKey<Long>