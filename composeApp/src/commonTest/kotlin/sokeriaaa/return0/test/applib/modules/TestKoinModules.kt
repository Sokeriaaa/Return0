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
package sokeriaaa.return0.test.applib.modules

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.emulator.EmulatorRepo
import sokeriaaa.return0.applib.repository.game.GameStateRepo
import sokeriaaa.return0.applib.repository.game.currency.GameCurrencyRepo
import sokeriaaa.return0.applib.repository.game.entity.GameEntityRepo
import sokeriaaa.return0.applib.repository.game.entity.GamePluginRepo
import sokeriaaa.return0.applib.repository.game.entity.GameTeamRepo
import sokeriaaa.return0.applib.repository.game.inventory.GameInventoryRepo
import sokeriaaa.return0.applib.repository.game.map.GameMapRepo
import sokeriaaa.return0.applib.repository.game.player.GamePlayerRepo
import sokeriaaa.return0.applib.repository.game.quest.GameQuestRepo
import sokeriaaa.return0.applib.repository.game.saved.SavedValuesRepo
import sokeriaaa.return0.applib.repository.save.SaveRepo
import sokeriaaa.return0.applib.room.AppDatabase

object TestKoinModules {
    val modules = module {
        // Repo
        single { ArchiveRepo() }
        single { EmulatorRepo(get(), get(), get()) }
        single { GameCurrencyRepo(get()) }
        single { GameEntityRepo(get(), get(), get(), get()) }
        single { GameInventoryRepo(get()) }
        single { GameMapRepo(get(), get(), get()) }
        single { GamePlayerRepo(get()) }
        single { GamePluginRepo(get(), get(), get(), get()) }
        single { GameQuestRepo(get()) }
        single {
            GameStateRepo(
                currency = get(),
                entity = get(),
                inventory = get(),
                map = get(),
                player = get(),
                plugin = get(),
                quest = get(),
                savedValues = get(),
                team = get(),
                transactionManager = get(),
            )
        }
        single { GameTeamRepo(get(), get()) }
        single { ResourceRepo() }
        single {
            SavedValuesRepo(
                savedSwitchDao = get(),
                savedTimestampDao = get(),
                savedVariableDao = get(),
            )
        }
        single {
            SaveRepo(
                transactionManager = get(),
                currencyDao = get(),
                entityDao = get(),
                eventRelocationDao = get(),
                inventoryDao = get(),
                questDao = get(),
                savedSwitchDao = get(),
                savedVariableDao = get(),
                saveMetaDao = get(),
                statisticsDao = get(),
                teamDao = get(),
            )
        }
        // Database: Dao
        single { get<AppDatabase>().getCurrencyDao() }
        single { get<AppDatabase>().getEmulatorEntryDao() }
        single { get<AppDatabase>().getEmulatorIndexDao() }
        single { get<AppDatabase>().getEntityDao() }
        single { get<AppDatabase>().getEventRelocationDao() }
        single { get<AppDatabase>().getIndexedHubDao() }
        single { get<AppDatabase>().getInventoryDao() }
        single { get<AppDatabase>().getPluginConstDao() }
        single { get<AppDatabase>().getPluginInventoryDao() }
        single { get<AppDatabase>().getQuestDao() }
        single { get<AppDatabase>().getSavedSwitchDao() }
        single { get<AppDatabase>().getSavedTimestampDao() }
        single { get<AppDatabase>().getSavedVariableDao() }
        single { get<AppDatabase>().getSaveMetaDao() }
        single { get<AppDatabase>().getStatisticsDao() }
        single { get<AppDatabase>().getTeamDao() }
    }

    inline fun withModules(block: () -> Unit) {
        try {
            start()
            block()
        } finally {
            stop()
        }
    }

    fun start() {
        startKoin {
            modules(modules, platformModules)
        }
    }

    fun stop() {
        stopKoin()
    }
}

expect val platformModules: Module