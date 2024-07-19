package com.example.todo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ThemeColors.Night.supportSeparator,
    secondary = ThemeColors.Night.supportOverlay,
    tertiary = ThemeColors.Night.blue,
    error = ThemeColors.Night.red,
    scrim = ThemeColors.Night.green,
    outline = ThemeColors.Night.grayLight,
    outlineVariant = ThemeColors.Night.gray,
    surface = ThemeColors.Night.backPrimary,
    background = ThemeColors.Night.backSecondary,
    surfaceTint = ThemeColors.Night.backElevated,
    onPrimary = ThemeColors.Night.labelPrimary,
    onSecondary = ThemeColors.Night.labelSecondary,
    onTertiary = ThemeColors.Night.labelTertiary,
    onSurfaceVariant = ThemeColors.Night.labelDisable,
    primaryContainer = ThemeColors.Night.white
)
private val LightColorScheme = lightColorScheme(
    primary = ThemeColors.Day.supportSeparator,
    secondary = ThemeColors.Day.supportOverlay,
    tertiary = ThemeColors.Day.blue,
    error = ThemeColors.Day.red,
    scrim = ThemeColors.Day.green,
    outline = ThemeColors.Day.grayLight,
    outlineVariant = ThemeColors.Day.gray,
    surface = ThemeColors.Day.backPrimary,
    background = ThemeColors.Day.backSecondary,
    surfaceTint = ThemeColors.Day.backElevated,
    onPrimary = ThemeColors.Day.labelPrimary,
    onSecondary = ThemeColors.Day.labelSecondary,
    onTertiary = ThemeColors.Day.labelTertiary,
    onSurfaceVariant = ThemeColors.Day.labelDisable,
    primaryContainer = ThemeColors.Day.white
)

@Composable
fun ToDoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}

@Composable
fun ColorBox(color: Color, label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun ThemePreviewContent() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorBox(color = MaterialTheme.colorScheme.primary, label = "Primary")
        ColorBox(color = MaterialTheme.colorScheme.secondary, label = "Secondary")
        ColorBox(color = MaterialTheme.colorScheme.tertiary, label = "Tertiary")
        ColorBox(color = MaterialTheme.colorScheme.error, label = "Error")
        ColorBox(color = MaterialTheme.colorScheme.background, label = "Background")
        ColorBox(color = MaterialTheme.colorScheme.surface, label = "Surface")
        ColorBox(color = MaterialTheme.colorScheme.surfaceTint, label = "Surface Tint")
        ColorBox(color = MaterialTheme.colorScheme.scrim, label = "Scrim")
        ColorBox(color = MaterialTheme.colorScheme.outline, label = "Outline")
        ColorBox(color = MaterialTheme.colorScheme.outlineVariant, label = "Outline Variant")
        ColorBox(color = MaterialTheme.colorScheme.onPrimary, label = "On Primary")
        ColorBox(color = MaterialTheme.colorScheme.onSecondary, label = "On Secondary")
        ColorBox(color = MaterialTheme.colorScheme.onTertiary, label = "On Tertiary")
        ColorBox(color = MaterialTheme.colorScheme.onSurfaceVariant, label = "On Surface Variant")
    }
}

@Preview(showBackground = true)
@Composable
fun LightThemePreview() {
    ToDoAppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ThemePreviewContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DarkThemePreview() {
    ToDoAppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ThemePreviewContent()
        }
    }
}
