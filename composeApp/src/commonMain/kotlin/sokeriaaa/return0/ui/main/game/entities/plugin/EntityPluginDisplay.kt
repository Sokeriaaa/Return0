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
package sokeriaaa.return0.ui.main.game.entities.plugin

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.component_extra_empty
import return0.composeapp.generated.resources.game_entity_function_specials
import return0.composeapp.generated.resources.game_plugin_const
import return0.composeapp.generated.resources.game_plugin_special_attack_rate_offset
import return0.composeapp.generated.resources.game_plugin_special_defend_rate_offset
import return0.composeapp.generated.resources.game_plugin_special_on_attack
import return0.composeapp.generated.resources.game_plugin_special_on_defend
import return0.composeapp.generated.resources.game_plugin_warn_different_path
import return0.composeapp.generated.resources.status_atk
import return0.composeapp.generated.resources.status_crit_dmg
import return0.composeapp.generated.resources.status_crit_rate
import return0.composeapp.generated.resources.status_def
import return0.composeapp.generated.resources.status_hid_rate
import return0.composeapp.generated.resources.status_hp
import return0.composeapp.generated.resources.status_sp
import return0.composeapp.generated.resources.status_spd
import return0.composeapp.generated.resources.status_tgt_rate
import sokeriaaa.return0.models.component.res.extra.extraResource
import sokeriaaa.return0.models.component.res.value.valueResource
import sokeriaaa.return0.models.entity.plugin.display.PluginInfo
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst
import sokeriaaa.return0.ui.common.widgets.OutlinedEmojiCard
import sokeriaaa.return0.ui.common.widgets.TextItem

@Composable
fun EntityPluginDisplay(
    modifier: Modifier = Modifier,
    plugin: PluginInfo,
    entityPath: EntityPath,
) {
    Column(
        modifier = modifier,
    ) {
        val isIdenticalPath = entityPath == plugin.data.path
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedEmojiCard(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(32.dp),
                emoji = plugin.data.path.icon,
            )
            Text(
                modifier = Modifier
                    .weight(1F)
                    .basicMarquee(iterations = Int.MAX_VALUE),
                text = plugin.name,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        // Warn
        if (!isIdenticalPath) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                text = stringResource(Res.string.game_plugin_warn_different_path),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
        // Description
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            text = buildAnnotatedString {
                if (isIdenticalPath) {
                    append(plugin.description)
                } else {
                    withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                        append(plugin.description)
                    }
                }
            },
            style = MaterialTheme.typography.bodySmall,
        )
        // Const
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            text = stringResource(Res.string.game_plugin_const),
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
        ) {
            plugin.constMap.forEach { entry ->
                ConstantEntry(
                    modifier = Modifier.padding(all = 2.dp),
                    constType = entry.key,
                    tier = entry.value,
                )
            }
        }
        // Special
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
            text = stringResource(Res.string.game_entity_function_specials),
        )
        var hasSpecial = false
        if (plugin.data.onAttack != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    if (isIdenticalPath) {
                        append(stringResource(Res.string.game_plugin_special_on_attack))
                        append('\n')
                        append(extraResource(plugin.data.onAttack))
                    } else {
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            append(stringResource(Res.string.game_plugin_special_on_attack))
                            append('\n')
                            append(extraResource(plugin.data.onAttack))
                        }
                    }
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (plugin.data.onDefend != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    if (isIdenticalPath) {
                        append(stringResource(Res.string.game_plugin_special_on_defend))
                        append('\n')
                        append(extraResource(plugin.data.onDefend))
                    } else {
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            append(stringResource(Res.string.game_plugin_special_on_defend))
                            append('\n')
                            append(extraResource(plugin.data.onDefend))
                        }
                    }
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (plugin.data.attackRateOffset != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    if (isIdenticalPath) {
                        append(stringResource(Res.string.game_plugin_special_attack_rate_offset))
                        append(valueResource(plugin.data.attackRateOffset))
                    } else {
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            append(stringResource(Res.string.game_plugin_special_attack_rate_offset))
                            append(valueResource(plugin.data.attackRateOffset))
                        }
                    }
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (plugin.data.defendRateOffset != null) {
            hasSpecial = true
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    if (isIdenticalPath) {
                        append(stringResource(Res.string.game_plugin_special_defend_rate_offset))
                        append(valueResource(plugin.data.defendRateOffset))
                    } else {
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            append(stringResource(Res.string.game_plugin_special_defend_rate_offset))
                            append(valueResource(plugin.data.defendRateOffset))
                        }
                    }
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


@Composable
private fun ConstantEntry(
    modifier: Modifier = Modifier,
    constType: PluginConst,
    tier: Int,
) {
    Card(modifier = modifier) {
        Text(
            modifier = Modifier.padding(all = 8.dp),
            text = buildString {
                append(
                    stringResource(
                        when (constType) {
                            PluginConst.ATK -> Res.string.status_atk
                            PluginConst.DEF -> Res.string.status_def
                            PluginConst.SPD -> Res.string.status_spd
                            PluginConst.HP -> Res.string.status_hp
                            PluginConst.SP -> Res.string.status_sp
                            PluginConst.CRIT_RATE -> Res.string.status_crit_rate
                            PluginConst.CRIT_DMG -> Res.string.status_crit_dmg
                            PluginConst.TGT_RATE -> Res.string.status_tgt_rate
                            PluginConst.HID_RATE -> Res.string.status_hid_rate
                        }
                    )
                )
                append(" +")
                append(tier)
                append("%")
            }
        )
    }
}