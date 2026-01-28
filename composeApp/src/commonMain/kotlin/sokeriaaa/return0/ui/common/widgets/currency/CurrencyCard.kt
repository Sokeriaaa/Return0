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
package sokeriaaa.return0.ui.common.widgets.currency

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.ui.common.res.CurrencyIcon

@Composable
fun CurrencyCard(
    modifier: Modifier = Modifier,
    value: Int,
    currencyType: CurrencyType,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    val animatedValue by animateIntAsState(
        targetValue = value,
        label = "AnimatedCurrencyValue",
    )
    Card(
        modifier = modifier.animateContentSize(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        )
    ) {
        Row(
            modifier = Modifier.padding(all = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = animatedValue.toString(),
                style = MaterialTheme.typography.titleMedium
            )
            CurrencyIcon(
                modifier = Modifier.padding(start = 4.dp),
                currencyType = currencyType
            )
        }
    }
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
fun CurrencyCardPreview() {
    CurrencyCard(
        value = 100,
        currencyType = CurrencyType.TOKEN
    )
}