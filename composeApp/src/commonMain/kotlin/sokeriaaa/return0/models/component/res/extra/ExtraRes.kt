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
package sokeriaaa.return0.models.component.res.extra

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.component_condition_if
import return0.composeapp.generated.resources.component_condition_if_else
import return0.composeapp.generated.resources.component_condition_unless
import return0.composeapp.generated.resources.component_extra_ap_change
import return0.composeapp.generated.resources.component_extra_attach_effect
import return0.composeapp.generated.resources.component_extra_attach_shield
import return0.composeapp.generated.resources.component_extra_attach_shield_permanent
import return0.composeapp.generated.resources.component_extra_empty
import return0.composeapp.generated.resources.component_extra_for_user
import return0.composeapp.generated.resources.component_extra_hp_change
import return0.composeapp.generated.resources.component_extra_no_effect
import return0.composeapp.generated.resources.component_extra_remove_all_effects
import return0.composeapp.generated.resources.component_extra_remove_all_shields
import return0.composeapp.generated.resources.component_extra_remove_effect
import return0.composeapp.generated.resources.component_extra_remove_shield
import return0.composeapp.generated.resources.component_extra_save_value
import return0.composeapp.generated.resources.component_extra_sp_change
import return0.composeapp.generated.resources.component_extra_swapped
import sokeriaaa.return0.models.component.res.condition.conditionResource
import sokeriaaa.return0.models.component.res.value.valueResource
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.extras.CommonExtra
import sokeriaaa.return0.shared.data.models.component.extras.Extra
import sokeriaaa.return0.ui.common.text.replaceAnnotatedString

@Composable
fun extraResource(extra: Extra): AnnotatedString = buildAnnotatedString {
    when (extra) {
        CommonExtra.Empty -> append(stringResource(Res.string.component_extra_empty))

        is CommonExtra.Conditioned -> append(
            when {
                extra.ifTrue != null && extra.ifFalse != null ->
                    stringResource(Res.string.component_condition_if_else)
                        .replaceAnnotatedString(
                            oldValue = "{{slot0}}",
                            newValue = conditionResource(extra.condition),
                        )
                        .replaceAnnotatedString(
                            oldValue = "{{slot1}}",
                            newValue = extraResource(extra.ifTrue),
                        )
                        .replaceAnnotatedString(
                            oldValue = "{{slot2}}",
                            newValue = extraResource(extra.ifFalse),
                        )

                extra.ifTrue != null && extra.ifFalse == null ->
                    stringResource(Res.string.component_condition_if)
                        .replaceAnnotatedString(
                            oldValue = "{{slot0}}",
                            newValue = conditionResource(extra.condition),
                        )
                        .replaceAnnotatedString(
                            oldValue = "{{slot1}}",
                            newValue = extraResource(extra.ifTrue),
                        )

                extra.ifTrue == null && extra.ifFalse != null ->
                    stringResource(Res.string.component_condition_unless)
                        .replaceAnnotatedString(
                            oldValue = "{{slot0}}",
                            newValue = conditionResource(extra.condition),
                        )
                        .replaceAnnotatedString(
                            oldValue = "{{slot1}}",
                            newValue = extraResource(extra.ifFalse),
                        )

                else -> stringResource(Res.string.component_extra_empty)
            }
        )

        is CommonExtra.Grouped -> {
            extra.extras.forEachIndexed { index, e ->
                if (index > 0) {
                    append(", ")
                }
                append(extraResource(e))
            }
        }

        is CommonExtra.SaveValue -> append(
            stringResource(Res.string.component_extra_save_value)
                .replaceAnnotatedString(
                    oldValue = "{{slot0}}",
                    newValue = AnnotatedString(extra.key),
                )
                .replaceAnnotatedString(
                    oldValue = "{{slot1}}",
                    newValue = valueResource(extra.value),
                )
        )

        is CommonExtra.ForUser -> append(
            stringResource(Res.string.component_extra_for_user)
                .replaceAnnotatedString(
                    oldValue = "{{slot0}}",
                    newValue = extraResource(extra.extra),
                )
        )

        is CommonExtra.Swapped -> append(
            stringResource(Res.string.component_extra_swapped)
                .replaceAnnotatedString(
                    oldValue = "{{slot0}}",
                    newValue = extraResource(extra.extra),
                )
        )
        // end - CommonExtra
        // start - CombatExtra
        is CombatExtra.HPChange -> append(
            stringResource(Res.string.component_extra_hp_change)
                .replaceAnnotatedString(
                    oldValue = "{{slot0}}",
                    newValue = valueResource(extra.hpChange),
                )
        )

        is CombatExtra.SPChange -> append(
            stringResource(Res.string.component_extra_sp_change)
                .replaceAnnotatedString(
                    oldValue = "{{slot0}}",
                    newValue = valueResource(extra.spChange),
                )
        )

        is CombatExtra.APChange -> append(
            stringResource(Res.string.component_extra_ap_change)
                .replaceAnnotatedString(
                    oldValue = "{{slot0}}",
                    newValue = valueResource(extra.apChange),
                )
        )

        is CombatExtra.AttachEffect -> append(
            stringResource(Res.string.component_extra_attach_effect)
                .replaceAnnotatedString(
                    oldValue = "{{slot0}}",
                    newValue = AnnotatedString(extra.name),
                )
                .replaceAnnotatedString(
                    oldValue = "{{slot1}}",
                    newValue = valueResource(extra.tier),
                )
                .replaceAnnotatedString(
                    oldValue = "{{slot2}}",
                    newValue = valueResource(extra.turns),
                )
        )

        is CombatExtra.RemoveEffect -> append(
            stringResource(Res.string.component_extra_remove_effect)
                .replaceAnnotatedString(
                    oldValue = "{{slot0}}",
                    newValue = AnnotatedString(extra.name),
                )
        )

        is CombatExtra.RemoveAllEffect ->
            append(stringResource(Res.string.component_extra_remove_all_effects))

        is CombatExtra.AttachShield -> append(
            if (extra.turns == null) {
                stringResource(Res.string.component_extra_attach_shield_permanent)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(extra.key),
                    )
                    .replaceAnnotatedString(
                        oldValue = "{{slot1}}",
                        newValue = valueResource(extra.value),
                    )
            } else {
                stringResource(Res.string.component_extra_attach_shield)
                    .replaceAnnotatedString(
                        oldValue = "{{slot0}}",
                        newValue = AnnotatedString(extra.key),
                    )
                    .replaceAnnotatedString(
                        oldValue = "{{slot1}}",
                        newValue = valueResource(extra.value),
                    )
                    .replaceAnnotatedString(
                        oldValue = "{{slot2}}",
                        newValue = valueResource(extra.turns),
                    )
            }
        )

        is CombatExtra.RemoveShield -> append(
            stringResource(Res.string.component_extra_remove_shield)
                .replaceAnnotatedString(
                    oldValue = "{{slot0}}",
                    newValue = AnnotatedString(extra.key),
                )
        )

        CombatExtra.RemoveAllShields ->
            append(stringResource(Res.string.component_extra_remove_all_shields))

        CombatExtra.NoEffect ->
            append(stringResource(Res.string.component_extra_no_effect))
    }
}