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

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppNavigateDrawerItem(
    modifier: Modifier = Modifier,
    iconRes: DrawableResource? = null,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) = NavigationDrawerItem(
    modifier = modifier,
    icon = {
        iconRes?.let {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                painter = painterResource(iconRes),
                contentDescription = label,
            )
        }
    },
    label = {
        Text(text = label)
    },
    selected = isSelected,
    onClick = onClick,
)