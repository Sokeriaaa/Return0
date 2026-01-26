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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.add
import return0.composeapp.generated.resources.ic_outline_add_24
import sokeriaaa.return0.ui.common.modifier.dashedBorder

@Composable
fun AddCard(
    modifier: Modifier = Modifier,
    addIconRes: DrawableResource = Res.drawable.ic_outline_add_24,
    label: String? = null,
    addContentDescription: String? = label ?: stringResource(Res.string.add),
    shape: Shape = RoundedCornerShape(32.dp),
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .dashedBorder(
                color = MaterialTheme.colorScheme.primary,
                shape = shape,
            )
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(addIconRes),
            contentDescription = addContentDescription,
            tint = MaterialTheme.colorScheme.primary,
        )
        label?.let {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = label,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Preview
@Composable
private fun AddCardExampleCommon() {
    AddCard(
        modifier = Modifier.size(width = 200.dp, height = 100.dp),
        onClick = {},
    )
}

@Preview
@Composable
private fun AddCardExampleLabeled() {
    AddCard(
        modifier = Modifier.size(width = 200.dp, height = 120.dp),
        label = "Add element",
        onClick = {},
    )
}