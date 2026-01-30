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
package sokeriaaa.return0.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import sokeriaaa.return0.applib.repository.settings.SettingsRepo
import sokeriaaa.return0.ui.nav.AppNavHost
import sokeriaaa.return0.ui.theme.Return0Theme

@Composable
@Preview
fun App() {
    val settingsRepo: SettingsRepo = koinInject()
    val darkThemeValue by settingsRepo.appearanceDarkTheme.flow.collectAsState(0)
    Return0Theme(
        darkTheme = when (darkThemeValue) {
            1 -> false
            2 -> true
            else -> isSystemInDarkTheme()
        },
    ) {
        Surface {
            AppNavHost(
                mainNavHostController = rememberNavController(),
                windowAdaptiveInfo = currentWindowAdaptiveInfo(),
            )
        }
    }
}