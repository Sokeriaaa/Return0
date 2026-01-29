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
package sokeriaaa.return0.models.story.map

sealed class MapRowText {
    data class Fun(val a: String) : MapRowText()
    data class If(val a: Int) : MapRowText()
    data class While(val a: Int, val b: Int) : MapRowText()
    data class For(val a: Int, val b: Int) : MapRowText()
    data class Repeat(val a: Int) : MapRowText()

    data class Random(val a: Int) : MapRowText()
    data class Variable(val a: Int, val b: Int) : MapRowText()
    data class Flag(val a: Int, val b: Boolean) : MapRowText()
    data class Temp(val a: Int, val b: Int) : MapRowText()
    data class Counter(val a: Int) : MapRowText()
    data class TempMinus(val a: Int, val b: Int) : MapRowText()
    data object CounterPP : MapRowText()
    data class Handle(val a: Int, val b: Int) : MapRowText()
    data class PrintLn(val a: Int) : MapRowText()
    data class Debug(val a: Int) : MapRowText()
    data object TodoRefactor : MapRowText()
    data object TodoReview : MapRowText()
    data object Legacy : MapRowText()
    data object Works : MapRowText()
    data class UnderscoreVariable(val a: Int) : MapRowText()
    data class Check(val a: Int) : MapRowText()
    data object Cleanup : MapRowText()
    data object SyncCache : MapRowText()
    data object Default : MapRowText()
    data object Close : MapRowText()

    data class Events(val a: String) : MapRowText()
    data class BlockedRow(val a: String) : MapRowText()
}