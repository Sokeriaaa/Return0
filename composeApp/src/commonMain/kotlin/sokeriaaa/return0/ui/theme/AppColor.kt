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
package sokeriaaa.return0.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.materialkolor.hct.Hct
import com.materialkolor.ktx.harmonize

object AppColor {
    val Red = Color(0xFFF44336)
    val DeepOrange = Color(0xFFFF5722)
    val Orange = Color(0xFFFF9800)
    val Yellow = Color(0xFFFFEB3B)
    val LightGreen = Color(0xFF8BC34A)
    val Green = Color(0xFF4CAF50)
    val LightBlue = Color(0xFF03A9F4)
    val Blue = Color(0xFF2196F3)
    val DeepPurple = Color(0xFF673AB7)
    val Purple = Color(0xFF9C27B0)
    val Grey = Color(0xFF9E9E9E)
    val DeepGrey = Color(0xFF616161)

    /**
     * Default color scheme for the app.
     */
    val colorScheme: AppColorScheme = AppColorScheme(
        damage = Red,
        critical = Orange,
        heal = LightGreen,
        nullified = Grey,
        debug = DeepGrey,
        info = LightGreen,
        warn = Orange,
        error = DeepOrange,
        fatal = Red,
        keyword = DeepPurple,
        number = DeepOrange,
        operator = Purple,
        string = Green,
        comment = Grey,
        variable = LightBlue,
        function = Orange,
        event = Blue,
        blocked = Red,
        common = Color.White,
        uncommon = LightGreen,
        rare = LightBlue,
        epic = Purple,
        legendary = Yellow,
        extremelyPowerful = Red,
        powerful = Orange,
        weak = LightBlue,
        extremelyWeak = Blue,
    )

    @Composable
    fun alignColor(
        source: Color,
        target: Color,
    ): Color {
        val harmonizedColor = source.harmonize(target)
        // Convert target to HCT to find its "Tone" (brightness)
        val targetHct = Hct.fromInt(target.toArgb())
        val targetTone = targetHct.tone

        // Convert source to HCT and apply the target tone
        val customHct = Hct.fromInt(harmonizedColor.toArgb())
        val alignedHct = Hct.from(customHct.hue, customHct.chroma, targetTone)

        // Return as a standard Compose Color
        return Color(alignedHct.toInt())
    }

    @Composable
    fun alignToPrimary(source: Color): Color = alignColor(
        source = source,
        target = MaterialTheme.colorScheme.primary,
    )

    @Composable
    fun Color.alignedToPrimary(): Color = alignToPrimary(this)
}