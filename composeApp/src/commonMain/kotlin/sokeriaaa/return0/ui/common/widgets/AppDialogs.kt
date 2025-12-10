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

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    title: String? = null,
    text: String? = null,
    confirmText: String = stringResource(Res.string.ok),
    cancelText: String? = stringResource(Res.string.cancel),
    onDismiss: () -> Unit,
    onConfirmed: () -> Unit = onDismiss,
    onCanceled: () -> Unit = onDismiss,
) = AlertDialog(
    modifier = modifier,
    onDismissRequest = onDismiss,
    title = title?.let { @Composable { Text(text = it) } },
    text = text?.let { @Composable { Text(text = it) } },
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