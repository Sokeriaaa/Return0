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
package sokeriaaa.return0.ui.common.entity

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import sokeriaaa.return0.models.entity.display.EntityProfile

@Composable
fun EntityProfileItem(
    modifier: Modifier = Modifier,
    display: EntityProfile,
) {
    OutlinedCard(modifier = modifier) {
        EntityProfileContent(
            modifier = Modifier.padding(all = 12.dp),
            display = display,
        )
    }
}

@Composable
fun EntityProfileContent(
    modifier: Modifier = Modifier,
    display: EntityProfile,
) {
    Column(modifier = modifier) {
        // Name
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(Int.MAX_VALUE)
                .padding(bottom = 8.dp),
            text = display.name,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EntityExpCircularIndicator(
                level = display.level,
                progress = { display.expProgress },
            )
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(start = 8.dp),
            ) {
                // HP
                EntityHPBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 3.dp),
                    label = "HP",
                    current = display.hp,
                    max = display.maxHP,
                    valueStyle = EntityHPBar.ValueStyle.FULL,
                    style = MaterialTheme.typography.labelMedium,
                )
                // SP
                EntityHPBar(
                    modifier = Modifier.fillMaxWidth(),
                    label = "SP",
                    current = display.sp,
                    max = display.maxSP,
                    valueStyle = EntityHPBar.ValueStyle.FULL,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun EntityProfilePreview() {
    EntityProfileItem(
        display = EntityProfile(
            name = "Example",
            level = 42,
            expProgress = 0.42F,
            hp = 1234,
            maxHP = 1357,
            sp = 1000,
            maxSP = 1111,
        ),
    )
}
