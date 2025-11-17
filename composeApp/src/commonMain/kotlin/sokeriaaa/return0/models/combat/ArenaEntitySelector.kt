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
package sokeriaaa.return0.models.combat

import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.action.function.FunctionTarget

/**
 * List the available targets for selecting from the input.
 */
fun Skill.availableTargetsFor(targets: List<Entity>): List<Entity> {
    return when (this.functionTarget) {
        FunctionTarget.Self -> listOf(user)

        FunctionTarget.SingleTeamMate,
        is FunctionTarget.RandomTeamMates,
        FunctionTarget.AllTeamMates ->
            targets.filter { !it.isFailed() && it.isParty == user.isParty }

        FunctionTarget.SingleEnemy,
        is FunctionTarget.RandomEnemies,
        FunctionTarget.AllEnemies ->
            targets.filter { !it.isFailed() && it.isParty != user.isParty }

        FunctionTarget.FullArena -> targets.filter { !it.isFailed() }

        FunctionTarget.FullArenaExceptSelf -> targets.filter { !it.isFailed() && it != user }
    }
}

/**
 * List the affected targets from the input.
 *
 * Return a non-null list means target of this function cannot be selected by user,
 *  it will be automatically applied.
 *
 *  Return null means the target of this function can be selected manually by user.
 */
fun Skill.affectedTargetsFor(targets: List<Entity>): List<Entity>? {
    return when (val functionTarget = this.functionTarget) {
        FunctionTarget.Self -> listOf(user)

        FunctionTarget.SingleTeamMate -> null

        is FunctionTarget.RandomTeamMates ->
            targets.asSequence()
                .filter { !it.isFailed() && it.isParty == user.isParty }
                .shuffled()
                .take(functionTarget.count)
                .toList()

        FunctionTarget.AllTeamMates ->
            targets.filter { !it.isFailed() && it.isParty == user.isParty }

        FunctionTarget.SingleEnemy -> null

        is FunctionTarget.RandomEnemies ->
            targets.asSequence()
                .filter { !it.isFailed() && it.isParty != user.isParty }
                .shuffled()
                .take(functionTarget.count)
                .toList()

        FunctionTarget.AllEnemies ->
            targets.filter { !it.isFailed() && it.isParty != user.isParty }

        FunctionTarget.FullArena -> targets.filter { !it.isFailed() }

        FunctionTarget.FullArenaExceptSelf -> targets.filter { !it.isFailed() && it != user }
    }
}

/**
 * Selectable target count.
 *
 * 0 For functions that targets is selected automatically instead of by user.
 */
fun Skill.selectableCount(): Int {
    return when (val functionTarget = this.functionTarget) {
        FunctionTarget.Self,
        is FunctionTarget.RandomTeamMates,
        FunctionTarget.AllTeamMates,
        is FunctionTarget.RandomEnemies,
        FunctionTarget.AllEnemies,
        FunctionTarget.FullArena,
        FunctionTarget.FullArenaExceptSelf -> 0

        FunctionTarget.SingleTeamMate,
        FunctionTarget.SingleEnemy -> 1
    }
}

/**
 * Automatically select random targets for the function.
 */
fun Skill.randomlyChooseTargets(targets: List<Entity>): List<Entity> {
    return affectedTargetsFor(targets) ?: when (this.functionTarget) {
        FunctionTarget.SingleTeamMate -> listOfNotNull(
            targets
                .filter { !it.isFailed() && it.isParty == user.isParty }
                .randomOrNull(),
        )

        FunctionTarget.SingleEnemy -> listOfNotNull(
            targets
                .filter { !it.isFailed() && it.isParty != user.isParty }
                .randomOrNull(),
        )

        else -> error("This will not happen.")
    }
}