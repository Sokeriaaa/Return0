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
package sokeriaaa.return0.models.component.executor.value

import sokeriaaa.return0.models.component.context.ItemContext
import sokeriaaa.return0.models.component.executor.condition.calculatedIn
import sokeriaaa.return0.shared.data.models.component.values.CommonValue
import sokeriaaa.return0.shared.data.models.component.values.EntityValue
import sokeriaaa.return0.shared.data.models.component.values.Value
import kotlin.math.abs
import kotlin.math.pow

fun Value.calculatedIn(context: ItemContext): Float {
    return when (this) {
        !is Value.Item -> 0F
        // start - CommonValue
        is CommonValue.Constant -> value
        is CommonValue.Math.Sum -> values.sumOf { it.calculatedIn(context).toDouble() }.toFloat()
        is CommonValue.Math.Times -> value1.calculatedIn(context) * value2.calculatedIn(context)
        is CommonValue.Math.TimesConst -> value1.calculatedIn(context) * value2
        is CommonValue.Math.Div -> value1.calculatedIn(context) / value2.calculatedIn(context)
        is CommonValue.Math.UnaryMinus -> -value1.calculatedIn(context)
        is CommonValue.Math.Shift -> {
            val digit = shift.calculatedIn(context).toInt()
            val value = value1.calculatedIn(context)
            return when {
                digit > 0 -> value.toInt().shl(digit).toFloat()
                digit < 0 -> value.toInt().shr(-digit).toFloat()
                else -> value
            }
        }
        is CommonValue.Math.Power -> {
            return value1.calculatedIn(context).pow(value2.calculatedIn(context))
        }

        is CommonValue.Math.AbsoluteValue -> abs(value.calculatedIn(context))
        is CommonValue.Math.Coerced -> {
            var finalValue = value.calculatedIn(context)
            min?.let {
                finalValue = finalValue.coerceAtLeast(it.calculatedIn(context))
            }
            max?.let {
                finalValue = finalValue.coerceAtMost(it.calculatedIn(context))
            }
            return finalValue
        }

        is CommonValue.Math.MinOf -> values.minOfOrNull { it.calculatedIn(context) } ?: 0F
        is CommonValue.Math.MaxOf -> values.maxOfOrNull { it.calculatedIn(context) } ?: 0F
        is CommonValue.Math.RandomInt -> context.random.nextInt(start, endInclusive + 1).toFloat()
        is CommonValue.Math.RandomFloat -> context.random.nextFloat() * (end - start) + start

        is CommonValue.Conditioned -> {
            return if (condition.calculatedIn(context)) {
                ifTrue
            } else {
                ifFalse
            }?.calculatedIn(context)
                ?: defaultValue?.calculatedIn(context)
                ?: 0F
        }

        is CommonValue.ForUser -> 0F
        is CommonValue.Swapped -> 0F
        is CommonValue.LoadValue -> 0F
        // end - CommonValue
        // start - EntityValue
        EntityValue.ATK -> context.target.atk.toFloat()
        EntityValue.DEF -> context.target.def.toFloat()
        EntityValue.SPD -> context.target.spd.toFloat()
        EntityValue.HP -> context.target.hp.toFloat()
        EntityValue.SP -> context.target.sp.toFloat()
        EntityValue.AP -> context.target.ap
        EntityValue.HPRate -> context.target.hp.toFloat() / context.target.maxhp
        EntityValue.SPRate -> context.target.sp.toFloat() / context.target.maxsp
        EntityValue.APRate -> context.target.ap / context.target.maxap
        EntityValue.MAXHP -> context.target.maxhp.toFloat()
        EntityValue.MAXSP -> context.target.maxsp.toFloat()
        EntityValue.MAXAP -> context.target.maxap.toFloat()
        EntityValue.BaseATK -> context.target.baseATK.toFloat()
        EntityValue.BaseDEF -> context.target.baseDEF.toFloat()
        EntityValue.BaseSPD -> context.target.baseSPD.toFloat()
        EntityValue.BaseHP -> context.target.baseHP.toFloat()
        EntityValue.BaseSP -> context.target.baseSP.toFloat()
        EntityValue.BaseAP -> context.target.baseAP.toFloat()
        EntityValue.CriticalRate -> context.target.critRate
        EntityValue.CriticalDMG -> context.target.critDMG
        is EntityValue.TurnsLeftOf -> 0F
        EntityValue.TurnsLeftOfAllEffects -> 0F
        is EntityValue.ShieldValueOf -> 0F
        EntityValue.SumOfShieldValue -> 0F
        // end - EntityValue
    }
}