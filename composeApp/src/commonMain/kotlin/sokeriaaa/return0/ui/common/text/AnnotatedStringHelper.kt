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
package sokeriaaa.return0.ui.common.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

fun CharSequence.replaceAnnotatedString(
    oldValue: String,
    newValue: AnnotatedString,
): CharSequence {
    val index = this.indexOf(oldValue)
    if (index == -1) {
        return this
    }
    val start = this.subSequence(0, index)
    val end = this.subSequence(index + oldValue.length, this.length)
    return buildAnnotatedString {
        append(start)
        append(newValue)
        append(end)
    }
}

@Preview
@Composable
private fun ReplaceExampleOriginal() {
    Text(
        text = buildAnnotatedString {
            append("Lorem ipsum dolor sit amet, ")
            withStyle(SpanStyle(color = Color.Red)) {
                append("consectetur adipiscing elit.")
            }
        }
    )
}


@Preview
@Composable
private fun ReplaceExample() {
    Text(
        // TODO Optimize code.
        text = buildAnnotatedString {
            append(
                buildAnnotatedString {
                    append("Lorem ipsum dolor sit amet, ")
                    withStyle(SpanStyle(color = Color.Red)) {
                        append("consectetur adipiscing elit.")
                    }
                }.replaceAnnotatedString(
                    "adipiscing",
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Blue)) {
                            append("adipiscing")
                        }
                    }
                )
            )
        }
    )
}