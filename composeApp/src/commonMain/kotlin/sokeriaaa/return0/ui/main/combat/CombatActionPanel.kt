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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.back
import return0.composeapp.generated.resources.combat_choose_action
import return0.composeapp.generated.resources.combat_choose_action_attack
import return0.composeapp.generated.resources.combat_choose_action_defend
import return0.composeapp.generated.resources.combat_choose_action_escape
import return0.composeapp.generated.resources.combat_choose_action_execute
import return0.composeapp.generated.resources.combat_choose_action_function
import return0.composeapp.generated.resources.combat_choose_action_item
import return0.composeapp.generated.resources.combat_choose_action_relax
import return0.composeapp.generated.resources.combat_choose_action_target_selected_count
import return0.composeapp.generated.resources.ic_outline_arrow_back_24
import return0.composeapp.generated.resources.ic_outline_check_24
import return0.composeapp.generated.resources.ic_outline_code_24
import return0.composeapp.generated.resources.ic_outline_door_open_24
import return0.composeapp.generated.resources.ic_outline_relax_24
import return0.composeapp.generated.resources.ic_outline_security_24
import return0.composeapp.generated.resources.ic_outline_swords_24
import return0.composeapp.generated.resources.ic_outline_terminal_24
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.mvi.intents.CombatIntent
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel
import sokeriaaa.return0.ui.common.entity.function.EntityFunctionCard
import sokeriaaa.sugarkane.compose.widgets.button.AppFilledTonalButton
import sokeriaaa.sugarkane.compose.widgets.button.AppOutlinedButton


/**
 * Action selection panel for the combat screen.
 */
@Composable
fun CombatActionPanel(
    modifier: Modifier = Modifier,
    viewModel: CombatViewModel,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    val entitySelecting = viewModel.entitySelecting
    val availableTargets = viewModel.availableTargets
    var isSelectingFunction by remember { mutableStateOf(false) }
    val status: ActionPanelStatus = when {
        isSelectingFunction -> ActionPanelStatus.FUNCTION
        availableTargets != null -> ActionPanelStatus.TARGET
        entitySelecting != null -> ActionPanelStatus.ACTION
        else -> ActionPanelStatus.HIDE
    }
    // Action selection
    AnimatedVisibility(
        modifier = modifier,
        visible = status == ActionPanelStatus.ACTION,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                stringResource(
                    resource = Res.string.combat_choose_action,
                    /* entity = */ entitySelecting?.name ?: ""
                )
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                AppFilledTonalButton(
                    text = stringResource(Res.string.combat_choose_action_attack),
                    iconRes = Res.drawable.ic_outline_swords_24,
                    onClick = {
                        entitySelecting?.attackAction?.let {
                            viewModel.onIntent(CombatIntent.ChooseAction(it))
                        }
                    },
                )
                AppFilledTonalButton(
                    text = stringResource(Res.string.combat_choose_action_defend),
                    iconRes = Res.drawable.ic_outline_security_24,
                    onClick = {
                        entitySelecting?.defendAction?.let {
                            viewModel.onIntent(CombatIntent.ChooseAction(it))
                        }
                    },
                )
                AppFilledTonalButton(
                    text = stringResource(Res.string.combat_choose_action_relax),
                    iconRes = Res.drawable.ic_outline_relax_24,
                    onClick = {
                        entitySelecting?.relaxAction?.let {
                            viewModel.onIntent(CombatIntent.ChooseAction(it))
                        }
                    },
                )
                AppFilledTonalButton(
                    text = stringResource(Res.string.combat_choose_action_function),
                    iconRes = Res.drawable.ic_outline_code_24,
                    enabled = !entitySelecting?.functions.isNullOrEmpty(),
                    onClick = {
                        isSelectingFunction = true
                    },
                )
                AppFilledTonalButton(
                    text = stringResource(Res.string.combat_choose_action_item),
                    iconRes = Res.drawable.ic_outline_terminal_24,
                    // TODO Not implemented yet.
                    enabled = false,
                    onClick = {},
                )
                AppFilledTonalButton(
                    text = stringResource(Res.string.combat_choose_action_escape),
                    iconRes = Res.drawable.ic_outline_door_open_24,
                    // TODO Not implemented yet.
                    enabled = false,
                    onClick = {},
                )
            }
        }
    }
    // Function selection
    AnimatedVisibility(
        modifier = modifier,
        visible = status == ActionPanelStatus.FUNCTION,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            entitySelecting?.functions?.let { items ->
                items(items = items) {
                    val isSufficient: Boolean = it.user.sp >= it.spCost
                    EntityFunctionCard(
                        modifier = Modifier.alpha(if (isSufficient) 1f else 0.4F),
                        name = it.name,
                        category = it.category,
                        tier = it.tier,
                        power = it.power,
                        spCost = it.spCost,
                        enabled = isSufficient,
                        onClick = {
                            viewModel.onIntent(CombatIntent.ChooseAction(it))
                            isSelectingFunction = false
                        }
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        AppOutlinedButton(
                            modifier = Modifier.weight(1F),
                            iconRes = Res.drawable.ic_outline_arrow_back_24,
                            text = stringResource(Res.string.back),
                            onClick = {
                                isSelectingFunction = false
                            }
                        )
                        Spacer(
                            modifier = Modifier.weight(1F),
                        )
                    }
                }
            }
        }
    }
    // Target selection
    AnimatedVisibility(
        modifier = modifier,
        visible = status == ActionPanelStatus.TARGET,
    ) {
        TargetSelection(
            modifier = Modifier.fillMaxWidth(),
            availableTargets = availableTargets,
            selectableCount = viewModel.selectableCount,
            onSubmit = {
                viewModel.onIntent(CombatIntent.ChooseTarget(it))
            },
            onBack = {
                viewModel.onIntent(CombatIntent.LeaveChooseTarget)
            },
        )
    }
}

