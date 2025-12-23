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
private fun PreviewNPCShowText() {
    EventShowText(
        effect = EventEffect.ShowText(
            type = EventEffect.ShowText.Type.NPC("Debugger 42"),
            text = "Hello world!"
        ),
        onClick = {},
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
        onClick = {},
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
        onClick = {},
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
        onClick = {},
    )
}