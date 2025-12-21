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
package sokeriaaa.return0.models.story.event

import sokeriaaa.return0.models.story.event.condition.calculatedIn
import sokeriaaa.return0.models.story.event.value.calculatedIn
import sokeriaaa.return0.shared.data.models.story.event.Event

suspend fun Event.executedIn(context: EventContext) {
    when (this) {
        Event.Empty -> {}
        is Event.Sequence -> {
            events.forEach { it.executedIn(context) }
        }

        is Event.Conditioned -> {
            if (condition.calculatedIn(context)) {
                ifTrue.executedIn(context)
            } else {
                ifFalse.executedIn(context)
            }
        }

        is Event.Text.Narrator -> {
            context.emitEffect(
                EventEffect.ShowText(
                    type = EventEffect.ShowText.Type.Narrator,
                    text = context.resources.getString(messageRes)
                )
            )
        }

        is Event.Text.User -> {
            context.emitEffect(
                EventEffect.ShowText(
                    type = EventEffect.ShowText.Type.User,
                    text = context.resources.getString(messageRes)
                )
            )
        }

        is Event.Text.NPC -> {
            context.emitEffect(
                EventEffect.ShowText(
                    type = EventEffect.ShowText.Type.NPC(
                        name = context.resources.getString(nameRes)
                    ),
                    text = context.resources.getString(messageRes)
                )
            )
        }

        is Event.Choice -> {
            context.emitEffect(
                EventEffect.ShowChoice(
                    choices = items.map {
                        context.resources.getString(it.first) to it.second
                    }
                )
            )
        }

        is Event.Combat -> {
            context.emitEffect(
                EventEffect.StartCombat(
                    config = config,
                    success = success,
                    failure = failure,
                )
            )
        }

        is Event.TeleportUserTo -> {
            val line = lineNumber.calculatedIn(context)
            context.gameState.map.updatePosition(
                fileName = map,
                lineNumber = line,
            )
            context.emitEffect(
                EventEffect.TeleportPlayer(
                    map = map,
                    line = line,
                )
            )
        }

        is Event.TeleportThisEventTo -> {
            context.key?.let {
                context.gameState.map.teleportEvent(
                    eventKey = it,
                    lineNumber = lineNumber.calculatedIn(context),
                )
            }
        }

        is Event.InventoryChange -> {
            context.gameState.inventory[inventoryKey] = change.calculatedIn(context)
        }

        is Event.CurrencyChange -> {
            context.gameState.currency[currency] += change.calculatedIn(context)
        }

        is Event.ClaimQuest -> {
            context.gameState.quest.acceptedQuest(key)
        }

        is Event.CompleteQuest -> {
            context.gameState.quest.completedQuest(key)
        }

        is Event.SaveSwitch -> {
            context.gameState.savedValues.setSwitch(key, switch.calculatedIn(context))
        }

        is Event.SaveVariable -> {
            context.gameState.savedValues.setVariable(key, variable.calculatedIn(context))
        }

        Event.RecoverAll -> {
            context.gameState.team.recoverAll()
        }

        Event.RequestSave -> {
            context.emitEffect(EventEffect.RequestSave)
        }

        Event.Failed -> {
            // TODO Teleport the player to last teleport point,
            //  with all entities slightly recovered.
        }
    }
}