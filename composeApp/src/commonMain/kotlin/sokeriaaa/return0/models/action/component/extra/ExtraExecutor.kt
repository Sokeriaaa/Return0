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
package sokeriaaa.return0.models.action.component.extra

import sokeriaaa.return0.models.action.attachEffect
import sokeriaaa.return0.models.action.component.condition.calculatedIn
import sokeriaaa.return0.models.action.component.value.calculatedIn
import sokeriaaa.return0.models.action.effect.generateEffectFor
import sokeriaaa.return0.models.action.forUser
import sokeriaaa.return0.models.action.instantAPChange
import sokeriaaa.return0.models.action.instantHPChange
import sokeriaaa.return0.models.action.instantSPChange
import sokeriaaa.return0.models.action.removeEffect
import sokeriaaa.return0.models.action.swappedEntities
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.extras.CommonExtra
import sokeriaaa.return0.shared.data.models.component.extras.Extra

fun Extra.executedIn(context: ActionExtraContext) {
    when (this) {
        // start - CommonExtra
        CommonExtra.Empty -> {}

        is CommonExtra.Conditioned -> {
            if (condition.calculatedIn(context)) {
                ifTrue?.executedIn(context)
            } else {
                ifFalse?.executedIn(context)
            }
        }

        is CommonExtra.Grouped -> {
            extras.forEach { it.executedIn(context) }
        }

        is CommonExtra.SaveValue -> {
            context.fromAction.values[key] = value.calculatedIn(context)
        }

        is CommonExtra.ForUser -> {
            context.forUser { ctx: ActionExtraContext -> extra.executedIn(ctx) }
        }

        is CommonExtra.Swapped -> {
            context.swappedEntities { ctx: ActionExtraContext -> extra.executedIn(ctx) }
        }
        // end - CommonExtra
        // start - CombatExtra
        is CombatExtra.HPChange -> {
            context.instantHPChange(hpChange.calculatedIn(context).toInt())
        }

        is CombatExtra.SPChange -> {
            context.instantSPChange(spChange.calculatedIn(context).toInt())
        }

        is CombatExtra.APChange -> {
            context.instantAPChange(apChange.calculatedIn(context).toInt())
        }

        is CombatExtra.AttachEffect -> {
            val effectData = context.archiveRepo.getEffectData(name) ?: return
            context.attachEffect(
                context.user.generateEffectFor(
                    effectData = effectData,
                    tier = tier.calculatedIn(context).toInt(),
                    turns = turns.calculatedIn(context).toInt()
                )
            )
        }

        is CombatExtra.RemoveEffect -> {
            context.target.effects.forEach {
                if (it.name == name) {
                    context.removeEffect(it)
                }
            }
        }

        is CombatExtra.RemoveAllEffect -> {
            context.target.effects
                .filter { if (it.isDebuff) debuff else buff }
                .forEach(context::removeEffect)

        }
        // end - CombatExtra
    }
}
