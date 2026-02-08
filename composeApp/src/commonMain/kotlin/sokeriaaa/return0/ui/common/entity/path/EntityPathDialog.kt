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
package sokeriaaa.return0.ui.common.entity.path

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.ui.common.res.EntityPathRes
import sokeriaaa.return0.ui.common.widgets.AppAlertDialog
import sokeriaaa.return0.ui.common.widgets.OutlinedEmojiHeader

@Composable
fun EntityPathDialog(
    modifier: Modifier = Modifier,
    entityPath: EntityPath,
    onDismiss: () -> Unit,
) {
    AppAlertDialog(
        modifier = modifier,
        title = null,
        content = {
            Column {
                OutlinedEmojiHeader(
                    emoji = entityPath.icon,
                    label = stringResource(EntityPathRes.nameOf(entityPath)),
                    supportingText = stringResource(EntityPathRes.tagsOf(entityPath))
                )
                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = stringResource(EntityPathRes.descriptionOf(entityPath)),
                )
                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = stringResource(EntityPathRes.loreOf(entityPath)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                )
            }
        },
        cancelText = null,
        onDismiss = onDismiss,
    )
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun EntityPathDialogPreview1() {
    EntityPathDialog(
        entityPath = EntityPath.HEAP,
        onDismiss = {},
    )
}

@Preview
@Composable
private fun EntityPathDialogPreview2() {
    EntityPathDialog(
        entityPath = EntityPath.SANDBOX,
        onDismiss = {},
    )
}

@Preview
@Composable
private fun EntityPathDialogPreview3() {
    EntityPathDialog(
        entityPath = EntityPath.DAEMON,
        onDismiss = {},
    )
}