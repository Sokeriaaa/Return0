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
package sokeriaaa.return0.ui.main.game.entities.details

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.status_ap
import return0.composeapp.generated.resources.status_atk
import return0.composeapp.generated.resources.status_def
import return0.composeapp.generated.resources.status_hp
import return0.composeapp.generated.resources.status_sp
import return0.composeapp.generated.resources.status_spd
import sokeriaaa.return0.models.entity.display.ExtendedEntityProfile
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.viewmodels.EntityDetailsViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.entity.EntityExpCircularIndicator
import sokeriaaa.return0.ui.common.entity.EntityHPBar
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton
import sokeriaaa.return0.ui.common.widgets.OutlinedEmojiCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityDetailsScreen(
    entityName: String,
    viewModel: EntityDetailsViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
        key = "EntityDetailsViewModel-$entityName",
        extras = MutableCreationExtras().apply {
            this[EntityDetailsViewModel.entityNameKey] = entityName
        }
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
                title = { Text(viewModel.entityName) },
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
        val entity = viewModel.entityProfile ?: return@AppScaffold
        if (windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(600)) {
            Row(modifier = Modifier.padding(paddingValues = paddingValues)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(360.dp)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            EntityNamePart(
                                modifier = Modifier
                                    .weight(1F)
                                    .basicMarquee(iterations = Int.MAX_VALUE),
                                entity = entity,
                            )
                            EntityPathCategoryPart(
                                modifier = Modifier.padding(start = 4.dp),
                                entity = entity,
                            )
                        }
                    }
                    item {
                        EntityExpPart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            entity = entity,
                        )
                    }
                    item {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        )
                    }
                    item {
                        EntityStatusPart(
                            modifier = Modifier.fillMaxWidth(),
                            entity = entity,
                        )
                    }
                }
                EntityMainPart(modifier = Modifier.weight(1F), entity = entity)
            }
        } else {
            Column(
                modifier = Modifier.padding(paddingValues = paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3F)
                            .padding(horizontal = 16.dp),
                    ) {
                        EntityNamePart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .basicMarquee(iterations = Int.MAX_VALUE),
                            entity = entity,
                        )
                        EntityPathCategoryPart(
                            modifier = Modifier.fillMaxWidth(),
                            entity = entity,
                        )
                        EntityExpPart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            entity = entity,
                        )
                    }
                    EntityStatusPart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2F)
                            .padding(horizontal = 16.dp),
                        entity = entity,
                    )
                }
                EntityMainPart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                        .padding(horizontal = 16.dp),
                    entity = entity,
                )
            }
        }
    }
}

@Composable
private fun EntityNamePart(
    modifier: Modifier = Modifier,
    entity: ExtendedEntityProfile,
) {
    Text(
        modifier = modifier,
        text = entity.name,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun EntityPathCategoryPart(
    modifier: Modifier = Modifier,
    entity: ExtendedEntityProfile,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedEmojiCard(
            modifier = Modifier.size(32.dp),
            emoji = entity.category.icon,
        )
        entity.category2?.let {
            OutlinedEmojiCard(
                modifier = Modifier.size(32.dp).padding(start = 4.dp),
                emoji = it.icon,
            )
        }
        VerticalDivider(modifier = Modifier.height(32.dp).padding(horizontal = 4.dp))
        OutlinedEmojiCard(
            modifier = Modifier.size(32.dp),
            emoji = entity.path.icon,
        )
    }
}

@Composable
private fun EntityExpPart(
    modifier: Modifier = Modifier,
    entity: ExtendedEntityProfile,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EntityExpCircularIndicator(
            level = entity.level,
            progress = { entity.expProgress },
        )
        Spacer(modifier = Modifier.weight(1F))
        Column(
            modifier = Modifier.width(192.dp),
        ) {
            // HP
            EntityHPBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp),
                label = "HP",
                current = entity.hp,
                max = entity.maxHP,
                valueStyle = EntityHPBar.ValueStyle.FULL,
                style = MaterialTheme.typography.labelMedium,
            )
            // SP
            EntityHPBar(
                modifier = Modifier.fillMaxWidth(),
                label = "SP",
                current = entity.sp,
                max = entity.maxSP,
                valueStyle = EntityHPBar.ValueStyle.FULL,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun EntityStatusPart(
    modifier: Modifier = Modifier,
    entity: ExtendedEntityProfile,
) {
    Column(
        modifier = modifier,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.status_atk))
            Spacer(modifier = Modifier.weight(1F))
            Text(entity.atk.toString())
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.status_def))
            Spacer(modifier = Modifier.weight(1F))
            Text(entity.def.toString())
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.status_spd))
            Spacer(modifier = Modifier.weight(1F))
            Text(entity.spd.toString())
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.status_hp))
            Spacer(modifier = Modifier.weight(1F))
            Text(entity.maxHP.toString())
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.status_sp))
            Spacer(modifier = Modifier.weight(1F))
            Text(entity.maxSP.toString())
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.status_ap))
            Spacer(modifier = Modifier.weight(1F))
            Text(entity.maxAP.toString())
        }
    }
}

@Composable
private fun EntityMainPart(
    modifier: Modifier = Modifier,
    entity: ExtendedEntityProfile,
) {

}