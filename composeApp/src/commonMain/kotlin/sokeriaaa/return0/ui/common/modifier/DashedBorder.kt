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
package sokeriaaa.return0.ui.common.modifier

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Add a dashed border.
 *
 * Source:
 * https://medium.com/@kappdev/dashed-borders-in-jetpack-compose-a-comprehensive-guide-de990a944c4c
 *
 * @param clipContent return 0 added: When this flag is `true`, clip the content with [shape].
 */
fun Modifier.dashedBorder(
    brush: Brush,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round,
    clipContent: Boolean = true,
): Modifier {
    // To draw the border on top of the content, we utilize the drawWithContent() modifier:
    val modifier = drawWithContent {
        // Next, we need to turn the `shape` into an Outline to draw it on the canvas:
        val outline = shape.createOutline(size, layoutDirection, density = this)
        // Now, we need to define the dashed stroke style with which we will draw the border:
        val dashedStroke = Stroke(
            cap = cap,
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
            ),
        )
        // Finally, we can draw the border on top of the main content by placing them
        // in other right order:
        // Draw the content
        drawContent()
        // Draw the border
        drawOutline(
            outline = outline,
            style = dashedStroke,
            brush = brush,
        )
    }
    return if (clipContent) {
        modifier.clip(shape)
    } else {
        modifier
    }
}

/**
 * DashedBorder with color overload.
 *
 * @param clipContent return 0 added: When this flag is `true`, clip the content with [shape].
 */
fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round,
    clipContent: Boolean = true,
) = dashedBorder(
    brush = SolidColor(color),
    shape = shape,
    strokeWidth = strokeWidth,
    dashLength = dashLength,
    gapLength = gapLength,
    cap = cap,
    clipContent = clipContent,
)


// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun DashedBorderExample() {
    Text(
        modifier = Modifier
            .dashedBorder(
                color = Color.Green,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = "DashedBorderExample",
    )
}