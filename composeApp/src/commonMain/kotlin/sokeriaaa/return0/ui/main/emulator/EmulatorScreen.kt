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
package sokeriaaa.return0.ui.main.emulator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.emulator
import return0.composeapp.generated.resources.emulator_combat
import return0.composeapp.generated.resources.emulator_enemy_w_count
import return0.composeapp.generated.resources.emulator_party_w_count
import return0.composeapp.generated.resources.emulator_warning_entity_not_enough
import return0.composeapp.generated.resources.ic_baseline_start_24
import sokeriaaa.return0.mvi.intents.CombatIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel
import sokeriaaa.return0.mvi.viewmodels.EmulatorViewModel
import sokeriaaa.return0.shared.data.models.combat.ArenaConfig
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.main.emulator.page.EmulatorPage
import sokeriaaa.return0.ui.nav.Scene
import sokeriaaa.return0.ui.nav.navigateSingleTop

/**
 * EmulatorScreen.
 *
 * Configure mock combats for both user and developer.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmulatorScreen(
    viewModel: EmulatorViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    // Page titles
    val pageTitles = listOf(
        Res.string.emulator_party_w_count,
        Res.string.emulator_enemy_w_count,
    )
    // Page data
    val data = listOf(
        viewModel.parties,
        viewModel.enemies,
    )
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.emulator)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues = paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val pagerState = rememberPagerState(pageCount = { pageTitles.size })
            val scope = rememberCoroutineScope()

            // Page tab
            PrimaryScrollableTabRow(
                // Our selected tab is our current page
                selectedTabIndex = pagerState.currentPage,
                // Override the indicator, using the provided pagerTabIndicatorOffset modifier
                indicator = {
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedTabIndex = pagerState.currentPage,
                        )
                    )
                },
            ) {
                pageTitles.forEachIndexed { index, titleRes ->
                    Tab(
                        text = {
                            Text(
                                text = stringResource(
                                    resource = titleRes,
                                    /* count = */ data[index].size
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    )
                }
            }

            // Pager
            HorizontalPager(
                modifier = Modifier.weight(1F),
                state = pagerState,
                verticalAlignment = Alignment.Top,
            ) { page ->
                when (page) {
                    0 -> EmulatorPage(
                        isParty = true,
                        entityStateList = viewModel.parties,
                        availableEntities = viewModel.availableEntities,
                        onIntent = viewModel::onIntent,
                        mainNavHostController = mainNavHostController,
                        windowAdaptiveInfo = windowAdaptiveInfo,
                    )

                    1 -> EmulatorPage(
                        isParty = false,
                        entityStateList = viewModel.enemies,
                        availableEntities = viewModel.availableEntities,
                        onIntent = viewModel::onIntent,
                        mainNavHostController = mainNavHostController,
                        windowAdaptiveInfo = windowAdaptiveInfo,
                    )
                }
            }

            val warningMessage = stringResource(Res.string.emulator_warning_entity_not_enough)
            val combatViewModel: CombatViewModel = viewModel(
                factory = koinInject(),
                viewModelStoreOwner = koinInject(),
            )
            // Start combat
            FilledTonalButton(
                modifier = Modifier.padding(vertical = 12.dp),
                onClick = {
                    if (viewModel.parties.isEmpty() || viewModel.enemies.isEmpty()) {
                        viewModel.onIntent(CommonIntent.ShowSnackBar(warningMessage))
                    } else {
                        // Navigate
                        mainNavHostController.navigateSingleTop(Scene.Combat.route)
                        // Send intent to CombatViewModel.
                        combatViewModel.onIntent(
                            intent = CombatIntent.Prepare(
                                config = ArenaConfig(
                                    parties = viewModel.parties,
                                    enemies = viewModel.enemies,
                                )
                            )
                        )
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(
                        size = ButtonDefaults.IconSize,
                    ),
                    painter = painterResource(Res.drawable.ic_baseline_start_24),
                    contentDescription = stringResource(Res.string.emulator_combat),
                )
                Text(
                    modifier = Modifier.padding(start = ButtonDefaults.IconSpacing),
                    text = stringResource(Res.string.emulator_combat),
                )
            }
        }
    }
}