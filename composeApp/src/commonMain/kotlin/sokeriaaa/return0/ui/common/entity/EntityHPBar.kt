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
package sokeriaaa.return0.ui.common.entity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * HP bar display for entities. Can also be used in SP, AP, etc.
 *
 * @param label The label of current bar. Displays on the start.
 * @param current Current value.
 * @param max Maximum value.
 * @param valueStyle the displaying style of value.
 */
@Composable
fun EntityHPBar(
    modifier: Modifier = Modifier,
    label: String,
    current: Int,
    max: Int,
    valueStyle: EntityHPBar.ValueStyle = EntityHPBar.ValueStyle.CURRENT,
    color: Color = ProgressIndicatorDefaults.linearColor,
    style: TextStyle = MaterialTheme.typography.bodySmall,
) {
    // Avoid divide by 0.
    val progress = (current * 1F / max.coerceAtLeast(1))
    Box(modifier = modifier) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            progress = { progress },
            color = color,
        )
        // Label
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = label,
            style = style,
        )
        // Value
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = when (valueStyle) {
                EntityHPBar.ValueStyle.FULL -> "$current / $max"
                EntityHPBar.ValueStyle.CURRENT -> "$current"
                EntityHPBar.ValueStyle.HIDE -> ""
            },
            style = style,
        )
    }
}

object EntityHPBar {
    /**
     * value style.
     */
    enum class ValueStyle {
        /**
         * Current / Max, e.g. 1234 / 5678
         */
        FULL,

        /**
         * Only current value, e.g. 1234
         */
        CURRENT,

        /**
         * Hide the value.
         */
        HIDE,
    }
}