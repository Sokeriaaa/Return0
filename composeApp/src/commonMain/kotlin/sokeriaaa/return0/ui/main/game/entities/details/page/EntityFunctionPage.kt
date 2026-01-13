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
package sokeriaaa.return0.ui.main.game.entities.details.page

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.component_extra_empty
import return0.composeapp.generated.resources.game_entity_function_max_tier
import return0.composeapp.generated.resources.game_entity_function_specials
import return0.composeapp.generated.resources.game_entity_function_specials_actual_power
import return0.composeapp.generated.resources.game_entity_function_specials_atk_override
import return0.composeapp.generated.resources.game_entity_function_specials_attack_times
import return0.composeapp.generated.resources.game_entity_function_specials_bullseye
import return0.composeapp.generated.resources.game_entity_function_specials_critical_dmg_offset
import return0.composeapp.generated.resources.game_entity_function_specials_critical_rate_offset
import return0.composeapp.generated.resources.game_entity_function_specials_def_override
import return0.composeapp.generated.resources.game_entity_function_specials_extra
import return0.composeapp.generated.resources.game_entity_function_specials_ignores_shields
import return0.composeapp.generated.resources.game_entity_function_specials_target_rate_offset
import return0.composeapp.generated.resources.game_entity_function_summary
import return0.composeapp.generated.resources.game_entity_function_unlock_at
import return0.composeapp.generated.resources.game_entity_function_upgrade_at
import return0.composeapp.generated.resources.game_select_function
import sokeriaaa.return0.models.component.res.condition.conditionResource
import sokeriaaa.return0.models.component.res.extra.extraResource
import sokeriaaa.return0.models.component.res.value.valueResource
import sokeriaaa.return0.models.entity.display.ExtendedEntityProfile
import sokeriaaa.return0.ui.common.widgets.OutlinedEmojiCard
import sokeriaaa.return0.ui.common.widgets.TextItem

@Composable
fun EntityFunctionPage(
    modifier: Modifier = Modifier,
    functions: List<ExtendedEntityProfile.Skill>,
) {
    var selectedSkill: ExtendedEntityProfile.Skill? by remember { mutableStateOf(null) }
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(160.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(6.dp))
        }
        items(items = functions) { function ->
            FunctionCard(
                modifier = Modifier.padding(all = 2.dp),
                skill = function,
                onSelected = { selectedSkill = it }
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            val skill = selectedSkill
            if (skill == null) {
                Text(
                    modifier = Modifier.padding(start = 2.dp, end = 2.dp, top = 4.dp),
                    text = stringResource(Res.string.game_select_function)
                )
            } else {
                FunctionDetails(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    skill = skill,
                )
            }
        }
    }
}

@Composable
private fun FunctionCard(
    modifier: Modifier = Modifier,
    skill: ExtendedEntityProfile.Skill,
    onSelected: (ExtendedEntityProfile.Skill) -> Unit,
) {
    OutlinedCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .alpha(if (skill.tier > 0) 1F else 0.4F)
                .clickable { onSelected(skill) },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .width(32.dp)
                    .padding(start = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Category icon
                Text(
                    text = skill.category.icon,
                    style = MaterialTheme.typography.bodyLarge,
                )
                // Tier
                Text(
                    text = skill.tier.toString(),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(
                        start = 10.dp,
                        end = 8.dp,
                        top = 6.dp,
                        bottom = 6.dp,
                    ),
                horizontalAlignment = Alignment.Start,
            ) {
                // Name
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = skill.name,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    // SP cost
                    Text(
                        modifier = Modifier.weight(1F),
                        text = "Power:${skill.power}",
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                    )
                    // SP cost
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = "${skill.spCost}SP",
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun FunctionDetails(
    modifier: Modifier = Modifier,
    skill: ExtendedEntityProfile.Skill
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedEmojiCard(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(32.dp),
                emoji = skill.category.icon,
            )
            Text(
                modifier = Modifier
                    .weight(1F)
                    .basicMarquee(iterations = Int.MAX_VALUE),
                text = skill.name,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        // Unlock/Upgrade
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            text = when {
                skill.tier >= (skill.data.growth?.size ?: 0) -> stringResource(
                    resource = Res.string.game_entity_function_max_tier
                )

                skill.tier == 0 -> stringResource(
                    resource = Res.string.game_entity_function_unlock_at,
                    /* level = */ skill.data.growth?.firstOrNull().toString()
                )

                else -> stringResource(
                    resource = Res.string.game_entity_function_upgrade_at,
                    /* level = */ skill.data.growth?.getOrNull(skill.tier).toString()
                )
            },
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
        )
        // Summary
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            text = stringResource(Res.string.game_entity_function_summary),
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            text = skill.description,
            style = MaterialTheme.typography.bodySmall,
        )
        // Special
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
            text = stringResource(Res.string.game_entity_function_specials),
        )
        var hasSpecial = false
        if (skill.data.bullseye) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.game_entity_function_specials_bullseye),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (skill.data.attackModifier?.attackTimes != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.game_entity_function_specials_attack_times))
                    append('\n')
                    append(valueResource(skill.data.attackModifier.attackTimes))
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (skill.data.attackModifier?.actualPower != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.game_entity_function_specials_actual_power))
                    append('\n')
                    append(valueResource(skill.data.attackModifier.actualPower))
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (skill.data.attackModifier?.targetRateOffset != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.game_entity_function_specials_target_rate_offset))
                    append('\n')
                    append(valueResource(skill.data.attackModifier.targetRateOffset))
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (skill.data.attackModifier?.criticalRateOffset != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.game_entity_function_specials_critical_rate_offset))
                    append('\n')
                    append(valueResource(skill.data.attackModifier.criticalRateOffset))
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (skill.data.attackModifier?.criticalDMGOffset != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.game_entity_function_specials_critical_dmg_offset))
                    append('\n')
                    append(valueResource(skill.data.attackModifier.criticalDMGOffset))
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (skill.data.attackModifier?.userBaseATKOverride != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.game_entity_function_specials_atk_override))
                    append('\n')
                    append(valueResource(skill.data.attackModifier.userBaseATKOverride))
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (skill.data.attackModifier?.targetBaseDEFOverride != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.game_entity_function_specials_def_override))
                    append('\n')
                    append(valueResource(skill.data.attackModifier.targetBaseDEFOverride))
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (skill.data.attackModifier?.ignoresShields != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.game_entity_function_specials_ignores_shields))
                    append('\n')
                    append(conditionResource(skill.data.attackModifier.ignoresShields))
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (skill.data.extra != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.game_entity_function_specials_extra))
                    append('\n')
                    append(extraResource(skill.data.extra))
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (!hasSpecial) {
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.component_extra_empty),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}