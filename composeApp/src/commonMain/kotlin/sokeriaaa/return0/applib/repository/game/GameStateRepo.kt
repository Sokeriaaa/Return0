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
package sokeriaaa.return0.applib.repository.game

import sokeriaaa.return0.applib.repository.game.base.BaseGameRepo
import sokeriaaa.return0.applib.repository.game.currency.GameCurrencyRepo
import sokeriaaa.return0.applib.repository.game.entity.GameEntityRepo
import sokeriaaa.return0.applib.repository.game.entity.GameTeamRepo
import sokeriaaa.return0.applib.repository.game.inventory.GameInventoryRepo
import sokeriaaa.return0.applib.repository.game.map.GameMapRepo
import sokeriaaa.return0.applib.repository.game.player.GamePlayerRepo
import sokeriaaa.return0.applib.repository.game.quest.GameQuestRepo
import sokeriaaa.return0.applib.repository.game.saved.SavedValuesRepo
import sokeriaaa.return0.applib.room.helper.TransactionManager

class GameStateRepo(
    // Sub-repos
    val currency: GameCurrencyRepo,
    val entity: GameEntityRepo,
    val inventory: GameInventoryRepo,
    val map: GameMapRepo,
    val player: GamePlayerRepo,
    val quest: GameQuestRepo,
    val savedValues: SavedValuesRepo,
    val team: GameTeamRepo,
    // Transaction manager.
    private val transactionManager: TransactionManager,
) : BaseGameRepo {

    /**
     * Load game state from database to this repo.
     */
    override suspend fun load() {
        currency.load()
        inventory.load()
        map.load()
        quest.load()
        savedValues.load()
    }

    /**
     * Flush the temp data to the database.
     */
    override suspend fun flush() {
        transactionManager.withTransaction {
            currency.flush()
            inventory.flush()
            map.flush()
            quest.flush()
            savedValues.flush()
        }
    }
}