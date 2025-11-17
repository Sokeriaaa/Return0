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
package sokeriaaa.return0.models.action

import sokeriaaa.return0.models.action.component.extra.ActionExtraContext

/**
 * Swap user and target in this block.
 */
inline fun <reified T> ActionContext.swappedEntities(
    block: (ActionContext) -> T,
): T {
    return block(
        ActionContext(
            fromAction = fromAction,
            user = target,
            target = user,
            attackDamageResult = attackDamageResult,
        )
    )
}

/**
 * Swap user and target in this block.
 */
inline fun <reified T> ActionExtraContext.swappedEntities(
    block: (ActionExtraContext) -> T,
): T {
    val swappedContext = ActionExtraContext(
        fromAction = fromAction,
        user = target,
        target = user,
        attackDamageResult = attackDamageResult,
    )
    val executed = block(swappedContext)
    saveResults(swappedContext.actionResults)
    return executed
}

/**
 * Replace target with user in this block.
 */
inline fun <reified T> ActionContext.forUser(
    block: (ActionContext) -> T,
): T {
    return block(
        ActionContext(
            fromAction = fromAction,
            user = user,
            target = user,
            attackDamageResult = attackDamageResult,
        )
    )
}

/**
 * Replace target with user in this block.
 */
inline fun <reified T> ActionExtraContext.forUser(
    block: (ActionExtraContext) -> T,
): T {
    val userContext = ActionExtraContext(
        fromAction = fromAction,
        user = user,
        target = user,
        attackDamageResult = attackDamageResult,
    )
    val executed = block(userContext)
    saveResults(userContext.actionResults)
    return executed
}
