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
package sokeriaaa.return0.ui.common.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A text item with a filled circle at start.
 */
@Composable
fun TextItem(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = " ● ",
            style = style,
        )
        Text(
            text = text,
            color = color,
            style = style,
        )
    }
}

/**
 * A text item with a filled circle at start.
 */
@Composable
fun TextItem(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = " ● ",
            style = style,
        )
        Text(
            text = text,
            color = color,
            style = style,
        )
    }
}

@Preview
@Composable
private fun TextItemExample() {
    TextItem(text = "Example text item")
}