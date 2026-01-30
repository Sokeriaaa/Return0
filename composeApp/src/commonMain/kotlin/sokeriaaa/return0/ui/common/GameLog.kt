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
package sokeriaaa.return0.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import com.materialkolor.ktx.harmonizeWithPrimary
import sokeriaaa.return0.models.common.LogLevel
import sokeriaaa.return0.ui.theme.AppColor

/**
 * Display a console-styled log.
 */
@Composable
fun GameLog(
    text: String,
    modifier: Modifier = Modifier,
    level: LogLevel = LogLevel.DEBUG,
    style: TextStyle = MaterialTheme.typography.bodySmall,
) {
    val color = MaterialTheme.colorScheme.harmonizeWithPrimary(
        color = when (level) {
            LogLevel.DEBUG -> AppColor.colorScheme.debug
            LogLevel.INFO -> AppColor.colorScheme.info
            LogLevel.WARN -> AppColor.colorScheme.warn
            LogLevel.ERROR -> AppColor.colorScheme.error
            LogLevel.FATAL -> AppColor.colorScheme.fatal
        },
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = when (level) {
                LogLevel.DEBUG -> "[D] "
                LogLevel.INFO -> "[I] "
                LogLevel.WARN -> "[W] "
                LogLevel.ERROR -> "[E] "
                LogLevel.FATAL -> "[F] "
            },
            style = style,
            color = color,
        )
        Text(
            text = text,
            style = style,
            color = color,
        )
    }
}

/**
 * Display a console-styled log.
 */
@Composable
fun GameLog(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    level: LogLevel = LogLevel.DEBUG,
    style: TextStyle = MaterialTheme.typography.bodySmall,
) {
    val color = MaterialTheme.colorScheme.harmonizeWithPrimary(
        color = when (level) {
            LogLevel.DEBUG -> AppColor.colorScheme.debug
            LogLevel.INFO -> AppColor.colorScheme.info
            LogLevel.WARN -> AppColor.colorScheme.warn
            LogLevel.ERROR -> AppColor.colorScheme.error
            LogLevel.FATAL -> AppColor.colorScheme.fatal
        },
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = when (level) {
                LogLevel.DEBUG -> "[D] "
                LogLevel.INFO -> "[I] "
                LogLevel.WARN -> "[W] "
                LogLevel.ERROR -> "[E] "
                LogLevel.FATAL -> "[F] "
            },
            style = style,
            color = color,
        )
        Text(
            text = text,
            style = style,
            color = color,
        )
    }
}