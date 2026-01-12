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

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.koin.compose.koinInject
import sokeriaaa.return0.mvi.viewmodels.EntityDetailsViewModel

@Composable
fun EntityDetailsScreen(
    entityName: String,
    viewModel: EntityDetailsViewModel = viewModel(
        factory = koinInject(),
        viewModelStoreOwner = koinInject(),
        extras = MutableCreationExtras().apply {
            this[EntityDetailsViewModel.entityNameKey] = entityName
        }
    ),
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {

}

@Composable
private fun EntityNamePart(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {

    }
}

@Composable
private fun EntityStatusPart(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {

    }
}