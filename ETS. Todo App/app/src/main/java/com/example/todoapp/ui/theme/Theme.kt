package com.example.todoapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ModernColorScheme = lightColorScheme(
    primary = Color(0xFF6C63FF),
    secondary = Color(0xFF4FC3F7),
    tertiary = Color(0xFF81C784),
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF333333),
    onSurface = Color(0xFF444444),
    surfaceVariant = Color(0xFFEEEEEE)
)

@Composable
fun TodoAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ModernColorScheme,
        typography = Typography,
        content = content
    )
}