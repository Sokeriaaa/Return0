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
package sokeriaaa.return0.ui.common.widgets

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppDropdownMenuItem(
    modifier: Modifier = Modifier,
    text: String,
    iconRes: DrawableResource? = null,
    contentDescription: String? = text,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        modifier = modifier,
        text = { Text(text) },
        onClick = onClick,
        leadingIcon = iconRes?.let {
            {
                Icon(
                    painter = painterResource(
                        resource = it,
                    ),
                    contentDescription = contentDescription,
                )
            }
        }
    )
}