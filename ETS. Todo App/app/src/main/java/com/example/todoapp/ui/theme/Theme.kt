package com.example.todoapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PastelColorScheme = lightColorScheme(
    primary = pastelBlue,
    secondary = pastelGreen,
    tertiary = pastelPink,
    background = pastelWhite,
    surface = pastelWhite,
    onPrimary = textDark,
    onSecondary = textDark,
    onBackground = textDark,
    onSurface = textDark,
)

private val pastelBlue = Color(0xFFA7C7E7)
private val pastelGreen = Color(0xFFC1E1C1)
private val pastelPink = Color(0xFFF8C8DC)
private val pastelWhite = Color(0xFFFAFAFA)
private val textDark = Color(0xFF333333)

@Composable
fun TodoAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PastelColorScheme,
        typography = Typography,
        content = content
    )
}