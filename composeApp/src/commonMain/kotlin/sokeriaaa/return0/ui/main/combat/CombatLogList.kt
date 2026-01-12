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
package sokeriaaa.return0.ui.main.combat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.combat_log_action_attack
import return0.composeapp.generated.resources.combat_log_action_defend
import return0.composeapp.generated.resources.combat_log_action_effect
import return0.composeapp.generated.resources.combat_log_action_effect_self
import return0.composeapp.generated.resources.combat_log_action_effect_turns
import return0.composeapp.generated.resources.combat_log_action_frozen
import return0.composeapp.generated.resources.combat_log_action_invoke
import return0.composeapp.generated.resources.combat_log_action_relax
import return0.composeapp.generated.resources.combat_log_action_throw
import return0.composeapp.generated.resources.combat_log_end_lose
import return0.composeapp.generated.resources.combat_log_end_win
import return0.composeapp.generated.resources.combat_log_entity_defeated
import return0.composeapp.generated.resources.combat_log_result_attach_effect
import return0.composeapp.generated.resources.combat_log_result_critical
import return0.composeapp.generated.resources.combat_log_result_damage
import return0.composeapp.generated.resources.combat_log_result_heal
import return0.composeapp.generated.resources.combat_log_result_missed
import return0.composeapp.generated.resources.combat_log_result_no_effect
import return0.composeapp.generated.resources.combat_log_result_remove_effect
import return0.composeapp.generated.resources.combat_log_welcome
import sokeriaaa.return0.models.action.effect.CommonEffects
import sokeriaaa.return0.models.action.function.CommonFunctions
import sokeriaaa.return0.models.combat.ArenaLogV4
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.ui.common.text.CommonStrings
import sokeriaaa.return0.ui.common.GameLog
import kotlin.jvm.JvmInline


/**
 * Display the log list in a combat.
 *
 * @param logList The reversed log list.
 * @param entities The entity index list.
 */
@Composable
fun CombatLogList(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    logList: List<ArenaLogV4>,
    entities: List<Entity>,
    verticalArrangement: Arrangement.Vertical,
    userScrollEnabled: Boolean,
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(vertical = 8.dp),
        reverseLayout = true,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = verticalArrangement,
        userScrollEnabled = userScrollEnabled,
    ) {
        with(LogContext(entities)) {
            items(items = logList) { log ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    ComposeArenaLog(log)
                }
            }
        }
    }
}

/**
 * Compose the log by type.
 */
@Composable
private fun LogContext.ComposeArenaLog(
    log: ArenaLogV4,
) {
    when (log) {
        is ArenaLogV4.General.Welcome -> GameLog(
            stringResource(
                resource = Res.string.combat_log_welcome,
                /* version = */ log.version,
            ),
        )

        ArenaLogV4.General.Lose -> GameLog(
            stringResource(resource = Res.string.combat_log_end_lose),
        )

        ArenaLogV4.General.Win -> GameLog(
            stringResource(resource = Res.string.combat_log_end_win),
        )

        is ArenaLogV4.Actions.EffectApplied -> {
            if (CommonEffects.shouldSkipLog(log.name)) {
                return
            }
            GameLog(
                if (log.userIndex == log.targetIndex) {
                    stringResource(
                        resource = Res.string.combat_log_action_effect,
                        /* name = */ log.name,
                        /* from = */ this[log.userIndex].name,
                        /* target = */ this[log.targetIndex].name,
                    )
                } else {
                    stringResource(
                        resource = Res.string.combat_log_action_effect_self,
                        /* name = */ log.name,
                        /* target = */ this[log.targetIndex].name,
                    )
                }
            )
            GameLog(
                pluralStringResource(
                    resource = Res.plurals.combat_log_action_effect_turns,
                    quantity = log.turnsLeft,
                    /* turns = */ log.turnsLeft,
                )
            )
        }

        is ArenaLogV4.Actions.Frozen -> GameLog(
            stringResource(
                resource = Res.string.combat_log_action_frozen,
                /* entity = */ this[log.entityIndex].name,
                /* name = */ log.name,
            ),
        )

        is ArenaLogV4.Actions.FunctionInvoked -> ComposeFunctionInvoked(log)
        is ArenaLogV4.Actions.Results -> ComposeActionResult(log)
        is ArenaLogV4.Entities.HPBar -> ComposeHPBar(log)

        is ArenaLogV4.Entities.Defeated -> GameLog(
            stringResource(
                resource = Res.string.combat_log_entity_defeated,
                /* defeated = */ this[log.defeatedIndex].name,
                /* defeatBy = */ this[log.defeatByIndex].name,
            )
        )

        is ArenaLogV4.Entities.Revived -> GameLog(
            stringResource(
                resource = Res.string.combat_log_entity_defeated,
                /* entity = */ this[log.entityIndex].name,
            )
        )
    }
}

