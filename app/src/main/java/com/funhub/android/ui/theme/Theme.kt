package com.funhub.android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = FunHubPink,
    onPrimary = Color.White,
    primaryContainer = FunHubPinkDark,
    onPrimaryContainer = Color.White,
    
    secondary = FunHubPinkLight,
    onSecondary = Color.White,
    secondaryContainer = FunHubPinkDark,
    onSecondaryContainer = Color.White,
    
    tertiary = FunHubPink,
    onTertiary = Color.White,
    
    background = DarkBackground,
    onBackground = DarkOnBackground,
    
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    
    outline = DarkOutline,
    
    error = Error,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = FunHubPink,
    onPrimary = Color.White,
    primaryContainer = FunHubPinkLight,
    onPrimaryContainer = FunHubPinkDark,
    
    secondary = FunHubPinkDark,
    onSecondary = Color.White,
    secondaryContainer = FunHubPinkLight,
    onSecondaryContainer = FunHubPinkDark,
    
    tertiary = FunHubPink,
    onTertiary = Color.White,
    
    background = LightBackground,
    onBackground = LightOnBackground,
    
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    
    outline = LightOutline,
    
    error = Error,
    onError = Color.White
)

@Composable
fun FunHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // 暂时禁用动态颜色，保持品牌一致性
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
