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
package sokeriaaa.return0.models.component.executor.extra

import androidx.compose.ui.util.fastCoerceIn
import sokeriaaa.return0.models.component.context.ItemContext
import sokeriaaa.return0.models.component.executor.condition.calculatedIn
import sokeriaaa.return0.models.component.executor.value.calculatedIn
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.extras.CommonExtra
import sokeriaaa.return0.shared.data.models.component.extras.Extra

fun Extra.executedIn(context: ItemContext) {
    when (this) {
        !is Extra.Item -> {}
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

        is CommonExtra.SaveValue -> {}
        is CommonExtra.ForUser -> {}
        is CommonExtra.Swapped -> {}
        // end - CommonExtra
        // start - CombatExtra
        is CombatExtra.HPChange -> {
            context.target.changeHP(hpChange.calculatedIn(context).toInt())
        }

        is CombatExtra.SPChange -> {
            context.target.changeSP(spChange.calculatedIn(context).toInt())
        }
        // end - CombatExtra
    }
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