@Composable
private fun LogContext.ComposeFunctionInvoked(
    log: ArenaLogV4.Actions.FunctionInvoked,
) {
    val logText = when {
        // Basic attack.
        log.name == CommonFunctions.Keys.ATTACK -> {
            stringResource(
                resource = Res.string.combat_log_action_attack,
                /* from = */ this[log.entityIndex].name,
                /* target = */ log.targetIndexes.joinToString { this[it].name },
            )
        }
        // Basic defend.
        log.name == CommonFunctions.Keys.DEFEND -> {
            stringResource(
                resource = Res.string.combat_log_action_defend,
                /* from = */ this[log.entityIndex].name,
            )
        }
        // Basic relax.
        log.name == CommonFunctions.Keys.RELAX -> {
            stringResource(
                resource = Res.string.combat_log_action_relax,
                /* from = */ this[log.entityIndex].name,
            )
        }
        // A function that ends with "Exception" or "Error".
        log.name.endsWith("Exception") || log.name.endsWith("Error") -> {
            stringResource(
                resource = Res.string.combat_log_action_throw,
                /* name = */ log.name,
                /* from = */ this[log.entityIndex].name,
                /* target = */ log.targetIndexes.joinToString { this[it].name },
            )
        }
        // Any other functions.
        else -> {
            stringResource(
                resource = Res.string.combat_log_action_invoke,
                /* name = */ log.name,
                /* from = */ this[log.entityIndex].name,
                /* target = */ log.targetIndexes.joinToString { this[it].name },
            )
        }
    }
    GameLog(logText)
}

@Composable
private fun LogContext.ComposeActionResult(
    log: ArenaLogV4.Actions.Results,
) {
    when (val result = log.actionResult) {
        is ActionResult.Damage -> {
            // Critical
            if (result.isCritical) {
                GameLog(
                    stringResource(Res.string.combat_log_result_critical)
                )
            }
            // Damage
            GameLog(
                stringResource(
                    resource = Res.string.combat_log_result_damage,
                    /* damage = */ result.finalDamage,
                    /* target = */ this[result.toIndex].name,
                )
            )
        }

        is ActionResult.Heal -> {
            GameLog(
                stringResource(
                    resource = Res.string.combat_log_result_heal,
                    /* damage = */ result.finalHeal,
                    /* target = */ this[result.toIndex].name,
                )
            )
        }

        is ActionResult.SpChange -> {}
        is ActionResult.ApChange -> {}

        is ActionResult.AttachEffect -> {
            if (CommonEffects.shouldSkipLog(result.effectName)) {
                return
            }
            GameLog(
                stringResource(
                    resource = Res.string.combat_log_result_attach_effect,
                    /* effect = */ result.effectName,
                    /* target = */ this[result.toIndex].name,
                )
            )
            GameLog(
                pluralStringResource(
                    resource = Res.plurals.combat_log_action_effect_turns,
                    quantity = result.turns,
                    /* turns = */ result.turns,
                )
            )
        }

        is ActionResult.RemoveEffect -> {
            if (CommonEffects.shouldSkipLog(result.effectName)) {
                return
            }
            GameLog(
                stringResource(
                    resource = Res.string.combat_log_result_remove_effect,
                    /* effect = */ result.effectName,
                    /* target = */ this[result.toIndex].name,
                    /* original = */ this[result.originalIndex].name,
                )
            )
        }

        is ActionResult.AttachShield -> {}
        is ActionResult.RemoveShield -> {}

        is ActionResult.Missed -> {
            GameLog(
                stringResource(
                    resource = Res.string.combat_log_result_missed,
                    /* target = */ this[result.toIndex].name,
                )
            )
        }

        is ActionResult.NoEffect -> {
            GameLog(
                stringResource(
                    resource = Res.string.combat_log_result_no_effect,
                    /* target = */ this[result.toIndex].name,
                )
            )
        }
    }
}

@Composable
private fun LogContext.ComposeHPBar(
    log: ArenaLogV4.Entities.HPBar,
) {
    GameLog("${this[log.entityIndex].name}: ${log.hp} / ${log.maxhp}")
    GameLog(CommonStrings.hpBar(log.hp.toFloat() / log.maxhp.toFloat()))

}

@JvmInline
private value class LogContext(val entities: List<Entity>) {
    operator fun get(index: Int): Entity = entities[index]
}