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

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import sokeriaaa.return0.shared.data.models.entity.category.Category

@Composable
fun OutlinedEmojiCard(
    modifier: Modifier = Modifier,
    emoji: String,
    shape: Shape = CircleShape,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    OutlinedCard(
        modifier = modifier,
        shape = shape,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(all = 4.dp),
                text = emoji,
                style = style,
            )
        }
    }
}

@Composable
fun OutlinedEmojiHeader(
    modifier: Modifier = Modifier,
    emoji: String,
    label: String,
    supportingText: String? = null,
    emojiSize: Dp = 36.dp,
    emojiShape: Shape = CircleShape,
    emojiStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    labelStyle: TextStyle = MaterialTheme.typography.titleMedium,
    supportingTextStyle: TextStyle = MaterialTheme.typography.labelSmall,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    supportingTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedEmojiCard(
            modifier = Modifier.size(emojiSize),
            emoji = emoji,
            shape = emojiShape,
            style = emojiStyle,
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(iterations = Int.MAX_VALUE),
                text = label,
                style = labelStyle,
                color = labelColor,
                maxLines = 1,
            )
            supportingText?.let {
                Text(
                    text = it,
                    style = supportingTextStyle,
                    color = supportingTextColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun OutlinedEmojiCardExample1() {
    OutlinedEmojiCard(
        modifier = Modifier.size(32.dp),
        emoji = Category.CLASS.icon,
    )
}

@Preview
@Composable
private fun OutlinedEmojiCardExample2() {
    OutlinedEmojiCard(
        modifier = Modifier.size(32.dp),
        emoji = Category.CONCURRENT.icon,
    )
}

@Preview
@Composable
private fun OutlinedEmojiCardExample3() {
    OutlinedEmojiCard(
        modifier = Modifier.size(32.dp),
        emoji = Category.INTERFACE.icon,
    )
}

@Preview
@Composable
private fun OutlinedEmojiHeaderExample1() {
    OutlinedEmojiHeader(
        emoji = Category.CLASS.icon,
        label = Category.CLASS.name,
        supportingText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
    )
}