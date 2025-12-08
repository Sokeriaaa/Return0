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
package sokeriaaa.return0.models.entity

import sokeriaaa.return0.models.action.effect.Effect
import sokeriaaa.return0.models.action.function.Skill
import sokeriaaa.return0.models.entity.shield.Shield
import sokeriaaa.return0.shared.data.models.entity.category.Category

/**
 * Super class of all entities.
 */
interface Entity {

    /**
     * The index in the arena, otherwise -1.
     */
    val index: Int

    /**
     * This entity is party.
     */
    val isParty: Boolean

    /**
     * Failed flag.
     */
    var isFailedFlag: Boolean

    /**
     * The name of entity.
     */
    val name: String

    /**
     * The level of entity.
     */
    val level: Int

    /**
     * Primary category of entity.
     */
    val category: Category

    /**
     * Secondary category of entity. (If have)
     */
    val category2: Category?

    /**
     * ATK. The higher ATK, the more damage will deal by attacking,
     *  the more HP will heal by healing.
     */
    val atk: Int

    /**
     * DEF. The higher DEF, the less damage will be taken by attacking.
     */
    val def: Int

    /**
     * SPD. The higher SPD, the faster for taking actions.
     */
    val spd: Int

    /**
     * The base ATK value from entity data, without any effect boost/debuff.
     */
    val baseATK: Int

    /**
     * The base DEF value from entity data, without any effect boost/debuff.
     */
    val baseDEF: Int

    /**
     * The base SPD value from entity data, without any effect boost/debuff.
     */
    val baseSPD: Int

    /**
     * Current HP. When HP drains to 0, this entity will fail.
     *
     * **Can not** exceed [maxhp] or go below 0.
     */
    var hp: Int

    /**
     * Max HP.
     */
    val maxhp: Int

    /**
     * Current SP. Some functions (skills) will cost SP to invoke.
     *
     * **Can not** exceed [maxsp] or go below 0.
     */
    var sp: Int

    /**
     * Max SP.
     */
    val maxsp: Int

    /**
     * Current AP. It recovers by time. When it reaches max AP, this entity can take next action
     *  then consume the entire [maxap]. The higher SPD entity has, the faster AP recovers.
     *
     * **Can** be negative by effects or functions.
     *
     * **Can** exceed the [maxap] by effects or functions,
     *  but will no longer recover unless it is consumed below [maxap].
     */
    var ap: Float

    /**
     * Max AP.
     */
    val maxap: Int

    /**
     * The base HP value from entity data, without any effect boost/debuff.
     */
    val baseHP: Int

    /**
     * The base SP value from entity data, without any effect boost/debuff.
     */
    val baseSP: Int

    /**
     * The base AP, without any effect boost/debuff.
     */
    val baseAP: Int

    /**
     * The Chance to make a critical attack.
     */
    val critRate: Float

    /**
     * Damage boost on critical attacks.
     */
    val critDMG: Float

    /**
     * Affect the target rate for non-bullseye functions.
     *
     * target rate = user's [targetRate] - target's [hideRate]
     */
    val targetRate: Float

    /**
     * Affect the target rate for non-bullseye functions.
     *
     * target rate = user's [targetRate] - target's [hideRate]
     */
    val hideRate: Float

    /**
     * The sum value of shields.
     */
    val shieldValue: Int

    /**
     * The common attack action.
     */
    val attackAction: Skill

    /**
     * The common defend action. Defend for 1 round and slightly recover SP.
     */
    val defendAction: Skill

    /**
     * The common relax action. Relax for 1 round and largely recover SP.
     */
    val relaxAction: Skill

    /**
     * Functions.
     */
    val functions: List<Skill>

    /**
     * Effects.
     */
    val effects: List<Effect>

    /**
     * Shields of current entity. Can nullify damage with in the shield value.
     *
     * A shield has the higher value and the same name will replace the older one.
     */
    val shields: Map<String, Shield>

    /**
     * Tick current entity.
     */
    fun tick()

    /**
     * Whether this entity is failed.
     */
    fun isFailed(): Boolean {
        return hp <= 0
    }

    /**
     * An effect is attached to this entity.
     */
    fun attachEffect(effect: Effect)

    /**
     * An effect is removed from this entity.
     */
    fun removeEffect(effect: Effect)

    /**
     * Attach a new shield to this entity with a specified key.
     */
    fun attachShield(key: String, value: Int, turns: Int? = null)

    /**
     * Remove the shield of the specified key.
     */
    fun removeShield(key: String)

    /**
     * Clean up invalid shields whose value or turns left is 0.
     */
    fun cleanUpShields()

}