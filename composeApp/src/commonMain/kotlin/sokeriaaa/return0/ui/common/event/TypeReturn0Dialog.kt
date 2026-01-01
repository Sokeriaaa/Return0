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
package sokeriaaa.return0.ui.common.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_complete_code
import return0.composeapp.generated.resources.game_complete_code_run
import return0.composeapp.generated.resources.game_complete_code_run_failed
import sokeriaaa.return0.ui.common.widgets.AppTextButton


/**
 * Select an entity in current team. Mainly for item usages.
 */
@Composable
fun TypeReturn0Dialog(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit,
) {
    // Text field
    var input by remember { mutableStateOf("") }
    // Warning
    var warningRes: StringResource? by remember { mutableStateOf(null) }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(Res.string.game_complete_code),
            )
        },
        text = {
            Column {
                Text(
                    text = "// Execute the process at the front of the queue",
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = "if (!readyQueue.isEmpty()) {",
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = "    Process current = readyQueue.poll();",
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = "    int completionTime = currentTime + current.burstTime;",
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = "    currentTime = completionTime;",
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = "}",
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = "// TODO Type \"return 0;\" below.",
                    style = MaterialTheme.typography.labelSmall,
                )
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = input,
                    onValueChange = { input = it },
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.labelSmall,
                )
                HorizontalDivider()
                // Warning text
                warningRes?.let {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        confirmButton = {
            AppTextButton(
                text = stringResource(Res.string.game_complete_code_run),
                onClick = {
                    if (input == "return 0;") {
                        input = ""
                        warningRes = null
                        onContinue()
                    } else {
                        warningRes = Res.string.game_complete_code_run_failed
                    }
                },
            )
        },
    )
}