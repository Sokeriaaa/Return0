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
package sokeriaaa.return0.models.action.effect

import sokeriaaa.return0.models.action.component.extra.createExtraContextFor
import sokeriaaa.return0.models.action.component.extra.executedIn
import sokeriaaa.return0.models.action.removeEffect
import sokeriaaa.return0.models.entity.Entity
import sokeriaaa.return0.shared.data.models.action.effect.EffectData
import sokeriaaa.return0.shared.data.models.action.effect.EffectModifier
import sokeriaaa.return0.shared.data.models.component.extras.Extra
import sokeriaaa.return0.shared.data.models.component.result.ActionResult

fun Entity.generateEffectFor(
    effectData: EffectData,
    tier: Int,
    turns: Int,
): Effect = EffectImpl(
    name = effectData.name,
    user = this,
    tier = tier,
    isDebuff = effectData.isDebuff,
    isStackable = effectData.isStackable,
    isRemovable = effectData.isRemovable,
    isFreeze = effectData.isFreeze,
    modifiers = effectData.modifiers,
    turnsLeft = turns,
    extra = effectData.extra,
)

internal class EffectImpl(
    override val name: String,
    override val user: Entity,
    override val tier: Int,
    override val isDebuff: Boolean,
    override val isStackable: Boolean,
    override val isRemovable: Boolean,
    override val isFreeze: Boolean,
    override val modifiers: List<EffectModifier>,
    override var turnsLeft: Int,
    override val extra: Extra?
) : Effect {

    /**
     * When the user is identical with target, the effect and turn reduction is skipped
     *  on the first turn.
     */
    private var _delayedStart = true

    override val values: MutableMap<String, Float> = HashMap()
    override var timesUsed: Int = 0
        private set
    override var timesRepeated: Int = 0
        private set

    override fun applyOn(target: Entity): List<ActionResult> {
        // Skip the first execution after user attached this effect to itself.
        if (_delayedStart && user == target) {
            _delayedStart = false
            return emptyList()
        }

        val context = createExtraContextFor(target)
        extra?.executedIn(context)
        timesUsed++
        turnsLeft--
        if (turnsLeft <= 0) {
            context.removeEffect(this)
        }
        return context.actionResults
    }

    override fun reset() {
        values.clear()
        timesUsed = 0
        timesRepeated = 0
    }
}