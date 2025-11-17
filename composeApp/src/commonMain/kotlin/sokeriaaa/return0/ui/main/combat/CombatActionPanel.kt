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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.combat_choose_action
import return0.composeapp.generated.resources.combat_choose_action_attack
import return0.composeapp.generated.resources.combat_choose_action_back
import return0.composeapp.generated.resources.combat_choose_action_defend
import return0.composeapp.generated.resources.combat_choose_action_execute
import return0.composeapp.generated.resources.combat_choose_action_function
import return0.composeapp.generated.resources.combat_choose_action_item
import return0.composeapp.generated.resources.combat_choose_action_relax
import return0.composeapp.generated.resources.combat_choose_action_target_selected_count
import return0.composeapp.generated.resources.ic_baseline_check_24
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.mvi.intents.CombatIntent
import sokeriaaa.return0.mvi.viewmodels.CombatViewModel
import sokeriaaa.return0.ui.common.widgets.AppTextButton


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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(
                    resource = Res.string.combat_choose_action,
                    /* entity = */ entitySelecting?.name ?: ""
                )
            )
            @Composable
            fun RowScope.Part1() {
                AppTextButton(
                    modifier = Modifier.weight(1F),
                    text = stringResource(Res.string.combat_choose_action_attack),
                    onClick = {
                        viewModel.onIntent(CombatIntent.ChooseAction(entitySelecting!!.attackAction))
                    },
                )
                AppTextButton(
                    modifier = Modifier.weight(1F),
                    text = stringResource(Res.string.combat_choose_action_function),
                    enabled = !entitySelecting?.functions.isNullOrEmpty(),
                    onClick = {
                        isSelectingFunction = true
                    },
                )
            }

            @Composable
            fun RowScope.Part2() {
                AppTextButton(
                    modifier = Modifier.weight(1F),
                    text = stringResource(Res.string.combat_choose_action_defend),
                    onClick = {
                        viewModel.onIntent(CombatIntent.ChooseAction(entitySelecting!!.defendAction))
                    },
                )
                AppTextButton(
                    modifier = Modifier.weight(1F),
                    text = stringResource(Res.string.combat_choose_action_relax),
                    onClick = {
                        viewModel.onIntent(CombatIntent.ChooseAction(entitySelecting!!.relaxAction))
                    },
                )
                AppTextButton(
                    modifier = Modifier.weight(1F),
                    text = stringResource(Res.string.combat_choose_action_item),
                    onClick = {
                        // TODO Not implemented yet.
                    },
                )
            }
            if (windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(600)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Part1()
                    Part2()
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Part1()
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Part2()
                }
            }
        }
    }
    // Function selection
    AnimatedVisibility(
        modifier = modifier,
        visible = status == ActionPanelStatus.FUNCTION,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            entitySelecting?.functions?.forEach {
                val isSufficient: Boolean = it.user.sp >= it.spCost
                FunctionItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = isSufficient) {
                            viewModel.onIntent(CombatIntent.ChooseAction(it))
                            isSelectingFunction = false
                        }
                        .alpha(if (isSufficient) 1f else 0.4F)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    function = it,
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                AppTextButton(
                    modifier = Modifier.weight(1F),
                    text = stringResource(Res.string.combat_choose_action_back),
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
            AppTextButton(
                modifier = Modifier.weight(1F),
                text = stringResource(Res.string.combat_choose_action_back),
                onClick = onBack
            )
            if (isSingleTarget) {
                // When the function only require a single target,
                // hide the execute button.
                // The target will be submitted directly in filter chips.
                Spacer(modifier = Modifier.weight(1F))
            } else {
                // Execute button.
                AppTextButton(
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
                    painter = painterResource(Res.drawable.ic_baseline_check_24),
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
 * Function item.
 */
@Composable
private fun FunctionItem(
    modifier: Modifier = Modifier,
    function: Skill,
) {
    Row(
        modifier = modifier,
    ) {
        Text(text = function.name)
        Spacer(modifier = Modifier.weight(1F))
        Text(text = function.spCost.toString())
    }
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