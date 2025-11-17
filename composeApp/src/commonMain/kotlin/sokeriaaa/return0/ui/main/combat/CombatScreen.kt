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
package sokeriaaa.return0.ui.main.combat

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.combat
import return0.composeapp.generated.resources.combat_defeat
import return0.composeapp.generated.resources.combat_victory
import return0.composeapp.generated.resources.ic_outline_autopause_24
import return0.composeapp.generated.resources.ic_outline_autoplay_24
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.mvi.intents.CombatIntent
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel
import sokeriaaa.return0.shared.data.models.combat.ArenaConfig
import sokeriaaa.return0.shared.data.models.combat.EnemyState
import sokeriaaa.return0.shared.data.models.combat.PartyState
import sokeriaaa.return0.shared.data.models.entity.EnemyData
import sokeriaaa.return0.shared.data.models.entity.EnemyRewardTable
import sokeriaaa.return0.shared.data.models.entity.EntityGrowth
import sokeriaaa.return0.shared.data.models.entity.PartyData
import sokeriaaa.return0.temp.TempData
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.entity.EntityHPBar

/**
 * The combat screen, for where the user to combat with bugs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombatScreen(
    viewModel: CombatViewModel = viewModel(
        factory = koinInject()
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    LaunchedEffect(viewModel) {
        // Start a testing combat.
        viewModel.onIntent(
            CombatIntent.Prepare(
                config = ArenaConfig(
                    parties = listOf(
                        PartyState(
                            partyData = PartyData(
                                entityData = TempData.objectEntity,
                                growth = EntityGrowth(
                                    atkGrowth = 0.25f,
                                    defGrowth = 0.25f,
                                    spdGrowth = 0.25f,
                                    hpGrowth = 0.25f,
                                    spGrowth = 0.25f,
                                )
                            ),
                            level = 100,
                        ),
                        PartyState(
                            partyData = PartyData(
                                entityData = TempData.iteratorEntity,
                                growth = EntityGrowth(
                                    atkGrowth = 0.25f,
                                    defGrowth = 0.25f,
                                    spdGrowth = 0.25f,
                                    hpGrowth = 0.25f,
                                    spGrowth = 0.25f,
                                )
                            ),
                            level = 100,
                        ),
                        PartyState(
                            partyData = PartyData(
                                entityData = TempData.systemEntity,
                                growth = EntityGrowth(
                                    atkGrowth = 0.25f,
                                    defGrowth = 0.25f,
                                    spdGrowth = 0.25f,
                                    hpGrowth = 0.25f,
                                    spGrowth = 0.25f,
                                )
                            ),
                            level = 100,
                        ),
                    ),
                    enemies = listOf(
                        EnemyState(
                            enemyData = EnemyData(
                                entityData = TempData.objectEntity,
                                growth = EntityGrowth(
                                    atkGrowth = 0.25f,
                                    defGrowth = 0.25f,
                                    spdGrowth = 0.25f,
                                    hpGrowth = 0.25f,
                                    spGrowth = 0.25f,
                                ),
                                rewardTable = EnemyRewardTable(
                                    token = 1,
                                    exp = 1,
                                )
                            ),
                            level = 100,
                        ),
                    )
                )
            )
        )
        // Start testing combat.
        viewModel.onIntent(CombatIntent.StartCombat)
    }
    AppScaffold(
        viewModel = viewModel,
        topBar = {
            // Hide the top bar when the screen is landscape.
            if (windowAdaptiveInfo.windowSizeClass.isHeightAtLeastBreakpoint(600)) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(
                                when (viewModel.combatStatus) {
                                    true -> Res.string.combat_victory
                                    false -> Res.string.combat_defeat
                                    null -> Res.string.combat
                                }
                            )
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
        ) {
            EnemyPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                viewModel = viewModel
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .padding(horizontal = 16.dp),
            ) {
                val lazyListState = rememberLazyListState()
                CombatLogList(
                    modifier = Modifier.fillMaxSize(),
                    lazyListState = lazyListState,
                    logList = viewModel.logList,
                    entities = viewModel.entities,
                    verticalArrangement = Arrangement.Bottom,
                    userScrollEnabled = false
                )
                // The auto-combat button.
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(all = 8.dp),
                    onClick = { viewModel.onIntent(CombatIntent.ToggleAutoCombat) },
                ) {
                    Crossfade(viewModel.isAutoCombat) { isAutoCombat ->
                        Icon(
                            painter = painterResource(
                                if (isAutoCombat) {
                                    Res.drawable.ic_outline_autopause_24
                                } else {
                                    Res.drawable.ic_outline_autoplay_24
                                }
                            ),
                            contentDescription = "Auto-combat",
                        )
                    }
                }
            }
            CombatActionPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                viewModel = viewModel,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
            PartyPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                viewModel = viewModel,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
    }
}

/**
 * The panel shows all enemies.
 */
