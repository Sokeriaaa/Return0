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
import sokeriaaa.return0.applib.repository.ArchiveRepo
import sokeriaaa.return0.applib.repository.CombatRepo
import sokeriaaa.return0.applib.room.AppDatabase
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel
import sokeriaaa.return0.mvi.viewmodels.EmulatorPresetViewModel
import sokeriaaa.return0.mvi.viewmodels.EmulatorViewModel
import sokeriaaa.return0.mvi.viewmodels.MainViewModel
import sokeriaaa.return0.mvi.viewmodels.ProfileViewModel
import sokeriaaa.return0.ui.main.combat.animation.EntityAnimatorManager

object KoinModules {

    val modules = module {
        // Include platform modules
        includes(platformModules)
        // ViewModelFactory
        single {
            viewModelFactory {
                initializer { MainViewModel() }
                initializer { CombatViewModel() }
                initializer { ProfileViewModel() }
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
        single { CombatRepo(get()) }
        // Database: Dao
        single { get<AppDatabase>().getEmulatorEntryDao() }
        single { get<AppDatabase>().getEmulatorIndexDao() }
        single { get<AppDatabase>().getEntityDao() }
    }

}

expect val platformModules: Module