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
            return MapRow(
                depth = state.braceDepth,
                text = MapRowText.Close,
                isInBuggyRange = isInBuggyRange,
            )
        }

        // Generate row.
        val random = Random(seed = "${mapData.name}-$index-${state.braceDepth}".hashCode())

        // Close the brace right now.
        if (state.braceDepth > 0) {
            val shouldClose = random.nextInt(100) < 5 shl state.braceDepth
            if (shouldClose) {
                state.braceDepth--
                return MapRow(
                    depth = state.braceDepth,
                    text = MapRowText.Close,
                    isInBuggyRange = isInBuggyRange,
                )
            }
        }

        // Type
        val type = random.nextInt(22)
        if (type in 0..3) {
            state.braceDepth++
        }
        // depth
        val depth = if (type in 0..3) {
            state.braceDepth - 1
        } else {
            state.braceDepth
        }
        // Generate code
        val text = when (type) {
            // braceDepth++
            0 -> MapRowText.If(random.nextInt(10))
            1 -> MapRowText.While(random.nextInt(5), random.nextInt(10) + 1)
            2 -> MapRowText.For(random.nextInt(10), random.nextInt(100))
            3 -> MapRowText.Repeat(random.nextInt(10))
            // common
            4 -> MapRowText.Random(random.nextInt(100))
            5 -> MapRowText.Variable(random.nextInt(100), random.nextInt(10))
            6 -> MapRowText.Flag(random.nextInt(100) + 10000, random.nextBoolean())
            7 -> MapRowText.Temp(random.nextInt(50), random.nextInt(5))
            8 -> MapRowText.Counter(random.nextInt(5))
            9 -> MapRowText.TempMinus(random.nextInt(50), random.nextInt(2))
            10 -> MapRowText.CounterPP
            11 -> MapRowText.Handle(random.nextInt(4), random.nextInt(100))
            12 -> MapRowText.PrintLn(random.nextInt(100))
            13 -> MapRowText.Debug(random.nextInt(1000))
            14 -> MapRowText.TodoRefactor
            15 -> MapRowText.TodoReview
            16 -> MapRowText.Legacy
            17 -> MapRowText.Works
            18 -> MapRowText.UnderscoreVariable(random.nextInt(10))
            19 -> MapRowText.Check(random.nextInt(10))
            20 -> MapRowText.Cleanup
            21 -> MapRowText.SyncCache
            else -> MapRowText.Default
        }
        return MapRow(
            depth = depth,
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