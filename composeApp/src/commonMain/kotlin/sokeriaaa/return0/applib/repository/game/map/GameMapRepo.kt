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
package sokeriaaa.return0.applib.repository.game.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import return0.composeapp.generated.resources.Res
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.repository.game.base.BaseGameRepo
import sokeriaaa.return0.applib.room.dao.EventRelocationDao
import sokeriaaa.return0.applib.room.dao.IndexedHubDao
import sokeriaaa.return0.applib.room.dao.SaveMetaDao
import sokeriaaa.return0.applib.room.table.EventRelocationTable
import sokeriaaa.return0.applib.room.table.IndexedHubTable
import sokeriaaa.return0.shared.common.helpers.JsonHelper
import sokeriaaa.return0.shared.data.models.story.map.MapData
import sokeriaaa.return0.shared.data.models.story.map.MapEvent

/**
 * Manage the game maps.
 */
class GameMapRepo(
    private val eventRelocationDao: EventRelocationDao,
    private val indexedHubDao: IndexedHubDao,
    private val saveMetaDao: SaveMetaDao,
) : BaseGameRepo {

    /**
     * Manages relocated events.
     */
    private val _eventRelocateBuffer: MutableMap<String, Pair<String, Int>> = HashMap()

    /**
     * Current map.
     */
    var current: MapData by mutableStateOf(
        MapData(
            name = "",
            lines = 0,
            buggyRange = listOf(),
            buggyEntries = listOf(),
            events = emptyList()
        )
    )
        private set

    /**
     * Current line number.
     */
    var lineNumber: Int by mutableIntStateOf(1)
        private set

    private val _indexedHubMap: MutableMap<Pair<String, Int>, Long> = mutableStateMapOf()
    val indexedHubMap: Map<Pair<String, Int>, Long> = _indexedHubMap

    /**
     * Update line number.
     */
    fun updateLineNumber(lineNumber: Int) {
        this.lineNumber = lineNumber
    }

    /**
     * Is [currentRow] in the buggy range.
     */
    fun isInBuggyRange(currentRow: Int): Boolean {
        return current.buggyRange.any { it.first <= currentRow && currentRow <= it.second }
    }

    fun indexedNewHub(location: Pair<String, Int>, time: Long) {
        if (indexedHubMap.containsKey(location)) {
            return
        }
        _indexedHubMap[location] = time
    }

    /**
     * Load a map with specified path.
     *
     * @throws org.jetbrains.compose.resources.MissingResourceException If the map file is not exist.
     */
    private suspend fun loadMap(path: String) {
        current = JsonHelper.decodeFromString(
            string = Res.readBytes("files/data/map/$path.json").decodeToString(),
        )
    }

    /**
     * Update user position.
     */
    suspend fun updatePosition(
        fileName: String = current.name,
        lineNumber: Int,
    ) {
        if (fileName != current.name) {
            loadMap(fileName)
        }
        this.lineNumber = lineNumber
    }

    /**
     * Teleport an event to specified location.
     */
    fun teleportEvent(
        eventKey: String,
        fileName: String = current.name,
        lineNumber: Int,
    ) {
        _eventRelocateBuffer[eventKey] = fileName to lineNumber
    }

    /**
     * Load events in a map.
     */
    suspend fun loadEvents(
        saveID: Int = -1,
        mapData: MapData = this.current,
    ): List<MapEvent> {
        val events = ArrayList<MapEvent>()
        // Check events in current map
        mapData.events.forEach {
            if (_eventRelocateBuffer.containsKey(it.key)) {
                // Load new location.
                val location = _eventRelocateBuffer[it.key]!!
                events.add(it.copy(lineNumber = location.second))
            } else if (it.key == null) {
                // Events with no key is guaranteed to exist here.
                events.add(it)
            } else {
                // Check database
                val relocation = eventRelocationDao.query(saveID, it.key, current.name)
                if (relocation == null) {
                    // Not relocated
                    events.add(it)
                } else {
                    // Load new location.
                    events.add(it.copy(lineNumber = relocation.lineNumber))
                }
            }
        }
        return events
    }

    override suspend fun load() {
        saveMetaDao.query(AppConstants.CURRENT_SAVE_ID)?.let {
            loadMap(it.fileName)
            lineNumber = it.lineNumber
        }
        _indexedHubMap.clear()
        indexedHubDao.queryAll(AppConstants.CURRENT_SAVE_ID).forEach {
            _indexedHubMap[it.fileName to it.lineNumber] = it.indexedTime
        }
    }

    override suspend fun flush() {
        // Save current position.
        saveMetaDao.updatePosition(
            saveID = AppConstants.CURRENT_SAVE_ID,
            fileName = current.name,
            lineNumber = lineNumber,
        )
        // Save relocated events.
        _eventRelocateBuffer.forEach { entry ->
            eventRelocationDao.insertOrUpdate(
                EventRelocationTable(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    eventKey = entry.key,
                    fileName = entry.value.first,
                    lineNumber = entry.value.second,
                )
            )
        }
        _eventRelocateBuffer.clear()
        // Save indexed hubs
        indexedHubDao.delete(AppConstants.CURRENT_SAVE_ID)
        indexedHubMap.forEach {
            indexedHubDao.insertOrUpdate(
                IndexedHubTable(
                    saveID = AppConstants.CURRENT_SAVE_ID,
                    fileName = it.key.first,
                    lineNumber = it.key.second,
                    indexedTime = it.value,
                )
            )
        }
    }

}