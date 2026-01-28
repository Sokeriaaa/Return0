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
package sokeriaaa.return0.ui.common.res

import org.jetbrains.compose.resources.DrawableResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.ic_outline_code_blocks_24
import return0.composeapp.generated.resources.ic_outline_deployed_code_24
import return0.composeapp.generated.resources.ic_outline_extension_24
import return0.composeapp.generated.resources.ic_outline_more_horiz_24
import return0.composeapp.generated.resources.ic_outline_terminal_24
import sokeriaaa.return0.shared.data.models.story.inventory.ItemData

object InventoryRes {
    fun iconOfType(itemType: ItemData.Type?): DrawableResource = when (itemType) {
        ItemData.Type.CONSUMABLE -> Res.drawable.ic_outline_terminal_24
        ItemData.Type.MATERIAL -> Res.drawable.ic_outline_code_blocks_24
        ItemData.Type.QUEST -> Res.drawable.ic_outline_deployed_code_24
        ItemData.Type.PLUGIN -> Res.drawable.ic_outline_extension_24
        ItemData.Type.OTHER, null -> Res.drawable.ic_outline_more_horiz_24
    }
}