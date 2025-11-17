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
package sokeriaaa.return0.models.action.function

import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.models.action.effect.CommonEffects
import sokeriaaa.return0.models.entity.category.Category
import sokeriaaa.return0.shared.data.api.component.extra.extrasGroupOf
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.api.component.value.times
import sokeriaaa.return0.shared.data.models.action.function.FunctionData
import sokeriaaa.return0.shared.data.models.action.function.FunctionTarget
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.values.EntityValue
import sokeriaaa.return0.shared.data.models.entity.EntityData

object CommonFunctions {

    object Keys {
        const val ATTACK = "attack"
        const val DEFEND = "defend"
        const val RELAX = "relax"
    }

    /**
     * Common attack action.
     */
    fun attack(attackModifier: EntityData.GeneralAttackModifier?) = FunctionData(
        name = Keys.ATTACK,
        category = Category.NORMAL,
        target = attackModifier?.target ?: FunctionTarget.SingleEnemy,
        bullseye = false,
        basePower = attackModifier?.power ?: AppConstants.DEFAULT_ATTACK_POWER,
        powerBonus = 0,
        baseSPCost = 0,
        spCostBonus = 0,
        growth = emptyList(),
        attackModifier = FunctionData.AttackModifier(
            attackTimes = Value(attackModifier?.attackTimes ?: 1),
        ),
    )

    /**
     * Common defend action, defend 1 round that reduces damage,
     *  and slightly recovers SP. (1/32 of maxsp)
     */
    val defend = FunctionData(
        name = Keys.DEFEND,
        category = Category.NORMAL,
        target = FunctionTarget.Self,
        bullseye = true,
        basePower = 0,
        powerBonus = 0,
        baseSPCost = 0,
        spCostBonus = 0,
        growth = emptyList(),
        extra = extrasGroupOf(
            CombatExtra.AttachEffect(
                name = CommonEffects.Keys.DEFENSE,
                tier = Value(1),
                turns = Value(2)
            ),
            CombatExtra.SPChange(
                spChange = EntityValue.MAXSP * 0.03125F,
            ),
        )
    )

    /**
     * Take a relax, largely recovers SP. (1/8 of maxsp)
     */
    val relax = FunctionData(
        name = Keys.RELAX,
        category = Category.NORMAL,
        target = FunctionTarget.Self,
        bullseye = true,
        basePower = 0,
        powerBonus = 0,
        baseSPCost = 0,
        spCostBonus = 0,
        growth = emptyList(),
        extra = CombatExtra.SPChange(
            spChange = EntityValue.MAXSP * 0.125F,
        ),
    )

}