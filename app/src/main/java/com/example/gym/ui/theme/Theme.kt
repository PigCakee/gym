package com.example.gym.ui.theme

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorPalette = GymColors(
    brand = Brand500,
    brandSecondary = Brand500,
    brandSecondaryAccent = Brand100,
    uiBackground = White,
    uiBorder = Mono200,
    surface = White,
    onSurface = Mono900,
    primary = Brand500,
    onPrimary = White,
    secondary = Brand500,
    onSecondary = White,
    windowBackground = White,
    statusBar = White,
    navBar = White,
    textPrimary = Mono900,
    textSecondary = Mono600,
    error = Error,
    success = Success,
    warning = Warning,
    isDark = false
)

private val DarkColorPalette = GymColors(
    brand = Brand500Dark,
    brandSecondary = Brand500Dark,
    brandSecondaryAccent = Brand100,
    uiBackground = MonoDark2,
    uiBorder = MonoDark3,
    surface = MonoDark1,
    onSurface = White,
    primary = Brand500Dark,
    onPrimary = MonoDark2,
    secondary = Brand500Dark,
    onSecondary = MonoDark2,
    windowBackground = MonoDark1,
    statusBar = MonoDark2,
    navBar = MonoDark2,
    textPrimary = White,
    textSecondary = MonoDark6,
    error = ErrorDark,
    success = SuccessDark,
    warning = WarningDark,
    isDark = true
)

@Composable
fun GymTheme(content: @Composable () -> Unit) {
    val colors = //if (isSystemInDarkTheme()) DarkColorPalette else
        LightColorPalette

    ProvideGymColors(colors) {
        MaterialTheme(
            typography = Typography,
            shapes = Shapes
        ){
            CompositionLocalProvider(
                LocalRippleTheme provides GymRippleTheme,
                content = content
            )
        }
    }
}

object GymTheme {
    val colors: GymColors
        @Composable
        get() = LocalGymColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalGymTypography.current
}

/**
 * ECabs custom Color Palette
 */
@Stable
class GymColors(
    brand: Color,
    brandSecondary: Color,
    brandSecondaryAccent: Color,
    uiBackground: Color,
    uiBorder: Color,
    surface: Color,
    onSurface: Color,
    primary: Color,
    onPrimary: Color,
    secondary: Color,
    onSecondary: Color,
    windowBackground: Color,
    statusBar: Color,
    navBar: Color,
    textPrimary: Color,
    textSecondary: Color,
    error: Color,
    success: Color,
    warning: Color,
    isDark: Boolean
) {
    var brand by mutableStateOf(brand)
        private set
    var brandSecondary: Color by mutableStateOf(brandSecondary)
        private set
    var uiBackground by mutableStateOf(uiBackground)
        private set
    var uiBorder by mutableStateOf(uiBorder)
        private set
    var surface by mutableStateOf(surface)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var primary by mutableStateOf(primary)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var onSecondary by mutableStateOf(onSecondary)
        private set
    var windowBackground by mutableStateOf(windowBackground)
        private set
    var statusBar by mutableStateOf(statusBar)
        private set
    var navBar by mutableStateOf(navBar)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var brandSecondaryAccent by mutableStateOf(brandSecondaryAccent)
        private set
    var error by mutableStateOf(error)
        private set
    var success by mutableStateOf(success)
        private set
    var warning by mutableStateOf(warning)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: GymColors) {
        brand = other.brand
        brandSecondary = other.brandSecondary
        brandSecondaryAccent = other.brandSecondaryAccent
        uiBackground = other.uiBackground
        uiBorder = other.uiBorder
        surface = other.surface
        onSurface = other.onSurface
        primary = other.primary
        onPrimary = other.onPrimary
        secondary = other.secondary
        onSecondary = other.onSecondary
        windowBackground = other.windowBackground
        statusBar = other.statusBar
        navBar = other.navBar
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        error = other.error
        success = other.success
        warning = other.warning
        isDark = other.isDark
    }

    fun copy(): GymColors = GymColors(
        brand = brand,
        brandSecondary = brandSecondary,
        brandSecondaryAccent = brandSecondaryAccent,
        uiBackground = uiBackground,
        uiBorder = uiBorder,
        surface = surface,
        onSurface = onSurface,
        primary = primary,
        onPrimary = onPrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        windowBackground = windowBackground,
        statusBar = statusBar,
        navBar = navBar,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        error = error,
        success = success,
        warning = warning,
        isDark = isDark,
    )
}

@Composable
fun ProvideGymColors(
    colors: GymColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalGymColors provides colorPalette, content = content)
}

private val LocalGymTypography = staticCompositionLocalOf { Typography }

private val LocalGymColors = staticCompositionLocalOf {
    GymColors(
        brand = Brand500,
        brandSecondary = Brand500,
        brandSecondaryAccent = Brand100,
        uiBackground = White,
        uiBorder = Mono200,
        surface = White,
        onSurface = Mono900,
        primary = Brand500,
        onPrimary = White,
        secondary = Brand500,
        onSecondary = White,
        windowBackground = White,
        statusBar = White,
        navBar = White,
        textPrimary = Mono900,
        textSecondary = Mono600,
        error = Error,
        success = Success,
        warning = Warning,
        isDark = false
    )
}

private object GymRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = GymTheme.colors.onSurface

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color.Black,
        lightTheme = true //!isSystemInDarkTheme()
    )
}
