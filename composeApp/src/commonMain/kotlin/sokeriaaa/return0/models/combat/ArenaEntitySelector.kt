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
import sokeriaaa.return0.models.component.context.createExtraContextFor
import sokeriaaa.return0.models.component.executor.value.calculatedIn
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

/**
 * Calculate priority.
 */
fun Skill.calculatePriorityFor(target: Entity): Float {
    return priority?.calculatedIn(createExtraContextFor(target)) ?: 0F
}

/**
 * Choose a best execution based on priority.
 */
fun Entity.chooseBestExecution(entities: List<Entity>): Arena.SkillExecution? {
    // Skill, Targets, Priority
    val allOptions = ArrayList<Triple<Skill, List<Entity>, Float>>()
    functions.asSequence()
        // Filter functions with enough SP.
        .filter { it.spCost <= sp }
        // Calculate all priorities.
        .forEach { function ->
            val selectableCount = function.selectableCount()
            val selectedEntities: List<Entity>
            val priority: Float
            if (selectableCount == 0) {
                // Not selectable.
                // Calculate the avenge priority.
                val availableTargets = function.availableTargetsFor(entities)
                selectedEntities = function.randomlyChooseTargets(availableTargets)
                priority = if (availableTargets.isEmpty()) {
                    Float.NEGATIVE_INFINITY
                } else {
                    availableTargets.sumOf { entity ->
                        function.calculatePriorityFor(entity).toDouble()
                    }.toFloat() / availableTargets.size
                }
            } else {
                // Selectable.
                // Calculate the highest priority.
                val availableTargets = function.availableTargetsFor(entities)
                val selectedEntityPriorityMap = availableTargets.asSequence()
                    .map { entity -> entity to function.calculatePriorityFor(entity) }
                    .sortedByDescending { it.second }
                    .take(selectableCount)
                    .toList()
                selectedEntities = selectedEntityPriorityMap.map { it.first }
                priority = selectedEntityPriorityMap.firstOrNull()?.second ?: 0F
            }
            allOptions.add(Triple(function, selectedEntities, priority))
        }
    return allOptions
        // Shuffled before choosing the best one.
        .shuffled()
        // Choosing the best one
        .maxByOrNull { it.third }
        ?.let { (skill, targets, _) -> Arena.SkillExecution(skill, targets) }
}