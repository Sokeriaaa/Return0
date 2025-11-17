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

import sokeriaaa.return0.models.action.Action
import sokeriaaa.return0.models.action.ActionContext
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.component.result.ActionResult

/**
 * Create an [ActionExtraContext].
 */
fun Action.createExtraContextFor(
    target: Entity,
    attackDamageResult: ActionResult.Damage? = null,
    actionResults: MutableList<ActionResult> = ArrayList(),
) = ActionExtraContext(
    fromAction = this,
    user = this.user,
    target = target,
    attackDamageResult = attackDamageResult,
    actionResults = actionResults,
)

/**
 * A context for saving action results in combat.
 */
class ActionExtraContext(
    override val fromAction: Action,
    override val user: Entity,
    override val target: Entity,
    attackDamageResult: ActionResult.Damage? = null,
    val actionResults: MutableList<ActionResult> = ArrayList(),
) : ActionContext(
    fromAction,
    user,
    target,
    attackDamageResult,
) {

    /**
     * Add [result] to [actionResults] if it is valid.
     */
    fun <R : ActionResult> saveResult(result: R) {
        if (result.isValid()) {
            actionResults.add(result)
        }
    }

    /**
     * Add all valid items in [results] to [actionResults].
     */
    fun <R : ActionResult> saveResults(results: Collection<R>) {
        actionResults.addAll(results.filter(ActionResult::isValid))
    }
}