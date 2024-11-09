package hr.foi.air.servicesync.ui.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label : String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun navItems(): List<NavItem> {
    return listOf(
        NavItem("Pretraži", Icons.Default.Search, route = "search"),
        NavItem("Kalendar", Icons.Default.DateRange, route = "calendar"),
        NavItem("Favoriti", Icons.Default.FavoriteBorder, route = "favorites"),
        NavItem("Profil", Icons.Default.AccountCircle, route = "profile")
    )
}