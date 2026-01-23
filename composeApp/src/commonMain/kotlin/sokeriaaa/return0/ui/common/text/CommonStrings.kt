package sokeriaaa.return0.ui.common.text

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
object CommonStrings {

    private val _romanMapping = listOf(
        1000 to "M",
        900 to "CM",
        500 to "D",
        400 to "CD",
        100 to "C",
        90 to "XC",
        50 to "L",
        40 to "XL",
        10 to "X",
        9 to "IX",
        5 to "V",
        4 to "IV",
        1 to "I",
    )

    /**
     * Display a console-styled progress bar as HP bar.
     */
    fun hpBar(
        rate: Float,
        length: Int = 18,
    ): String {
        val consumed = (length * (1 - rate)).toInt().coerceIn(0, length)
        return buildString {
            append('[')
            repeat(length - consumed) {
                append('#')
            }
            repeat(consumed) {
                append('.')
            }
            append(']')
        }
    }

    /**
     * Convert an Int to roman number. Returns itself when a roman number is not available.
     */
    fun intToRomanOrDefault(value: Int): String {
        return if (value in 1..3999) {
            buildString {
                var valueLeft = value
                _romanMapping.forEach { (v, l) ->
                    while (valueLeft >= v) {
                        append(l)
                        valueLeft -= v
                    }
                }
            }
        } else {
            value.toString()
        }
    }

}