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

import androidx.compose.ui.graphics.Color

object AppColor {
    val Red = Color(0xFFF44336)
    val DeepOrange = Color(0xFFFF5722)
    val Orange = Color(0xFFFF9800)
    val LightGreen = Color(0xFF8BC34A)
    val Green = Color(0xFF4CAF50)
    val LightBlue = Color(0xFF03A9F4)
    val Blue = Color(0xFF2196F3)
    val DeepPurple = Color(0xFF673AB7)
    val Purple = Color(0xFF9C27B0)
    val Grey = Color(0xFF9E9E9E)

    /**
     * Default color scheme for the app.
     */
    val colorScheme: AppColorScheme = AppColorScheme(
        damage = Red,
        critical = Orange,
        heal = LightGreen,
        nullified = Grey,
        keyword = DeepPurple,
        number = DeepOrange,
        operator = Purple,
        string = Green,
        comment = Grey,
        variable = LightBlue,
        function = Orange,
        event = Blue,
        blocked = Red,
    )
}