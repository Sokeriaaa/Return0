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
package sokeriaaa.return0.applib.modules

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import sokeriaaa.return0.applib.repository.data.ArchiveRepo
import sokeriaaa.return0.applib.repository.data.ResourceRepo
import sokeriaaa.return0.applib.repository.emulator.EmulatorRepo
import sokeriaaa.return0.applib.repository.game.GameStateRepo
import sokeriaaa.return0.applib.repository.game.currency.GameCurrencyRepo
import sokeriaaa.return0.applib.repository.game.entity.GameEntityRepo
import sokeriaaa.return0.applib.repository.game.entity.GameTeamRepo
import sokeriaaa.return0.applib.repository.game.inventory.GameInventoryRepo
import sokeriaaa.return0.applib.repository.game.map.GameMapRepo
import sokeriaaa.return0.applib.repository.game.player.GamePlayerRepo
import sokeriaaa.return0.applib.repository.game.quest.GameQuestRepo
import sokeriaaa.return0.applib.repository.game.saved.SavedValuesRepo
import sokeriaaa.return0.applib.repository.save.SaveRepo
import sokeriaaa.return0.applib.repository.settings.SettingsRepo
import sokeriaaa.return0.applib.room.AppDatabase
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel
import sokeriaaa.return0.mvi.viewmodels.EmulatorPresetViewModel
import sokeriaaa.return0.mvi.viewmodels.EmulatorViewModel
import sokeriaaa.return0.mvi.viewmodels.EntitiesViewModel
import sokeriaaa.return0.mvi.viewmodels.EntityDetailsViewModel
import sokeriaaa.return0.mvi.viewmodels.GameViewModel
import sokeriaaa.return0.mvi.viewmodels.InventoryViewModel
import sokeriaaa.return0.mvi.viewmodels.MainViewModel
import sokeriaaa.return0.mvi.viewmodels.QuestsViewModel
import sokeriaaa.return0.mvi.viewmodels.SaveViewModel
import sokeriaaa.return0.mvi.viewmodels.SettingsViewModel
import sokeriaaa.return0.mvi.viewmodels.TeamsViewModel
import sokeriaaa.return0.ui.main.combat.animation.EntityAnimatorManager

object KoinModules {

    val modules = module {
        // Include platform modules
        includes(platformModules)
        // ViewModelFactory
        single {
            viewModelFactory {
                initializer { MainViewModel() }
                initializer { GameViewModel() }
                initializer { QuestsViewModel() }
                initializer { EntitiesViewModel() }
                initializer {
                    EntityDetailsViewModel(
                        entityName = this[EntityDetailsViewModel.entityNameKey]!!
                    )
                }
                initializer { TeamsViewModel() }
                initializer { InventoryViewModel() }
                initializer { CombatViewModel() }
                initializer {
                    SaveViewModel(
                        isSaving = this[SaveViewModel.isSavingKey]!!,
                    )
                }
                initializer {
                    SettingsViewModel(
                        isInGame = this[SettingsViewModel.isInGameKey]!!,
                    )
                }
                initializer { EmulatorViewModel() }
                initializer { EmulatorPresetViewModel() }
            }
        }
        // ViewModelStoreOwner
        single<ViewModelStoreOwner> {
            object : ViewModelStoreOwner {
                override val viewModelStore = ViewModelStore()
            }
        }
        // Entity animator manager
        single { EntityAnimatorManager() }
        // Repo
        single { ArchiveRepo() }
        single { EmulatorRepo(get(), get(), get()) }
        single { GameCurrencyRepo(get()) }
        single { GameEntityRepo(get(), get(), get()) }
        single { GameInventoryRepo(get()) }
        single { GameMapRepo(get(), get()) }
        single { GamePlayerRepo(get()) }
        single { GameQuestRepo(get()) }
        single {
            GameStateRepo(
                currency = get(),
                entity = get(),
                inventory = get(),
                map = get(),
                player = get(),
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
        single {
            SettingsRepo(get())
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

}

expect val platformModules: Module