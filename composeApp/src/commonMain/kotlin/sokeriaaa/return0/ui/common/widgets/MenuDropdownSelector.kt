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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import org.jetbrains.compose.resources.painterResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ic_baseline_arrow_drop_down_24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdownSelector(
    modifier: Modifier = Modifier,
    title: String,
    menuOptions: Array<String>,
    initSelectedOption: Int = 0,
    onItemSelected: (index: Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    // We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(
                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
            ),
            readOnly = true,
            value = menuOptions.getOrNull(initSelectedOption) ?: menuOptions[0],
            onValueChange = { },
            label = { Text(text = title) },
            trailingIcon = { AppTrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            menuOptions.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = Modifier.padding(
                                paddingValues = ExposedDropdownMenuDefaults.ItemContentPadding,
                            ),
                            text = selectionOption,
                        )
                    },
                    onClick = {
                        onItemSelected(index)
                        expanded = false
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppOutlinedDropdownSelector(
    modifier: Modifier = Modifier,
    title: String,
    menuOptions: Array<String>,
    initSelectedOption: Int = 0,
    onItemSelected: (index: Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    // We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(
                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
            ),
            readOnly = true,
            value = menuOptions.getOrNull(initSelectedOption) ?: menuOptions[0],
            onValueChange = { },
            label = { Text(text = title) },
            trailingIcon = { AppTrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            menuOptions.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = Modifier.padding(
                                paddingValues = ExposedDropdownMenuDefaults.ItemContentPadding,
                            ),
                            text = selectionOption,
                        )
                    },
                    onClick = {
                        onItemSelected(index)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun AppTrailingIcon(expanded: Boolean) {
    val animatedRotate: Float by animateFloatAsState(
        targetValue = if (expanded) {
            180f
        } else {
            0f
        },
        label = "DropdownMenuTrailer",
    )
    Icon(
        modifier = Modifier.rotate(animatedRotate),
        painter = painterResource(Res.drawable.ic_baseline_arrow_drop_down_24),
        contentDescription = null,
    )
}