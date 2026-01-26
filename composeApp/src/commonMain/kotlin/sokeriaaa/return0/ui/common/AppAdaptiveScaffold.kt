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
package sokeriaaa.return0.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.window.core.layout.WindowSizeClass
import sokeriaaa.return0.mvi.viewmodels.BaseViewModel

/**
 * Base adaptive Scaffold for the app.
 *
 * @param mainContent The main content.
 * @param paneContent The pane content displays when [state].isShowingPane is true.
 * @param panePlaceholder The placeholder displays when [state].isShowingPane is false.
 * @param mainContentWidth The width of main content. Only has effect on wide screens.
 */
@Composable
fun <VM : BaseViewModel> AppAdaptiveScaffold(
    modifier: Modifier = Modifier,
    viewModel: VM,
    state: AppAdaptiveScaffoldState,
    topBar: @Composable () -> Unit = {},
    mainContentWidth: Dp = 300.dp,
    mainContent: @Composable (BoxScope) -> Unit,
    paneContent: @Composable (BoxScope) -> Unit,
    panePlaceholder: @Composable (BoxScope) -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }
    // Listen to the snack bar intent.
    LaunchedEffect(Unit) {
        viewModel.snackBarIntents.collect { intent ->
            snackBarHostState.showSnackbar(intent.message)
        }
    }
    // Loading dialog
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
    if (state.isWideScreen) {
        // On a wide screen, show both two panes.
        Row(modifier = modifier) {
            Scaffold(
                modifier = Modifier.width(mainContentWidth),
                topBar = topBar,
                snackbarHost = { SnackbarHost(snackBarHostState) },
                content = { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues),
                        content = mainContent,
                    )
                },
            )
            Surface(
                modifier = Modifier.weight(1F),
                content = {
                    Crossfade(state.isShowingPane) { isShowingPane ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            content = if (isShowingPane) {
                                paneContent
                            } else {
                                panePlaceholder
                            }
                        )
                    }
                },
            )
        }
    } else {
        // Only show one pane.
        Scaffold(
            modifier = modifier,
            topBar = topBar,
            snackbarHost = { SnackbarHost(snackBarHostState) },
            content = { paddingValues ->
                Crossfade(state.isShowingPane) { isShowingPane ->
                    Box(
                        modifier = Modifier.padding(paddingValues),
                        content = if (isShowingPane) {
                            paneContent
                        } else {
                            mainContent
                        },
                    )
                }
            },
        )
    }
}

class AppAdaptiveScaffoldState internal constructor(
    val isWideScreen: Boolean
) {
    var isShowingPane: Boolean by mutableStateOf(false)
        private set

    fun showPane() {
        isShowingPane = true
    }

    fun hidePane() {
        isShowingPane = false
    }
}

@Composable
fun rememberAppAdaptiveScaffoldState(
    windowAdaptiveInfo: WindowAdaptiveInfo,
) = remember {
    // Is wide screen
    val isWideScreen = windowAdaptiveInfo.windowSizeClass
        .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    AppAdaptiveScaffoldState(isWideScreen)
}