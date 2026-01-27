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
import return0.composeapp.generated.resources.meta_grand_compiler
import return0.composeapp.generated.resources.meta_grand_compiler_info
import return0.composeapp.generated.resources.meta_intern
import return0.composeapp.generated.resources.meta_intern_info
import return0.composeapp.generated.resources.meta_junior
import return0.composeapp.generated.resources.meta_junior_info
import return0.composeapp.generated.resources.meta_lead
import return0.composeapp.generated.resources.meta_lead_info
import return0.composeapp.generated.resources.meta_mid_level
import return0.composeapp.generated.resources.meta_mid_level_info
import return0.composeapp.generated.resources.meta_principal
import return0.composeapp.generated.resources.meta_principal_info
import return0.composeapp.generated.resources.meta_senior
import return0.composeapp.generated.resources.meta_senior_info
import return0.composeapp.generated.resources.meta_staff
import return0.composeapp.generated.resources.meta_staff_info
import return0.composeapp.generated.resources.meta_the_source
import return0.composeapp.generated.resources.meta_the_source_info
import return0.composeapp.generated.resources.meta_thread_binder
import return0.composeapp.generated.resources.meta_thread_binder_info
import sokeriaaa.return0.shared.data.models.title.Title

object TitleRes {
    fun nameOf(title: Title): StringResource = when (title) {
        Title.INTERN -> Res.string.meta_intern
        Title.JUNIOR -> Res.string.meta_junior
        Title.MID_LEVEL -> Res.string.meta_mid_level
        Title.SENIOR -> Res.string.meta_senior
        Title.LEAD -> Res.string.meta_lead
        Title.STAFF -> Res.string.meta_staff
        Title.PRINCIPAL -> Res.string.meta_principal
        Title.THREAD_BINDER -> Res.string.meta_thread_binder
        Title.GRAND_COMPILER -> Res.string.meta_grand_compiler
        Title.THE_SOURCE -> Res.string.meta_the_source
    }

    fun descriptionOf(title: Title): StringResource = when (title) {
        Title.INTERN -> Res.string.meta_intern_info
        Title.JUNIOR -> Res.string.meta_junior_info
        Title.MID_LEVEL -> Res.string.meta_mid_level_info
        Title.SENIOR -> Res.string.meta_senior_info
        Title.LEAD -> Res.string.meta_lead_info
        Title.STAFF -> Res.string.meta_staff_info
        Title.PRINCIPAL -> Res.string.meta_principal_info
        Title.THREAD_BINDER -> Res.string.meta_thread_binder_info
        Title.GRAND_COMPILER -> Res.string.meta_grand_compiler_info
        Title.THE_SOURCE -> Res.string.meta_the_source_info
    }
}