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

import org.jetbrains.compose.resources.StringResource
import return0.composeapp.generated.resources.Res
import return0.composeapp.generated.resources.empty_slot
import return0.composeapp.generated.resources.meta_path_daemon
import return0.composeapp.generated.resources.meta_path_daemon_desc
import return0.composeapp.generated.resources.meta_path_daemon_lore
import return0.composeapp.generated.resources.meta_path_heap
import return0.composeapp.generated.resources.meta_path_heap_desc
import return0.composeapp.generated.resources.meta_path_heap_lore
import return0.composeapp.generated.resources.meta_path_kernel
import return0.composeapp.generated.resources.meta_path_kernel_desc
import return0.composeapp.generated.resources.meta_path_kernel_lore
import return0.composeapp.generated.resources.meta_path_overclock
import return0.composeapp.generated.resources.meta_path_overclock_desc
import return0.composeapp.generated.resources.meta_path_overclock_lore
import return0.composeapp.generated.resources.meta_path_protocol
import return0.composeapp.generated.resources.meta_path_protocol_desc
import return0.composeapp.generated.resources.meta_path_protocol_lore
import return0.composeapp.generated.resources.meta_path_runtime
import return0.composeapp.generated.resources.meta_path_runtime_desc
import return0.composeapp.generated.resources.meta_path_runtime_lore
import return0.composeapp.generated.resources.meta_path_sandbox
import return0.composeapp.generated.resources.meta_path_sandbox_desc
import return0.composeapp.generated.resources.meta_path_sandbox_lore
import return0.composeapp.generated.resources.meta_path_thread
import return0.composeapp.generated.resources.meta_path_thread_desc
import return0.composeapp.generated.resources.meta_path_thread_lore
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath

object EntityPathRes {
    fun nameOf(entityPath: EntityPath): StringResource = when (entityPath) {
        EntityPath.UNSPECIFIED -> Res.string.empty_slot
        EntityPath.HEAP -> Res.string.meta_path_heap
        EntityPath.THREAD -> Res.string.meta_path_thread
        EntityPath.OVERCLOCK -> Res.string.meta_path_overclock
        EntityPath.SANDBOX -> Res.string.meta_path_sandbox
        EntityPath.PROTOCOL -> Res.string.meta_path_protocol
        EntityPath.DAEMON -> Res.string.meta_path_daemon
        EntityPath.KERNEL -> Res.string.meta_path_kernel
        EntityPath.RUNTIME -> Res.string.meta_path_runtime
    }

    fun descriptionOf(entityPath: EntityPath): StringResource = when (entityPath) {
        EntityPath.UNSPECIFIED -> Res.string.empty_slot
        EntityPath.HEAP -> Res.string.meta_path_heap_desc
        EntityPath.THREAD -> Res.string.meta_path_thread_desc
        EntityPath.OVERCLOCK -> Res.string.meta_path_overclock_desc
        EntityPath.SANDBOX -> Res.string.meta_path_sandbox_desc
        EntityPath.PROTOCOL -> Res.string.meta_path_protocol_desc
        EntityPath.DAEMON -> Res.string.meta_path_daemon_desc
        EntityPath.KERNEL -> Res.string.meta_path_kernel_desc
        EntityPath.RUNTIME -> Res.string.meta_path_runtime_desc
    }

    fun loreOf(entityPath: EntityPath): StringResource = when (entityPath) {
        EntityPath.UNSPECIFIED -> Res.string.empty_slot
        EntityPath.HEAP -> Res.string.meta_path_heap_lore
        EntityPath.THREAD -> Res.string.meta_path_thread_lore
        EntityPath.OVERCLOCK -> Res.string.meta_path_overclock_lore
        EntityPath.SANDBOX -> Res.string.meta_path_sandbox_lore
        EntityPath.PROTOCOL -> Res.string.meta_path_protocol_lore
        EntityPath.DAEMON -> Res.string.meta_path_daemon_lore
        EntityPath.KERNEL -> Res.string.meta_path_kernel_lore
        EntityPath.RUNTIME -> Res.string.meta_path_runtime_lore
    }
}