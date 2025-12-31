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
package sokeriaaa.return0.models.action.effect

import sokeriaaa.return0.shared.data.models.action.effect.EffectData
import sokeriaaa.return0.shared.data.models.action.effect.EffectModifier

object CommonEffects {
    object Keys {
        const val DEFENSE = "defense"
    }

    /**
     * The common effect for defensing.
     *
     * Increase the DEF for 200% for 1 turn. Each tier higher than 1 increases additional
     * 20% of the DEF.
     */
    val defense = EffectData(
        name = Keys.DEFENSE,
        abbr = "DEF",
        isDebuff = false,
        isRemovable = false,
        modifiers = listOf(
            EffectModifier(
                type = EffectModifier.Types.DEF,
                offset = 2F,
                tierBonus = 0.2F
            )
        ),
    )

    /**
     * For common effects, the log of them should be skipped.
     */
    fun shouldSkipLog(name: String): Boolean {
        return name == Keys.DEFENSE
    }
}