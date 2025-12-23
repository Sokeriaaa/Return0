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
package sokeriaaa.return0.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Block clicks behind it.
 */
@Composable
fun ModalOverlay(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    dim: Boolean = enabled,
    content: @Composable BoxScope.() -> Unit
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (dim) 0.2F else 0F,
        label = "animatedAlpha",
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            // Blocks ALL clicks behind it
            .modalInputBlocker(enabled = enabled)
            // Visual dim
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = animatedAlpha)),
        content = content
    )
}

/**
 * Blocks ALL clicks behind it
 */
fun Modifier.modalInputBlocker(enabled: Boolean): Modifier =
    if (enabled) {
        this.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent()
                }
            }
        }
    } else {
        this
    }