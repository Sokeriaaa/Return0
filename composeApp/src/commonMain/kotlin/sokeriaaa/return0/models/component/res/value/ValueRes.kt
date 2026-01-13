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
package sokeriaaa.return0.models.component.res.value

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.component_condition_if_else
import return0.composeapp.generated.resources.component_value_action_tier
import return0.composeapp.generated.resources.component_value_action_times_repeated
import return0.composeapp.generated.resources.component_value_action_times_used
import return0.composeapp.generated.resources.component_value_coerce_in
import return0.composeapp.generated.resources.component_value_coerce_max
import return0.composeapp.generated.resources.component_value_coerce_min
import return0.composeapp.generated.resources.component_value_combat_damage
import return0.composeapp.generated.resources.component_value_combat_damage_coerced
import return0.composeapp.generated.resources.component_value_combat_damage_shielded
import return0.composeapp.generated.resources.component_value_combat_load_value
import return0.composeapp.generated.resources.component_value_combat_swapped
import return0.composeapp.generated.resources.component_value_combat_user
import return0.composeapp.generated.resources.component_value_effect_turns_left
import return0.composeapp.generated.resources.component_value_entity_shield_all
import return0.composeapp.generated.resources.component_value_entity_shield_of
import return0.composeapp.generated.resources.component_value_entity_turns_left_all
import return0.composeapp.generated.resources.component_value_entity_turns_left_of
import return0.composeapp.generated.resources.component_value_event_currency
import return0.composeapp.generated.resources.component_value_event_enemy_level
import return0.composeapp.generated.resources.component_value_event_inventory
import return0.composeapp.generated.resources.component_value_event_saved_variable
import return0.composeapp.generated.resources.component_value_event_title
import return0.composeapp.generated.resources.component_value_max_of
import return0.composeapp.generated.resources.component_value_min_of
import return0.composeapp.generated.resources.component_value_plugin_tier
import return0.composeapp.generated.resources.component_value_random_float
import return0.composeapp.generated.resources.component_value_random_int
import return0.composeapp.generated.resources.component_value_skill_base_power
import return0.composeapp.generated.resources.component_value_skill_power
import return0.composeapp.generated.resources.component_value_time_after
import return0.composeapp.generated.resources.component_value_time_next_day
import return0.composeapp.generated.resources.component_value_time_next_week
import return0.composeapp.generated.resources.component_value_time_now
import return0.composeapp.generated.resources.component_value_time_saved
import return0.composeapp.generated.resources.status_ap
import return0.composeapp.generated.resources.status_ap_rate
import return0.composeapp.generated.resources.status_atk
import return0.composeapp.generated.resources.status_base_ap
import return0.composeapp.generated.resources.status_base_atk
import return0.composeapp.generated.resources.status_base_def
import return0.composeapp.generated.resources.status_base_hp
import return0.composeapp.generated.resources.status_base_sp
import return0.composeapp.generated.resources.status_base_spd
import return0.composeapp.generated.resources.status_crit_dmg
import return0.composeapp.generated.resources.status_crit_rate
import return0.composeapp.generated.resources.status_def
import return0.composeapp.generated.resources.status_hp
import return0.composeapp.generated.resources.status_hp_rate
import return0.composeapp.generated.resources.status_max_ap
import return0.composeapp.generated.resources.status_max_hp
import return0.composeapp.generated.resources.status_max_sp
import return0.composeapp.generated.resources.status_sp
import return0.composeapp.generated.resources.status_sp_rate
import return0.composeapp.generated.resources.status_spd
import sokeriaaa.return0.models.component.res.condition.conditionResource
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.component.values.ActionValue
import sokeriaaa.return0.shared.data.models.component.values.CombatValue
import sokeriaaa.return0.shared.data.models.component.values.CommonValue
import sokeriaaa.return0.shared.data.models.component.values.EntityValue
import sokeriaaa.return0.shared.data.models.component.values.EventValue
import sokeriaaa.return0.shared.data.models.component.values.PluginValue
import sokeriaaa.return0.shared.data.models.component.values.TimeValue
import sokeriaaa.return0.shared.data.models.component.values.Value
import sokeriaaa.return0.ui.common.text.replaceAnnotatedString

