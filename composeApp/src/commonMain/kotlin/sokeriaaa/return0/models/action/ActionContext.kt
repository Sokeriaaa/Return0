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
package sokeriaaa.return0.models.action

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sokeriaaa.return0.applib.repository.ArchiveRepo
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.component.result.ActionResult

/**
 * A context class for executing extras, calculating conditions and values during combat.
 *
 * @param fromAction The action current executing.
 * @param user User of the action.
 * @param target Target of this action.
 * @param attackDamageResult The damage result for extras to reference after a successful attack.
 */
open class ActionContext(
    open val fromAction: Action,
    open val user: Entity,
    open val target: Entity,
    attackDamageResult: ActionResult.Damage? = null
) : KoinComponent {
    /**
     * ArchiveRepo. Mainly for getting effect data.
     * Maybe there's a better way.
     */
    val archiveRepo: ArchiveRepo by inject()

    var attackDamageResult: ActionResult.Damage? = attackDamageResult
        protected set

    /**
     * Temporary alter the [attackDamageResult] within the [action].
     */
    fun withDamageResult(
        attackDamageResult: ActionResult.Damage,
        action: () -> Unit,
    ) {
        val original = this.attackDamageResult
        this.attackDamageResult = attackDamageResult
        action()
        this.attackDamageResult = original
    }

}