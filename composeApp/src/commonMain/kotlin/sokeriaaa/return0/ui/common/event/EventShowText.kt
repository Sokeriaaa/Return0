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
package sokeriaaa.return0.ui.common.event

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ic_baseline_arrow_drop_down_24
import return0.composeapp.generated.resources.player_name
import sokeriaaa.return0.models.story.event.EventEffect
import sokeriaaa.return0.shared.common.helpers.TimeHelper

/**
 * Show the dialogues.
 */
@Composable
fun EventShowText(
    modifier: Modifier = Modifier,
    effect: EventEffect.ShowText,
    onContinue: () -> Unit
) {
    val text = effect.text
    val textLength = text.length
    val scope = rememberCoroutineScope()

    // Typing progress [0f, 1f]
    val typingProgress = remember { Animatable(0f) }

    // Click rate limit
    var lastClickTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(text) {
        typingProgress.snapTo(0f)
        typingProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = textLength * 50,
                easing = LinearEasing
            )
        )
    }

    val visibleChars = (typingProgress.value * textLength)
        .toInt()
        .coerceIn(0, textLength)
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            if (effect.type is EventEffect.ShowText.Type.NPC) {
                EventShowTextNameCard(
                    name = effect.type.name,
                )
            }
            Spacer(modifier = Modifier.weight(1F))
            if (effect.type == EventEffect.ShowText.Type.User) {
                EventShowTextNameCard(
                    name = stringResource(Res.string.player_name),
                )
            }
        }
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val now = TimeHelper.currentTimeMillis()
                // The gap of two clicks should be longer than 300ms.
                if (now - lastClickTime < 300) return@OutlinedCard
                lastClickTime = now
                if (visibleChars < textLength) {
                    scope.launch {
                        typingProgress.snapTo(1f)
                    }
                } else {
                    onContinue()
                }
            }
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = text.take(visibleChars),
                minLines = 3,
            )
            // Continue indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, end = 32.dp),
            ) {
                Spacer(modifier = Modifier.weight(1F))
                Icon(
                    painterResource(Res.drawable.ic_baseline_arrow_drop_down_24),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun EventShowTextNameCard(
    modifier: Modifier = Modifier,
    name: String,
) {
    OutlinedCard(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = name,
        )
    }
}

data class EventShowTextState(
    val visible: Boolean = false,
    val effect: EventEffect.ShowText? = null,
)

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun PreviewNPCShowText() {
    EventShowText(
        effect = EventEffect.ShowText(
            type = EventEffect.ShowText.Type.NPC("Debugger 42"),
            text = "Hello world!"
        ),
        onContinue = {},
    )
}

@Preview
@Composable
private fun PreviewNPCShowTextLong() {
    EventShowText(
        effect = EventEffect.ShowText(
            type = EventEffect.ShowText.Type.NPC("Debugger 42"),
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam interdum purus eu lectus aliquam, at tristique elit consectetur. In finibus eu ipsum in tincidunt. Quisque vitae lacus nunc. Etiam sed cursus ante. Nunc sed mi vitae velit viverra imperdiet. Praesent auctor id nisi vitae placerat. Proin auctor lobortis lacus, porta facilisis leo vehicula et. Phasellus lacus felis, malesuada ut vulputate at, mattis nec nibh. Donec euismod lacinia ipsum, sit amet molestie orci finibus a. Maecenas ac ullamcorper quam, sit amet porttitor dolor. Nullam ornare urna lorem, et ultricies arcu vestibulum vel. Sed eget magna ex."
        ),
        onContinue = {},
    )
}

@Preview
@Composable
private fun PreviewUserShowText() {
    EventShowText(
        effect = EventEffect.ShowText(
            type = EventEffect.ShowText.Type.User,
            text = "Hi there!"
        ),
        onContinue = {},
    )
}

@Preview
@Composable
private fun PreviewNarratorShowText() {
    EventShowText(
        effect = EventEffect.ShowText(
            type = EventEffect.ShowText.Type.Narrator,
            text = "Something suspicious happened nearby."
        ),
        onContinue = {},
    )
}