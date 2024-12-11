package hr.foi.air.servicesync.ui.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import hr.foi.air.servicesync.R

data class NavItem(
    val label : String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun navItems(): List<NavItem> {
    return listOf(
        NavItem(stringResource(R.string.search), Icons.Default.Search, route = "search"),
        NavItem(stringResource(R.string.calendar), Icons.Default.DateRange, route = "calendar"),
        NavItem(stringResource(R.string.favorites), Icons.Default.FavoriteBorder, route = "favorites"),
        NavItem(stringResource(R.string.profile), Icons.Default.AccountCircle, route = "profile")
    )
}