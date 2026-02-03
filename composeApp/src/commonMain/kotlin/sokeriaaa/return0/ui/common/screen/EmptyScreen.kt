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
package sokeriaaa.return0.ui.common.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ic_outline_upcoming_24

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    iconRes: DrawableResource? = null,
    iconContentDescription: String? = null,
    label: String,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            iconRes?.let {
                Icon(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(bottom = 8.dp),
                    painter = painterResource(it),
                    contentDescription = iconContentDescription,
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun EmptyScreenPreview() {
    EmptyScreen(
        iconRes = Res.drawable.ic_outline_upcoming_24,
        iconContentDescription = null,
        label = "Example",
    )
}