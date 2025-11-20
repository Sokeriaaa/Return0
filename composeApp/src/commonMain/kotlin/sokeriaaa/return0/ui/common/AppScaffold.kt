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
package sokeriaaa.return0.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import sokeriaaa.return0.mvi.viewmodels.BaseViewModel

/**
 * Base Scaffold for the app.
 */
@Composable
fun <VM : BaseViewModel> AppScaffold(
    modifier: Modifier = Modifier,
    viewModel: VM,
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    // Listen to the snack bar intent.
    LaunchedEffect(Unit) {
        viewModel.snackBarIntents.collect { intent ->
            snackBarHostState.showSnackbar(intent.message)
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        if (viewModel.isLoading) {
            // Show a loading dialog.
            Dialog(onDismissRequest = {}) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(
                        all = 16.dp,
                    ),
                )
            }
        }
        content(it)
    }
}