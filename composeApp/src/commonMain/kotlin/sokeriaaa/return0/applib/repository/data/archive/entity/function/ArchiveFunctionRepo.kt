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
package sokeriaaa.return0.applib.repository.data.archive.entity.function

import sokeriaaa.return0.shared.data.models.action.function.FunctionData

class ArchiveFunctionRepo {
    // All function data.
    private val _functionNameDataMap: MutableMap<String, FunctionData> = HashMap()

    operator fun get(name: String): FunctionData? = _functionNameDataMap[name]
    operator fun set(name: String, functionData: FunctionData) {
        _functionNameDataMap[name] = functionData
    }

    fun add(functionData: FunctionData) {
        this[functionData.name] = functionData
    }

    fun addAll(vararg functionData: FunctionData) {
        functionData.forEach { this[it.name] = it }
    }

    /**
     * Returns all available function data.
     */
    fun toList(): List<FunctionData> = _functionNameDataMap.values.toList()

    /**
     * Reset all data.
     */
    fun reset() {
        _functionNameDataMap.clear()
    }
}