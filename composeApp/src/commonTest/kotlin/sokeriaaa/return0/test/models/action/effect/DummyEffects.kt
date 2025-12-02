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
package sokeriaaa.return0.test.models.action.effect

import sokeriaaa.return0.shared.data.models.action.effect.EffectData
import sokeriaaa.return0.shared.data.models.action.effect.EffectModifier
import sokeriaaa.return0.shared.data.models.component.extras.Extra

object DummyEffects {

    fun generateEffectData(
        name: String = "Dummy",
        isDebuff: Boolean = false,
        isStackable: Boolean = false,
        isRemovable: Boolean = true,
        isFreeze: Boolean = false,
        modifiers: List<EffectModifier> = emptyList(),
        extra: Extra? = null,
    ): EffectData = EffectData(
        name = name,
        isDebuff = isDebuff,
        isStackable = isStackable,
        isRemovable = isRemovable,
        isFreeze = isFreeze,
        modifiers = modifiers,
        extra = extra,
    )

}