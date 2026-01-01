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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ack
import return0.composeapp.generated.resources.tips
import sokeriaaa.return0.models.story.event.EventEffect
import sokeriaaa.return0.ui.common.widgets.AppAlertDialog

/**
 * Show the dialogues.
 */
@Composable
fun EventShowTips(
    modifier: Modifier = Modifier,
    effect: EventEffect.Tips,
    onContinue: () -> Unit
) {
    AppAlertDialog(
        modifier = modifier,
        title = stringResource(Res.string.tips),
        text = effect.text,
        confirmText = stringResource(Res.string.ack),
        cancelText = null,
        onDismiss = {},
        onConfirmed = onContinue,
    )
}

data class EventShowTipsState(
    val visible: Boolean = false,
    val effect: EventEffect.Tips? = null,
)