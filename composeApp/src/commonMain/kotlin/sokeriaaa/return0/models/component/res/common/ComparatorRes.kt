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
package sokeriaaa.return0.models.component.res.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.component_comparator_eq
import return0.composeapp.generated.resources.component_comparator_gt
import return0.composeapp.generated.resources.component_comparator_gt_eq
import return0.composeapp.generated.resources.component_comparator_lt
import return0.composeapp.generated.resources.component_comparator_lt_eq
import return0.composeapp.generated.resources.component_comparator_neq
import sokeriaaa.sugarkane.compose.helper.replaceAnnotatedString
import sokeriaaa.sugarkane.kelp.math.components.CompareOp

@Composable
fun comparatorResource(
    comparator: CompareOp,
    left: AnnotatedString,
    right: AnnotatedString,
): CharSequence = stringResource(
    when (comparator) {
        CompareOp.GT -> Res.string.component_comparator_gt
        CompareOp.GTEQ -> Res.string.component_comparator_gt_eq
        CompareOp.LT -> Res.string.component_comparator_lt
        CompareOp.LTEQ -> Res.string.component_comparator_lt_eq
        CompareOp.EQ -> Res.string.component_comparator_eq
        CompareOp.NEQ -> Res.string.component_comparator_neq
    }
).replaceAnnotatedString(
    oldValue = "{{slot0}}",
    newValue = left,
).replaceAnnotatedString(
    oldValue = "{{slot1}}",
    newValue = right,
)