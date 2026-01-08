/**
 * Copyright (C) 2025 Sokeriaaa
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
package sokeriaaa.return0.models.story.map

import sokeriaaa.return0.shared.data.models.story.map.MapData
import kotlin.random.Random

/**
 * Generate map rows.
 */
object MapGenerator {

    /**
     * Generate a full source code file.
     */
    fun generateCode(
        mapData: MapData
    ): List<MapRow> {
        val state = State()
        val rows = ArrayList<MapRow>()
        repeat(mapData.lines) {
            rows.add(generateLine(mapData = mapData, index = it, state = state))
        }
        return rows
    }

    /**
     * Generate a single line of code.
     */
    private fun generateLine(
        mapData: MapData,
        index: Int,
        state: State,
    ): MapRow {
        val currentRow = index + 1
        val isInBuggyRange = mapData.buggyRange.any {
            it.first <= currentRow && currentRow <= it.second
        }
        // If the map generation is about to finish, and brace depth is too high to finish the code,
        // Force it to generate the brace.
        if (mapData.lines - index <= state.braceDepth) {
            state.braceDepth--
            return MapRow(depth = state.braceDepth, text = "}", isInBuggyRange = isInBuggyRange)
        }

        // Generate row.
        val random = Random(seed = "${mapData.name}-$index-${state.braceDepth}".hashCode())

        // Close the brace right now.
        if (state.braceDepth > 0) {
            val shouldClose = random.nextInt(100) < 5 shl state.braceDepth
            if (shouldClose) {
                state.braceDepth--
                return MapRow(depth = state.braceDepth, text = "}", isInBuggyRange = isInBuggyRange)
            }
        }

        val type = random.nextInt(22)
        // Generate code
        val text = when (type) {
            // braceDepth++
            0 -> "if (flag${random.nextInt(10)}) {"
            1 -> "while (counter${random.nextInt(5)} < ${random.nextInt(10) + 1}) {"
            2 -> "for (item${random.nextInt(10)} in list${random.nextInt(100)}) {"
            3 -> "repeat (${random.nextInt(10)}) {"
            // common
            4 -> "val rnd${random.nextInt(100)} = Random.nextInt()"
            5 -> "val v${random.nextInt(100)} = ${random.nextInt(10)}"
            6 -> "val v${random.nextInt(100) + 10000} = ${random.nextBoolean()}"
            7 -> "var temp${random.nextInt(50)} = ${random.nextInt(5)}"
            8 -> "var counter = ${random.nextInt(5)}"
            9 -> "temp${random.nextInt(50)} -= ${random.nextInt(2)}"
            10 -> "counter++"
            11 -> "handle${random.nextInt(4)}(v${random.nextInt(100)})"
            12 -> "println(\"state=${random.nextInt(100)}\")"
            13 -> "debug(\"trace ${random.nextInt(1000)}\")"
            14 -> "// TODO: refactor later"
            15 -> "// TODO: review logic"
            16 -> "// legacy code"
            17 -> "// works for now"
            18 -> "val _ = ${random.nextInt(10)}"
            19 -> "check(flag${random.nextInt(10)})"
            20 -> "cleanup()"
            21 -> "syncCache()"
            else -> "val nullable: Any? = null"
        }
        if (type in 0..3) {
            state.braceDepth++
        }
        return MapRow(
            depth = if (type in 0..3) {
                state.braceDepth - 1
            } else {
                state.braceDepth
            },
            text = text,
            isInBuggyRange = isInBuggyRange,
        )
    }

    /**
     * Generator state.
     */
    private class State(
        var braceDepth: Int = 0,
    )
}