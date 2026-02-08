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
package sokeriaaa.return0.ui.main.archive.category

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.meta_category
import sokeriaaa.return0.applib.repository.data.archive.ArchiveRepo
import sokeriaaa.return0.shared.common.helpers.toPrecision
import sokeriaaa.return0.shared.data.models.entity.category.Category
import sokeriaaa.return0.shared.data.models.entity.category.CategoryEffectiveness
import sokeriaaa.return0.ui.common.widgets.AppBackIconButton
import sokeriaaa.return0.ui.common.widgets.OutlinedEmojiCard
import sokeriaaa.return0.ui.theme.AppColor
import sokeriaaa.return0.ui.theme.AppColor.alignedToPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveCategoryDetailsScreen(
    initialCategory: Category,
    mainNavHostController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo,
) {
    val archiveRepo: ArchiveRepo = koinInject()
    var selectedCategory: Category by remember { mutableStateOf(initialCategory) }
    var effectiveness: CategoryEffectiveness? by remember { mutableStateOf(null) }
    var isDefend: Boolean by remember { mutableStateOf(false) }
    // Refresh data
    LaunchedEffect(selectedCategory) {
        effectiveness = archiveRepo.getCategoryEffectiveness(selectedCategory)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.meta_category)) },
                navigationIcon = {
                    AppBackIconButton(onClick = { mainNavHostController.navigateUp() })
                },
            )
        }
    ) { paddingValues ->
        // TODO Category display and switching.
        CategoryList(
            modifier = Modifier.padding(paddingValues = paddingValues),
            effectiveMap = effectiveness?.let {
                if (isDefend) it.defend else it.attack
            } ?: emptyMap(),
            onCategorySelected = { selectedCategory = it },
        )
    }
}

@Composable
private fun CategoryList(
    modifier: Modifier = Modifier,
    effectiveMap: Map<Category, Int>,
    onCategorySelected: (Category) -> Unit,
) {
    val fullList = Category.entries.asSequence()
        // Remove hidden categories.
        .filter { it != Category.NORMAL && it != Category.ITEM }
        .map { it to (effectiveMap[it] ?: 0) }
        .sortedByDescending {
            // Sorted by descending, with the "0" placed at the end.
            if (it.second == 0) Int.MIN_VALUE else it.second
        }
        .toList()
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = fullList,
            key = { it.first },
        ) {
            CategoryItem(
                modifier = Modifier.animateItem(),
                category = it.first,
                effective = it.second,
                onClick = { onCategorySelected(it.first) },
            )
        }
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    effective: Int,
    onClick: () -> Unit,
) {
    val animatedColor by animateColorAsState(
        targetValue = when {
            effective >= 2 -> AppColor.colorScheme.extremelyPowerful.alignedToPrimary()
            effective == 1 -> AppColor.colorScheme.powerful.alignedToPrimary()
            effective == -1 -> AppColor.colorScheme.weak.alignedToPrimary()
            effective <= -2 -> AppColor.colorScheme.extremelyWeak.alignedToPrimary()
            else -> MaterialTheme.colorScheme.onSurface
        },
        label = "AnimatedCategoryColor"
    )
    val animatedRate by animateFloatAsState(
        targetValue = 1 + effective * 0.1F,
        label = "AnimatedCategoryRate"
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (effective == 0) 0.4F else 1F,
        label = "AnimatedCategoryAlpha"
    )

    OutlinedCard(
        modifier = modifier.alpha(animatedAlpha),
        border = BorderStroke(
            width = 1.dp,
            color = animatedColor,
        ),
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onClick),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedEmojiCard(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                    )
                    .size(36.dp),
                emoji = category.icon,
            )
            Text(
                modifier = Modifier.padding(
                    vertical = 5.dp,
                ),
                text = category.toString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
            HorizontalDivider(
                modifier = Modifier.alpha(0.2F),
                color = animatedColor,
            )
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = "x${animatedRate.toPrecision(2)}",
                color = animatedColor,
            )
        }
    }
}

// =========================================
// Previews
// =========================================
@Preview
@Composable
private fun CategoryItemPreview1() {
    CategoryItem(
        category = Category.CLASS,
        effective = 1,
        onClick = {},
    )
}

@Preview
@Composable
private fun CategoryItemPreview2() {
    CategoryItem(
        category = Category.CLASS,
        effective = 0,
        onClick = {},
    )
}

@Preview
@Composable
private fun CategoryItemPreview3() {
    CategoryItem(
        category = Category.CLASS,
        effective = -1,
        onClick = {},
    )
}