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

import kotlinx.coroutines.flow.Flow

/**
 * Preference manager.
 */
interface AppKeyValues {
    fun getIntFlow(key: String, defaultValue: Int = 0): Flow<Int>
    fun getIntOrNullFlow(key: String): Flow<Int?>
    fun getLongFlow(key: String, defaultValue: Long = 0L): Flow<Long>
    fun getLongOrNullFlow(key: String): Flow<Long?>
    fun getStringFlow(key: String, defaultValue: String = ""): Flow<String>
    fun getStringOrNullFlow(key: String): Flow<String?>
    fun getFloatFlow(key: String, defaultValue: Float = 0F): Flow<Float>
    fun getFloatOrNullFlow(key: String): Flow<Float?>
    fun getDoubleFlow(key: String, defaultValue: Double = 0.0): Flow<Double>
    fun getDoubleOrNullFlow(key: String): Flow<Double?>
    fun getBooleanFlow(key: String, defaultValue: Boolean = false): Flow<Boolean>
    fun getBooleanOrNullFlow(key: String): Flow<Boolean?>

    suspend operator fun set(key: String, value: Int)
    suspend operator fun set(key: String, value: Long)
    suspend operator fun set(key: String, value: String)
    suspend operator fun set(key: String, value: Float)
    suspend operator fun set(key: String, value: Double)
    suspend operator fun set(key: String, value: Boolean)

    suspend fun remove(key: String)
    suspend fun clear()
}