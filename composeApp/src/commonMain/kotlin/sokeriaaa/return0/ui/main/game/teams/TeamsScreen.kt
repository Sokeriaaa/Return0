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
package sokeriaaa.return0.ui.main.game.teams

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.empty_slot
import return0.composeapp.generated.resources.game_menu_teams
import return0.composeapp.generated.resources.game_select_entity
import return0.composeapp.generated.resources.game_team_activate
import return0.composeapp.generated.resources.game_team_activated
import return0.composeapp.generated.resources.game_team_default
import return0.composeapp.generated.resources.game_team_new
import sokeriaaa.return0.applib.common.AppConstants
import sokeriaaa.return0.applib.room.table.EntityTable
import sokeriaaa.return0.mvi.intents.BaseIntent
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.intents.TeamsIntent
import sokeriaaa.return0.mvi.viewmodels.TeamsViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.entity.EntityProfileItem
import sokeriaaa.return0.ui.common.widgets.AddCard
import sokeriaaa.return0.ui.common.widgets.AppAlertDialog
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton
import sokeriaaa.return0.ui.common.widgets.AppButton
import sokeriaaa.return0.ui.common.widgets.AppRadioGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    viewModel: TeamsViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    LaunchedEffect(Unit) {
        viewModel.onIntent(CommonIntent.Refresh)
    }
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.game_menu_teams)
                    )
                },
                navigationIcon = {
                    AppBackIconButton(
                        onClick = {
                            mainNavHostController.navigateUp()
                        }
                    )
                },
            )
        }
    ) { paddingValues ->
        val pagerState = rememberPagerState(pageCount = { viewModel.teams.size })
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier.padding(paddingValues = paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PrimaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedTabIndex = pagerState.currentPage,
                        )
                    )
                },
            ) {
                viewModel.teams.forEachIndexed { index, team ->
                    Tab(
                        text = {
                            Text(
                                text = team.name ?: stringResource(
                                    Res.string.game_team_default,
                                    index
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
                // Create new team
                if (viewModel.teams.size < AppConstants.MAXIMUM_TEAMS) {
                    Tab(
                        text = {
                            Text(
                                text = stringResource(Res.string.game_team_new),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        selected = false,
                        onClick = {
                            viewModel.onIntent(TeamsIntent.RequestCreateTeam)
                        },
                    )
                }
            }
            HorizontalPager(
                modifier = Modifier.weight(1F),
                state = pagerState,
                verticalAlignment = Alignment.Top,
            ) { page ->
                viewModel.teams.getOrNull(page)?.let {
                    TeamContent(
                        availableEntities = viewModel.availableEntities,
                        teamIndex = page,
                        display = it,
                        onIntent = viewModel::onIntent,
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamContent(
    modifier: Modifier = Modifier,
    availableEntities: List<EntityTable>,
    teamIndex: Int,
    display: TeamsViewModel.TeamDisplay,
    onIntent: (BaseIntent) -> Unit,
) {
    var selectingIndex by remember { mutableStateOf(-1) }
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.FixedSize(192.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            val name = display.name ?: stringResource(Res.string.game_team_default, teamIndex)
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                text = name,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        itemsIndexed(
            items = display.entities,
        ) { index, entity ->
            if (entity == null) {
                // EmptySlot
                AddCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(all = 4.dp),
                    shape = RoundedCornerShape(24.dp),
                    onClick = { selectingIndex = index },
                )
            } else {
                EntityProfileItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 4.dp)
                        .clickable { selectingIndex = index },
                    display = entity,
                )
            }
        }
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                AppButton(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(
                        if (display.isActivated) {
                            Res.string.game_team_activated
                        } else {
                            Res.string.game_team_activate
                        }
                    ),
                    enabled = !display.isActivated && display.entities.any { it != null },
                    onClick = {
                        onIntent(TeamsIntent.ActivateTeam(teamIndex))
                    },
                )
            }
        }
    }
    // Select entity dialog
    if (selectingIndex >= 0) {
        SelectEntityDialog(
            modifier = Modifier.padding(vertical = 64.dp),
            availableEntities = availableEntities,
            display = display,
            selectingIndex = selectingIndex,
            onSelected = {
                onIntent(
                    TeamsIntent.SwitchEntity(
                        teamIndex = teamIndex,
                        entityIndex = selectingIndex,
                        newEntity = it,
                    ),
                )
                selectingIndex = -1
            },
            onDismiss = { selectingIndex = -1 }
        )
    }
}

@Composable
private fun SelectEntityDialog(
    modifier: Modifier = Modifier,
    availableEntities: List<EntityTable>,
    display: TeamsViewModel.TeamDisplay,
    selectingIndex: Int,
    onSelected: (String?) -> Unit,
    onDismiss: () -> Unit,
) {
    // Selected entity
    var selectedEntity: String? by remember {
        mutableStateOf(display.entities.getOrNull(selectingIndex)?.name)
    }

    // Other entities in team
    val otherEntitiesInTeam = display.entities
        .asSequence()
        .filterIndexed { index, _ -> selectingIndex != index }
        .map { it?.name }
        .toList()
    // Available entity list
    val entityList: MutableList<String?> = availableEntities
        .asSequence()
        .map { it.entityName }
        .filter { it !in otherEntitiesInTeam }
        .toMutableList()

    // Check if it's the last entity in an activated team.
    val emptyNotAllowed = display.isActivated && otherEntitiesInTeam.all { it == null }

    if (!emptyNotAllowed) {
        entityList.add(0, null)
    }

    AppAlertDialog(
        modifier = modifier,
        title = stringResource(Res.string.game_select_entity),
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                item {
                    AppRadioGroup(
                        modifier = Modifier.fillMaxWidth(),
                        items = entityList,
                        selectedIndex = entityList.indexOf(selectedEntity),
                        itemLabel = {
                            it ?: stringResource(Res.string.empty_slot)
                        },
                        onSelected = {
                            selectedEntity = entityList[it]
                        },
                    )
                }
            }
        },
        onDismiss = onDismiss,
        onConfirmed = { onSelected(selectedEntity) },
    )
}