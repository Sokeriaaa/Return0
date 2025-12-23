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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.back

/**
 * Simplified for default buttons to avoid nesting.
 *
 * @see Button
 */
@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconRes: DrawableResource? = null,
    contentDescription: String? = null,
    text: String,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        iconRes?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = contentDescription,
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(
                modifier = Modifier.size(ButtonDefaults.IconSpacing),
            )
        }
        Text(text)
    }
}

/**
 * Simplified for default buttons to avoid nesting.
 *
 * @see FilledTonalButton
 */
@Composable
fun AppFilledTonalButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconRes: DrawableResource? = null,
    contentDescription: String? = null,
    text: String,
    shape: Shape = ButtonDefaults.filledTonalShape,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    elevation: ButtonElevation? = ButtonDefaults.filledTonalButtonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    FilledTonalButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        iconRes?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = contentDescription,
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(
                modifier = Modifier.size(ButtonDefaults.IconSpacing),
            )
        }
        Text(text)
    }
}

/**
 * Simplified for default buttons to avoid nesting.
 *
 * @see TextButton
 */
@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.textShape,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    interactionSource: MutableInteractionSource? = null,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        Text(text)
    }
}

/**
 * Simplified for default buttons to avoid nesting.
 *
 * @see IconButton
 */
@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    iconRes: DrawableResource,
    onClick: () -> Unit,
    contentDescription: String?,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
        )
    }
}

/**
 * Simplified for default buttons to avoid nesting.
 *
 * @see FilledIconButton
 */
@Composable
fun AppFilledIconButton(
    modifier: Modifier = Modifier,
    iconRes: DrawableResource,
    onClick: () -> Unit,
    contentDescription: String?,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.filledShape,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
) {
    FilledIconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = colors,
        interactionSource = interactionSource,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
        )
    }
}

/**
 * Common back button for the TopBar, etc.
 *
 * @see AppIconButton
 * @see IconButton
 */
@Composable
fun AppBackIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) = AppIconButton(
    modifier = modifier,
    iconRes = backIconRes,
    onClick = onClick,
    contentDescription = stringResource(Res.string.back),
    enabled = enabled,
    colors = colors,
    interactionSource = interactionSource,
)

internal expect val backIconRes: DrawableResource