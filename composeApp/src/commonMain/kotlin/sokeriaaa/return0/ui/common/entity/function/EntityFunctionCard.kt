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
package sokeriaaa.return0.ui.common.entity.function

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.ui.common.text.CommonStrings

@Composable
fun EntityFunctionCard(
    modifier: Modifier = Modifier,
    name: String,
    category: Category,
    tier: Int,
    power: Int,
    spCost: Int,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .width(32.dp)
                    .padding(start = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Category icon
                Text(
                    text = category.icon,
                    style = MaterialTheme.typography.bodyLarge,
                )
                // Tier
                Text(
                    text = if (tier > 0) {
                        CommonStrings.intToRomanOrDefault(tier)
                    } else {
                        "-"
                    },
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(
                        start = 10.dp,
                        end = 8.dp,
                        top = 6.dp,
                        bottom = 6.dp,
                    ),
                horizontalAlignment = Alignment.Start,
            ) {
                // Name
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = name,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    // SP cost
                    Text(
                        modifier = Modifier.weight(1F),
                        text = "Power:${power}",
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                    )
                    // SP cost
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = "${spCost}SP",
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}


// =========================================
// Previews
// =========================================
@Preview
@Composable
fun EntityFunctionCardExample1() {
    EntityFunctionCard(
        name = "Example",
        category = Category.CLASS,
        tier = 1,
        power = 100,
        spCost = 100,
        onClick = {},
    )
}

@Preview
@Composable
fun EntityFunctionCardExample2() {
    EntityFunctionCard(
        name = "Example",
        category = Category.DEBUG,
        tier = 5,
        power = 0,
        spCost = 80,
        onClick = {},
    )
}

@Preview
@Composable
fun EntityFunctionCardExample3() {
    EntityFunctionCard(
        name = "Example",
        category = Category.INTERFACE,
        tier = 8,
        power = -10,
        spCost = 50,
        onClick = {},
    )
}