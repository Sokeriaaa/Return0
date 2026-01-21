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
import sokeriaaa.return0.models.action.effect.Effect
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.combat.CombatCalculator
import sokeriaaa.return0.models.component.context.ActionExtraContext
import sokeriaaa.return0.models.component.context.swappedEntities
import sokeriaaa.return0.models.component.executor.condition.calculatedIn
import sokeriaaa.return0.models.component.executor.extra.executedIn
import sokeriaaa.return0.models.component.executor.value.calculatedIn
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Execute a single time for the function on this context.
 */
fun ActionExtraContext.singleExecute(random: Random = Random) {
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
        val healRaw = CombatCalculator.baseHeal(powerAbs = -power, atk = userATK).toInt()
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
        if (
            !fromAction.bullseye &&
            CombatCalculator.isMissed(targetRate, target.hideRate, random = random)
        ) {
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
        val damageRaw = CombatCalculator.baseDamage(
            power = power,
            atk = user.atk.toFloat(),
            def = target.def.toFloat(),
        )
        // Is critical attack
        val criticalRate = user.critRate + (fromAction.attackModifier
            ?.criticalRateOffset
            ?.calculatedIn(this) ?: 0F)
        val isCritical = CombatCalculator.isCritical(criticalRate, random = random)
        // TODO Damage rates may be applied here.
        var damage = damageRaw
        // Category effectiveness
        val categoryEffectiveness = archiveRepo.getCategoryEffectiveness(fromAction.category)
        var categoryRate = 1F
        // Primary category
        categoryEffectiveness.attack[target.category]?.let { rateType1 ->
            categoryRate += rateType1 * 0.1F
        }
        // Secondary category
        categoryEffectiveness.attack[target.category2]?.let { rateType2 ->
            categoryRate += rateType2 * 0.1F
        }
        damage *= categoryRate
        if (isCritical) {
            // Critical multiplier.
            val criticalDMG = user.critDMG + (fromAction.attackModifier
                ?.criticalDMGOffset
                ?.calculatedIn(this) ?: 0F)
            damage *= 1 + criticalDMG
        }
        // Entity multipliers
        // Coerced in 0.01..100 to prevent extremely huge values.
        user.attackRateOffset?.let {
            damage *= (1 + it.calculatedIn(this)).coerceIn(0.01F..100F)
        }
        target.defendRateOffset?.let {
            damage /= (1 + it.calculatedIn(this)).coerceIn(0.01F..100F)
        }

        val finalDamage = damage.toInt()
        // Calculate Shields
        var damageToTake = finalDamage
        if (fromAction.attackModifier?.ignoresShields?.calculatedIn(this) != true) {
            // Use the shields that have the shorter remaining time first.
            for (shield in target.shields.values.sortedBy { it.turnsLeft ?: Int.MAX_VALUE }) {
                if (damageToTake >= shield.value) {
                    damageToTake -= shield.value
                    shield.value = 0
                } else {
                    shield.value -= damageToTake
                    damageToTake = 0
                    break
                }
            }
            // Clear invalid shields
            target.cleanUpShields()
        }
        val result = ActionResult.Damage(
            fromIndex = user.index,
            toIndex = target.index,
            finalDamage = finalDamage,
            shieldedDamage = finalDamage - damageToTake,
            damageCoerced = finalDamage.coerceAtMost(target.hp),
            effectiveness = 0, // TODO Category rate implement.
            isCritical = isCritical,
        )
        saveResult(result)
        // Execute
        target.changeHP(-damageToTake)
        // Execute extras with the damage result.
        withDamageResult(result) {
            fromAction.extra?.executedIn(this)
            user.onAttack?.executedIn(this)
            swappedEntities { context: ActionExtraContext ->
                target.onDefend?.executedIn(context)
            }
        }
    }
}

/**
 * Execute an instant HP change "as-is", ignore any effects and multipliers.
 *
 * Can be used in both functions and effects.
 *
 * @param hpChange HP change.
 * @param ignoresShield This HP change will ignore the shields when applying.
 */
fun ActionExtraContext.instantHPChange(hpChange: Int, ignoresShield: Boolean = false) {
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
        val finalDamage = -hpChange
        // Calculate Shields
        var damageToTake = finalDamage
        if (!ignoresShield) {
            // Use the shields that have the shorter remaining time first.
            for (shield in target.shields.values.sortedBy { it.turnsLeft ?: Int.MAX_VALUE }) {
                if (damageToTake >= shield.value) {
                    damageToTake -= shield.value
                    shield.value = 0
                } else {
                    shield.value -= damageToTake
                    damageToTake = 0
                    break
                }
            }
            // Clear invalid shields
            target.cleanUpShields()
        }
        // Damage
        val result = ActionResult.Damage(
            fromIndex = user.index,
            toIndex = target.index,
            finalDamage = finalDamage,
            shieldedDamage = finalDamage - damageToTake,
            damageCoerced = finalDamage.coerceAtMost(target.hp),
            effectiveness = 0,
            isCritical = false,
        )
        saveResult(result)
        target.changeHP(-damageToTake)
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
fun ActionExtraContext.instantAPChange(apChange: Float) {
    if (apChange != 0F) {
        val result = ActionResult.ApChange(
            fromIndex = user.index,
            toIndex = target.index,
            finalChange = apChange.toInt(),
            changeCoerced = apChange.toInt(),
        )
        saveResult(result)
        target.changeAP(apChange)
    }
}

/**
 * Add/Override an effect.
 */
fun ActionExtraContext.attachEffect(effect: Effect) {
    val currentEffect = if (effect.isStackable) {
        target.effects.firstOrNull { it.name == effect.name && it.user == user }
    } else {
        target.effects.firstOrNull { it.name == effect.name }
    }
    // Compare two effects and keep the one
    //  with higher priority.
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
        if (effect >= currentEffect) {
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

/**
 * Remove an effect.
 */
fun ActionExtraContext.removeEffect(effect: Effect) {
    // Save result
    saveResult(
        ActionResult.RemoveEffect(
            fromIndex = user.index,
            toIndex = target.index,
            originalIndex = effect.user.index,
            effectName = effect.name,
        )
    )
    // Execute
    target.removeEffect(effect)
}

fun ActionExtraContext.attachShield(key: String, value: Int, turns: Int? = null) {
    // Save result
    saveResult(
        ActionResult.AttachShield(
            fromIndex = user.index,
            toIndex = target.index,
            key = key,
            value = value,
            turns = turns,
        )
    )
    // Execute
    target.attachShield(key, value, turns)
}

fun ActionExtraContext.removeShield(key: String) {
    // Save result
    saveResult(
        ActionResult.RemoveShield(
            fromIndex = user.index,
            toIndex = target.index,
            key = key,
        )
    )
    // Execute
    target.removeShield(key)
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

