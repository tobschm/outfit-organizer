package de.schmelzle.outfitorganizer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RosePink = Color(0xFFF48FB1)
private val RosePinkContainer = Color(0xFFFCE4EC)
private val OnRosePink = Color(0xFF880E4F)
private val Background = Color(0xFFFFFBFE)
private val Surface = Color(0xFFFFFFFF)
private val OnSurface = Color(0xFF1C1B1F)
private val OnSurfaceVariant = Color(0xFF49454F)

private val LightColors = lightColorScheme(
    primary = RosePink,
    onPrimary = Color.White,
    primaryContainer = RosePinkContainer,
    onPrimaryContainer = OnRosePink,
    background = Background,
    surface = Surface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
)

@Composable
fun OutfitOrganizerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
