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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.game.GameStateRepo
import sokeriaaa.return0.models.component.context.EventContext
import sokeriaaa.return0.models.component.executor.condition.calculatedIn
import sokeriaaa.return0.models.story.event.EventEffect
import sokeriaaa.return0.models.story.event.executedIn
import sokeriaaa.return0.models.story.map.MapGenerator
import sokeriaaa.return0.models.story.map.MapRow
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.GameIntent
import sokeriaaa.return0.shared.data.models.story.event.Event
import sokeriaaa.return0.shared.data.models.story.map.MapData
import sokeriaaa.return0.shared.data.models.story.map.MapEvent
import kotlin.random.Random

class GameViewModel : BaseViewModel(), EventContext.Callback {

    // Repo
    private val _gameStateRepo: GameStateRepo by inject()

    /**
     * Is prepared for new game.
     */
    private var _isPreparedForNewGame = false

    /**
     * File name.
     */
    val current: MapData get() = _gameStateRepo.map.current

    /**
     * Line number.
     */
    val lineNumber: Int get() = _gameStateRepo.map.lineNumber

    // Map rows.
    private val _mapRows: MutableList<MapRow> = mutableStateListOf()
    val mapRows: List<MapRow> = _mapRows

    // No location events which don't have the line number.
    private val _noLocationEvents: MutableList<MapEvent> = mutableStateListOf()

    // The line numbers with an event triggered by overlapping.
    private val _overlapRows: MutableSet<Int> = mutableStateSetOf()

    // Lines that are blocked by events.
    private val _blockedRows: MutableSet<Int> = mutableStateSetOf()

    /**
     * The player is currently moving by an event.
     */
    var isMovingByEvent by mutableStateOf(false)
        private set

    /**
     * The player is currently switching to another file.
     */
    var isSwitchingFile by mutableStateOf(false)
        private set

    /**
     * Lines passed since last combat. It affects the chance to encounter a new combat
     * in the buggy range. Set to 0 after a combat is finished or the player leaves the range.
     */
    private var _linesSinceLastCombat = 0

    /**
     * Moving job.
     */
    private var _movingJob: Job? = null

    // Event deferred.
    private var _continueDeferred: CompletableDeferred<Unit>? = null
    private var _choiceDeferred: CompletableDeferred<Int>? = null
    private var _moveDeferred: CompletableDeferred<Unit>? = null
    private var _combatDeferred: CompletableDeferred<Boolean>? = null

    // Current event effects
    private val _effects = MutableSharedFlow<EventEffect>(
        replay = 0,
        extraBufferCapacity = 16,
    )
    val effects = _effects.asSharedFlow()

    override fun onIntent(intent: BaseIntent) {
        super.onIntent(intent)
        when (intent) {
            GameIntent.PrepareNewGame -> {
                _isPreparedForNewGame = true
            }

            GameIntent.StartNewGame -> {
                if (_isPreparedForNewGame) {
                    _isPreparedForNewGame = false
                    viewModelScope.launch {
                        // Trigger the events in entrance when a new game is started.
                        triggerNoLocationEvents()
                    }
                }
            }
            is GameIntent.RequestMoveTo -> requestMoveTo(
                targetLine = intent.line,
                isByEvent = intent.isByEvent,
                isEncounterDisabled = intent.isEncounterDisabled,
            )

            is GameIntent.TeleportTo -> teleportTo(
                targetFileName = intent.fileName,
                targetLine = intent.line,
            )

            GameIntent.RefreshMap -> viewModelScope.launch {
                reloadMap()
            }

            GameIntent.RefreshEvent -> viewModelScope.launch {
                clearAllEvents()
                loadAllEvents()
            }

            is GameIntent.ExecuteEvent -> viewModelScope.launch {
                executeEvent(intent.mapEvent)
            }

            GameIntent.EventContinue -> onUserContinue()
            is GameIntent.EventChoice -> onChoiceSelected(intent.index)
            is GameIntent.EventCombatResult -> onCombatFinished(intent.result)
            else -> {}
        }
    }

    /**
     * Reload the map rows for current map.
     */
    private suspend fun reloadMap() {
        _mapRows.clear()
        clearAllEvents()
        _mapRows.addAll(MapGenerator.generateCode(current))
        loadAllEvents()
    }

    /**
     * Clear all the events in this map.
     */
    private fun clearAllEvents() {
        _mapRows.forEach { it.events.clear() }
        _overlapRows.clear()
        _noLocationEvents.clear()
        _blockedRows.clear()
    }

