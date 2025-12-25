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
package sokeriaaa.return0.test.models.story.map

import sokeriaaa.return0.models.story.map.MapGenerator
import sokeriaaa.return0.shared.data.models.story.map.MapData
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MapGeneratorTest {

    @Test
    fun `generateCode is deterministic`() {
        val mapData = MapData(
            name = "generated_${Random.nextInt(10000)}",
            lines = Random.nextInt(100) + 100,
            buggyRange = emptyList(),
            buggyEntries = emptyList(),
            difficulty = 1,
            events = emptyList(),
        )

        val mapRows1 = MapGenerator.generateCode(mapData)
        val mapRows2 = MapGenerator.generateCode(mapData)

        // Compare two lists.
        assertEquals(mapRows1.size, mapRows2.size)
        repeat(mapRows1.size) { i ->
            assertEquals(mapRows1[i].text, mapRows2[i].text)
            assertEquals(mapRows1[i].depth, mapRows2[i].depth)
            assertEquals(mapRows1[i].events.size, mapRows2[i].events.size)
            repeat(mapRows1[i].events.size) { j ->
                assertEquals(mapRows1[i].events[j], mapRows2[i].events[j])
            }
        }
    }

    @Test
    fun `generateCode produces exactly N rows`() {
        val expectedRows = Random.nextInt(100) + 100
        val mapData = MapData(
            name = "generated_${Random.nextInt(10000)}",
            lines = expectedRows,
            buggyRange = emptyList(),
            buggyEntries = emptyList(),
            difficulty = 1,
            events = emptyList(),
        )
        val rows = MapGenerator.generateCode(mapData)
        assertEquals(expectedRows, rows.size)
    }

    @Test
    fun `brace depth never goes negative`() {
        val mapData = MapData(
            name = "generated_${Random.nextInt(10000)}",
            lines = Random.nextInt(100) + 100,
            buggyRange = emptyList(),
            buggyEntries = emptyList(),
            difficulty = 1,
            events = emptyList(),
        )
        val rows = MapGenerator.generateCode(mapData)
        rows.forEach {
            assertTrue(it.depth >= 0)
        }
    }

    @Test
    fun `all braces are closed at end`() {
        val mapData = MapData(
            name = "generated_${Random.nextInt(10000)}",
            lines = Random.nextInt(100) + 100,
            buggyRange = emptyList(),
            buggyEntries = emptyList(),
            difficulty = 1,
            events = emptyList(),
        )
        val rows = MapGenerator.generateCode(mapData)

        var depth = 0
        rows.forEach { row ->
            if (row.text.endsWith("{")) depth++
            if (row.text == "}") depth--
        }

        assertEquals(0, depth)
    }

    @Test
    fun `generated text is never blank`() {
        val mapData = MapData(
            name = "generated_${Random.nextInt(10000)}",
            lines = Random.nextInt(100) + 100,
            buggyRange = emptyList(),
            buggyEntries = emptyList(),
            difficulty = 1,
            events = emptyList(),
        )
        val rows = MapGenerator.generateCode(mapData)

        rows.forEach {
            assertTrue(it.text.isNotBlank())
        }
    }

}