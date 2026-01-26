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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.component_extra_empty
import return0.composeapp.generated.resources.game_entity_function_specials
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
import sokeriaaa.return0.ui.common.widgets.OutlinedEmojiHeader
import sokeriaaa.return0.ui.common.widgets.TextItem

@Composable
fun EntityPluginDisplay(
    modifier: Modifier = Modifier,
    plugin: PluginInfo,
    entityPath: EntityPath,
) {
    val isIdenticalPath = entityPath == plugin.data.path
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Plugin name & path
            PluginHeader(
                modifier = Modifier.fillMaxWidth(),
                name = plugin.name,
                path = plugin.data.path,
            )
            // Constants
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                plugin.constMap.forEach { entry ->
                    ConstantEntry(
                        modifier = Modifier.padding(all = 2.dp),
                        constType = entry.key,
                        tier = entry.value,
                    )
                }
            }
            // Different path warning
            if (!isIdenticalPath) {
                DifferentPathWarning()
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
}

@Composable
private fun PluginHeader(
    modifier: Modifier = Modifier,
    name: String,
    path: EntityPath,
) {
    OutlinedEmojiHeader(
        modifier = modifier,
        emoji = path.icon,
        label = name,
        supportingText = path.name
    )
}

@Composable
private fun DifferentPathWarning(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
            text = stringResource(Res.string.game_plugin_warn_different_path),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun ConstantEntry(
    modifier: Modifier = Modifier,
    constType: PluginConst,
    tier: Int,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
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
            },
            style = MaterialTheme.typography.labelSmall,
        )
    }
}


// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun PluginHeaderExample1() {
    PluginHeader(
        name = "Example Plugin I",
        path = EntityPath.THREAD,
    )
}

@Preview
@Composable
private fun DifferentPathWarningExample() {
    DifferentPathWarning()
}

@Preview
@Composable
private fun ConstantEntryExample1() {
    ConstantEntry(
        constType = PluginConst.ATK,
        tier = 42,
    )
}

@Preview
@Composable
private fun ConstantEntryExample2() {
    ConstantEntry(
        constType = PluginConst.DEF,
        tier = 24,
    )
}

@Preview
@Composable
private fun ConstantEntryExample3() {
    ConstantEntry(
        constType = PluginConst.CRIT_DMG,
        tier = 88,
    )
}