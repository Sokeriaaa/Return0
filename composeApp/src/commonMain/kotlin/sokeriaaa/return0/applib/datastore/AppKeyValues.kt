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
    fun getIntFlow(key: AppKey<Int>, defaultValue: Int = 0): Flow<Int>
    fun getIntOrNullFlow(key: AppKey<Int>): Flow<Int?>
    fun getLongFlow(key: AppKey<Long>, defaultValue: Long = 0L): Flow<Long>
    fun getLongOrNullFlow(key: AppKey<Long>): Flow<Long?>
    fun getStringFlow(key: AppKey<String>, defaultValue: String = ""): Flow<String>
    fun getStringOrNullFlow(key: AppKey<String>): Flow<String?>
    fun getFloatFlow(key: AppKey<Float>, defaultValue: Float = 0F): Flow<Float>
    fun getFloatOrNullFlow(key: AppKey<Float>): Flow<Float?>
    fun getDoubleFlow(key: AppKey<Double>, defaultValue: Double = 0.0): Flow<Double>
    fun getDoubleOrNullFlow(key: AppKey<Double>): Flow<Double?>
    fun getBooleanFlow(key: AppKey<Boolean>, defaultValue: Boolean = false): Flow<Boolean>
    fun getBooleanOrNullFlow(key: AppKey<Boolean>): Flow<Boolean?>

    suspend operator fun set(key: AppKey<Int>, value: Int)
    suspend operator fun set(key: AppKey<Long>, value: Long)
    suspend operator fun set(key: AppKey<String>, value: String)
    suspend operator fun set(key: AppKey<Float>, value: Float)
    suspend operator fun set(key: AppKey<Double>, value: Double)
    suspend operator fun set(key: AppKey<Boolean>, value: Boolean)

    suspend fun <T> remove(key: AppKey<T>)
    suspend fun clear()

}