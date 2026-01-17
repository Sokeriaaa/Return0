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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.game.entity.GameEntityRepo
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.combat.ArenaConfig
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import kotlin.math.min
import kotlin.random.Random

/**
 * Main logic of the arena.
 *
 * TODO Refactor this class and split the delay logic.
 */
class Arena(
    private val arenaConfig: ArenaConfig,
    private val callback: Callback,
) : KoinComponent {

    // Entity Repo
    private val _entityRepo: GameEntityRepo by inject()

    var isCombating: Boolean = false
        private set

    /**
     * All entities in this arena. Used for indexing purposes.
     */
    val entities: Array<Entity>

    val parties: Team<Entity>

    val enemies: Team<Entity>

    /**
     * Uses to receive the user's action.
     */
    private var _actionChannel: Channel<SkillExecution>? = null

    private var _playSpeed = AppConstants.ARENA_PLAY_SPEED

    /**
     * Current tick.
     */
    var tick: Int by mutableIntStateOf(0)
        private set

    init {
        // Create entities.
        parties = Team(
            entities = arenaConfig.parties.mapIndexed { index, state ->
                _entityRepo.generateEntityInstance(
                    entityData = state.entityData,
                    index = index,
                    level = state.level,
                    isParty = true,
                    // For parties, apply current HP/SP.
                    currentHP = state.currentHP,
                    currentSP = state.currentSP,
                    plugin = state.plugin,
                )
            }
        )
        val partiesSize = parties.entities.size
        enemies = Team(
            entities = arenaConfig.enemies.mapIndexed { index, state ->
                _entityRepo.generateEntityInstance(
                    entityData = state.entityData,
                    index = partiesSize + index,
                    level = state.level,
                    isParty = false,
                    plugin = state.plugin,
                    // For enemies, apply full HP/SP.
                )
            }
        )
        entities = (parties.entities + enemies.entities).toTypedArray()
    }

    /**
     * Start this combat!
     */
    suspend fun start() {
        isCombating = true
        // Welcome!
        pushLog(
            ArenaLogV4.General.Welcome(
                // TODO Use actual version of the project here.
                version = "1.0.0",
                time = 0L,
            )
        )
        while (true) {
            tick++
            if (tick()) {
                isCombating = false
                break
            }
            if (_playSpeed > 0) {
                delay(16)
            }
        }
    }

    /**
     * Tick the arena, recovers AP of all entities.
     *
     * This function will be optimized in the future.
     *
     * @return Whether the combat is finished.
     */
    suspend fun tick(): Boolean {
        // 1. Tick
        entities.forEach {
            if (!it.isFailed()) {
                it.tick()
            }
        }
        // 2. Get the entities with full AP to take next action,
        //  sorted by speed descending.
        //  If multiple entities whose AP is full, execute one by one by speed descending.
        entities.asSequence()
            .filter { it.ap >= it.maxap }
            .sortedByDescending { it.spd }
            // 3. Execute actions.
            .forEach { entity ->
                // Skip the failed entities which defeated before they take actions.
                if (entity.isFailed()) {
                    return@forEach
                }
                // 3.1.1 Check frozen effect.
                val frozenEffect = entity.effects.firstOrNull { it.isFreeze }
                if (frozenEffect != null) {
                    // 3.1.2 Skip action.
                    // Push log.
                    pushLog(
                        ArenaLogV4.Actions.Frozen(
                            entityIndex = entity.index,
                            name = frozenEffect.name,
                        )
                    )
                    delay(6 * _playSpeed)
                } else {
                    val skillExecution: SkillExecution
                    val actionResults: List<ActionResult>
                    if (entity.isParty) {
                        // 3.2.1 Wait for player to select an action and targets.
                        waitForPlayerAction(entity)
                        // 3.2.2 Execute
                        skillExecution = _actionChannel!!.receive()
                        actionResults = executeSkill(skillExecution)
                        _actionChannel = null
                    } else {
                        // 3.3.1 For the enemy, execute a random action.
                        skillExecution = randomSkillExecutionFor(entity)
                        actionResults = executeSkill(skillExecution)
                    }
                    // Action executed
                    entity.onAction()
                    // Push log
                    pushLog(
                        ArenaLogV4.Actions.FunctionInvoked(
                            entityIndex = entity.index,
                            name = skillExecution.skill.name,
                            targetIndexes = skillExecution.targets.map { it.index },
                        )
                    )
                    delay(5 * _playSpeed)
                    // 3.4.1 Display the action results one by one
                    logActionResults(actionResults)
                    // 3.4.2 Check if any entities is failed.
                    if (handleFailedEntities(entity, *skillExecution.targets.toTypedArray())) {
                        return true
                    }
                    // 3.4.3 Also check for the user for possible side effects on function.
                    if (handleFailedEntities(entity, entity)) {
                        return true
                    }
                }
                // 3.5 Cost AP.
                entity.ap -= entity.maxap

                // 3.6 Execute effects after entity's action.
                entity.effects.toList().forEach {
                    // 3.6.1 Apply effect and remove if no turns left.
                    val results = it.applyOn(entity)
                    // Push log.
                    pushLog(
                        ArenaLogV4.Actions.EffectApplied(
                            userIndex = it.user.index,
                            targetIndex = entity.index,
                            name = it.name,
                            turnsLeft = it.turnsLeft,
                        )
                    )
                    delay(6 * _playSpeed)
                    // 3.6.2 Display the action results one by one
                    logActionResults(results)
                    // 3.6.3 Check if any entities is failed.
                    if (handleFailedEntities(entity, entity)) {
                        return true
                    }
                }
                // 3.7 Tick shields.
                entity.shields.values.forEach {
                    if (it.turnsLeft != null) {
                        it.turnsLeft = it.turnsLeft!! - 1
                    }
                }
                entity.cleanUpShields()
            }
        return false
    }

    /**
     * Wait the player to select an action and targets.
     */
    private fun waitForPlayerAction(entity: Entity) {
        _actionChannel = Channel(capacity = 1)
        callback.onRequestUserTakeActionFor(entity)
    }

    /**
     * Submit user's selection.
     */
    suspend fun submitAction(skillExecution: SkillExecution) {
        _actionChannel?.send(skillExecution)
    }

    /**
     * Submit user's selection.
     */
    suspend fun submitAction(skill: Skill, targets: List<Entity>) {
        _actionChannel?.send(SkillExecution(skill, targets))
    }

    /**
     * Create a random action.
     */
    fun randomSkillExecutionFor(entity: Entity): SkillExecution {
        // Firstly choose a random function that is sufficient for current SP.
        val selectedFunction = entity.functions.filter { it.spCost <= entity.sp }.randomOrNull()
        return if (selectedFunction != null) {
            // Select random targets and execute
            randomExecutionFor(selectedFunction)
        } else {
            // If no functions are available, execute a random common action.
            when (Random.nextInt(0, 3)) {
                0 -> randomExecutionFor(entity.attackAction)
                1 -> randomExecutionFor(entity.defendAction)
                2 -> randomExecutionFor(entity.relaxAction)
                else -> error("This will not happen.")
            }
        }
    }

    /**
     * Generate a [SkillExecution] for [Skill].
     */
    private fun randomExecutionFor(skill: Skill) = SkillExecution(
        skill = skill,
        targets = skill.randomlyChooseTargets(entities.asList()),
    )

    /**
     * Execute a function.
     */
    private fun executeSkill(execution: SkillExecution): List<ActionResult> {
        return execution.execute()
    }

    /**
     * Log the action results from function/effect execution.
     */
    private suspend fun logActionResults(actionResults: List<ActionResult>) {
        if (actionResults.isEmpty()) {
            return
        }
        val showHPBarForIndex: MutableSet<Int> = HashSet()
        // Calculate the delay time for each log.
        val resultDelay = (_playSpeed * min(50.0 / actionResults.size, 3.0)).toLong()
        actionResults.forEach {
            // Store entities that HP has changed.
            if (it is ActionResult.Damage || it is ActionResult.Heal) {
                showHPBarForIndex.add(it.toIndex)
            }
            // Push log.
            pushLog(ArenaLogV4.Actions.Results(it))
            delay(resultDelay)
        }
        // Show HP bar.
        if (showHPBarForIndex.isEmpty()) {
            return
        }
        val hpDelay = (_playSpeed * min(50.0 / showHPBarForIndex.size, 3.0)).toLong()
        showHPBarForIndex.forEach {
            // Push log.
            val entity = entities[it]
            pushLog(
                ArenaLogV4.Entities.HPBar(
                    entityIndex = it,
                    hp = entity.hp,
                    maxhp = entity.maxhp,
                )
            )
            delay(hpDelay)
        }
    }

    /**
     * Check if any entity is failed.
     *
     * @return Whether the combat is finished.
     */
    private suspend fun handleFailedEntities(user: Entity, vararg entity: Entity): Boolean {
        entity.asSequence()
            .filter { it.isFailed() && !it.isFailedFlag }
            .forEach {
                // Handle failed flag.
                it.isFailedFlag = true
                // Empty current action bar.
                it.ap = 0F
                // Push entity defeated log.
                pushLog(
                    ArenaLogV4.Entities.Defeated(
                        defeatedIndex = it.index,
                        defeatByIndex = user.index,
                    ),
                )
                delay(timeMillis = 5 * _playSpeed)
            }
        if (parties.isFailed()) {
            pushLog(ArenaLogV4.General.Lose)
            callback.onCombatEnd(false)
            return true
        }
        if (enemies.isFailed()) {
            pushLog(ArenaLogV4.General.Win)
            callback.onCombatEnd(true)
            return true
        }
        return false
    }

    private fun pushLog(log: ArenaLogV4) {
        callback.onReceivedLog(log)
    }

    /**
     * Arena callback.
     */
    interface Callback {
        /**
         * Invokes when the arena requests the [entity] to take action.
         */
        fun onRequestUserTakeActionFor(entity: Entity)

        /**
         * Invokes when the arena output a new log.
         */
        fun onReceivedLog(log: ArenaLogV4)

        /**
         * Invokes when the combat is end.
         *
         * @param result true for winning, false for losing.
         */
        fun onCombatEnd(result: Boolean) {}
    }

    /**
     * A data class contains a single-time action and target selection.
     */
    class SkillExecution(
        val skill: Skill,
        val targets: List<Entity>,
    ) {
        fun execute(): List<ActionResult> = skill.invokeOn(targets)
    }
}