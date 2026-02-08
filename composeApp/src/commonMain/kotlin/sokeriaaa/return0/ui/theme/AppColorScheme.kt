package sokeriaaa.return0.ui.theme

import androidx.compose.ui.graphics.Color

data class AppColorScheme(
    // Combat
    val damage: Color,
    val critical: Color,
    val heal: Color,
    val nullified: Color,
    // Log display
    val debug: Color,
    val info: Color,
    val warn: Color,
    val error: Color,
    val fatal: Color,
    // Code display (Game field)
    val keyword: Color,
    val number: Color,
    val operator: Color,
    val string: Color,
    val comment: Color,
    val variable: Color,
    val function: Color,
    val event: Color,
    val blocked: Color,
    // Rarity
    val common: Color,
    val uncommon: Color,
    val rare: Color,
    val epic: Color,
    val legendary: Color,
    // Category effectiveness
    val extremelyPowerful: Color,
    val powerful: Color,
    val weak: Color,
    val extremelyWeak: Color,
)
