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

import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.models.component.context.EventContext
import sokeriaaa.return0.models.component.context.ItemContext
import sokeriaaa.return0.models.component.executor.condition.calculatedIn
import sokeriaaa.return0.models.component.executor.extra.executedIn
import sokeriaaa.return0.models.component.executor.value.calculateTime
import sokeriaaa.return0.models.component.executor.value.calculatedIn
import sokeriaaa.return0.shared.data.models.combat.ArenaConfig
import sokeriaaa.return0.shared.data.models.combat.EnemyState
import sokeriaaa.return0.shared.data.models.combat.PartyState
import sokeriaaa.return0.shared.data.models.component.values.Value
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
            context.callback.onEffect(
                EventEffect.ShowText(
                    type = EventEffect.ShowText.Type.Narrator,
                    text = context.resources.getString(messageRes)
                )
            )
            context.callback.waitForUserContinue()
        }

        is Event.Text.User -> {
            context.callback.onEffect(
                EventEffect.ShowText(
                    type = EventEffect.ShowText.Type.User,
                    text = context.resources.getString(messageRes)
                )
            )
            context.callback.waitForUserContinue()
        }

        is Event.Text.NPC -> {
            context.callback.onEffect(
                EventEffect.ShowText(
                    type = EventEffect.ShowText.Type.NPC(
                        name = context.resources.getString(nameRes)
                    ),
                    text = context.resources.getString(messageRes)
                )
            )
            context.callback.waitForUserContinue()
        }

        is Event.Tips -> {
            context.callback.onEffect(
                EventEffect.Tips(
                    text = context.resources.getString(tipsRes)
                )
            )
            context.callback.waitForUserContinue()
        }

        is Event.Choice -> {
            context.callback.onEffect(
                EventEffect.ShowChoice(
                    choices = items.map {
                        context.resources.getString(it.first)
                    }
                )
            )
            items[context.callback.waitForChoice()].second.executedIn(context)
        }

        is Event.ChoiceOneByOne -> {
            val selected = mutableSetOf<Int>()
            while (selected.size < items.size) {
                context.callback.onEffect(
                    EventEffect.ShowChoice(
                        choices = items.map {
                            context.resources.getString(it.first)
                        },
                        selected = selected,
                    )
                )
                val selectedIndex = context.callback.waitForChoice()
                items[selectedIndex].second.executedIn(context)
                selected.add(selectedIndex)
            }
            onFinished.executedIn(context)
        }

        is Event.Combat -> {
            // Convert the Config to ArenaConfig.
            val temporaryEntities = mutableSetOf<String>()

            val partyLevels = resolvePartyLevels(
                context = context,
                config = config,
                temporaryEntities = temporaryEntities
            )

            val arenaConfig = ArenaConfig(
                mode = ArenaConfig.Mode.COMMON,
                saveStatus = config.statusOverride == null,
                parties = buildParties(context, partyLevels, config.statusOverride),
                enemies = buildEnemies(context, config.enemies),
                difficulty = config.difficulty,
                temporaryEntities = temporaryEntities
            )

            context.callback.onEffect(EventEffect.StartCombat(config = arenaConfig))
            if (context.callback.waitForCombatResult()) {
                success.executedIn(context)
            } else {
                failure.executedIn(context)
            }
        }

        is Event.MoveUserTo -> {
            val line = lineNumber.calculatedIn(context)
            context.callback.onEffect(EventEffect.MovePlayer(line = line))
        }

        is Event.TeleportUserTo -> {
            val line = lineNumber.calculatedIn(context)
            context.callback.onEffect(
                EventEffect.TeleportPlayer(
                    fileName = map,
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
            context.gameState.inventory[inventoryKey] += change.calculatedIn(context)
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

        is Event.SaveTimeStamp -> {
            context.gameState.savedValues.setTimestamp(key, time.calculateTime(context))
        }

        Event.RefreshEvents -> {
            context.callback.onEffect(EventEffect.RefreshEvents)
        }

        is Event.ObtainEntity -> {
            context.gameState.entity.obtainEntity(
                entityName = entityName,
                level = level,
                exp = exp,
                currentHP = currentHP,
                currentSP = currentSP,
                pluginID = pluginID,
            )
            context.gameState.team.obtainedNewEntity(entityName)
        }

        Event.ShowMap -> context.callback.onEffect(EventEffect.ShowMap)
        Event.HideMap -> context.callback.onEffect(EventEffect.HideMap)
        Event.ShakeMap -> context.callback.onEffect(EventEffect.ShakeMap)

        Event.RecoverAll -> {
            context.gameState.team.recoverAll()
        }

        is Event.ExecuteExtra -> {
            if (extra == null) {
                onCanceled.executedIn(context)
                return
            }

            context.callback.onEffect(EventEffect.ChooseEntity)
            val choice = context.callback.waitForChoice()
            // Get the entity of the selected index.
            val entity = context.gameState.team.loadTeamLevelPairs()
                .getOrNull(choice)
                ?.first
                ?.let { context.gameState.entity.getEntityStatus(it) }
            if (entity == null) {
                onCanceled.executedIn(context)
                return
            }

            ItemContext(target = entity).let {
                // Execute
                extra.executedIn(it)
                // Save
                context.gameState.entity.saveEntityState(listOf(it.target))
            }
        }

        Event.RequestSave -> {
            context.callback.onEffect(EventEffect.RequestSave)
            context.callback.waitForUserContinue()
        }

        Event.Failed -> {
            // TODO Teleport the player to last teleport point,
            //  with all entities slightly recovered.
        }

        Event.TypeReturn0 -> {
            context.callback.onEffect(EventEffect.TypeReturn0)
            context.callback.waitForUserContinue()
        }

        is Event.RefuseToUse -> {
            reasonRes?.let {
                context.callback.onEffect(
                    EventEffect.ShowText(
                        type = EventEffect.ShowText.Type.Narrator,
                        text = context.resources.getString(it)
                    )
                )
                context.callback.waitForUserContinue()
            }
            throw RefuseToUseException()
        }
    }
}

private suspend fun resolvePartyLevels(
    context: EventContext,
    config: Event.Combat.Config,
    temporaryEntities: MutableSet<String>
): Map<String, Int> {

    if (config.useOnlyAdditional) {
        return config.additionalParties.associate {
            it.first to it.second.calculatedIn(context)
        }
    }

    val result = context.gameState.team
        .loadTeamLevelPairs()
        .toMutableList()

    val additional = config.additionalParties.associate {
        it.first to it.second.calculatedIn(context)
    }

    additional.forEach { (id, level) ->
        val existingIndex = result.indexOfFirst { it.first == id }

        when {
            // Replace weaker existing member
            existingIndex >= 0 && result[existingIndex].second < level -> {
                result[existingIndex] = id to level
                temporaryEntities += id
            }

            // Insert or replace if not in team
            existingIndex < 0 -> {
                insertOrReplace(result, id to level, additional.keys, temporaryEntities)
            }
        }
    }

    return result.toMap()
}

private fun insertOrReplace(
    team: MutableList<Pair<String, Int>>,
    entry: Pair<String, Int>,
    requiredIds: Set<String>,
    temporaryEntities: MutableSet<String>
) {
    for (index in AppConstants.ARENA_MAX_PARTY - 1 downTo 0) {
        val current = team.getOrNull(index)

        when {
            // Empty slot
            current == null -> {
                team.add(entry)
                temporaryEntities += entry.first
                return
            }

            // Skip required entities
            current.first in requiredIds -> continue

            // Replace
            else -> {
                team[index] = entry
                temporaryEntities += entry.first
                return
            }
        }
    }
    // If we get here: team is full of required entities -> ignore
}

private suspend fun buildParties(
    context: EventContext,
    partyLevels: Map<String, Int>,
    statusOverride: Map<String, Event.Combat.Config.StatusOverride>?
): List<PartyState> {

    return partyLevels.mapNotNull { (id, level) ->
        val entityData = context.archive.getEntityData(id) ?: return@mapNotNull null
        val table = context.gameState.entity.getEntityTable(id)
        val override = statusOverride?.get(id)

        PartyState(
            entityData = entityData,
            level = override?.level?.calculatedIn(context) ?: level,
            currentHP = override?.hp?.calculatedIn(context) ?: table?.currentHP,
            currentSP = override?.sp?.calculatedIn(context) ?: table?.currentSP,
        )
    }
}

private suspend fun buildEnemies(
    context: EventContext,
    enemies: List<Pair<String, Value.Event>>
): List<EnemyState> {
    return enemies.mapNotNull { (id, levelValue) ->
        EnemyState(
            entityData = context.archive.getEntityData(id) ?: return@mapNotNull null,
            level = levelValue.calculatedIn(context)
        )
    }
}
