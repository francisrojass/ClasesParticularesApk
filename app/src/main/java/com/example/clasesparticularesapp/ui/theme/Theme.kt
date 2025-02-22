package com.example.clasesparticularesapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Definir los nuevos colores en azul oscuro
private val DarkBlue = Color(0xFF081534)  // Azul oscuro
private val LightBlue = Color(0xFF1C3D75) // Azul más claro para secundarios
private val BackgroundLight = Color(0xFFFFFFFF) // Fondo blanco en modo claro
private val BackgroundDark = Color(0xFF121212) // Fondo oscuro en modo oscuro

// Esquema de colores para modo claro
private val LightColorScheme = lightColorScheme(
    primary = DarkBlue, // Azul oscuro para botones y elementos principales
    secondary = LightBlue, // Azul más claro para acentos
    tertiary = LightBlue, // Tercera variante en azul
    background = BackgroundLight,
    surface = Color(0xFFF5F5F7), // Superficies en gris claro
    onPrimary = Color.White, // Texto sobre botones azul oscuro
    onBackground = Color.Black, // Texto sobre fondo claro
    onSurface = Color.Black
)

// Esquema de colores para modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue,
    secondary = LightBlue,
    tertiary = LightBlue,
    background = BackgroundDark,
    surface = Color(0xFF1C1C1E),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun ClasesParticularesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de definir tu tipografía personalizada en Typography.kt
        content = content
    )
}
