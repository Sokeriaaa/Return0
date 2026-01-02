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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ic_baseline_arrow_drop_down_24
import return0.composeapp.generated.resources.player_name
import sokeriaaa.return0.models.story.event.EventEffect

/**
 * Show the dialogues.
 */
@Composable
fun EventShowText(
    modifier: Modifier = Modifier,
    effect: EventEffect.ShowText,
    onContinue: () -> Unit
) {
    var showTextLength by remember { mutableIntStateOf(0) }
    LaunchedEffect(effect) {
        showTextLength = 0
        val finalLength = effect.text.length
        while (showTextLength < finalLength) {
            showTextLength++
            delay(50)
        }
    }
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
                if (showTextLength < effect.text.length) {
                    showTextLength = effect.text.length
                } else {
                    onContinue()
                }
            },
        ) {
            Text(
                modifier = Modifier.padding(all = 16.dp),
                text = effect.text.take(showTextLength),
                minLines = 3,
            )
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