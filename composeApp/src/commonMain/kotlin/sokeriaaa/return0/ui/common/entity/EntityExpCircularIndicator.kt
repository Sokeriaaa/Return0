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
package sokeriaaa.return0.ui.common.entity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EntityExpCircularIndicator(
    modifier: Modifier = Modifier,
    level: Int,
    progress: () -> Float,
) {
    Box(modifier = modifier) {
        // EXP progress
        CircularProgressIndicator(
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.Center),
            progress = progress,
        )
        // Level
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = level.toString(),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}