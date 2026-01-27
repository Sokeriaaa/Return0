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
package sokeriaaa.return0.ui.common.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ic_baseline_arrow_drop_down_24

@Composable
fun AppDropdownVisibility(
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        AppDropdownVisibilityHeader(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = !expanded },
            label = label,
            expanded = expanded,
        )
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = expanded,
        ) {
            content()
        }
    }
}

fun LazyListScope.stickyHeaderedDropdownVisibility(
    label: @Composable () -> String,
    expandedState: MutableState<Boolean>,
    content: @Composable LazyItemScope.() -> Unit,
) {
    stickyHeader {
        AppDropdownVisibilityHeader(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expandedState.value = !expandedState.value },
            label = label(),
            expanded = expandedState.value,
        )
    }
    item {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = expandedState.value,
        ) {
            content()
        }
    }
}

@Composable
fun AppDropdownVisibilityHeader(
    modifier: Modifier = Modifier,
    label: String,
    expanded: Boolean,
) {
    val animatedRotate: Float by animateFloatAsState(
        targetValue = if (expanded) 0f else -90f,
        label = "DropdownMenuTrailer",
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .rotate(animatedRotate),
            painter = painterResource(Res.drawable.ic_baseline_arrow_drop_down_24),
            contentDescription = null,
        )
        Text(text = label)
    }
}