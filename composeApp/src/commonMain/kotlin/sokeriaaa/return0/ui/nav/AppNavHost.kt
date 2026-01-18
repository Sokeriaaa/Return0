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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.savedstate.read
import sokeriaaa.return0.ui.main.MainScreen
import sokeriaaa.return0.ui.main.combat.CombatScreen
import sokeriaaa.return0.ui.main.emulator.EmulatorPresetScreen
import sokeriaaa.return0.ui.main.emulator.EmulatorScreen
import sokeriaaa.return0.ui.main.game.GameScreen
import sokeriaaa.return0.ui.main.game.entities.EntitiesScreen
import sokeriaaa.return0.ui.main.game.entities.details.EntityDetailsScreen
import sokeriaaa.return0.ui.main.game.entities.plugin.EntityPluginSelectionScreen
import sokeriaaa.return0.ui.main.game.inventory.InventoryScreen
import sokeriaaa.return0.ui.main.game.quests.QuestsScreen
import sokeriaaa.return0.ui.main.game.teams.TeamsScreen
import sokeriaaa.return0.ui.main.save.SaveScreen
import sokeriaaa.return0.ui.main.settings.SettingsScreen

@Composable
fun AppNavHost(
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    NavHost(
        navController = mainNavHostController,
        startDestination = Scene.Main.route,
        route = "r0_app_root",
    ) {
        myComposable(Scene.Main) {
            MainScreen(
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }

        myComposable(
            scene = Scene.Game,
            arguments = listOf(
                myNavArgument(
                    name = "isNewGame",
                    type = NavType.BoolType,
                ),
            ),
        ) {
            val argument = it.arguments!!
            GameScreen(
                isNewGame = argument.read { getBoolean("isNewGame") },
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(Scene.Quests) {
            QuestsScreen(
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(Scene.Entities) {
            EntitiesScreen(
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(
            scene = Scene.EntityDetails,
            arguments = listOf(
                myNavArgument(
                    name = "entityName",
                    type = NavType.StringType,
                )
            ),
        ) {
            val argument = it.arguments!!
            EntityDetailsScreen(
                entityName = argument.read { getString("entityName") },
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(
            scene = Scene.EntityPluginSelection,
            arguments = listOf(
                myNavArgument(
                    name = "entityName",
                    type = NavType.StringType,
                )
            ),
        ) {
            val argument = it.arguments!!
            EntityPluginSelectionScreen(
                entityName = argument.read { getString("entityName") },
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(Scene.Teams) {
            TeamsScreen(
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(Scene.Inventory) {
            InventoryScreen(
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
        myComposable(
            scene = Scene.Save,
            arguments = listOf(
                myNavArgument(
                    name = "isSave",
                    type = NavType.BoolType,
                ),
            ),
        ) {
            val argument = it.arguments!!
            SaveScreen(
                isSaving = argument.read { getBoolean("isSave") },
                mainNavHostController = mainNavHostController,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
        myComposable(
            scene = Scene.Settings,
            arguments = listOf(
                myNavArgument(
                    name = "isInGame",
                    type = NavType.BoolType,
                ),
            ),
        ) {
            val argument = it.arguments!!
            SettingsScreen(
                isInGame = argument.read { getBoolean("isInGame") },
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
        myComposable(Scene.EmulatorPreset) {
            EmulatorPresetScreen(
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

private inline fun <reified T> myNavArgument(
    name: String,
    type: NavType<T>
): NamedNavArgument = navArgument(
    name = name,
) {
    this.type = type
}

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

fun NavController.navigatePopUpTo(route: String) {
    navigate(
        route = route
    ) {
        launchSingleTop = true
        currentDestination?.route?.let {
            popUpTo(it) { inclusive = true }
        }
    }
}

sealed class Scene(val route: String) {
    data object Main : Scene(route = "r0_main")
    data object Game : Scene(route = "r0_game")
    data object Quests : Scene(route = "r0_quests")
    data object Entities : Scene(route = "r0_entities")
    data object EntityDetails : Scene(route = "r0_entity_details")
    data object EntityPluginSelection : Scene(route = "r0_entity_plugin_selection")
    data object Teams : Scene(route = "r0_teams")
    data object Inventory : Scene(route = "r0_inventory")

    data object Combat : Scene(route = "r0_combat")
    data object Save : Scene(route = "r0_save")
    data object Settings : Scene(route = "r0_settings")
    data object Emulator : Scene(route = "r0_emulator")
    data object EmulatorPreset : Scene(route = "r0_emulator_preset")
}