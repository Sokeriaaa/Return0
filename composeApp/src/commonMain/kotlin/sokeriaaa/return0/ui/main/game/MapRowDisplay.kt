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
package sokeriaaa.return0.ui.main.game

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.materialkolor.ktx.harmonizeWithPrimary
import sokeriaaa.return0.models.story.map.MapRowText
import sokeriaaa.return0.ui.theme.AppColor


@Composable
fun MapRowText.assembleText(depth: Int): AnnotatedString = buildAnnotatedString {
    append("    ".repeat(depth + 1))
    when (this@assembleText) {
        // fun
        is MapRowText.Fun -> {
            keyword("fun ")
            function(a)
            operator("() {")
        }

        // braceDepth++
        is MapRowText.If -> {
            keyword("if ")
            operator("(")
            variable("flag$a")
            operator(") {")
        }

        is MapRowText.While -> {
            keyword("while ")
            operator("(")
            variable("counter$a")
            operator(" < ")
            number("$b")
            operator(") {")
        }

        is MapRowText.For -> {
            keyword("for ")
            operator("(")
            variable("item$a")
            keyword(" in ")
            variable("list$b")
            operator(") {")
        }

        is MapRowText.Repeat -> {
            function("repeat ")
            operator("(")
            number(a.toString())
            operator(") {")
        }

        is MapRowText.Random -> {
            keyword("val ")
            variable("rnd$a")
            operator(" = ")
            function("Random")
            operator(".")
            function("nextInt")
            operator("()")
        }

        is MapRowText.Variable -> {
            keyword("val ")
            variable("v$a")
            operator(" = ")
            number(b.toString())
        }

        is MapRowText.Flag -> {
            keyword("val ")
            variable("v$a")
            operator(" = ")
            keyword(b.toString())
        }

        is MapRowText.Temp -> {
            keyword("var ")
            variable("temp$a")
            operator(" = ")
            number(b.toString())
        }

        is MapRowText.Counter -> {
            keyword("var ")
            variable("counter")
            operator(" = ")
            number(a.toString())
        }

        is MapRowText.TempMinus -> {
            variable("temp$a")
            operator(" -= ")
            number(b.toString())
        }

        is MapRowText.CounterPP -> {
            variable("counter")
            operator("++")
        }

        is MapRowText.Handle -> {
            function("handle$a")
            operator("(")
            variable("v$b")
            operator(")")
        }

        is MapRowText.PrintLn -> {
            function("println")
            operator("(")
            string("\"state=$a\"")
            operator(")")
        }

        is MapRowText.Debug -> {
            function("debug")
            operator("(")
            string("\"trace $a\"")
            operator(")")
        }

        MapRowText.TodoRefactor -> {
            comment("// TODO: refactor later")
        }

        MapRowText.TodoReview -> {
            comment("// TODO: review logic")
        }

        MapRowText.Legacy -> {
            comment("// legacy code")
        }

        MapRowText.Works -> {
            comment("// works for now")
        }

        is MapRowText.UnderscoreVariable -> {
            keyword("val ")
            variable("_")
            operator(" = ")
            number(a.toString())
        }

        is MapRowText.Check -> {
            function("check")
            operator("(")
            variable("flag$a")
            operator(")")
        }

        MapRowText.Cleanup -> {
            function("cleanup")
            operator("()")
        }

        MapRowText.SyncCache -> {
            function("syncCache")
            operator("()")
        }

        MapRowText.Default -> {
            keyword("val ")
            variable("nullable")
            operator(": ")
            function("Any")
            operator("?")
            operator(" = ")
            keyword("null")
        }

        MapRowText.Close -> {
            operator("}")
        }

        is MapRowText.Events -> {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.harmonizeWithPrimary(
                        color = AppColor.colorScheme.event,
                    ),
                    textDecoration = TextDecoration.Underline,
                ),
            ) {
                append(a)
            }
        }

        is MapRowText.BlockedRow -> {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.harmonizeWithPrimary(
                        color = AppColor.colorScheme.blocked,
                    ),
                    textDecoration = TextDecoration.Underline,
                ),
            ) {
                append(a)
            }
        }
    }
}

// AnnotationString extensions
@Composable
private fun AnnotatedString.Builder.keyword(keyword: String) {
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.harmonizeWithPrimary(
                color = AppColor.colorScheme.keyword,
            ),
        ),
    ) {
        append(keyword)
    }
}

@Composable
private fun AnnotatedString.Builder.number(number: String) {
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.harmonizeWithPrimary(
                color = AppColor.colorScheme.number,
            ),
        ),
    ) {
        append(number)
    }
}

@Composable
private fun AnnotatedString.Builder.operator(operator: String) {
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.harmonizeWithPrimary(
                color = AppColor.colorScheme.operator,
            ),
        ),
    ) {
        append(operator)
    }
}

@Composable
private fun AnnotatedString.Builder.string(string: String) {
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.harmonizeWithPrimary(
                color = AppColor.colorScheme.string,
            ),
        ),
    ) {
        append(string)
    }
}

@Composable
private fun AnnotatedString.Builder.comment(comment: String) {
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.harmonizeWithPrimary(
                color = AppColor.colorScheme.comment,
            ),
        ),
    ) {
        append(comment)
    }
}

@Composable
private fun AnnotatedString.Builder.variable(variable: String) {
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.harmonizeWithPrimary(
                color = AppColor.colorScheme.variable,
            ),
        ),
    ) {
        append(variable)
    }
}

@Composable
private fun AnnotatedString.Builder.function(function: String) {
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.harmonizeWithPrimary(
                color = AppColor.colorScheme.function,
            ),
        ),
    ) {
        append(function)
    }
}