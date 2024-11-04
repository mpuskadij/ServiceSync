package hr.foi.air.servicesync.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int) {
    when (selectedIndex)
    {
        0 -> SearchScreen()
        1 -> CalendarScreen()
        2 -> FavoriteScreen()
        3 -> ProfileScreen()
    }
}