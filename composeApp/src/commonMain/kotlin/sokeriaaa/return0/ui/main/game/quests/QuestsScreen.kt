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
package sokeriaaa.return0.ui.main.game.quests

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.game_menu_quests
import return0.composeapp.generated.resources.general_location
import return0.composeapp.generated.resources.ic_outline_assignment_24
import sokeriaaa.return0.models.story.quest.QuestDisplay
import sokeriaaa.return0.mvi.intents.CommonIntent
import sokeriaaa.return0.mvi.viewmodels.QuestsViewModel
import sokeriaaa.return0.ui.common.AppScaffold
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestsScreen(
    viewModel: QuestsViewModel = viewModel(
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
                        text = stringResource(Res.string.game_menu_quests)
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
        LazyColumn(
            modifier = Modifier.padding(paddingValues = paddingValues),
        ) {
            items(
                items = viewModel.activatedQuests
            ) {
                QuestItem(
                    modifier = Modifier.fillMaxWidth(),
                    quest = it,
                )
            }
        }
    }
}

@Composable
private fun QuestItem(
    modifier: Modifier = Modifier,
    quest: QuestDisplay,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                painter = painterResource(Res.drawable.ic_outline_assignment_24),
                contentDescription = null,
            )
        },
        overlineContent = quest.navigation?.let {
            {
                Text(
                    stringResource(
                        resource = Res.string.general_location,
                        /* file = */ it.first,
                        /* row = */ it.second,
                    )
                )
            }
        },
        headlineContent = { Text(quest.name) },
        supportingContent = { Text(quest.description) }
    )
}