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
package sokeriaaa.return0.ui.common.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_shop_qty_decrease
import return0.composeapp.generated.resources.game_shop_qty_increase
import return0.composeapp.generated.resources.ic_outline_add_circle_24
import return0.composeapp.generated.resources.ic_outline_do_not_disturb_on_24
import sokeriaaa.sugarkane.compose.widgets.button.AppIconButton

@Composable
fun AmountSelector(
    amount: Int,
    modifier: Modifier = Modifier,
    minimum: Int = 1,
    maximum: Int = Int.MAX_VALUE,
    color: Color = MaterialTheme.colorScheme.onSurface,
    focusManager: FocusManager = LocalFocusManager.current,
    onAmountChange: (Int) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AmountSelectorContent(
            amount = amount,
            minimum = minimum,
            maximum = maximum,
            color = color,
            focusManager = focusManager,
            onAmountChange = onAmountChange,
        )
    }
}

@Composable
fun AmountSelectorContent(
    amount: Int,
    minimum: Int = 1,
    maximum: Int = Int.MAX_VALUE,
    color: Color = MaterialTheme.colorScheme.onSurface,
    focusManager: FocusManager = LocalFocusManager.current,
    onAmountChange: (Int) -> Unit,
) {
    var amountText: String by remember { mutableStateOf("") }
    LaunchedEffect(amount) {
        amountText = amount.toString()
    }
    // -
    AppIconButton(
        iconRes = Res.drawable.ic_outline_do_not_disturb_on_24,
        contentDescription = stringResource(Res.string.game_shop_qty_decrease),
        colors = IconButtonDefaults.iconButtonColors(contentColor = color),
        enabled = amount > minimum,
        onClick = {
            onAmountChange((amount - 1).coerceAtLeast(minimum))
        },
    )
    // Amount
    BasicTextField(
        modifier = Modifier
            .width(48.dp)
            .onFocusChanged {
                if (!it.isFocused) {
                    onAmountChange(amountText.toIntOrNull() ?: minimum)
                }
            },
        value = amountText,
        onValueChange = { amountText = it },
        singleLine = true,
        textStyle = MaterialTheme.typography.titleLarge
            .copy(
                color = color,
                textAlign = TextAlign.Center,
            ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
    )
    // +
    AppIconButton(
        iconRes = Res.drawable.ic_outline_add_circle_24,
        contentDescription = stringResource(Res.string.game_shop_qty_increase),
        colors = IconButtonDefaults.iconButtonColors(contentColor = color),
        enabled = amount < maximum,
        onClick = {
            onAmountChange((amount + 1).coerceAtMost(maximum))
        },
    )
}


// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun AmountSelectorPreview() {
    Surface {
        AmountSelector(
            amount = 42,
            onAmountChange = {},
        )
    }
}