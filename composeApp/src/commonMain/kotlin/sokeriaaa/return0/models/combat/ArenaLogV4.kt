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
package sokeriaaa.return0.models.combat

import kotlinx.serialization.Serializable
import sokeriaaa.return0.shared.data.models.component.result.ActionResult

/**
 * Log displaying for the arena.
 */
sealed interface ArenaLogV4 {

    sealed interface General : ArenaLogV4 {
        /**
         * The combat begins.
         */
        data class Welcome(
            val version: String,
            val time: Long,
        ) : General

        /**
         * Player won the combat.
         */
        data object Win : General

        /**
         * Player lost the combat.
         */
        data object Lose : General
    }

    sealed interface Actions : ArenaLogV4 {
        /**
         * An entity invokes a function.
         */
        data class FunctionInvoked(
            val entityIndex: Int,
            val name: String,
            val targetIndexes: List<Int>,
        ) : Actions

        /**
         * An effect applied on the entity.
         */
        data class EffectApplied(
            val userIndex: Int,
            val targetIndex: Int,
            val name: String,
            val turnsLeft: Int,
        ) : Actions

        /**
         * An entity is frozen and skipped the action.
         */
        data class Frozen(
            val entityIndex: Int,
            val name: String,
        ) : Actions

        /**
         * Display an [ActionResult].
         */
        data class Results(val actionResult: ActionResult) : Actions
    }

    @Serializable
    sealed interface Entities : ArenaLogV4 {
        /**
         * An entity is defeated.
         */
        data class Defeated(
            val defeatedIndex: Int,
            val defeatByIndex: Int,
        ) : Entities

        /**
         * An entity is revived.
         */
        data class Revived(
            val entityIndex: Int,
        ) : Entities

        /**
         * Display a HP bar when the HP of an entity is changed.
         */
        data class HPBar(
            val entityIndex: Int,
            val hp: Int,
            val maxhp: Int,
        ) : Entities
    }
}