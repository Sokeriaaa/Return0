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
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.entity.EntityHPBar
import sokeriaaa.return0.ui.main.combat.animation.EntityAnimator
import sokeriaaa.return0.ui.main.combat.animation.EntityAnimatorManager

/**
 * The combat screen, for where the user to combat with bugs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombatScreen(
    viewModel: CombatViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    LaunchedEffect(Unit) {
        // Start combat.
        viewModel.onIntent(CombatIntent.StartCombat)
    }
    AppScaffold(
        viewModel = viewModel,
        topBar = {
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
                    // User can scroll the combat log after the combat is finished.
                    userScrollEnabled = viewModel.combatStatus != null
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
    val animatorManager: EntityAnimatorManager = koinInject()
    val scope = rememberCoroutineScope()

    var glowJob by remember { mutableStateOf<Job?>(null) }
    var glowColor by remember { mutableStateOf<Color?>(null) }
    val glowAlpha = remember { Animatable(0f) }

    var shakeJob by remember { mutableStateOf<Job?>(null) }
    val shakeOffset = remember { Animatable(0f) }

    val floatingTexts = remember { mutableStateSetOf<EntityAnimator.FloatingText>() }
    // Collect animations.
    LaunchedEffect(Unit) {
        animatorManager.entityAnimators
            .map { it[entity.index] }
            .collect { animators ->
                animators?.forEach {
                    when (it) {
                        EntityAnimator.Shake -> {
                            shakeJob?.cancel()
                            shakeJob = scope.launch {
                                // Animate shake.
                                repeat(3) {
                                    shakeOffset.animateTo(4f, tween(30))
                                    shakeOffset.animateTo(-4f, tween(30))
                                }
                                shakeOffset.animateTo(0f)
                            }
                            scope.launch {
                                // Clear after finished.
                                delay(500)
                                animatorManager.clearAnimator(entity.index, it)
                            }
                        }

                        is EntityAnimator.Glow -> {
                            glowJob?.cancel()
                            glowJob = scope.launch {
                                // Animate glow.
                                glowColor = it.color
                                glowAlpha.snapTo(0.1f)
                                glowAlpha.animateTo(0f, tween(300))
                            }
                            scope.launch {
                                // Clear after finished.
                                delay(500)
                                animatorManager.clearAnimator(entity.index, it)
                                glowColor = null
                            }
                        }

                        is EntityAnimator.FloatingText -> {
                            floatingTexts += it
                        }
                    }
                }
            }
    }
    Box(
        modifier = modifier,
    ) {
        // Entity card
        OutlinedCard(
            modifier = Modifier
                // Animate: Shake
                .graphicsLayer { translationX = shakeOffset.value },
            border = if (isWaitingAction) {
                BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
            } else {
                CardDefaults.outlinedCardBorder()
            },
        ) {
            val apProgress = entity.ap / entity.maxap
            val isFailed = entity.isFailedFlag
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
                        // Animate: Glow
                        glowColor?.let {
                            if (glowAlpha.value > 0f) {
                                drawRect(
                                    color = it,
                                    alpha = glowAlpha.value,
                                )
                            }
                        }
                        // Action bar
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
        // Floating texts (Damage/Heal)
        floatingTexts.forEach { text ->
            key(text.id) {
                val anim = remember { Animatable(0F) }
                LaunchedEffect(text.id) {
                    anim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(1000)
                    )
                    floatingTexts.remove(text)
                    animatorManager.clearAnimator(entity.index, text)
                }
                // Individual parabolic animation
                // Define a simple parabolic path
                val x = text.xOffset * anim.value
                val y = text.yOffset * (1 - (anim.value - 0.5f) * (anim.value - 0.5f)) + 15F
                // Draw the floating text
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .graphicsLayer {
                            translationX = x
                            translationY = y
                            alpha = 1f - anim.value
                        },
                    text = text.text,
                    color = text.color,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}