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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
inline fun <reified T> AppRadioGroup(
    modifier: Modifier = Modifier,
    items: Collection<T>,
    selectedIndex: Int,
    crossinline itemLabel: @Composable (T) -> String = { it.toString() },
    crossinline itemEnabled: (T) -> Boolean = { true },
    crossinline onSelected: (Int) -> Unit,
) {
    Column(
        modifier = modifier.selectableGroup(),
    ) {
        items.forEachIndexed { index, item ->
            val label = itemLabel(item)
            val enabled = itemEnabled(item)
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = index == selectedIndex,
                        enabled = enabled,
                        onClick = { onSelected(index) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = index == selectedIndex,
                    enabled = enabled,
                    // null recommended for accessibility with screen readers
                    onClick = null,
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppRadioGroupExample() {
    AppRadioGroup(
        items = listOf("one", "two", "three"),
        selectedIndex = 0,
        onSelected = {},
    )
}