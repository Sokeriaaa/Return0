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
import org.koin.dsl.module
import sokeriaaa.return0.applib.repository.ArchiveRepo
import sokeriaaa.return0.applib.repository.CombatRepo

object TestKoinModules {
    val modules = module {
        // Repo
        single { ArchiveRepo() }
        single { CombatRepo(get()) }
    }

    inline fun withModules(block: () -> Unit) {
        start()
        block()
        stop()
    }

    fun start() {
        startKoin {
            modules(modules)
        }
    }

    fun stop() {
        stopKoin()
    }

}