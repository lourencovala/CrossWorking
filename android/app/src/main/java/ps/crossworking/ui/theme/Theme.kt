package ps.crossworking.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    background = backgroundDark,
    primary = mainColorDark,
    secondary = secondaryColorDark,
)

private val LightColorPalette = lightColors(
    background = backgroundLight,
    primary = mainColorLight,
    secondary = secondaryColorLight,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

val Colors.buttonText: Color
    get() = if (isLight) buttonTextColorLight else buttonTextColorDark

val Colors.outline: Color
    get() = if (isLight) outlineColorLight else outlineColorDark

val Colors.topBar: Color
    get() = if (isLight) topBarLight else topBarDark

val Colors.appBar: Color
    get() = if (isLight) appBarLight else appBarDark

@Composable
fun CrossWorkingTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
