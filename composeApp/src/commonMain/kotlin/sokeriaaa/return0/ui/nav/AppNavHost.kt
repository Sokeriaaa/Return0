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
package sokeriaaa.return0.ui.nav

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import sokeriaaa.return0.ui.main.MainScreen
import sokeriaaa.return0.ui.main.combat.CombatScreen
import sokeriaaa.return0.ui.main.emulator.EmulatorScreen
import sokeriaaa.return0.ui.main.profile.ProfileScreen

@Composable
fun AppNavHost(
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    NavHost(
        navController = mainNavHostController,
//        startDestination = Scene.Main.route,
        startDestination = Scene.Emulator.route,
        route = "r0_app_root",
    ) {
        myComposable(Scene.Main) {
            MainScreen(
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(Scene.Combat) {
            CombatScreen(
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(Scene.Profile) {
            ProfileScreen(
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(Scene.Emulator) {
            EmulatorScreen(
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
    }
}

private fun NavGraphBuilder.myComposable(
    scene: Scene,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(
    route = scene.route + arguments.joinToString(separator = "") { "/{${it.name}}" },
    arguments = arguments,
    enterTransition = { EnterTransition() },
    exitTransition = { ExitTransition() },
    content = content,
)

private fun EnterTransition() =
    slideInHorizontally(
        animationSpec = tween(durationMillis = 400),
        initialOffsetX = { it / 2 },
    ) + fadeIn()

private fun ExitTransition() =
    slideOutHorizontally(
        animationSpec = tween(durationMillis = 400),
        targetOffsetX = { it / 2 },
    ) + fadeOut()

fun NavController.navigateSingleTop(route: String) {
    navigate(
        route = route
    ) {
        launchSingleTop = true
    }
}

sealed class Scene(val route: String) {
    data object Main : Scene(route = "r0_main")
    data object Combat : Scene(route = "r0_combat")
    data object Profile : Scene(route = "r0_profile")
    data object Emulator : Scene(route = "r0_emulator")
}