    /**
     * Load all events in this map.
     */
    private suspend fun loadAllEvents() {
        _gameStateRepo.map.loadEvents().forEach {
            val context = getEventContext(it)
            if (it.enabled.calculatedIn(context)) {
                // If the event is enabled
                if (it.lineNumber == null) {
                    // Handle events that doesn't have a line number.
                    if (
                        it.trigger != MapEvent.Trigger.OVERLAPPED &&
                        it.trigger != MapEvent.Trigger.INTERACTED
                    ) {
                        _noLocationEvents.add(it)
                    }
                } else {
                    // Add the event
                    if (it.trigger != MapEvent.Trigger.ENTERED) {
                        _mapRows[it.lineNumber - 1].events.add(it)
                        if (it.trigger == MapEvent.Trigger.OVERLAPPED) {
                            // Add overlap trigger.
                            _overlapRows.add(it.lineNumber)
                        }
                        if (it.blocksUser.calculatedIn(context)) {
                            // Lines blocked
                            _blockedRows.add(it.lineNumber)
                        }
                    }
                }
            }
        }
    }

    /**
     * Trigger the events without line number in this map (when the user entered the map).
     */
    private suspend fun triggerNoLocationEvents() {
        executeEvent(*_noLocationEvents.toTypedArray())
    }

    /**
     * Send a request for moving the player to specified line animatedly.
     */
    private fun requestMoveTo(
        targetLine: Int,
        isByEvent: Boolean = false,
        isEncounterDisabled: Boolean = false,
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
                if (isByEvent) {
                    isMovingByEvent = true
                }
                if (current + direction in _blockedRows) {
                    // Blocked by event.
                    interruptMoving()
                    break
                }
                // Move for every 200ms.
                delay(200)
                current += direction
                _gameStateRepo.map.updateLineNumber(current)
                if (!isByEvent && current in _overlapRows) {
                    // Trigger overlapped event when the player is not moving bu an event.
                    executeEvent(*_mapRows[current - 1].events.toTypedArray())
                    interruptMoving()
                    break
                }
                if (!isEncounterDisabled) {
                    // Check buggy area.
                    if (_gameStateRepo.map.isInBuggyRange(current)) {
                        // Is in buggy range
                        _linesSinceLastCombat++
                        if (checkEncounter()) {
                            _linesSinceLastCombat = 0
                            // Interrupt
                            isMovingByEvent = false
                            onMoveFinished()
                            // Start combat
                            encounteredCombat()
                            interruptMoving()
                            break
                        }
                    } else {
                        // Outside in buggy range
                        _linesSinceLastCombat = 0
                    }
                }
            }
            onMoveFinished()
            isMovingByEvent = false
        }
    }

    /**
     * Teleport the player to another position.
     */
    private fun teleportTo(
        targetFileName: String,
        targetLine: Int,
    ) {
        viewModelScope.launch {
            val switchedFile = targetFileName != _gameStateRepo.map.current.name
            if (switchedFile) {
                isSwitchingFile = true
                _gameStateRepo.map.updatePosition(targetFileName, targetLine)
                reloadMap()
                isSwitchingFile = false
                triggerNoLocationEvents()
            } else {
                _gameStateRepo.map.updatePosition(targetFileName, targetLine)
            }
        }
    }

    private fun getEventContext(event: MapEvent?): EventContext = EventContext(
        key = event?.key,
        location = event?.lineNumber?.let { current.name to it },
        callback = this,
    )

    /**
     * The player is about to encounter the enemies
     */
    private fun checkEncounter(): Boolean {
        return Random.nextInt(AppConstants.COMBAT_RATE_BASE ushr _linesSinceLastCombat) == 0
    }

    private suspend fun executeEvent(vararg mapEvents: MapEvent) {
        mapEvents.forEach {
            it.event.executedIn(getEventContext(it))
        }
        // After execution finished, finish this event.
        _effects.emit(EventEffect.EventFinished)
    }

    private suspend fun encounteredCombat() {
        val entry = _gameStateRepo.map.current.buggyEntries.random()
        Event.Combat(
            config = Event.Combat.Config(
                enemies = entry.enemies.map { it to entry.level }
            ),
        ).executedIn(getEventContext(event = null))
    }

    private fun interruptMoving() {
        _movingJob?.cancel()
        _movingJob = null
    }

    override suspend fun waitForUserContinue() {
        val deferred = CompletableDeferred<Unit>()
        _continueDeferred = deferred
        deferred.await()
    }

    override suspend fun waitForChoice(): Int {
        val deferred = CompletableDeferred<Int>()
        _choiceDeferred = deferred
        return deferred.await()
    }

    override suspend fun waitForMoveFinished() {
        val deferred = CompletableDeferred<Unit>()
        _moveDeferred = deferred
        deferred.await()
    }

    override suspend fun waitForCombatResult(): Boolean {
        val deferred = CompletableDeferred<Boolean>()
        _combatDeferred = deferred
        return deferred.await()
    }

    override suspend fun onEffect(effect: EventEffect) {
        _effects.emit(effect)
    }

    private fun onUserContinue() {
        _continueDeferred?.complete(Unit)
    }

    private fun onChoiceSelected(index: Int) {
        _choiceDeferred?.complete(index)
    }

    private fun onCombatFinished(result: Boolean) {
        _combatDeferred?.complete(result)
    }

    private fun onMoveFinished() {
        _moveDeferred?.complete(Unit)
    }

}