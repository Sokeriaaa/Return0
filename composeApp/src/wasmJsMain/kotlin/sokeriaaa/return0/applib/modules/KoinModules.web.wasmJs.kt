/**
 * Copyright (C) 2026 Sokeriaaa
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

import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import org.w3c.dom.Worker

actual val subPlatformModules: Module = module {
    // WebWorkerSQLiteDriver
    single {
        @OptIn(ExperimentalWasmJsInterop::class)
        WebWorkerSQLiteDriver(
            worker = Worker(
                scriptURL = js(
                    code = """new URL("sqlite-web-worker/worker.js", import.meta.url)""",
                ),
            ),
        )
    }
}