/**
 * Copyright (C) 2026 Sokeriaaa
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
package sokeriaaa.return0.models.component.res.condition

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.component_condition_and
import return0.composeapp.generated.resources.component_condition_chance
import return0.composeapp.generated.resources.component_condition_combat_critical
import return0.composeapp.generated.resources.component_condition_combat_targeting_self
import return0.composeapp.generated.resources.component_condition_entity_has_any_effect
import return0.composeapp.generated.resources.component_condition_entity_has_any_shield
import return0.composeapp.generated.resources.component_condition_entity_has_category
import return0.composeapp.generated.resources.component_condition_entity_has_effect
import return0.composeapp.generated.resources.component_condition_entity_has_shield
import return0.composeapp.generated.resources.component_condition_entity_is_failed
import return0.composeapp.generated.resources.component_condition_event_quest_completed
import return0.composeapp.generated.resources.component_condition_event_saved_switch
import return0.composeapp.generated.resources.component_condition_not
import return0.composeapp.generated.resources.component_condition_or
import return0.composeapp.generated.resources.game_now
import return0.composeapp.generated.resources.game_user_title
import return0.composeapp.generated.resources.status_hp_rate
import return0.composeapp.generated.resources.status_sp_rate
import sokeriaaa.return0.models.component.res.common.comparatorResource
import sokeriaaa.return0.models.component.res.value.valueResource
import sokeriaaa.return0.shared.data.models.component.conditions.CombatCondition
import sokeriaaa.return0.shared.data.models.component.conditions.CommonCondition
import sokeriaaa.return0.shared.data.models.component.conditions.Condition
import sokeriaaa.return0.shared.data.models.component.conditions.EntityCondition
import sokeriaaa.return0.shared.data.models.component.conditions.EventCondition
import sokeriaaa.return0.ui.common.text.replaceAnnotatedString

@Composable
fun conditionResource(condition: Condition): AnnotatedString {
    return buildAnnotatedString {
        when (condition) {
            // start - CommonCondition
            is CommonCondition.And -> {
                append("(")
                condition.conditions.forEachIndexed { index, c ->
                    if (index > 0) {
                        append(stringResource(Res.string.component_condition_and))
                    }
                    append(conditionResource(c))
                }
            }

            is CommonCondition.Or -> {
                append("(")
                condition.conditions.forEachIndexed { index, c ->
                    if (index > 0) {
                        append(stringResource(Res.string.component_condition_or))
                    }
                    append(conditionResource(c))
                }
            }

            is CommonCondition.Not -> append(
                stringResource(Res.string.component_condition_not)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = conditionResource(condition.condition),
                    )
            )

            is CommonCondition.CompareValues -> append(
                comparatorResource(
                    comparator = condition.comparator,
                    left = valueResource(condition.value1),
                    right = valueResource(condition.value2),
                )
            )

            is CommonCondition.Chance -> append(
                stringResource(Res.string.component_condition_chance)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = valueResource(condition.success),
                    )
            )

            CommonCondition.True -> append("true")
            CommonCondition.False -> append("false")
            // end - CommonCondition
            // start - CombatCondition
            CombatCondition.Critical ->
                append(stringResource(Res.string.component_condition_combat_critical))

            CombatCondition.TargetingSelf ->
                append(stringResource(Res.string.component_condition_combat_targeting_self))
            // end - CombatCondition
            // start - EntityCondition
            is EntityCondition.Categories.Has -> append(
                stringResource(Res.string.component_condition_entity_has_category)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(condition.category.name),
                    )
            )

            is EntityCondition.Categories.HasOneOf -> append(
                stringResource(Res.string.component_condition_entity_has_category)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = buildAnnotatedString {
                            condition.categories.forEachIndexed { index, c ->
                                if (index > 0) {
                                    append(", ")
                                }
                                append(AnnotatedString(c.name))
                            }
                        },
                    )
            )

            is EntityCondition.Effects.Has -> append(
                stringResource(Res.string.component_condition_entity_has_effect)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(condition.effectName),
                    )
            )

            EntityCondition.Effects.HasAny ->
                append(stringResource(Res.string.component_condition_entity_has_any_effect))

            is EntityCondition.Shields.Has -> append(
                stringResource(Res.string.component_condition_entity_has_shield)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(condition.key),
                    )
            )

            is EntityCondition.Shields.HasAny ->
                append(stringResource(Res.string.component_condition_entity_has_any_shield))

            is EntityCondition.Status.HPRate -> append(
                comparatorResource(
                    comparator = condition.comparator,
                    left = AnnotatedString(stringResource(Res.string.status_hp_rate)),
                    right = valueResource(condition.rate),
                )
            )

            is EntityCondition.Status.SPRate -> append(
                comparatorResource(
                    comparator = condition.comparator,
                    left = AnnotatedString(stringResource(Res.string.status_sp_rate)),
                    right = valueResource(condition.rate),
                )
            )

            EntityCondition.Status.IsFailed ->
                append(stringResource(Res.string.component_condition_entity_is_failed))
            // end - EntityCondition
            // start - EventCondition
            is EventCondition.PlayerTitle -> append(
                comparatorResource(
                    comparator = condition.comparator,
                    left = AnnotatedString(stringResource(Res.string.game_user_title)),
                    right = AnnotatedString(condition.title.name),
                )
            )

            is EventCondition.QuestCompleted -> append(
                stringResource(Res.string.component_condition_event_quest_completed)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(condition.key),
                    )
            )

            is EventCondition.SavedSwitch -> append(
                stringResource(Res.string.component_condition_event_saved_switch)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(condition.key),
                    )
            )

            is EventCondition.CompareTime -> append(
                comparatorResource(
                    comparator = condition.comparator,
                    left = AnnotatedString(stringResource(Res.string.game_now)),
                    right = valueResource(condition.time),
                )
            )
            // end - EventCondition
        }
    }
}