/**
 * The target selection panel.
 */
@Composable
private fun TargetSelection(
    modifier: Modifier = Modifier,
    availableTargets: List<Entity>?,
    selectableCount: Int,
    onSubmit: (List<Entity>) -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = modifier) {
        // Selected entities.
        val selectedEntities = remember { mutableStateListOf<Entity>() }
        // This action only requires a single target.
        val isSingleTarget: Boolean = selectableCount <= 1
        // This action is ready to execute.
        val isReady: Boolean = selectedEntities.size == selectableCount

        // Handle select.
        fun handleSelect(entity: Entity) {
            when {
                isSingleTarget -> {
                    // When the function only require a single target,
                    // submit it directly.
                    onSubmit(listOf(entity))
                    selectedEntities.clear()
                }

                entity in selectedEntities -> {
                    // Remove selection.
                    selectedEntities.remove(entity)
                }

                !isReady -> {
                    // Add selection.
                    selectedEntities.add(entity)
                }
            }
        }

        // Target items.
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            availableTargets?.forEach { entity ->
                val selected = selectedEntities.contains(entity)
                TargetItem(
                    selected = selected,
                    enabled = selected || !isReady,
                    entity = entity,
                    onClick = { handleSelect(entity) }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            AppOutlinedButton(
                modifier = Modifier.weight(1F),
                iconRes = Res.drawable.ic_outline_arrow_back_24,
                text = stringResource(Res.string.back),
                onClick = onBack,
            )
            if (isSingleTarget) {
                // When the function only require a single target,
                // hide the execute button.
                // The target will be submitted directly in filter chips.
                Spacer(modifier = Modifier.weight(1F))
            } else {
                // Execute button.
                AppFilledTonalButton(
                    modifier = Modifier.weight(1F),
                    text = if (isReady) {
                        stringResource(Res.string.combat_choose_action_execute)
                    } else {
                        stringResource(
                            Res.string.combat_choose_action_target_selected_count,
                            selectedEntities.size,
                            selectableCount,
                        )
                    },
                    iconRes = Res.drawable.ic_outline_code_24,
                    onClick = {
                        onSubmit(selectedEntities)
                        selectedEntities.clear()
                    }
                )
            }
        }
    }
}

@Composable
private fun TargetItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    enabled: Boolean,
    entity: Entity,
    onClick: () -> Unit,
) {
    FilterChip(
        modifier = modifier,
        onClick = onClick,
        label = {
            val color = MaterialTheme.colorScheme.primary
            Box(
                modifier = Modifier.drawBehind {
                    val w = size.width * entity.hp / entity.maxhp
                    val h = 4F
                    // Draw a simple HP bar on the background
                    drawRect(
                        color = color,
                        topLeft = Offset(0f, size.height - h),
                        size = Size(w, h)
                    )
                },
            ) {
                Text(entity.name)
            }
        },
        selected = selected,
        enabled = enabled,
        leadingIcon = if (selected) {
            {
                Icon(
                    painter = painterResource(Res.drawable.ic_outline_check_24),
                    contentDescription = "Selected icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

/**
 * The current status for the [CombatActionPanel].
 */
private enum class ActionPanelStatus {
    /**
     * Hide.
     */
    HIDE,

    /**
     * Require the user to select action for a party.
     */
    ACTION,

    /**
     * Require the user to select a function.
     */
    FUNCTION,

    /**
     * Require the user to choose target(s) for an action.
     */
    TARGET,
}