@Composable
private fun EnemyPanel(
    modifier: Modifier = Modifier,
    viewModel: CombatViewModel,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 160.dp),
        state = rememberLazyGridState(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        items(
            items = viewModel.enemies,
        ) { enemy ->
            EntityItem(
                modifier = Modifier.width(140.dp),
                entity = enemy,
                isWaitingAction = false,
            )
        }
    }
}

/**
 * The panel shows all parties.
 */
@Composable
private fun PartyPanel(
    modifier: Modifier = Modifier,
    viewModel: CombatViewModel,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(840)) {
            // Flat to a single row when the screen is wide enough.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                repeat(4) { index ->
                    val party = viewModel.parties.getOrNull(index)
                    if (party != null) {
                        EntityItem(
                            modifier = Modifier
                                .weight(1f),
                            entity = party,
                            isWaitingAction = viewModel.entitySelecting == party
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f),
                        )
                    }
                }
            }
        } else {
            // For narrow screens, use two rows.
            repeat(2) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    repeat(2) { col ->
                        val index = row * 2 + col
                        val party = viewModel.parties.getOrNull(index)
                        if (party != null) {
                            EntityItem(
                                modifier = Modifier
                                    .weight(1f),
                                entity = party,
                                isWaitingAction = viewModel.entitySelecting == party
                            )
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f),
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Entity item for both parties and enemies.
 */
@Composable
private fun EntityItem(
    modifier: Modifier = Modifier,
    entity: Entity,
    isWaitingAction: Boolean,
) {
    OutlinedCard(
        modifier = modifier,
        border = if (isWaitingAction) {
            BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
        } else {
            CardDefaults.outlinedCardBorder()
        },
    ) {
        val apProgress = entity.ap / entity.maxap
        val isFailed = entity.isFailed()
        val animatedAlpha = if (isFailed) {
            0.4F
        } else {
            1F
        }
        val color = MaterialTheme.colorScheme.primary.copy(
            alpha = if (isWaitingAction) {
                0.24F
            } else {
                0.08f
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    if (!isFailed) {
                        val w = size.width * apProgress
                        val h = size.height
                        // Draw AP progress on the background
                        drawRect(
                            color = color,
                            topLeft = Offset(0f, 0f),
                            size = Size(w, h)
                        )
                    }
                }
                .padding(all = 8.dp)
                .alpha(animatedAlpha),
        ) {
            val animatedHP by animateIntAsState(
                targetValue = entity.hp,
                label = "EntityItemHP",
            )
            val animatedSP by animateIntAsState(
                targetValue = entity.sp,
                label = "EntityItemHP",
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.weight(1F),
                    text = entity.name
                )
                Text(
                    text = "Lv.${entity.level}",
                )
            }
            EntityHPBar(
                modifier = Modifier.fillMaxWidth(),
                label = "HP",
                current = animatedHP,
                max = entity.maxhp,
            )
            EntityHPBar(
                modifier = Modifier.fillMaxWidth(),
                label = "SP",
                current = animatedSP,
                max = entity.maxsp,
            )
        }
    }
}