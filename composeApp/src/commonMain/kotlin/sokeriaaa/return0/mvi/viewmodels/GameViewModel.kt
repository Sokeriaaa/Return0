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

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.game.GameStateRepo
import sokeriaaa.return0.shared.data.models.combat.ArenaConfig
import sokeriaaa.return0.shared.data.models.combat.EnemyState
import sokeriaaa.return0.shared.data.models.story.event.Event
import kotlin.random.Random

class GameViewModel : BaseViewModel() {
    /**
     * Archive repo.
     */
    private val _archiveRepo: ArchiveRepo by inject()

    /**
     * Game state repo.
     */
    private val _gameStateRepo: GameStateRepo by inject()

    private val _combatEvents = MutableSharedFlow<Event.Combat>()

    /**
     * Combat events.
     */
    val combatEvents = _combatEvents.asSharedFlow()

    /**
     * File name.
     */
    val fileName: String get() = _gameStateRepo.map.current.name

    /**
     * Line number.
     */
    val lineNumber: Int get() = _gameStateRepo.map.lineNumber

    /**
     * Lines passed since last combat. It affects the chance to encounter a new combat
     * in the buggy range. Set to 0 after a combat is finished or the player leaves the range.
     */
    private var _linesSinceLastCombat = 0

    /**
     * Moving job.
     */
    private var _movingJob: Job? = null

    fun requestMoveTo(
        targetLine: Int
    ) {
        // Cancel the previous
        _movingJob?.cancel()
        val start = _gameStateRepo.map.lineNumber
        if (start == targetLine) {
            return
        }
        _movingJob = viewModelScope.launch {
            val direction = if (targetLine > start) 1 else -1
            var current = start
            while (current != targetLine) {
                // Move for every 200ms.
                delay(200)
                current += direction
                _gameStateRepo.map.updateLineNumber(current)
                if (
                    _gameStateRepo.map.current.buggyRange.any {
                        it.first <= current && current <= it.second
                    }
                ) {
                    // Is in buggy range
                    _linesSinceLastCombat++
                    if (checkEncounter()) {
                        _linesSinceLastCombat = 0
                        // Start combat
                        _combatEvents.emit(
                            Event.Combat(
                                config = ArenaConfig(
                                    mode = ArenaConfig.Mode.COMMON,
                                    parties = _gameStateRepo.loadTeam(useCurrentData = false),
                                    enemies = _gameStateRepo.map.current.buggyEntries.random()
                                        .enemies.map {
                                            EnemyState(
                                                entityData = _archiveRepo.getEntityData(it)!!,
                                                level = 1
                                            )
                                        }
                                )
                            )
                        )
                        interruptMoving()
                    }
                } else {
                    // Outside in buggy range
                    _linesSinceLastCombat = 0
                }
            }
        }
    }

    /**
     * The player is about to encounter the enemies
     */
    private fun checkEncounter(): Boolean {
        return Random.nextInt(AppConstants.COMBAT_RATE_BASE ushr _linesSinceLastCombat) == 0
    }

    private fun interruptMoving() {
        _movingJob?.cancel()
        _movingJob = null
    }
}