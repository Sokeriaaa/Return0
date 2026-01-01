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

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import sokeriaaa.return0.models.story.event.EventEffect

@Preview
@Composable
private fun PreviewChoices() {
    EventShowChoice(
        effect = EventEffect.ShowChoice(
            choices = listOf("One", "Two", "Three"),
        ),
        onSelected = {}
    )
}

@Preview
@Composable
private fun PreviewChoicesSelected() {
    EventShowChoice(
        effect = EventEffect.ShowChoice(
            choices = listOf("One", "Two", "Three"),
            selected = setOf(0),
        ),
        onSelected = {}
    )
}