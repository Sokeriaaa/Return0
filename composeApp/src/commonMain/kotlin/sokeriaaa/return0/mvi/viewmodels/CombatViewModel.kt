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
package sokeriaaa.return0.mvi.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.game.GameStateRepo
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.combat.Arena
import sokeriaaa.return0.models.combat.ArenaLogV4
import sokeriaaa.return0.models.combat.affectedTargetsFor
import sokeriaaa.return0.models.combat.availableTargetsFor
import sokeriaaa.return0.models.combat.selectableCount
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.models.entity.level.EntityLevelHelper
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CombatIntent
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel.RewardSummary.EntityEntry
import sokeriaaa.return0.shared.data.models.combat.ArenaConfig
import sokeriaaa.return0.shared.data.models.component.result.ActionResult
import sokeriaaa.return0.shared.data.models.story.currency.CurrencyType
import sokeriaaa.return0.ui.main.combat.animation.EntityAnimator
import sokeriaaa.return0.ui.main.combat.animation.EntityAnimatorManager

class CombatViewModel : BaseViewModel(), Arena.Callback {

    // Game state Repo.
    private val _gameStateRepo: GameStateRepo by inject()

    /**
     * The combat status.
     * `null` - Combating
     * `false` - Defeat
     * `true` - Victory
     */
    var combatStatus: Boolean? by mutableStateOf(null)
        private set

    /**
     * Whether auto-combat is enabled.
     */
    var isAutoCombat: Boolean by mutableStateOf(false)
        private set

    /**
     * The entity currently waiting for action.
     */
    var entitySelecting: Entity? by mutableStateOf(null)
        private set

    /**
     * The function which user are selecting targets for it.
     */
    private var functionSelecting: Skill? = null

    /**
     * Available targets for the selected action/function.
     */
    var availableTargets: List<Entity>? by mutableStateOf(null)
        private set

    /**
     * Max acceptable target count for this function.
     */
    var selectableCount: Int by mutableIntStateOf(0)
        private set

    private val _logList: MutableList<ArenaLogV4> = mutableStateListOf()

    /**
     * Combat log list. New logs are inserted in the beginning.
     */
    val logList: List<ArenaLogV4> = _logList

    private val _animatorManager: EntityAnimatorManager by inject()

    /**
     * Reward summary.
     */
    var rewardSummary: RewardSummary? by mutableStateOf(null)
        private set

    /**
     * Arena
     */
    private var arena: Arena? = null

    private var arenaConfig: ArenaConfig? = null

    private val _entities: MutableList<Entity> = mutableStateListOf()
    val entities: List<Entity> = _entities

    private val _parties: MutableList<Entity> = mutableStateListOf()
    val parties: List<Entity> = _parties

    private val _enemies: MutableList<Entity> = mutableStateListOf()
    val enemies: List<Entity> = _enemies

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            is CombatIntent.Prepare -> {
                // Reset the arena.
                _logList.clear()
                _entities.clear()
                _parties.clear()
                _enemies.clear()
                _animatorManager.reset()
                combatStatus = null
                entitySelecting = null
                // Create arena.
                arena = Arena(
                    arenaConfig = intent.config,
                    callback = this,
                )
                arenaConfig = intent.config
                _entities.addAll(arena!!.entities)
                _parties.addAll(arena!!.parties.entities)
                _enemies.addAll(arena!!.enemies.entities)
            }

            is CombatIntent.StartCombat -> {
                if (arena?.isCombating == true) {
                    return
                }
                viewModelScope.launch {
                    arena?.start()
                }
            }

            is CombatIntent.ChooseAction -> {
                if (entitySelecting == null) {
                    return
                }
                viewModelScope.launch {
                    // 1. Get affected targets
                    val affectedTargets = intent.function.affectedTargetsFor(entities)
                    if (affectedTargets != null) {
                        // If the targets is selected automatically,
                        // submit the execution directly.
                        arena?.submitAction(intent.function, affectedTargets)
                        entitySelecting = null
                        availableTargets = null
                        functionSelecting = null
                        selectableCount = 0
                        return@launch
                    }
                    // 2. Get available target list.
                    // Notify the user to choose targets.
                    availableTargets = intent.function.availableTargetsFor(entities)
                    selectableCount = intent.function.selectableCount()
                    functionSelecting = intent.function
                }
            }

            is CombatIntent.ChooseTarget -> {
                // Submit the execution then clear.
                functionSelecting?.let { skill ->
                    viewModelScope.launch {
                        arena?.submitAction(skill, intent.targets)
                        entitySelecting = null
                        availableTargets = null
                        functionSelecting = null
                        selectableCount = 0
                    }
                }
            }

            CombatIntent.LeaveChooseTarget -> {
                // Return to the action selection.
                availableTargets = null
                functionSelecting = null
                selectableCount = 0
            }

