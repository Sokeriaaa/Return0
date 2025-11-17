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
package sokeriaaa.return0.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import return0.composeapp.generated.resources.FiraMono_Bold
import return0.composeapp.generated.resources.FiraMono_Regular
import return0.composeapp.generated.resources.Res

val FiraMonoFontFamily: FontFamily
    @Composable
    get() = FontFamily(
        Font(
            resource = Res.font.FiraMono_Regular,
            weight = FontWeight.Normal,
        ),
        Font(
            resource = Res.font.FiraMono_Bold,
            weight = FontWeight.Bold,
        ),
    )


val selectedFontFamily: FontFamily
    @Composable
    get() = FiraMonoFontFamily

val Typography: Typography
    @Composable
    get() = with(MaterialTheme.typography) {
        copy(
            displayLarge = this.displayLarge + TextStyle(fontFamily = selectedFontFamily),
            displayMedium = this.displayMedium + TextStyle(fontFamily = selectedFontFamily),
            displaySmall = this.displaySmall + TextStyle(fontFamily = selectedFontFamily),
            headlineLarge = this.headlineLarge + TextStyle(fontFamily = selectedFontFamily),
            headlineMedium = this.headlineMedium + TextStyle(fontFamily = selectedFontFamily),
            headlineSmall = this.headlineSmall + TextStyle(fontFamily = selectedFontFamily),
            titleLarge = this.titleLarge + TextStyle(fontFamily = selectedFontFamily),
            titleMedium = this.titleMedium + TextStyle(fontFamily = selectedFontFamily),
            titleSmall = this.titleSmall + TextStyle(fontFamily = selectedFontFamily),
            bodyLarge = this.bodyLarge + TextStyle(fontFamily = selectedFontFamily),
            bodyMedium = this.bodyMedium + TextStyle(fontFamily = selectedFontFamily),
            bodySmall = this.bodySmall + TextStyle(fontFamily = selectedFontFamily),
            labelLarge = this.labelLarge + TextStyle(fontFamily = selectedFontFamily),
            labelMedium = this.labelMedium + TextStyle(fontFamily = selectedFontFamily),
            labelSmall = this.labelSmall + TextStyle(fontFamily = selectedFontFamily),
        )
    }