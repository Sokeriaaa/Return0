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

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.dsl.module
import sokeriaaa.return0.applib.repository.ArchiveRepo
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel
import sokeriaaa.return0.mvi.viewmodels.EmulatorViewModel
import sokeriaaa.return0.mvi.viewmodels.MainViewModel
import sokeriaaa.return0.mvi.viewmodels.ProfileViewModel

object KoinModules {

    val modules = module {
        // ViewModelFactory
        single {
            viewModelFactory {
                initializer { MainViewModel() }
                initializer { CombatViewModel() }
                initializer { ProfileViewModel() }
                initializer { EmulatorViewModel() }
            }
        }
        // Repo
        single { ArchiveRepo() }
    }

}