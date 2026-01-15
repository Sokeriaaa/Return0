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

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.cancel
import return0.composeapp.generated.resources.ok

/**
 * Wrapped [AlertDialog] to reduce nesting.
 */
@Composable
fun AppAlertDialog(
    modifier: Modifier = Modifier,
    iconRes: DrawableResource? = null,
    iconContentDescription: String? = null,
    title: String? = null,
    text: String? = null,
    confirmText: String = stringResource(Res.string.ok),
    cancelText: String? = stringResource(Res.string.cancel),
    onDismiss: () -> Unit,
    onConfirmed: () -> Unit = onDismiss,
    onCanceled: () -> Unit = onDismiss,
) = AppAlertDialog(
    modifier = modifier,
    iconRes = iconRes,
    iconContentDescription = iconContentDescription,
    title = title,
    content = text?.let { @Composable { Text(text = it) } },
    confirmText = confirmText,
    cancelText = cancelText,
    onDismiss = onDismiss,
    onConfirmed = onConfirmed,
    onCanceled = onCanceled,
)

@Composable
fun AppAlertDialog(
    modifier: Modifier = Modifier,
    iconRes: DrawableResource? = null,
    iconContentDescription: String? = null,
    title: String? = null,
    content: (@Composable () -> Unit)?,
    confirmText: String = stringResource(Res.string.ok),
    cancelText: String? = stringResource(Res.string.cancel),
    onDismiss: () -> Unit,
    onConfirmed: () -> Unit = onDismiss,
    onCanceled: () -> Unit = onDismiss,
) = AlertDialog(
    modifier = modifier,
    onDismissRequest = onDismiss,
    icon = iconRes?.let {
        @Composable {
            Icon(
                painter = painterResource(it),
                contentDescription = iconContentDescription,
            )
        }
    },
    title = title?.let { @Composable { Text(text = it) } },
    text = content,
    confirmButton = {
        AppTextButton(
            text = confirmText,
            onClick = onConfirmed,
        )
    },
    dismissButton = cancelText?.let {
        @Composable {
            AppTextButton(
                text = cancelText,
                onClick = onCanceled,
            )
        }
    },
)