package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun isDark(primary: Color, secondary: Color) = if (isSystemInDarkTheme()) primary else secondary