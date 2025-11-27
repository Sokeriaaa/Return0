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
package sokeriaaa.return0.models.action

import androidx.compose.ui.util.fastCoerceIn
import sokeriaaa.return0.models.action.component.extra.ActionExtraContext
import sokeriaaa.return0.models.action.component.extra.executedIn
import sokeriaaa.return0.models.action.component.value.calculatedIn
import sokeriaaa.return0.models.action.effect.Effect
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.common.helpers.chance
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import kotlin.math.roundToInt

/**
 * Execute a single time for the function on this context.
 */
fun ActionExtraContext.singleExecute() {
    // This function must be executed from [Skill].
    require(fromAction is Skill) {
        "Only functions (skills) can use singleExecute(), but current action is: $fromAction"
    }
    // Calculate power.
    val power = fromAction.attackModifier
        ?.actualPower
        ?.calculatedIn(this)
        ?.roundToInt()
        ?: fromAction.power
    if (power == 0) {
        // Execute extras
        fromAction.extra?.executedIn(this)
    } else if (power < 0) {
        // Calculate data
        val userATK = fromAction.attackModifier
            ?.userBaseATKOverride
            ?.calculatedIn(this)
            ?: user.atk.toFloat()
        // Healing
        // Generate action result and save.
        // Calculate raw heal.
        val healRaw = baseHealCalc(powerAbs = -power, atk = userATK).toInt()
        // TODO Healing rates may be applied here.
        val finalHeal = healRaw
        val result = ActionResult.Heal(
            fromIndex = user.index,
            toIndex = target.index,
            finalHeal = finalHeal,
            healCoerced = finalHeal.coerceAtMost(target.maxhp - target.hp)
        )
        saveResult(result)
        // Execute
        target.changeHP(finalHeal)
        // Execute extras
        fromAction.extra?.executedIn(this)
    } else {
        // Damage
        // Calculate data
        val targetRate = user.targetRate + (fromAction.attackModifier
            ?.targetRateOffset
            ?.calculatedIn(this) ?: 0F)
        // Missed
        if (!fromAction.bullseye && isMissed(targetRate, target.hideRate)) {
            missed()
            return
        }
        // Calculate data
        fromAction.attackModifier
            ?.userBaseATKOverride
            ?.calculatedIn(this)
            ?: user.atk.toFloat()
        // Calculate data
        fromAction.attackModifier
            ?.targetBaseDEFOverride
            ?.calculatedIn(this)
            ?: target.def.toFloat()
        // Generate action result and save.
        // Calculate raw damage.
        val damageRaw = baseDamageCalc(
            power = power,
            atk = user.atk.toFloat(),
            def = target.def.toFloat(),
        )
        // Is critical attack
        val criticalRate = user.critRate + (fromAction.attackModifier
            ?.criticalRateOffset
            ?.calculatedIn(this) ?: 0F)
        val isCritical = isCritical(criticalRate)
        // TODO Damage rates may be applied here.
        var damage = damageRaw
        if (isCritical) {
            // Critical multiplier.
            val criticalDMG = user.critDMG + (fromAction.attackModifier
                ?.criticalDMGOffset
                ?.calculatedIn(this) ?: 0F)
            damage *= 1 + criticalDMG
        }
        val finalDamage = damage.toInt()
        val result = ActionResult.Damage(
            fromIndex = user.index,
            toIndex = target.index,
            finalDamage = finalDamage,
            damageCoerced = finalDamage.coerceAtMost(target.hp),
            effectiveness = 0, // TODO Category rate implement.
            isCritical = isCritical,
        )
        saveResult(result)
        // Execute
        target.changeHP(-finalDamage)
        // Execute extras with the damage result.
        withDamageResult(result) {
            fromAction.extra?.executedIn(this)
        }
    }
}

/**
 * Execute an instant HP change "as-is", ignore any effects and multipliers.
 *
 * Can be used in both functions and effects.
 *
 * @param hpChange HP change.
 */
fun ActionExtraContext.instantHPChange(hpChange: Int) {
    if (hpChange > 0) {
        // Heal
        val result = ActionResult.Heal(
            fromIndex = user.index,
            toIndex = target.index,
            finalHeal = hpChange,
            healCoerced = hpChange.coerceAtMost(target.maxhp - target.hp),
        )
        saveResult(result)
        target.changeHP(hpChange)
    } else if (hpChange < 0) {
        // Damage
        val result = ActionResult.Damage(
            fromIndex = user.index,
            toIndex = target.index,
            finalDamage = -hpChange,
            damageCoerced = (-hpChange).coerceAtMost(target.hp),
            effectiveness = 0,
            isCritical = false,
        )
        saveResult(result)
        target.changeHP(hpChange)
    }
}

/**
 * Execute an instant SP change "as-is", ignore any effects and multipliers.
 *
 * @param spChange SP change.
 */
