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

}