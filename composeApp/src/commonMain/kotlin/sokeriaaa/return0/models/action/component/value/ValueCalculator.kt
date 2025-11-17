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
package sokeriaaa.return0.models.action.component.value

import sokeriaaa.return0.models.action.ActionContext
import sokeriaaa.return0.models.action.component.condition.calculatedIn
import sokeriaaa.return0.models.action.effect.Effect
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.shared.data.models.component.values.ActionValue
import sokeriaaa.return0.shared.data.models.component.values.CombatValue
import sokeriaaa.return0.shared.data.models.component.values.CommonValue
import sokeriaaa.return0.shared.data.models.component.values.EntityValue
import sokeriaaa.return0.shared.data.models.component.values.Value
import kotlin.math.abs
import kotlin.random.Random

/**
 * Calculate the [Value] in specified [context].
 */
fun Value.calculatedIn(context: ActionContext): Float {
    return when (this) {
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
        is CommonValue.Math.RandomInt -> Random.nextInt(start, endInclusive + 1).toFloat()
        is CommonValue.Math.RandomFloat -> Random.nextFloat() * (end - start) + start

        is CommonValue.Conditioned -> {
            return if (condition.calculatedIn(context)) {
                ifTrue
            } else {
                ifFalse
            }?.calculatedIn(context)
                ?: defaultValue?.calculatedIn(context)
                ?: 0F
        }

        is CommonValue.LoadValue -> context.fromAction.values[key]
            ?: defaultValue?.calculatedIn(context) ?: 0F
        // end - CommonValue
        // start - ActionValue
        ActionValue.Tier -> context.fromAction.tier.toFloat()
        ActionValue.TimesUsed -> context.fromAction.timesUsed.toFloat()
        ActionValue.TimesRepeated -> context.fromAction.timesRepeated.toFloat()
        ActionValue.Effects.TurnsLeft -> {
            (context.fromAction as? Effect)?.turnsLeft?.toFloat() ?: 0F
        }

        ActionValue.Skills.Power -> {
            (context.fromAction as? Skill)?.power?.toFloat() ?: 0F
        }

        ActionValue.Skills.BasePower -> {
            (context.fromAction as? Skill)?.basePower?.toFloat() ?: 0F
        }
        // end - ActionValue
        // start - CombatValue
        CombatValue.Damage -> context.attackDamageResult?.finalDamage?.toFloat() ?: 0F
        CombatValue.DamageCoerced -> context.attackDamageResult?.damageCoerced?.toFloat() ?: 0F
        // end - CombatValue
        // start - EntityValue
        EntityValue.ATK -> context.target.atk.toFloat()
        EntityValue.DEF -> context.target.def.toFloat()
        EntityValue.SPD -> context.target.spd.toFloat()
        EntityValue.HP -> context.target.hp.toFloat()
        EntityValue.SP -> context.target.sp.toFloat()
        EntityValue.AP -> context.target.ap
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

        is EntityValue.TurnsLeftOf -> context.target.effects
            .asSequence()
            .filter { it.name == effectName }
            .sumOf { it.turnsLeft }
            .toFloat()

        EntityValue.TurnsLeftOfAllEffects -> context.target.effects
            .sumOf { it.turnsLeft }
            .toFloat()
        // end - EntityValue
    }
}