fun ActionExtraContext.instantSPChange(spChange: Int) {
    if (spChange != 0) {
        val result = ActionResult.SpChange(
            fromIndex = user.index,
            toIndex = target.index,
            finalChange = spChange,
            changeCoerced = spChange.coerceIn(-target.sp, target.maxsp - target.sp),
        )
        saveResult(result)
        target.changeSP(spChange)
    }
}

/**
 * Execute an instant AP change "as-is", ignore any effects and multipliers.
 *
 * @param apChange AP change.
 */
fun ActionExtraContext.instantAPChange(apChange: Int) {
    if (apChange != 0) {
        val result = ActionResult.ApChange(
            fromIndex = user.index,
            toIndex = target.index,
            finalChange = apChange,
            changeCoerced = apChange,
        )
        saveResult(result)
        target.changeSP(apChange)
    }
}

/**
 * Add/Override an effect.
 */
fun ActionExtraContext.attachEffect(effect: Effect) {
    if (effect.isStackable) {
        // If the effect is stackable, directly add it.
        saveResult(
            ActionResult.AttachEffect(
                fromIndex = user.index,
                toIndex = target.index,
                effectName = effect.name,
                turns = effect.turnsLeft,
            )
        )
        // Execute
        target.attachEffect(effect)
    } else {
        // If the effect is not stackable, compare two effects and keep the one
        //  with higher priority.
        val currentEffect = target.effects.firstOrNull { it.name == effect.name }
        if (currentEffect == null) {
            saveResult(
                ActionResult.AttachEffect(
                    fromIndex = user.index,
                    toIndex = target.index,
                    effectName = effect.name,
                    turns = effect.turnsLeft,
                )
            )
            // Execute
            target.attachEffect(effect)
        } else {
            if (effect > currentEffect) {
                // Copy action values.
                currentEffect.values.forEach { entry ->
                    effect.values[entry.key] = (effect.values[entry.key] ?: 0F) + entry.value
                }
                // Save result
                saveResult(
                    ActionResult.AttachEffect(
                        fromIndex = user.index,
                        toIndex = target.index,
                        effectName = effect.name,
                        turns = effect.turnsLeft,
                    )
                )
                // Execute
                target.removeEffect(currentEffect)
                target.attachEffect(effect)
            } else {
                // Still save the result and display logs, but actually no effect.
                saveResult(
                    ActionResult.AttachEffect(
                        fromIndex = user.index,
                        toIndex = target.index,
                        effectName = effect.name,
                        turns = effect.turnsLeft,
                    )
                )
            }
        }
    }
}

/**
 * Remove an effect.
 */
fun ActionExtraContext.removeEffect(effect: Effect) {
    // Save result
    saveResult(
        ActionResult.RemoveEffect(
            fromIndex = user.index,
            toIndex = target.index,
            effectName = effect.name,
        )
    )
    // Execute
    target.removeEffect(effect)
}

/**
 * Display a "missed" log in arena.
 */
fun ActionExtraContext.missed() {
    saveResult(
        ActionResult.Missed(
            fromIndex = user.index,
            toIndex = target.index,
        )
    )
}

/**
 * Display a "no effect" log in arena.
 */
fun ActionExtraContext.noEffect() {
    saveResult(
        ActionResult.NoEffect(
            fromIndex = user.index,
            toIndex = target.index,
        )
    )
}

/**
 * Change HP.
 */
private fun Entity.changeHP(offset: Int) {
    hp = (hp + offset).fastCoerceIn(0, maxhp)
}

/**
 * Change SP.
 */
private fun Entity.changeSP(offset: Int) {
    sp = (sp + offset).fastCoerceIn(0, maxsp)
}

/**
 * Change AP.
 */
private fun Entity.changeAP(offset: Float) {
    ap += offset
}

/**
 * Calculate the damage based on [power], [atk] and [def].
 *  Uses ratio with diminishing returns.
 *
 * This is the base damage calculated **with** current ATK of user and current DEF of target,
 *  but **without** any other multipliers such as critical damage, category rate, etc.
 *
 * No matter how low the ATK is and how high the DEF is, it will damage at least 1.
 *
 * @param power Power of action.
 * @param atk Current ATK of user.
 * @param def Current DEF of target.
 */
private fun baseDamageCalc(
    power: Int,
    atk: Float,
    def: Float,
): Float = (power * atk.coerceAtLeast(1F) * 5 / (100 + def.coerceAtLeast(1F)))
    .coerceAtLeast(1F)

/**
 * Calculate the healing based on [powerAbs] and [atk].
 *
 * Now matter how low the ATK is, it will heal at least 1.
 *
 * @param powerAbs Absolute value of the power.
 * @param atk Current ATK of user.
 */
private fun baseHealCalc(
    powerAbs: Int,
    atk: Float,
): Float = (powerAbs * atk.coerceAtLeast(1F) / 100).coerceAtLeast(1F)

/**
 * Whether this attack will be critical.
 */
private fun isCritical(
    criticalRate: Float
): Boolean = chance(success = criticalRate)

/**
 * Whether this attack will be missed.
 */
private fun isMissed(
    targetRate: Float,
    hideRate: Float
): Boolean = chance(success = 1F - targetRate + hideRate)