            CombatIntent.ToggleAutoCombat -> {
                // Toggle.
                isAutoCombat = !isAutoCombat
                // The user turned on auto-combat while selecting an action.
                if (isAutoCombat && entitySelecting != null) {
                    val entity = entitySelecting!!
                    viewModelScope.launch {
                        // Submit a random action for current entity.
                        arena?.submitAction(arena!!.randomSkillExecutionFor(entity))
                        // Reset data.
                        entitySelecting = null
                        availableTargets = null
                        functionSelecting = null
                        selectableCount = 0
                    }
                }
            }

            CombatIntent.DismissRewardSummary -> rewardSummary = null

            else -> {}
        }
    }

    override fun onRequestUserTakeActionFor(entity: Entity) {
        if (isAutoCombat) {
            // Select a random action for the entity when auto-combat is enabled.
            viewModelScope.launch {
                arena?.submitAction(arena!!.randomSkillExecutionFor(entity))
            }
        } else {
            // Notify the user to select an entity.
            entitySelecting = entity
        }
    }

    override fun onReceivedLog(log: ArenaLogV4) {
        _logList.add(0, log)
        if (log is ArenaLogV4.Actions.Results) {
            // Animate
            when (val result = log.actionResult) {
                is ActionResult.Damage -> {
                    _animatorManager.triggerAnimation(
                        index = result.toIndex,
                        animator = EntityAnimator.FloatingText(
                            text = "${result.finalDamage}",
                            color = Color.Red,
                            isCritical = result.isCritical,
                        ),
                    )
                    _animatorManager.triggerAnimation(
                        index = result.toIndex,
                        animator = EntityAnimator.Shake,
                    )
                }

                is ActionResult.Heal -> {
                    _animatorManager.triggerAnimation(
                        index = result.toIndex,
                        animator = EntityAnimator.FloatingText(
                            text = "+${result.finalHeal}",
                            color = Color.Green,
                        ),
                    )
                    _animatorManager.triggerAnimation(
                        index = result.toIndex,
                        animator = EntityAnimator.Glow(Color.Green),
                    )
                }

                is ActionResult.Missed -> {
                    _animatorManager.triggerAnimation(
                        index = result.toIndex,
                        animator = EntityAnimator.FloatingText(
                            text = "miss",
                            color = Color.Black,
                        ),
                    )
                }

                is ActionResult.NoEffect -> {
                    _animatorManager.triggerAnimation(
                        index = result.toIndex,
                        animator = EntityAnimator.FloatingText(
                            text = "null",
                            color = Color.Black,
                        ),
                    )
                }

                else -> {}
            }
        }
    }

    override fun onCombatEnd(result: Boolean) {
        viewModelScope.launch {
            // Save state if the flag is `true`.
            if (arenaConfig?.saveStatus == true) {
                val partiesForSaving = parties.filter {
                    // Filter the temporary entities.
                    arenaConfig?.temporaryEntities?.contains(it.name) != true
                }
                // Save state.
                _gameStateRepo.entity.saveEntityState(partiesForSaving)
                // If it's victory
                if (result) {
                    // Obtain exp.
                    val entityExp = HashMap<String, EntityEntry>()
                    partiesForSaving.forEach { entity ->
                        if (!entity.isFailed()) {
                            // Calculate exp based on party and enemy level.
                            val result = _gameStateRepo.entity.obtainedExp(
                                entityName = entity.name,
                                obtainedExp = enemies.sumOf { enemy ->
                                    EntityLevelHelper.enemyExp(
                                        partyLevel = entity.level,
                                        enemyLevel = enemy.level,
                                    ).toDouble()
                                }.toFloat() * (arenaConfig?.difficulty ?: 1F)
                            )
                            // Save to reward summary
                            entityExp[entity.name] = EntityEntry(
                                obtainedExp = result.first,
                                levelChange = entity.level to result.second
                            )
                        }
                    }
                    // Obtain token based on enemy level.
                    val obtainedTokens = (enemies.sumOf {
                        it.level + 20
                    } / 2F * (arenaConfig?.difficulty ?: 1F)).toInt().coerceAtLeast(1)
                    _gameStateRepo.currency[CurrencyType.TOKEN] += obtainedTokens
                    // Generate reward summary
                    rewardSummary = RewardSummary(
                        obtainedTokens = obtainedTokens,
                        entityExp = entityExp,
                    )
                }
            }
            combatStatus = result
        }
    }

    data class RewardSummary(
        val obtainedTokens: Int,
        val entityExp: Map<String, EntityEntry>,
    ) {
        data class EntityEntry(
            val obtainedExp: Int,
            val levelChange: Pair<Int, Int>
        )
    }
}