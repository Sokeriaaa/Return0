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
package sokeriaaa.return0.test.ui.common.text

import sokeriaaa.return0.ui.common.text.CommonStrings
import kotlin.test.Test
import kotlin.test.assertEquals

class CommonStringsTest {

    @Test
    fun `hpBar should render correct proportions`() {
        assertEquals("[##################]", CommonStrings.hpBar(1.0f))
        assertEquals("[..................]", CommonStrings.hpBar(0.0f))
        assertEquals("[#########.........]", CommonStrings.hpBar(0.5f))
        assertEquals("[#####.]", CommonStrings.hpBar(0.7f, length = 6))
    }

    @Test
    fun `hpBar should handle out of bounds rates`() {
        // Rate > 1 should be treated as full
        assertEquals("[##########]", CommonStrings.hpBar(1.5f, length = 10))
        // Rate < 0 should be treated as empty
        assertEquals("[..........]", CommonStrings.hpBar(-0.5f, length = 10))
    }

    @Test
    fun `intToRomanOrDefault should convert valid integers to Roman numerals`() {
        val cases = mapOf(
            1 to "I",
            4 to "IV",
            9 to "IX",
            42 to "XLII",
            99 to "XCIX",
            500 to "D",
            1987 to "MCMLXXXVII",
            3999 to "MMMCMXCIX"
        )

        cases.forEach { (input, expected) ->
            assertEquals(expected, CommonStrings.intToRomanOrDefault(input))
        }
    }

    @Test
    fun `intToRomanOrDefault should return string representation for values out of Roman range`() {
        assertEquals("0", CommonStrings.intToRomanOrDefault(0))
        assertEquals("-1", CommonStrings.intToRomanOrDefault(-1))
        assertEquals("4000", CommonStrings.intToRomanOrDefault(4000))
    }
}
