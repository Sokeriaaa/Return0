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
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sokeriaaa.return0.models.story.event.EventEffect
import sokeriaaa.return0.ui.common.widgets.AppButton

@Composable
fun EventShowChoice(
    modifier: Modifier = Modifier,
    effect: EventEffect.ShowChoice,
    onSelected: (index: Int) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        effect.choices.forEachIndexed { index, choice ->
            AppButton(
                modifier = Modifier.padding(vertical = 8.dp),
                text = choice.first,
                onClick = { onSelected(index) }
            )
        }
    }
}