@Composable
fun valueResource(value: Value): AnnotatedString {
    return buildAnnotatedString {
        when (value) {
            // start - CommonValue
            is CommonValue.Constant -> append(value.formatter.format(value.value))

            is CommonValue.Math.Sum -> {
                append("(")
                value.values.forEachIndexed { index, v ->
                    if (index > 0) {
                        append(" + ")
                    }
                    append(valueResource(v))
                }
                append(")")
            }

            is CommonValue.Math.Times -> {
                append("(")
                append(valueResource(value.value1))
                append(" * ")
                append(valueResource(value.value2))
                append(")")
            }

            is CommonValue.Math.TimesConst -> {
                append("(")
                append(valueResource(value.value1))
                append(" * ")
                append(value.value2.toString())
                append(")")
            }

            is CommonValue.Math.Div -> {
                append("(")
                append(valueResource(value.value1))
                append(" / ")
                append(valueResource(value.value2))
                append(")")
            }

            is CommonValue.Math.UnaryMinus -> {
                append("-")
                append(valueResource(value.value1))
            }

            is CommonValue.Math.Shift -> {
                append("(")
                append(valueResource(value.value1))
                append(" << ")
                append(valueResource(value.shift))
                append(")")
            }

            is CommonValue.Math.Power -> {
                append("(")
                append(valueResource(value.value1))
                append(" ^ ")
                append(valueResource(value.value2))
                append(")")
            }

            is CommonValue.Math.AbsoluteValue -> {
                append("|")
                append(valueResource(value.value))
                append("|")
            }

            is CommonValue.Math.Coerced -> append(
                when {
                    value.min != null && value.max == null ->
                        stringResource(Res.string.component_value_coerce_min)
                            .replaceAnnotatedString("{{slot0}}", valueResource(value.value))
                            .replaceAnnotatedString("{{slot1}}", valueResource(value.min))

                    value.min == null && value.max != null ->
                        stringResource(Res.string.component_value_coerce_max)
                            .replaceAnnotatedString("{{slot0}}", valueResource(value.value))
                            .replaceAnnotatedString("{{slot1}}", valueResource(value.max))

                    value.min != null && value.max != null ->
                        stringResource(Res.string.component_value_coerce_in)
                            .replaceAnnotatedString("{{slot0}}", valueResource(value.value))
                            .replaceAnnotatedString("{{slot1}}", valueResource(value.min))
                            .replaceAnnotatedString("{{slot2}}", valueResource(value.max))

                    else -> valueResource(value.value)
                }
            )

            is CommonValue.Math.MinOf -> append(
                stringResource(Res.string.component_value_min_of)
                    .replaceAnnotatedString(
                        "{{slot0}}",
                        buildAnnotatedString {
                            value.values.forEachIndexed { index, v ->
                                if (index > 0) {
                                    append(", ")
                                }
                                append(valueResource(v))
                            }
                        }
                    )
            )

            is CommonValue.Math.MaxOf -> append(
                stringResource(Res.string.component_value_max_of)
                    .replaceAnnotatedString(
                        "{{slot0}}",
                        buildAnnotatedString {
                            value.values.forEachIndexed { index, v ->
                                if (index > 0) {
                                    append(", ")
                                }
                                append(valueResource(v))
                            }
                        }
                    )
            )

            is CommonValue.Math.RandomInt -> append(
                stringResource(Res.string.component_value_random_int)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.start.toString()),
                    )
                    .replaceAnnotatedString(
                        oldValue = "{{slot1}}",
                        newValue = AnnotatedString(value.endInclusive.toString()),
                    )
            )

            is CommonValue.Math.RandomFloat -> append(
                stringResource(Res.string.component_value_random_float)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.formatter.format(value.start)),
                    )
                    .replaceAnnotatedString(
                        oldValue = "{{slot1}}",
                        newValue = AnnotatedString(value.formatter.format(value.end)),
                    )
            )

            is CommonValue.Conditioned -> append(
                stringResource(Res.string.component_condition_if_else)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = conditionResource(value.condition),
                    )
                    .replaceAnnotatedString(
                        oldValue = "{{slot1}}",
                        newValue = valueResource(value.ifTrue ?: Value(0)),
                    )
                    .replaceAnnotatedString(
                        oldValue = "{{slot2}}",
                        newValue = valueResource(value.ifFalse ?: Value(0)),
                    )
            )
            // end - CommonValue
            // start - ActionValue
            ActionValue.Tier ->
                append(stringResource(Res.string.component_value_action_tier))

            ActionValue.TimesUsed ->
                append(stringResource(Res.string.component_value_action_times_used))

            ActionValue.TimesRepeated ->
                append(stringResource(Res.string.component_value_action_times_repeated))

            ActionValue.Effects.TurnsLeft ->
                append(stringResource(Res.string.component_value_effect_turns_left))

            ActionValue.Skills.Power ->
                append(stringResource(Res.string.component_value_skill_power))

            ActionValue.Skills.BasePower ->
                append(stringResource(Res.string.component_value_skill_base_power))
            // end - ActionValue
            // start - CombatValue
            is CombatValue.ForUser -> append(
                stringResource(Res.string.component_value_combat_user)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = valueResource(value.value),
                    )
            )

            is CombatValue.Swapped -> append(
                stringResource(Res.string.component_value_combat_swapped)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = valueResource(value.value),
                    )
            )

            is CombatValue.LoadValue -> append(
                stringResource(Res.string.component_value_combat_load_value)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.key),
                    )
            )

            CombatValue.Damage ->
                append(stringResource(Res.string.component_value_combat_damage))

            CombatValue.DamageCoerced ->
                append(stringResource(Res.string.component_value_combat_damage_coerced))

            CombatValue.ShieldedDamage ->
                append(stringResource(Res.string.component_value_combat_damage_shielded))
            // end - CombatValue
            // start - EntityValue
            EntityValue.ATK -> append(stringResource(Res.string.status_atk))
            EntityValue.DEF -> append(stringResource(Res.string.status_def))
            EntityValue.SPD -> append(stringResource(Res.string.status_spd))
            EntityValue.HP -> append(stringResource(Res.string.status_hp))
            EntityValue.SP -> append(stringResource(Res.string.status_sp))
            EntityValue.AP -> append(stringResource(Res.string.status_ap))
            EntityValue.HPRate -> append(stringResource(Res.string.status_hp_rate))
            EntityValue.SPRate -> append(stringResource(Res.string.status_sp_rate))
            EntityValue.APRate -> append(stringResource(Res.string.status_ap_rate))
            EntityValue.MAXHP -> append(stringResource(Res.string.status_max_hp))
            EntityValue.MAXSP -> append(stringResource(Res.string.status_max_sp))
            EntityValue.MAXAP -> append(stringResource(Res.string.status_max_ap))
            EntityValue.BaseATK -> append(stringResource(Res.string.status_base_atk))
            EntityValue.BaseDEF -> append(stringResource(Res.string.status_base_def))
            EntityValue.BaseSPD -> append(stringResource(Res.string.status_base_spd))
            EntityValue.BaseHP -> append(stringResource(Res.string.status_base_hp))
            EntityValue.BaseSP -> append(stringResource(Res.string.status_base_sp))
            EntityValue.BaseAP -> append(stringResource(Res.string.status_base_ap))
            EntityValue.CriticalRate -> append(stringResource(Res.string.status_crit_rate))
            EntityValue.CriticalDMG -> append(stringResource(Res.string.status_crit_dmg))

            is EntityValue.TurnsLeftOf -> append(
                stringResource(Res.string.component_value_entity_turns_left_of)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.effectName),
                    )
            )

            EntityValue.TurnsLeftOfAllEffects ->
                append(stringResource(Res.string.component_value_entity_turns_left_all))

            is EntityValue.ShieldValueOf -> append(
                stringResource(Res.string.component_value_entity_shield_of)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.key),
                    )
            )

            EntityValue.SumOfShieldValue ->
                append(stringResource(Res.string.component_value_entity_shield_all))
            // end - EntityValue
            // start - PluginValue
            PluginValue.Tier ->
                append(stringResource(Res.string.component_value_plugin_tier))
            // end - PluginValue
            // start - EventValue
            is EventValue.SavedVariable -> append(
                stringResource(Res.string.component_value_event_saved_variable)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.key),
                    )
            )

            is EventValue.Currency -> append(
                stringResource(Res.string.component_value_event_currency)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.type.name),
                    )
            )

            is EventValue.Inventory -> append(
                stringResource(Res.string.component_value_event_inventory)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.key),
                    )
            )

            EventValue.TitleOrdinal ->
                append(stringResource(Res.string.component_value_event_title))

            is EventValue.EnemyLevel ->
                append(stringResource(Res.string.component_value_event_enemy_level))
            // end - EventValue
            // start - TimeValue
            // TODO Time strings
            TimeValue.Now -> append(stringResource(Res.string.component_value_time_now))
            is TimeValue.After -> append(
                stringResource(Res.string.component_value_time_after)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.millis.toString()),
                    )
            )

            is TimeValue.NextDay -> append(
                stringResource(Res.string.component_value_time_next_day)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.offsetMillis.toString()),
                    )
            )

            is TimeValue.NextWeek -> append(
                stringResource(Res.string.component_value_time_next_week)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.offsetMillis.toString()),
                    )
            )

            is TimeValue.Saved -> append(
                stringResource(Res.string.component_value_time_saved)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(value.key),
                    )
            )

            is TimeValue.Custom -> append(value.timeInMillis.toString())
            // end - TimeValue
        }
    }
}