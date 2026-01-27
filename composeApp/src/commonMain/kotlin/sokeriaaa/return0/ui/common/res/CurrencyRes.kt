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
package sokeriaaa.return0.ui.common.res

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ic_outline_encrypted_24
import return0.composeapp.generated.resources.ic_outline_token_24
import return0.composeapp.generated.resources.meta_crypto
import return0.composeapp.generated.resources.meta_token
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType

object CurrencyRes {
    fun iconOf(currencyType: CurrencyType): DrawableResource = when (currencyType) {
        CurrencyType.TOKEN -> Res.drawable.ic_outline_token_24
        CurrencyType.CRYPTO -> Res.drawable.ic_outline_encrypted_24
    }

    fun nameOf(currencyType: CurrencyType): StringResource = when (currencyType) {
        CurrencyType.TOKEN -> Res.string.meta_token
        CurrencyType.CRYPTO -> Res.string.meta_crypto
    }
}

@Composable
fun CurrencyIcon(
    modifier: Modifier = Modifier,
    currencyType: CurrencyType,
    tint: Color = LocalContentColor.current,
) = Icon(
    modifier = modifier,
    painter = painterResource(CurrencyRes.iconOf(currencyType)),
    contentDescription = stringResource(CurrencyRes.nameOf(currencyType)),
    tint = tint,
)