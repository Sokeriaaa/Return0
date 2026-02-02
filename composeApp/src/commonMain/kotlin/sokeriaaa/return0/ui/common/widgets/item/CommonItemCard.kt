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
package sokeriaaa.return0.ui.common.widgets.item

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ic_outline_payments_24

/**
 * A universal item card framework used in the project.
 */
@Composable
fun CommonItemCard(
    modifier: Modifier = Modifier,
    iconRes: DrawableResource? = null,
    iconContentDescription: String? = null,
    label: String,
    supportingText: String? = null,
    shape: Shape = RoundedCornerShape(32.dp),
    colors: CardColors = CardDefaults.cardColors(),
    labelColor: Color = colors.contentColor,
    supportingTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    trailingContent: @Composable RowScope.() -> Unit = {},
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
    ) {
        CardContent(
            iconRes = iconRes,
            iconContentDescription = iconContentDescription,
            label = label,
            supportingText = supportingText,
            labelColor = labelColor,
            supportingTextColor = supportingTextColor,
            trailingContent = trailingContent,
        )
    }
}

/**
 * A universal clickable item card framework used in the project.
 */
@Composable
fun ClickableCommonItemCard(
    modifier: Modifier = Modifier,
    iconRes: DrawableResource? = null,
    iconContentDescription: String? = null,
    label: String,
    supportingText: String? = null,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(32.dp),
    colors: CardColors = CardDefaults.cardColors(),
    labelColor: Color = colors.contentColor,
    supportingTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    trailingContent: @Composable RowScope.() -> Unit = {},
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        onClick = onClick,
    ) {
        CardContent(
            iconRes = iconRes,
            iconContentDescription = iconContentDescription,
            label = label,
            supportingText = supportingText,
            labelColor = labelColor,
            supportingTextColor = supportingTextColor,
            trailingContent = trailingContent,
        )
    }
}

@Composable
private fun CardContent(
    iconRes: DrawableResource?,
    iconContentDescription: String?,
    label: String,
    supportingText: String?,
    labelColor: Color,
    supportingTextColor: Color,
    trailingContent: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon
        iconRes?.let {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(it),
                contentDescription = iconContentDescription,
            )
        }
        // Text
        Column(
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 8.dp),
        ) {
            // Label
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(iterations = Int.MAX_VALUE),
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = labelColor,
                maxLines = 1,
            )
            // Supporting text
            supportingText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = supportingTextColor,
                )
            }
        }
        trailingContent()
    }
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun CommonItemCardExample1() {
    CommonItemCard(
        iconRes = Res.drawable.ic_outline_payments_24,
        label = "Example Item",
    )
}

@Preview
@Composable
private fun CommonItemCardExample2() {
    CommonItemCard(
        iconRes = Res.drawable.ic_outline_payments_24,
        label = "Example Item2",
        supportingText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
    )
}

@Preview
@Composable
private fun CommonItemCardExample3() {
    CommonItemCard(
        iconRes = Res.drawable.ic_outline_payments_24,
        label = "Example Item3",
        supportingText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        trailingContent = {
            Switch(checked = true, onCheckedChange = {})
        }
    )
}