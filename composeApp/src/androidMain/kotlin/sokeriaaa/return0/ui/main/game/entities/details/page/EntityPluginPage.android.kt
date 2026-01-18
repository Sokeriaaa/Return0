package sokeriaaa.return0.ui.main.game.entities.details.page

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import sokeriaaa.return0.models.entity.plugin.display.PluginInfo
import sokeriaaa.return0.shared.data.api.component.value.PercentValue
import sokeriaaa.return0.shared.data.api.component.value.Value
import sokeriaaa.return0.shared.data.models.component.extras.CombatExtra
import sokeriaaa.return0.shared.data.models.component.extras.CommonExtra
import sokeriaaa.return0.shared.data.models.entity.path.EntityPath
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginConst
import sokeriaaa.return0.shared.data.models.entity.plugin.PluginData

@Preview
@Composable
private fun EntityPluginPageNone() {
    EntityPluginPage(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        plugin = null,
        entityPath = EntityPath.UNSPECIFIED,
        onRequestSwitch = {},
        onRequestUninstall = {},
    )
}

@Preview
@Composable
private fun EntityPluginPageIdenticalPath() {
    EntityPluginPage(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        plugin = PluginInfo(
            name = "Example Plugin",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            tier = 1,
            constMap = mapOf(
                PluginConst.ATK to 1,
                PluginConst.DEF to 2,
                PluginConst.CRIT_RATE to 3,
                PluginConst.CRIT_DMG to 4,
            ),
            data = PluginData(
                key = "example",
                nameRes = "plugin.example",
                descriptionRes = "plugin.example.desc",
                path = EntityPath.HEAP,
                onAttack = CombatExtra.HPChange(Value(-42)),
                onDefend = CommonExtra.ForUser(CombatExtra.HPChange(Value(42))),
                attackRateOffset = PercentValue(0.42F),
                defendRateOffset = PercentValue(0.42F),
            ),
            isLocked = false,
            installedBy = "Example",
        ),
        entityPath = EntityPath.HEAP,
        onRequestSwitch = {},
        onRequestUninstall = {},
    )
}


@Preview
@Composable
private fun EntityPluginPageDifferentPath() {
    EntityPluginPage(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        plugin = PluginInfo(
            name = "Example Plugin",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            tier = 1,
            constMap = mapOf(
                PluginConst.ATK to 1,
                PluginConst.DEF to 2,
                PluginConst.CRIT_RATE to 3,
                PluginConst.CRIT_DMG to 4,
            ),
            data = PluginData(
                key = "example",
                nameRes = "plugin.example",
                descriptionRes = "plugin.example.desc",
                path = EntityPath.HEAP,
                onAttack = CombatExtra.HPChange(Value(-42)),
                onDefend = CommonExtra.ForUser(CombatExtra.HPChange(Value(42))),
                attackRateOffset = PercentValue(0.42F),
                defendRateOffset = PercentValue(0.42F),
            ),
            isLocked = false,
            installedBy = "Example",
        ),
        entityPath = EntityPath.PROTOCOL,
        onRequestSwitch = {},
        onRequestUninstall = {},
    )
}